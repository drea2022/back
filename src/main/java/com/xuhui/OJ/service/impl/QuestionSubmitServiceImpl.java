package com.xuhui.OJ.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuhui.OJ.common.ErrorCode;
import com.xuhui.OJ.exception.BusinessException;
import com.xuhui.OJ.mapper.QuestionSubmitMapper;
import com.xuhui.OJ.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import com.xuhui.OJ.model.entity.User;
import com.xuhui.OJ.model.enums.QuestionSubmitLangueEnum;
import com.xuhui.OJ.model.enums.QuestionSubmitStatusEnum;
import com.xuhui.OJ.service.IQuestionService;
import com.xuhui.OJ.service.IQuestionSubmitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 题目提交 服务实现类
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-19
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements IQuestionSubmitService {
    @Resource
    private IQuestionService questionService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验语言是否合法
        String langue=questionSubmitAddRequest.getLanguage();
        QuestionSubmitLangueEnum langueEnum=QuestionSubmitLangueEnum.getEnumByValue(langue);
        if (langueEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question=questionService.getById(questionId);
        // 判断实体是否存在，根据类别获取实体
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //是否已提交题目
        long userId=loginUser.getId();
        QuestionSubmit questionSubmit=new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        //  设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }
        return  questionSubmit.getId();
    }




}
