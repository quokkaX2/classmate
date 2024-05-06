package com.quokka.classmate.domain.controller.api;

import com.quokka.classmate.domain.dto.RedisQueueRequestDto;
import com.quokka.classmate.global.security.UserDetailsImpl;
import com.quokka.classmate.domain.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QueueController {
    private final QueueService queueService;
    @DeleteMapping("/api/queue/{subjectId}")
    public ResponseEntity<Void> removeQueue(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long subjectId
            ) {
        RedisQueueRequestDto requestDto = new RedisQueueRequestDto(userDetails.getUser().getId(), subjectId);
        queueService.removeQueue(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
