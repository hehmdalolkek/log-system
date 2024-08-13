package ru.hehmdalolkek.logcollector.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Value("${broker.kafka.topics.logs.name}")
    private String logsTopicName;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(this.logsTopicName)
                .partitions(3)
                .replicas(1)
                .build();
    }

}
