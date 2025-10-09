package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.response.HomeResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/srw")
public class HomeController {

    @GetMapping("/home")
    public ResponseEntity<HomeResponseBody> home() {
        try{
            HomeResponseBody successResponse = new HomeResponseBody(
                    "Connected successfully to srw-springboot",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            HomeResponseBody errorResponse = new HomeResponseBody(
                    "Error connecting to srw-react: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}