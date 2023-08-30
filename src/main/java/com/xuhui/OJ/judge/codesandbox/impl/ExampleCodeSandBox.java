package com.xuhui.OJ.judge.codesandbox.impl;

import com.xuhui.OJ.judge.codesandbox.CodeSandBox;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeRequest;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeResponse;
import com.xuhui.OJ.model.dto.questionSubmit.JudgeInfo;
import com.xuhui.OJ.model.enums.JudgeInfoMEssageEnum;
import com.xuhui.OJ.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandBox implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList=executeCodeRequest.getInputList();

        ExecuteCodeResponse executeCodeResponse=new ExecuteCodeResponse();
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo=new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMEssageEnum.ACCEPTED.getValue());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
