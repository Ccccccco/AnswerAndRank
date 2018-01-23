package com.xyl.game.utils;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.Answer;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * initData
 *
 * @author Naah
 * @date 2018-01-22
 */
public class InitData {
    private final static Logger log = Logger.getLogger(InitData.class);

    public static void initVariable() {
        log.info("初始化用户Map。。。");
        HeapVariable.usersMap = new ConcurrentHashMap<>(1024);
        HeapVariable.annualMeetingGameQuestionVos = new HashMap<String, AnnualMeetingGameQuestionVo>();
    }

    public static void initQuestion() {
        AnnualMeetingGameQuestionMapper mapper = HeapVariable.mapper;
        log.info("初始化题目列表。。。");
        log.info("初始化题目DTO列表。。。");
        List<AnnualMeetingGameQuestion> questionList = mapper.selectAll();
        List<QuestionDTO> questionDTOList = new ArrayList<>(questionList.size());
        for (AnnualMeetingGameQuestion question : questionList) {
            questionDTOList.add(new QuestionDTO(question.getId(), question.getTopic(), question.getAnswerOne(),
                    question.getAnswerTwo(), question.getAnswerThree(), question.getAnswerFour()));
        }
        HeapVariable.questionsList = questionList;
        HeapVariable.questionDTOList = questionDTOList;
        HeapVariable.atomic = new AtomicInteger();
        HeapVariable.atomic.set(questionList.size());;
        HeapVariable.answerList=new CopyOnWriteArrayList<>();
        for (int i = 0; i <  HeapVariable.questionsList.size(); i++) {
            HashMap<Integer, ConcurrentLinkedQueue<Answer>> hashMap = new HashMap<>();
            HeapVariable.answerList.add(hashMap);
            hashMap.put(0,new ConcurrentLinkedQueue<Answer>() );
            hashMap.put(1,new ConcurrentLinkedQueue<Answer>() );
            hashMap.put(2,new ConcurrentLinkedQueue<Answer>() );
            hashMap.put(3,new ConcurrentLinkedQueue<Answer>() );
            hashMap.put(4,new ConcurrentLinkedQueue<Answer>() );

        }

    }

    public static void initTable() {
        AnnualMeetingGameQuestionMapper mapper = HeapVariable.mapper;
        mapper.copyTableStruct();
        mapper.deleteTable();
        mapper.renameTableName();
    }
}
