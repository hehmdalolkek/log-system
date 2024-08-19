package ru.hehmdalolkek.logintegrator.mapper;

import org.mapstruct.Mapper;
import ru.hehmdalolkek.logintegrator.dto.RequestLogDto;
import ru.hehmdalolkek.logintegrator.model.LogEntity;

@Mapper(componentModel = "spring")
public interface LogMapper {

    LogEntity toEntity(RequestLogDto requestLogDto);

}
