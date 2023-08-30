package com.xuhui.OJ.judge.codesandbox.impl;

import com.xuhui.OJ.judge.codesandbox.CodeSandBox;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeRequest;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（直接 调用网上封装好的沙箱接口）
 */
public class ThirdCodeSandBox implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
