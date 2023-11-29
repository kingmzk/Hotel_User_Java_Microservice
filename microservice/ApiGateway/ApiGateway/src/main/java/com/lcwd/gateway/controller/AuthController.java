package com.lcwd.gateway.controller;

import com.lcwd.gateway.models.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client
    , @AuthenticationPrincipal OidcUser user, Model model) {
        logger.info("User Logged In : " + user.getEmail());
        //creating auth response object
        AuthResponse authResponse = new AuthResponse();

        //settig email to auth response
        authResponse.setUserId(user.getEmail());

        //setting access token to auth response
        authResponse.setAccessToken(client.getAccessToken().getTokenValue());

        //setting access token to auth response
        authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());

        //setting expire time to auth response
        authResponse.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());

        List<String> authorities = user.getAuthorities().stream().map(grantedAuthority -> {
            return grantedAuthority.getAuthority();
        }).collect(Collectors.toList());

        //setting expire time to auth response
        authResponse.setAuthorities(authorities);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);


    }
}
