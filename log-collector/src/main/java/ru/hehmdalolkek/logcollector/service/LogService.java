package ru.hehmdalolkek.logcollector.service;

import ru.hehmdalolkek.logcollector.dto.RequestLogDto;

public interface LogService {

    void createLog(RequestLogDto requestLogDto);

}
