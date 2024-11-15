package com.yobrunox.gestionmarko.controllers;

import com.yobrunox.gestionmarko.dto.auth.LoginRequest;
import com.yobrunox.gestionmarko.dto.auth.RegisterRequest;
import com.yobrunox.gestionmarko.dto.auth.TokenResponse;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.dto.exception.ErrorDTO;
import com.yobrunox.gestionmarko.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;


    @PostMapping("auth/register")
    public ResponseEntity<?> register(@RequestBody final RegisterRequest request) {
        //final TokenResponse token =

        //SAVE USER
        //JWT GENEARTE TOKEN
        //GENERATE REFRESH TOKEN
        //GUARDAR TOKEN TOKENREPOSITORY
        //String message = userService.register(request);
        /*ErrorDTO response = ErrorDTO.builder()
                .message(message)
                //.code("M-200")
                .build();*/
        TokenResponse response = userService.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody final LoginRequest request) {
        try {
            TokenResponse response = userService.login(request);
            return ResponseEntity.ok(response);

        }catch (BusinessException e){
            throw new BusinessException(e.getCode(),e.getStatus(),e.getMessage());
        }
    }


    @PostMapping("admin/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest userRegisterDTO,
                                      @RequestHeader("Authorization") String authHeader){
        String tokenAdmin = authHeader.replace("Bearer ","");
        String res = userService.registerForAdmin(userRegisterDTO,tokenAdmin);
        ErrorDTO response = ErrorDTO.builder()
                .message(res)
                .code("M-200")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
    @PostMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        return service.refreshToken(authHeader);
    }*/
}

