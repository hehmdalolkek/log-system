package ru.hehmdalolkek.logintegrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hehmdalolkek.logintegrator.model.LogEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class JDBCLogService implements LogService {

    private final static String INSERT_LOG_SQL = "INSERT INTO log (logger_name, thread_name, `type`, message, stack_trace) " +
            "VALUES (?, ?, ?, ?, ?)";

    private final static String INSERT_SERVICE_SQL = "INSERT INTO service (name, instance_id) " +
            "VALUES (?, ?)";

    private final static String INSERT_ERROR_SQL = "INSERT INTO error (id, date_time, service_id, log_id) " +
            "VALUES (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void saveLog(LogEntity logEntity) {
        KeyHolder logKeyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_LOG_SQL, new String[] { "id" });
            ps.setString(1, logEntity.getLog().getLoggerName());
            ps.setString(2, logEntity.getLog().getThreadName());
            ps.setString(3, logEntity.getLog().getType().toString());
            ps.setString(4, logEntity.getLog().getMessage());
            ps.setString(5, logEntity.getLog().getStackTrace());
            return ps;
        }, logKeyHolder);


        KeyHolder serviceKeyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SERVICE_SQL, new String[] { "id" });
            ps.setString(1, logEntity.getService().getName());
            ps.setString(2, logEntity.getService().getInstanceId());
            return ps;
        }, serviceKeyHolder);

        Object[] args = {
            logEntity.getUuid(),
            logEntity.getDatetime(),
            serviceKeyHolder.getKey(),
            logKeyHolder.getKey()
        };
        this.jdbcTemplate.update(INSERT_ERROR_SQL, new ArgumentPreparedStatementSetter(args));
    }

}
