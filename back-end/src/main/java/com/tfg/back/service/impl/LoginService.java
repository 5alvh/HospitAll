package com.tfg.back.service.impl;

import com.tfg.back.model.User;
import com.tfg.back.model.dtos.auth.AuthRequest;
import com.tfg.back.model.dtos.auth.AuthResponse;
import com.tfg.back.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            User userDetails = (User) authentication.getPrincipal();
            long expiration = request.rememberMe() ? 30 * 24 * 60 * 60 * 1000L : 60 * 60 * 1000L;
            String jwt = jwtUtil.generateToken(userDetails, expiration);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok(new AuthResponse(jwt, role));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Your account is inactive, Contact support to reactivate"));
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Your account is suspended, Contact support to know more"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials, Try again"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
