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
 * 题目提交
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionSubmit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编程语音
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 数组）
     */
    @TableField("judgeInfo")
    private String judgeInfo;

    /**
     * 判题状态（0 -待判题、1 -判题中、2 -成功、 3 -失败）
     */
    private Integer status;

    /**
     * 题目id
     */
    @TableField("questionId")
    private Long questionId;

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
