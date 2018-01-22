package com.xyl.game;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.PropertiesUtils;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpringBOOT 启动类
 * @author Naah
 */

@SpringBootApplication
@MapperScan("com.xyl.game.mapper")
public class GameApplication {
     private  static Logger log=null;

	public static void main(String[] args) {

       ApplicationContext context=SpringApplication.run(GameApplication.class, args);
       AnnualMeetingGameQuestionMapper mapper = context.getBean(AnnualMeetingGameQuestionMapper.class);

       initLog4j();
       initData(mapper);



    }

    private static void initLog4j() {
	    PropertyConfigurator.configure(PropertiesUtils.initProperties("log4j.properties"));
	    log= Logger.getLogger(GameApplication.class);

    }

    private static void initData(AnnualMeetingGameQuestionMapper mapper) {
        log.info("初始化题目列表。。。");
        log.info("初始化用户Map。。。");
        log.info("初始化题目DTO列表。。。");
        List<AnnualMeetingGameQuestion> questionList = mapper.selectAll();
        List<QuestionDTO> questionDTOList=new ArrayList<>(questionList.size());
        for (AnnualMeetingGameQuestion question : questionList) {
            questionDTOList.add(new QuestionDTO(question.getId(),question.getTopic()));
        }
        HeapVariable.questionsList=questionList;
        HeapVariable.usersMap=new ConcurrentHashMap<>(1024);
        HeapVariable.questionDTOList = questionDTOList;
        Map<String, AnnualMeetingGameQuestionVo> annualMeetingGameQuestionVos = HeapVariable.annualMeetingGameQuestionVos;
        if(annualMeetingGameQuestionVos == null){
        	annualMeetingGameQuestionVos = new HashMap<String, AnnualMeetingGameQuestionVo>();
        }
        
    }
}
