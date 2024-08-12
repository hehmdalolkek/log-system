package ru.hehmdalolkek.logcollector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseLogDto {

    private UUID uuid;

    private ZonedDateTime datetime;

    private ServiceDto service;

    private LogDto log;

}
