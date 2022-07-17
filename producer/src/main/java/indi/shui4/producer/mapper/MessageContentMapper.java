package indi.shui4.producer.mapper;

import indi.shui4.producer.model.po.MessageContent;

/**
 * @author shui4
 */
public interface MessageContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageContent record);

    int insertSelective(MessageContent record);

    MessageContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageContent record);

    int updateByPrimaryKey(MessageContent record);
}