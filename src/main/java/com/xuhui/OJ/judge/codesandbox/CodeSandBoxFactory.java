package com.xuhui.OJ.judge.codesandbox;

import com.github.mustachejava.Code;
import com.xuhui.OJ.judge.codesandbox.impl.ExampleCodeSandBox;
import com.xuhui.OJ.judge.codesandbox.impl.RemoteCodeSandBox;
import com.xuhui.OJ.judge.codesandbox.impl.ThirdCodeSandBox;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeRequest;
import com.xuhui.OJ.judge.codesandbox.mode.ExecuteCodeResponse;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandBoxFactory {
    /**
     * 使用静态工厂模式
     * @param type
     * @return
     */
    public  static CodeSandBox newInstance(String type){
        switch (type){
            case "example":
                return new  ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "third":
                return new ThirdCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }
}
