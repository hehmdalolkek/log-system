package ru.hehmdalolkek.logcollector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.logcollector.dto.RequestLogDto;
import ru.hehmdalolkek.logcollector.dto.ResponseLogDto;
import ru.hehmdalolkek.logcollector.mapper.LogMapper;
import ru.hehmdalolkek.logcollector.model.LogEntity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaLogService implements LogService {

    @Value("${broker.kafka.topics.logs.name}")
    private String logsTopicName;

    private final KafkaTemplate<String, ResponseLogDto> kafkaTemplate;

    private final LogMapper logMapper;

    @Override
    public void createLog(RequestLogDto requestLogDto) {
        LogEntity entity = this.logMapper.toEntity(requestLogDto);
        entity.setUuid(UUID.randomUUID());
        String messageKey = "%s:%s".formatted(entity.getService().getName(), entity.getService().getInstanceId());
        ResponseLogDto responseLogDto = this.logMapper.toResponseDto(entity);
        CompletableFuture<SendResult<String, ResponseLogDto>> resultCompletableFuture =
                this.kafkaTemplate.send(this.logsTopicName, messageKey, responseLogDto);
        resultCompletableFuture.whenComplete((result, ex) -> {
            ProducerRecord<String, ResponseLogDto> record = result.getProducerRecord();
            log.info("Log send successful.\nTopic: {}; \nKey: {}; \nValue: {}.",
                    record.topic(), record.key(), record.value());
        });
    }

}
