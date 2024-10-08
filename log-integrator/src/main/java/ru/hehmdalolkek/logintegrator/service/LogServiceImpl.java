package ru.hehmdalolkek.logintegrator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.logintegrator.dao.LogDao;
import ru.hehmdalolkek.logintegrator.model.LogEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogServiceImpl implements LogService {

    private final LogDao logDao;

    @Override
    public void saveLog(LogEntity logEntity) {
        logDao.saveLog(logEntity);
    }

}
