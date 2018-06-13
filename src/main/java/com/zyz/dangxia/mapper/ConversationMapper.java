package com.zyz.dangxia.mapper;

import com.zyz.dangxia.model.ConversationDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConversationDO record);

    int insertSelective(ConversationDO record);

    ConversationDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConversationDO record);

    int updateByPrimaryKey(ConversationDO record);

    ConversationDO getByInitiatorIdAndTaskId(int initiatorId, int taskId);

    List<ConversationDO> listAboutSomeone(int userId);

    int getIdByInitiatorIdAndTaskId(int initiatorId, int taskId);
}