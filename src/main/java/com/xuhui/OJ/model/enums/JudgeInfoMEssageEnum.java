package com.xuhui.OJ.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息枚举
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum JudgeInfoMEssageEnum {

    ACCEPTED("成功","Accepted"),
    WRONG_ANSWER("答案错误","Wrong Answer"),
    COMPILE_ERROR("编译错误","Compile Error"),
    MEMORY_LIMIT_EXCEEDED("内存溢出","Memory Limit Exceeded"),
    TIME_LIMIT_EXCEEDED("超时","Time Limit Exceeded"),
    PRESENTATION_ERROR("展示错误","Presentation Error"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出","Output Limit Exceeded"),
    WAITING("等待中","Waiting"),
    DANGEROUS_OPERATION("危险操作","Dangerous Operation"),
    RUNTIME_ERROR("运行错误","Runtime Error"),
    SYSTEM_ERROR("系统错误","System Error");


    private final String text;

    private final String value;

    JudgeInfoMEssageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMEssageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMEssageEnum anEnum : JudgeInfoMEssageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
