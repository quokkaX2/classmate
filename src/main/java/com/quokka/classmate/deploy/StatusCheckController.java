package com.quokka.classmate.deploy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusCheckController {

    @GetMapping("/health-check")
    public ResponseEntity<Void> checkHealthStatus() {
        System.out.println("/health-check 수신 확인");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<Void> checkHealth() {
        System.out.println("/health 수신 확인");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
