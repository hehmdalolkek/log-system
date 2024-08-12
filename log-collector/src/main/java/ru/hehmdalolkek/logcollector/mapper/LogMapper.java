package ru.hehmdalolkek.logcollector.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hehmdalolkek.logcollector.dto.RequestLogDto;
import ru.hehmdalolkek.logcollector.dto.ResponseLogDto;
import ru.hehmdalolkek.logcollector.model.LogEntity;

@Mapper(componentModel = "spring")
public interface LogMapper {

    @Mapping(target = "uuid", ignore = true)
    LogEntity toEntity(RequestLogDto requestLogDto);

    ResponseLogDto toResponseDto(LogEntity logEntity);

}
