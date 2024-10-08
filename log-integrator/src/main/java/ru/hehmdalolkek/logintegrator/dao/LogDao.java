package ru.hehmdalolkek.logintegrator.dao;

import ru.hehmdalolkek.logintegrator.model.LogEntity;

public interface LogDao {

    void saveLog(LogEntity logEntity);

}
