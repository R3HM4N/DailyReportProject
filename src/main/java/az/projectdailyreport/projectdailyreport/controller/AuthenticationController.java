package az.projectdailyreport.projectdailyreport.controller;

import az.projectdailyreport.projectdailyreport.dto.request.AuthenticationRequest;
import az.projectdailyreport.projectdailyreport.dto.request.AuthenticationResponse;
import az.projectdailyreport.projectdailyreport.dto.request.RefreshToken;
import az.projectdailyreport.projectdailyreport.service.impl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/refresh-token")
    public  ResponseEntity<String> refresh(@RequestBody RefreshToken refreshToken,HttpServletRequest request, HttpServletResponse response){

        return ResponseEntity.ok(service.generateAccessToken(refreshToken.getRefreshToken(),request,response))   ;
    }
}
