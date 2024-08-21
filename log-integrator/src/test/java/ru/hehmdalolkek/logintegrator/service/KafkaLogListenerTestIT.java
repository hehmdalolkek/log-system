package ru.hehmdalolkek.logintegrator.service;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import ru.hehmdalolkek.logintegrator.dto.RequestLogDto;
import ru.hehmdalolkek.logintegrator.model.Log;
import ru.hehmdalolkek.logintegrator.model.LogEntity;
import ru.hehmdalolkek.logintegrator.model.Service;
import ru.hehmdalolkek.logintegrator.util.LogUtil;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class KafkaLogListenerTestIT {

    static final String LOGS_TOPIC_NAME = "errors";

    @Container
    static final KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka:3.8.0");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Save log from kafka topic integration test")
    public void saveLogFromKafkaTopicIT() throws InterruptedException {
        // given
        String bootstrapServers = kafkaContainer.getBootstrapServers();
        RequestLogDto logDto = LogUtil.getRequestLogDto();
        String messageKey = "%s:%s".formatted(logDto.getService().getName(), logDto.getService().getInstanceId());

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<String, RequestLogDto> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        KafkaTemplate<String, RequestLogDto> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        // when
        Thread.sleep(1000);
        kafkaTemplate.send(LOGS_TOPIC_NAME, messageKey, logDto);
        Thread.sleep(1000);

        //then
        LogEntity resultLogEntity = jdbcTemplate.queryForObject(
                "SELECT * FROM error WHERE error.id = ?",
                (rs, rowNum) -> {
                    LogEntity logEntity = new LogEntity();
                    Service logEntityService = new Service();
                    Log logEntityLog = new Log();
                    logEntity.setUuid(rs.getObject("id", UUID.class));
                    logEntity.setDatetime(rs.getObject("date_time", ZonedDateTime.class));
                    logEntityService.setId(rs.getLong("service_id"));
                    logEntity.setService(logEntityService);
                    logEntityLog.setId(rs.getLong("log_id"));
                    logEntity.setLog(logEntityLog);
                    return logEntity;
                },
                logDto.getUuid());
        Log resultLog = jdbcTemplate.queryForObject(
                "SELECT * FROM log WHERE log.id = ?",
                (rs, rowNum) -> {
                    Log log = new Log();
                    log.setId(rs.getLong("id"));
                    log.setLoggerName(rs.getString("logger_name"));
                    log.setThreadName(rs.getString("thread_name"));
                    log.setMessage(rs.getString("message"));
                    log.setType(Log.LogType.valueOf(rs.getString("type")));
                    log.setStackTrace(rs.getString("stack_trace"));
                    return log;
                },
                resultLogEntity.getLog().getId());
        resultLogEntity.setLog(resultLog);
        Service resultService = jdbcTemplate.queryForObject(
                "SELECT * FROM service WHERE service.id = ?",
                (rs, rowNum) -> {
                    Service service = new Service();
                    service.setId(rs.getLong("id"));
                    service.setName(rs.getString("name"));
                    service.setInstanceId(rs.getString("instance_id"));
                    return service;
                },
                resultLogEntity.getService().getId());
        resultLogEntity.setService(resultService);

        assertThat(resultLogEntity).isNotNull();
        assertThat(resultLogEntity.getUuid()).isEqualTo(logDto.getUuid());
        assertThat(resultLogEntity.getDatetime()).isCloseTo(logDto.getDatetime(), new TemporalUnitWithinOffset(2, ChronoUnit.SECONDS));
        assertThat(resultLogEntity.getService().getName()).isEqualTo(logDto.getService().getName());
        assertThat(resultLogEntity.getService().getInstanceId()).isEqualTo(logDto.getService().getInstanceId());
        assertThat(resultLogEntity.getLog().getLoggerName()).isEqualTo(logDto.getLog().getLoggerName());
        assertThat(resultLogEntity.getLog().getStackTrace()).isEqualTo(logDto.getLog().getStackTrace());
        assertThat(resultLogEntity.getLog().getType().toString()).isEqualTo(logDto.getLog().getType().toString());
        assertThat(resultLogEntity.getLog().getThreadName()).isEqualTo(logDto.getLog().getThreadName());
        assertThat(resultLogEntity.getLog().getMessage()).isEqualTo(logDto.getLog().getMessage());
    }

}