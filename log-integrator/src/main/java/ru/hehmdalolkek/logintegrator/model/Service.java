package ru.hehmdalolkek.logintegrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Service {

    private long id;

    private String name;

    private String instanceId;

}
