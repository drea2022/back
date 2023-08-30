package com.xuhui.OJ.judge.codesandbox.impl;

import com.xuhui.OJ.judge.codesandbox.CodeSandBox;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeRequest;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeResponse;

/**
 * 远程代码沙箱（实际使用沙箱）
 */
public class RemoteCodeSandBox implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
