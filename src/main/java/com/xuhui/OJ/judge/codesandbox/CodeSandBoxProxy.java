package com.xuhui.OJ.judge.codesandbox;


import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeRequest;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox {

    private CodeSandBox codeSandBox;
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：{}",executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse=codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：{}",executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
