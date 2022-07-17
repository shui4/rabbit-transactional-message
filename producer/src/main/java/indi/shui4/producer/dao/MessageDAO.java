package indi.shui4.producer.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author shui4
 */
@AllArgsConstructor
@Repository
public class MessageDAO {

  private JdbcTemplate jdbcTemplate;
}
