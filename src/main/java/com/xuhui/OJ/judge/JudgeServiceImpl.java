package com.xuhui.OJ.judge;

import cn.hutool.json.JSONUtil;
import com.xuhui.OJ.common.ErrorCode;
import com.xuhui.OJ.exception.BusinessException;
import com.xuhui.OJ.judge.codesandbox.CodeSandBox;
import com.xuhui.OJ.judge.codesandbox.CodeSandBoxFactory;
import com.xuhui.OJ.judge.codesandbox.CodeSandBoxProxy;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeRequest;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeResponse;
import com.xuhui.OJ.judge.strategy.DefaultJudgeStrategy;
import com.xuhui.OJ.judge.strategy.JudgeContext;
import com.xuhui.OJ.judge.strategy.JudgeStrategy;
import com.xuhui.OJ.model.dto.question.JudgeCase;
import com.xuhui.OJ.model.dto.question.JudgeConfig;
import com.xuhui.OJ.model.dto.questionSubmit.JudgeInfo;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import com.xuhui.OJ.model.enums.JudgeInfoMEssageEnum;
import com.xuhui.OJ.model.enums.QuestionSubmitLangueEnum;
import com.xuhui.OJ.model.enums.QuestionSubmitStatusEnum;
import com.xuhui.OJ.model.vo.QuestionSubmitVO;
import com.xuhui.OJ.service.IQuestionService;
import com.xuhui.OJ.service.IQuestionSubmitService;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService{

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private IQuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManage judgeManage;

    @Resource
    private IQuestionService questionService;
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //传入题目提交id
        QuestionSubmit questionSubmit=questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        Long questionId=questionSubmit.getQuestionId();
        Question question=questionService.getById(questionId);
        if (question == null ){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        //判断题目的提交状态，如果不是等待中，就不需要重复执行
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判题中");
        }
        //更改判题状态
        QuestionSubmit questionSubmitUpdate=new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        //调用代码沙箱，获取执行结果
        CodeSandBox codeSandBox= CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList =judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language)
                .build();
        ExecuteCodeResponse executeCodeResponse=codeSandBox.executeCode(executeCodeRequest);
        JudgeContext judgeContext=new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManage.doJudge(judgeContext);
        //更改判题状态
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        QuestionSubmit questionSubmitResponse = questionSubmitService.getById(questionId);
        return questionSubmitResponse;
    }
}
