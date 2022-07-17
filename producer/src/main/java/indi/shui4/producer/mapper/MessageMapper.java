package indi.shui4.producer.mapper;

import indi.shui4.producer.model.po.Message;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shui4
 */
public interface MessageMapper {
  int deleteByPrimaryKey(Long id);

  int insert(Message record);

  int insertSelective(Message record);

  Message selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(Message record);

  int updateByPrimaryKey(Message record);

  List<Message> findList(@Param("ts") List<Integer> ts, @Param("time") LocalDateTime time);

  LocalDateTime selectSuccessfulMaxCreateTime(Message businessKey);
}
