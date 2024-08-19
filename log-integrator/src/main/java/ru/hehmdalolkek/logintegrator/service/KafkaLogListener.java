package ru.hehmdalolkek.logintegrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.hehmdalolkek.logintegrator.dto.RequestLogDto;
import ru.hehmdalolkek.logintegrator.mapper.LogMapper;
import ru.hehmdalolkek.logintegrator.model.LogEntity;

@Component
@RequiredArgsConstructor
public class KafkaLogListener {

    private final LogService logService;

    private final LogMapper logMapper;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${broker.kafka.topics.errors.name}")
    public void listen(final RequestLogDto logDto) {
        LogEntity logEntity = this.logMapper.toEntity(logDto);
        this.logService.saveLog(logEntity);
    }

}
