package com.xyl.game.Service.impl;

import com.xyl.game.Service.AnnualMeetingQuestionExctFileSerivce;
import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.TimeParam;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.InitData;
import com.xyl.game.utils.StringUtil;
import com.xyl.game.utils.TimeFormatUtil;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author dazhi
 */
@Service
public class AnnualMeetingQuestionExctFileServiceImpl implements AnnualMeetingQuestionExctFileSerivce{

	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingQuestionExctFileServiceImpl.class);

	@Autowired
	private AnnualMeetingGameQuestionMapper annualMeetingGameQuestionMapper;

	@Override
	public AnnualMeetingGameQuestionVo savaDataForExct(InputStream exctFileStream){
		//创建对Excel工作簿文件的引用
		Workbook workbook = null;
		AnnualMeetingGameQuestionVo annualMeetingGameQuestionVo= new AnnualMeetingGameQuestionVo();

		try {
			try{
				workbook = new XSSFWorkbook(exctFileStream);
			}catch(Exception e){
				//上传的文件可能是offic2007以上的版本,记录日志
				logger.info("可能上传offic2003以下的版本");
				workbook = new HSSFWorkbook(exctFileStream);
			}
			List<AnnualMeetingGameQuestion> analyzingExctData = analyzingExctData(workbook);

			//封装解析出来的所有exct数据
			annualMeetingGameQuestionVo.setAllQuestions(analyzingExctData);
			annualMeetingGameQuestionVo.setState(1);
			annualMeetingGameQuestionVo.setStateInfo("success");

			return annualMeetingGameQuestionVo;
		} catch (Exception e) {
			logger.error("文件加载异常，"+e.getMessage());
			annualMeetingGameQuestionVo.setState(2);
			annualMeetingGameQuestionVo.setStateInfo("文件加载异常"+e.getMessage());
			return annualMeetingGameQuestionVo;
		} finally {
			if(workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					logger.warn("workbook资源没有关闭");
				}
			}
		}
	}

	/**
	 * 解析Excel表格数据
	 * @param workbook
	 * @return List<AnnualMeetingGameQuestion>
	 * @throws Exception
	 */
	private List<AnnualMeetingGameQuestion> analyzingExctData(Workbook workbook) throws Exception{
		//获得第一个sheet
		Sheet sheetAt = workbook.getSheetAt(0);

		List<AnnualMeetingGameQuestion> questionsList = HeapVariable.questionsList;
		int id = questionsList.size()+1 ;
		int lastRowNum = sheetAt.getLastRowNum();
		HeapVariable.atomic.addAndGet(lastRowNum);
		List<AnnualMeetingGameQuestion> annualMeetingGameQuestions = new ArrayList<AnnualMeetingGameQuestion>();
		//解析第一个sheet数据
		for (int i = 2 ; i<lastRowNum+1 ; i++) {
			Row row = sheetAt.getRow(i);
			//解析每行数据
			AnnualMeetingGameQuestion annualMeetingGameQuestion = new AnnualMeetingGameQuestion();
			annualMeetingGameQuestion.setId(id++);
			annualMeetingGameQuestion.setTopic(row.getCell(0).toString());
			annualMeetingGameQuestion.setAnswerOne(row.getCell(1).toString());
			annualMeetingGameQuestion.setAnswerTwo(row.getCell(2).toString());
			annualMeetingGameQuestion.setAnswerThree(row.getCell(3).toString());
			annualMeetingGameQuestion.setAnswerFour(row.getCell(4).toString());
			structureRightAnswer(annualMeetingGameQuestions, row, annualMeetingGameQuestion);
		}

		return annualMeetingGameQuestions;
	}

	private void structureRightAnswer(List<AnnualMeetingGameQuestion> annualMeetingGameQuestions, Row row,
			AnnualMeetingGameQuestion annualMeetingGameQuestion) {
		String rightAnswerStr = row.getCell(5).toString();
		byte rightAnswer = (byte)(Float.valueOf(rightAnswerStr).floatValue());
		annualMeetingGameQuestion.setRightAnswer(rightAnswer);
		annualMeetingGameQuestions.add(annualMeetingGameQuestion);
	}

	@Override
	public Boolean updataData(AnnualMeetingGameQuestionVo vo,Integer id,String fieldValue,String fieldName) throws Exception{
		//获得缓存中的所有数据
		List<AnnualMeetingGameQuestion> allQuestions = vo.getAllQuestions();
		//遍历数据，查找需要修改的字段
		for (AnnualMeetingGameQuestion annualMeetingGameQuestion : allQuestions) {
			if(annualMeetingGameQuestion.getId() == id){
				Class<? extends AnnualMeetingGameQuestion> clazz = annualMeetingGameQuestion.getClass();
				//查找字段
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					if(fieldName.equals(fields[i].getName())){
						Method method = clazz.getMethod("set"+StringUtil.initialsUpper(fieldName),fields[i].getType() );
						if(!fieldValue.equals("rightAnswer")){
							method.invoke(annualMeetingGameQuestion, fieldValue);
						}else{
							method.invoke(annualMeetingGameQuestion, (byte)(Float.valueOf(fieldValue).floatValue()));
						}
						break;
					}
				}
				return true;
			}
		}

		return false;
	}


	/**
	 * 获得所有年会问题数据
	 */
	 @Override
	 public AnnualMeetingGameQuestionVo getAllGameQuestion() {
		try {
			List<AnnualMeetingGameQuestion> selectAll = HeapVariable.questionsList;
			return new AnnualMeetingGameQuestionVo(selectAll, AnnualMeetingGameQuestionVo.STATE_NUM_SUCCESS, "success", selectAll.size());
		} catch (Exception e) {
			logger.error("数据查询异常");
			e.printStackTrace();
			return new AnnualMeetingGameQuestionVo(null, AnnualMeetingGameQuestionVo.STATE_NUM_EXCEPTION, "数据查询异常" , 0);
		}

	}

	@Override
	public Boolean savaAnnualMeetingGameQuestion(List<AnnualMeetingGameQuestion> annualMeetingGameQuestions) {
		try {
			//InitData.initTable();
			annualMeetingGameQuestionMapper.insertQuestion(annualMeetingGameQuestions);
			InitData.initQuestion();
			logger.info("插入成功！");
			return true;
		} catch (Exception e) {
			logger.error("插入失败！");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void clearAllData() {
		InitData.initTable();
		InitData.initQuestion();
	}

	@Override
	public TimeParam getTimeParam() {
		Timestamp beginTime = HeapVariable.beginTime;
		TimeParam param = new TimeParam();
		if(beginTime != null){
			param.setBeginTime(beginTime.getTime());
			param.setBeginTimeStr(TimeFormatUtil.getTimeStr(new Date(beginTime.getTime())));
		}else{
			param.setBeginTime(0L);
			param.setBeginTimeStr("");
		}
		param.setIntervalTime(HeapVariable.intervalSecond);

		return param;
	}

}
