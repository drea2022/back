package com.xuhui.OJ.controller;


import com.xuhui.OJ.common.BaseResponse;
import com.xuhui.OJ.common.ErrorCode;
import com.xuhui.OJ.common.ResultUtils;
import com.xuhui.OJ.exception.BusinessException;
import com.xuhui.OJ.model.dto.postthumb.PostThumbAddRequest;
import com.xuhui.OJ.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import com.xuhui.OJ.model.entity.User;
import com.xuhui.OJ.service.IQuestionSubmitService;
import com.xuhui.OJ.service.PostThumbService;
import com.xuhui.OJ.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 题目提交 前端控制器
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-19
 */
@RestController
@RequestMapping("/question-submit")
public class QuestionSubmitController {
    @Resource
    private IQuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     *
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                         HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }
}

