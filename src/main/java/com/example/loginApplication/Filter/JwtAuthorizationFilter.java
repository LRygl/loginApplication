package com.example.loginApplication.Filter;

import com.example.loginApplication.Constant.SecurityConstant;
import com.example.loginApplication.Utility.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Filter for every request will trigger once

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;
    //Add Token Prrovider
    public JwtAuthorizationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //Check if http method is OPTION -> Set OK ... Option is sent before every request to check the information about the server
        if (httpServletRequest.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)){
            httpServletResponse.setStatus(HttpStatus.OK.value());
            //Get authorization header
        } else {
            //Pass authorization header from authorization
            String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            //if header is null or doesnt start with bearer token
            if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)){
                //Not our token - filter this request
                filterChain.doFilter(httpServletRequest,httpServletResponse);
                return;
            }
            //Try to get the token
            //Remove Bearer from token string "Bearer "
            String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
            //retrieve username from token - if the token was tampered with - we cant obtain the username from jwt
            String username = tokenProvider.getSubject(token);
            //check if token is valid  and check if user does not have authentication
            if (tokenProvider.isTokenValid(username,token) && SecurityContextHolder.getContext().getAuthentication() == null){
                //get authorities from the token
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
                //Create authentication
                Authentication authentication = tokenProvider.getAuthentication(username,authorities,httpServletRequest);
                //Set authentication to security context holder
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                //If something is wrong - clear context
                SecurityContextHolder.clearContext();
            }
        }
        //continue
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
