package com.xuhui.OJ.model.dto.questionSubmit;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xuhui.OJ.common.PageRequest;
import com.xuhui.OJ.model.dto.question.JudgeCase;
import com.xuhui.OJ.model.dto.question.JudgeConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 编程语音
     */
    private String language;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 题目id
     */
    @TableField("questionId")
    private Long questionId;
    /**
     * 用户id
     */
    private Long userId;
}