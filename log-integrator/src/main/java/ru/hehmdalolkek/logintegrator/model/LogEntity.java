package ru.hehmdalolkek.logintegrator.model;

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
public class LogEntity {

    private UUID uuid;

    private ZonedDateTime datetime;

    private Service service;

    private Log log;

}