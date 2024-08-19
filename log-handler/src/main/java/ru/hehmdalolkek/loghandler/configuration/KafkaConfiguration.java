package ru.hehmdalolkek.loghandler.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;
import ru.hehmdalolkek.loghandler.dto.LogDto;
import ru.hehmdalolkek.loghandler.dto.LogEntityDto;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration(proxyBeanMethods = false)
@EnableKafkaStreams
public class KafkaConfiguration {

    @Value("${broker.kafka.topics.logs.name}")
    private String logsTopicName;

    @Value("${broker.kafka.topics.errors.name}")
    private String errorsTopicName;

    @Value("${notification.errors.duration}")
    private long notificationErrorsDuration;

    @Value("${notification.errors.limit}")
    private int notificationErrorsLimit;

    @Bean
    public NewTopic logsTopic() {
        return new NewTopic(this.logsTopicName, 3, (short) 1);
    }

    @Bean
    public NewTopic errorsTopic() {
        return new NewTopic(this.errorsTopicName, 3, (short) 1);
    }

    @Bean
    public KStream<String, LogEntityDto> kStream(StreamsBuilder streamsBuilder) {
        JsonSerde<LogEntityDto> jsonSerde = new JsonSerde<>(LogEntityDto.class);
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put("spring.json.add.type.headers", false);
        jsonSerde.configure(serdeProps, false);

        KStream<String, LogEntityDto> stream =
                streamsBuilder.stream(this.logsTopicName, Consumed.with(Serdes.String(), jsonSerde));

        KStream<String, LogEntityDto> errorsStream = stream.filter(this::filterErrors);
        errorsStream.to(this.errorsTopicName);

        TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(this.notificationErrorsDuration));
        KTable<Windowed<String>, Long> errorsCount = errorsStream
                .groupByKey()
                .windowedBy(timeWindows)
                .count(Materialized.as("errors-count"));
        errorsCount
                .toStream()
                .filter((windowedKey, count) -> count >= this.notificationErrorsLimit)
                .foreach(this::sendNotification);

        return stream;
    }

    private boolean filterErrors(String key, LogEntityDto value) {
        return Objects.nonNull(value) && value.getLog().getType().equals(LogDto.LogType.ERROR);
    }

    private void sendNotification(Windowed<String> windowed, long count) {
        System.out.println("=======================================================================");
        System.out.printf(
                "Warning! Service %s has experienced %d errors in time period %s - %s.\n",
                windowed.key(),
                count,
                windowed.window().startTime().toString(),
                windowed.window().endTime().toString()
        );
        System.out.println("=======================================================================");
    }

}
