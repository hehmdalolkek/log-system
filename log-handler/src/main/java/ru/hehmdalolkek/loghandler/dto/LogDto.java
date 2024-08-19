package ru.hehmdalolkek.loghandler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDto {

    @JsonProperty("logger_name")
    private String loggerName;

    @JsonProperty("thread_name")
    private String threadName;

    private LogType type;

    private String message;

    @JsonProperty("stack_trace")
    private String stackTrace;

    public enum LogType {

        FATAL, ERROR, WARN, INFO, DEBUG, TRACE

    }

}
