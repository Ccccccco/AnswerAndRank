package com.xyl.game.utils;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.User;

import java.util.List;
import java.util.Map;

/**
 * HeapVariable
 *
 * 内存变量
 * @author Naah
 * @date 2018-01-21
 */


public class HeapVariable {
    public static List<AnnualMeetingGameQuestion> questionsList;
    public static List<QuestionDTO> questionDTOList;
    public static Map<String,User> usersMap;
}
