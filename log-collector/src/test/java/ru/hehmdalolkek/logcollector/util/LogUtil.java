package ru.hehmdalolkek.logcollector.util;

import ru.hehmdalolkek.logcollector.dto.LogDto;
import ru.hehmdalolkek.logcollector.dto.RequestLogDto;
import ru.hehmdalolkek.logcollector.dto.ResponseLogDto;
import ru.hehmdalolkek.logcollector.dto.ServiceDto;

import java.time.ZonedDateTime;
import java.util.UUID;

public class LogUtil {

    public static ResponseLogDto getResponseLOgDto() {
        ServiceDto serviceDto = ServiceDto.builder()
                .name("Authorization")
                .instanceId("xyz3")
                .build();
        LogDto logDto = LogDto.builder()
                .loggerName("Auth")
                .threadName("main")
                .type(LogDto.LogType.INFO)
                .message("Authorization service started")
                .build();
        return ResponseLogDto.builder()
                .uuid(UUID.randomUUID())
                .datetime(ZonedDateTime.now())
                .service(serviceDto)
                .log(logDto)
                .build();
    }

    public static RequestLogDto getRequestLogDto() {
        ServiceDto serviceDto = ServiceDto.builder()
                .name("Authorization")
                .instanceId("xyz3")
                .build();
        LogDto logDto = LogDto.builder()
                .loggerName("Auth")
                .threadName("main")
                .type(LogDto.LogType.INFO)
                .message("Authorization service started")
                .build();
        return RequestLogDto.builder()
                .datetime(ZonedDateTime.now())
                .service(serviceDto)
                .log(logDto)
                .build();
    }

}
