package ru.hehmdalolkek.logcollector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestLogDto {

    private ZonedDateTime datetime;

    private ServiceDto service;

    private LogDto log;

}