package ru.hehmdalolkek.logintegrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Log {

    private long id;

    private String loggerName;

    private String threadName;

    private LogType type;

    private String message;

    private String stackTrace;

    public enum LogType {

        FATAL, ERROR, WARN, INFO, DEBUG, TRACE

    }

}
