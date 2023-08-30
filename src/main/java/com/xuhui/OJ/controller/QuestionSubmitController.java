package com.xuhui.OJ.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuhui.OJ.common.BaseResponse;
import com.xuhui.OJ.common.ErrorCode;
import com.xuhui.OJ.common.ResultUtils;
import com.xuhui.OJ.exception.BusinessException;
import com.xuhui.OJ.exception.ThrowUtils;
import com.xuhui.OJ.judge.JudgeService;
import com.xuhui.OJ.model.dto.postthumb.PostThumbAddRequest;
import com.xuhui.OJ.model.dto.question.QuestionQueryRequest;
import com.xuhui.OJ.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.xuhui.OJ.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import com.xuhui.OJ.model.entity.User;
import com.xuhui.OJ.model.vo.QuestionSubmitVO;
import com.xuhui.OJ.model.vo.QuestionVO;
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
    /**
     * 分页获取列表（封装类）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionVOByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                     HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        //从数据库中查询到原始的分页信息
        Page<QuestionSubmit> questionPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser=userService.getLoginUser(request);
        //返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionPage, loginUser));
    }
}

