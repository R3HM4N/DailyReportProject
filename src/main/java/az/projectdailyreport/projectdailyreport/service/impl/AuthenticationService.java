package az.projectdailyreport.projectdailyreport.service.impl;

import az.projectdailyreport.projectdailyreport.dto.request.AuthenticationRequest;
import az.projectdailyreport.projectdailyreport.dto.request.AuthenticationResponse;
import az.projectdailyreport.projectdailyreport.exception.EmailNotSentException;
import az.projectdailyreport.projectdailyreport.exception.MailAlreadyExistsException;
import az.projectdailyreport.projectdailyreport.exception.UserNotFoundException;
import az.projectdailyreport.projectdailyreport.model.User;
import az.projectdailyreport.projectdailyreport.model.token.Token;
import az.projectdailyreport.projectdailyreport.model.token.TokenType;
import az.projectdailyreport.projectdailyreport.repository.TokenRepository;
import az.projectdailyreport.projectdailyreport.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var userOptional = repository.findByEmail(request.getMail());

        if (userOptional.isEmpty()) {
            throw new EmailNotSentException("User not found");
        }

        var user = userOptional.get();

        if (!passwordMatches(user, request.getPassword())) {
            throw new EmailNotSentException("password is incorrect");
        }

        var jwtToken = jwtService.createToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    private boolean passwordMatches(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }


    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String mail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        mail = jwtService.extractUsername(refreshToken);
        if (mail != null) {
            var user = this.repository.findByEmail(mail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.createToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .id(user.getId())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public String generateAccessToken(String refreshToken) {
        String mail = jwtService.extractUsername(refreshToken);
        if (mail != null) {
            var user = repository.findByEmail(mail)
                    .orElseThrow(() -> new MailAlreadyExistsException("User not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.createToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                return accessToken;
            }
        }
        return null;
    }
}



