package com.uniqwrites.security;

import com.uniqwrites.model.Role;
import com.uniqwrites.model.User;
import com.uniqwrites.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Handler for successful OAuth2 authentication
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                       Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauthUser = ((OAuth2AuthenticationToken) authentication).getPrincipal();
        
        try {
            logger.info("Processing OAuth2 authentication success");
            
            String email = extractEmail(oauthUser);
            String name = extractName(oauthUser);
            
            // Check if the user exists in our database
            Optional<User> userOptional = userRepository.findByEmail(email);
            User user;
            
            if (userOptional.isPresent()) {
                user = userOptional.get();
                logger.info("Existing user found with Google login: {}", email);
            } else {
                // Create a new user if one doesn't exist
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setRole(Role.STUDENT); // Default role for OAuth2 registrations
                user.setActive(true);
                
                userRepository.save(user);
                logger.info("New user registered via Google: {}", email);
            }
            
            // Generate token with appropriate user details
            String token = tokenProvider.generateTokenFromOAuth2(email, name, user.getRole().name());
            
            // Redirect to the frontend with the token
            String redirectUrl = determineRedirectUrl(user);            String uri = UriComponentsBuilder.fromUriString(redirectUrl)
                                          .queryParam("token", token)
                                          .queryParam("role", user.getRole().name())
                                          .queryParam("name", user.getName())
                                          .queryParam("email", user.getEmail())
                                          .build().toUriString();
            
            getRedirectStrategy().sendRedirect(request, response, uri);
            
        } catch (Exception e) {
            logger.error("Error during OAuth2 authentication success", e);
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
    
    private String extractEmail(OAuth2User oauthUser) {
        return (String) oauthUser.getAttributes().get("email");
    }
    
    private String extractName(OAuth2User oauthUser) {
        return (String) oauthUser.getAttributes().get("name");
    }
    
    private String determineRedirectUrl(User user) {
        // Frontend URL where users should be redirected after authentication
        // This should be configurable in application.yml
        return "http://localhost:5173/auth/oauth2/success";
    }
}
