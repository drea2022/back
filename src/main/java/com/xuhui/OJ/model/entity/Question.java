package com.xuhui.OJ.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 题目
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Question implements Serializable {

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
    private String tags;

    /**
     * 题目答案
     */
    private String answer;

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
     * 判题用例（JSON 数组）
     */
    @TableField("judgeCase")
    private String judgeCase;

    /**
     * 判题配置（JSON 数组）
     */
    @TableField("judgeConfig")
    private String judgeConfig;

    /**
     * 点赞数
     */
    @TableField("thumbNum")
    private Integer thumbNum;

    /**
     * 收藏数
     */
    @TableField("favourNum")
    private Integer favourNum;

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


}
