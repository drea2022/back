package com.xuhui.OJ.model.dto.questionSubmit;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xuhui.OJ.model.entity.User;
import lombok.Data;

@Data
public class QuestionSubmitAddRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 编程语音
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;
    /**
     * 题目id
     */
    @TableField("questionId")
    private Long questionId;

}
