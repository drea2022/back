package com.xuhui.OJ.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.xuhui.OJ.model.dto.question.JudgeCase;
import com.xuhui.OJ.model.dto.question.JudgeConfig;
import com.xuhui.OJ.model.dto.questionSubmit.JudgeInfo;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.enums.JudgeInfoMEssageEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList=judgeContext.getJudgeCaseList();
        //根据沙箱执行结果，设置题目的判题状态和信息
        JudgeInfoMEssageEnum judgeInfoMEssageEnum=JudgeInfoMEssageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse=new JudgeInfo();
        judgeInfoResponse.setMessage(judgeInfoMEssageEnum.getValue());
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        //判断输出和和预期输出是否数量一致
        if (outputList.size()!=inputList.size()){
            judgeInfoMEssageEnum=JudgeInfoMEssageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMEssageEnum.getValue());
            return judgeInfoResponse;
        }
        //依次判断每次的输出和预期输出是否一致
        for(int i=0;i<=judgeCaseList.size();i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))){
                judgeInfoMEssageEnum=JudgeInfoMEssageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMEssageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        //判断题目限制

        String judgeConfigStr=question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit){
            judgeInfoMEssageEnum=JudgeInfoMEssageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMEssageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time > needTimeLimit){
            judgeInfoMEssageEnum=JudgeInfoMEssageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMEssageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMEssageEnum.getValue());
        return judgeInfoResponse;
    }
}
