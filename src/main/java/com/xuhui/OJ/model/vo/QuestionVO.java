package com.xuhui.OJ.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.gson.reflect.TypeToken;
import com.xuhui.OJ.model.dto.question.JudgeConfig;
import com.xuhui.OJ.model.entity.Post;
import com.xuhui.OJ.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionVO {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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
     * 提交答案人数
     */
    @TableField("submitNum")
    private Integer submitNum;

    /**
     * 通过人数
     */
    @TableField("acceptedNum")
    private Integer acceptedNum;


    /**
     * 判题配置（JSON 数组）
     */
    @TableField("judgeConfig")
    private JudgeConfig judgeConfig;

    /**
     * 创建用户 id
     */
    @TableField("userId")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("isDelete")
    private Integer isDelete;
    /**
     * 创建题目人信息
     */
    private UserVO userVO;
    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig voJudgeConfig = questionVO.getJudgeConfig();
        if (voJudgeConfig != null){
            question.setJudgeConfig(JSONUtil.toJsonStr(voJudgeConfig));
        }
        return question;

    }

    /**
     * 对象转包装类
     *
     * @param post
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> tagList=JSONUtil.toList(question.getTags(),String.class);
        questionVO.setTags(tagList);
        String judgeConfigStr = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr,JudgeConfig.class));

        return questionVO;
    }
}
