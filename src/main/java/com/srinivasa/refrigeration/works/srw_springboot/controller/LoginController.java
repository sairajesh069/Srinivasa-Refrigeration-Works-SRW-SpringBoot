package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CredentialsDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.HomeResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.LoginResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/srw")
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> login(@RequestBody CredentialsDTO credentials) {
        try{
            LoginResponseBody successResponse = new LoginResponseBody(
                    "Login success",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            LoginResponseBody errorResponse = new LoginResponseBody(
                    "Login failed",
                    HttpStatus.UNAUTHORIZED.value()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
