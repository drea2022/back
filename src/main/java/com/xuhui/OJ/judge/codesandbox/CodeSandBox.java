package com.xuhui.OJ.judge.codesandbox;

import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeRequest;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandBox {
    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
