package com.xuhui.OJ.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuhui.OJ.common.ErrorCode;
import com.xuhui.OJ.constant.CommonConstant;
import com.xuhui.OJ.exception.BusinessException;
import com.xuhui.OJ.judge.JudgeService;
import com.xuhui.OJ.mapper.QuestionSubmitMapper;
import com.xuhui.OJ.model.dto.question.JudgeCase;
import com.xuhui.OJ.model.dto.question.JudgeConfig;
import com.xuhui.OJ.model.dto.question.QuestionQueryRequest;
import com.xuhui.OJ.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.xuhui.OJ.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import com.xuhui.OJ.model.entity.User;
import com.xuhui.OJ.model.enums.QuestionSubmitLangueEnum;
import com.xuhui.OJ.model.enums.QuestionSubmitStatusEnum;
import com.xuhui.OJ.model.vo.QuestionSubmitVO;
import com.xuhui.OJ.model.vo.QuestionVO;
import com.xuhui.OJ.model.vo.UserVO;
import com.xuhui.OJ.service.IQuestionService;
import com.xuhui.OJ.service.IQuestionSubmitService;
import com.xuhui.OJ.service.UserService;
import com.xuhui.OJ.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    @Resource
    private UserService userService;
    @Resource
    @Lazy
    private JudgeService judgeService;
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
        Long questionSubmitId = questionSubmit.getId();
        //todo 执行判题服务
        CompletableFuture.runAsync(()->{
            judgeService.doJudge(questionSubmitId);
        });
        return  questionSubmitId;
    }

    /**
     * 获取查询包装类（用户根据某个字段去查询 ）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        long userId=loginUser.getId();
        //处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser) ) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmits = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmits)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOS = questionSubmits.stream().map(questionSubmit -> {
            return getQuestionSubmitVO(questionSubmit, loginUser);
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOS);
        return questionSubmitVOPage;
    }
}
