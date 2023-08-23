package com.xuhui.OJ.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.xuhui.OJ.model.dto.question.QuestionQueryRequest;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 题目 服务类
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-19
 */
public interface IQuestionService extends IService<Question> {
    /**
     * 校验
     *
     * @param Question
     * @param add
     */
    void validQuestion(Question Question, boolean add);

    /**
     * 获取查询条件
     *
     * @param QuestionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest QuestionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param Question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question Question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param QuestionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> QuestionPage, HttpServletRequest request);
}
