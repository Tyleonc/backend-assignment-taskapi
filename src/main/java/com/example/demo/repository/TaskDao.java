package com.example.demo.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class TaskDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TaskDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ClaimedTask> claimTask(Collection<String> taskIds, String appId, int batch) {
        String updateSql = """
            UPDATE scheduled_tasks
               SET status = 'PROCESSING', claim_by = :appId, claim_at = NOW()
             WHERE status = 'PENDING' AND execute_at <= NOW() AND task_id IN (:taskIds)
             LIMIT :batch
        """;

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("appId", appId)
                .addValue("taskIds", taskIds)
                .addValue("batch", batch);
        jdbcTemplate.update(updateSql, param);

        String getTaskSql = """
            SELECT task_id, payload
              FROM scheduled_tasks
             WHERE claim_by = :appId AND status = 'PROCESSING' AND task_id IN (:ids)
             LIMIT :batch
        """;

        return jdbcTemplate.query(getTaskSql, param,
                (rs, rowNum) -> new ClaimedTask(rs.getString("task_id"),rs.getString("payload")));
    }

}
