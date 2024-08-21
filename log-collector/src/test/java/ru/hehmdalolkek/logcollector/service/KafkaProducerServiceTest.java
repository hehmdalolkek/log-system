package ru.hehmdalolkek.logcollector.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ru.hehmdalolkek.logcollector.dto.RequestLogDto;
import ru.hehmdalolkek.logcollector.dto.ResponseLogDto;
import ru.hehmdalolkek.logcollector.util.LogUtil;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 3, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaProducerServiceTest {

    static final String LOGS_TOPIC_NAME = "logs";

    @Autowired
    KafkaLogService kafkaLogService;

    @Test
    @DisplayName("Send log to kafka test")
    void sendLogToKafkaTest() {
        // given
        RequestLogDto log = LogUtil.getRequestLogDto();

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ResponseLogDto.class);
        KafkaConsumer<String, ResponseLogDto> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(LOGS_TOPIC_NAME));

        // when
        this.kafkaLogService.createLog(log);

        // then
        ConsumerRecords<String, ResponseLogDto> records = consumer.poll(Duration.ofMillis(10000L));
        consumer.close();

        assertThat(records.count()).isEqualTo(1);
        assertThat(records.iterator().next().value().getUuid()).isNotNull();
        assertThat(records.iterator().next().value().getDatetime()).isEqualTo(log.getDatetime());
        assertThat(records.iterator().next().value().getService()).isEqualTo(log.getService());
        assertThat(records.iterator().next().value().getLog()).isEqualTo(log.getLog());
    }

}