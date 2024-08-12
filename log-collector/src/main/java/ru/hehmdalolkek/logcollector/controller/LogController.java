package ru.hehmdalolkek.logcollector.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hehmdalolkek.logcollector.dto.RequestLogDto;
import ru.hehmdalolkek.logcollector.service.LogService;

@RestController
@RequestMapping("/api/v1/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @PostMapping
    public ResponseEntity<?> createLog(@RequestBody RequestLogDto requestLogDto) {
        this.logService.createLog(requestLogDto);
        return ResponseEntity.ok().build();
    }

}
