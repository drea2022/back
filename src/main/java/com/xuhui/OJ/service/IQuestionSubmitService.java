package com.xuhui.OJ.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xuhui.OJ.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import com.xuhui.OJ.model.entity.User;

/**
 * <p>
 * 题目提交 服务类
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-19
 */
public interface IQuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

}
