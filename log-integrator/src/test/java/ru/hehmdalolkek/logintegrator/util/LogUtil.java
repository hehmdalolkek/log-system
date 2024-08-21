package ru.hehmdalolkek.logintegrator.util;

import ru.hehmdalolkek.logintegrator.dto.LogDto;
import ru.hehmdalolkek.logintegrator.dto.RequestLogDto;
import ru.hehmdalolkek.logintegrator.dto.ServiceDto;

import java.time.ZonedDateTime;
import java.util.UUID;

public class LogUtil {

    public static RequestLogDto getRequestLogDto() {
        ServiceDto serviceDto = ServiceDto.builder()
                .name("Authorization")
                .instanceId("xyz3")
                .build();
        LogDto logDto = LogDto.builder()
                .loggerName("Auth")
                .threadName("main")
                .type(LogDto.LogType.ERROR)
                .message("Authorization service not started")
                .build();
        return RequestLogDto.builder()
                .uuid(UUID.randomUUID())
                .datetime(ZonedDateTime.now())
                .service(serviceDto)
                .log(logDto)
                .build();
    }

}
