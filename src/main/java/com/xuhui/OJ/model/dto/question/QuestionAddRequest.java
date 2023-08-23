package com.xuhui.OJ.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组,难易程度区分）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例（JSON 数组）
     */
    @TableField("judgeCase")
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（JSON 数组）
     */
    @TableField("judgeConfig")
    private JudgeConfig judgeConfig;

}