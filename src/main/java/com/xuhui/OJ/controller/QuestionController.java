package com.xuhui.OJ.controller;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.xuhui.OJ.annotation.AuthCheck;
import com.xuhui.OJ.common.BaseResponse;
import com.xuhui.OJ.common.DeleteRequest;
import com.xuhui.OJ.common.ErrorCode;
import com.xuhui.OJ.common.ResultUtils;
import com.xuhui.OJ.constant.UserConstant;
import com.xuhui.OJ.exception.BusinessException;
import com.xuhui.OJ.exception.ThrowUtils;
import com.xuhui.OJ.model.dto.question.*;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.entity.User;
import com.xuhui.OJ.model.vo.QuestionVO;
import com.xuhui.OJ.service.IQuestionService;
import com.xuhui.OJ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 题目 前端控制器
 * </p>
 *
 * @author zhangxuhui
 * @since 2023-08-19
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

        @Resource
        private IQuestionService questionService;

        @Resource
        private UserService userService;

        private final static Gson GSON = new Gson();

        // region 增删改查

        /**
         * 创建
         *
         * @param questionAddRequest
         * @param request
         * @return
         */
        @PostMapping("/add")
        public BaseResponse<Long> addquestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
            if (questionAddRequest == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            Question question=new Question();
            BeanUtils.copyProperties(questionAddRequest, question);
            List<String> tags = questionAddRequest.getTags();
            if (tags != null) {
                question.setTags(JSONUtil.toJsonStr(tags));
            }
            JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
            if (judgeConfig != null){
                question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
            }
            List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
            if (judgeCase != null) {
                question.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
            }
            questionService.validQuestion(question, true);
            User loginUser = userService.getLoginUser(request);
            question.setUserId(loginUser.getId());
            question.setFavourNum(0);
            question.setThumbNum(0);
            boolean result = questionService.save(question);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
            long newquestionId = question.getId();
            return ResultUtils.success(newquestionId);
        }

        /**
         * 删除
         *
         * @param deleteRequest
         * @param request
         * @return
         */
        @PostMapping("/delete")
        public BaseResponse<Boolean> deletequestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
            if (deleteRequest == null || deleteRequest.getId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User user = userService.getLoginUser(request);
            long id = deleteRequest.getId();
            // 判断是否存在
            Question oldquestion = questionService.getById(id);
            ThrowUtils.throwIf(oldquestion == null, ErrorCode.NOT_FOUND_ERROR);
            // 仅本人或管理员可删除
            if (!oldquestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            boolean b = questionService.removeById(id);
            return ResultUtils.success(b);
        }

        /**
         * 更新（仅管理员）
         *
         * @param questionUpdateRequest
         * @return
         */
        @PostMapping("/update")
        @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
        public BaseResponse<Boolean> updatequestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
            if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            Question question = new Question();
            BeanUtils.copyProperties(questionUpdateRequest, question);
            List<String> tags = questionUpdateRequest.getTags();
            if (tags != null) {
                question.setTags(GSON.toJson(tags));
            }
            JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
            if (judgeConfig != null){
                question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
            }
            List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
            if (judgeCase != null) {
                question.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
            }
            // 参数校验
            questionService.validQuestion(question, false);
            long id = questionUpdateRequest.getId();
            // 判断是否存在
            Question oldquestion = questionService.getById(id);
            ThrowUtils.throwIf(oldquestion == null, ErrorCode.NOT_FOUND_ERROR);
            boolean result = questionService.updateById(question);
            return ResultUtils.success(result);
        }

        /**
         * 根据 id 获取
         *
         * @param id
         * @return
         */
        @GetMapping("/get/vo")
        public BaseResponse<QuestionVO> getquestionVOById(long id, HttpServletRequest request) {
            if (id <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            Question question = questionService.getById(id);
            if (question == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
            }
            return ResultUtils.success(questionService.getQuestionVO(question, request));
        }

        /**
         * 分页获取列表（封装类）
         *
         * @param questionQueryRequest
         * @param request
         * @return
         */
        @PostMapping("/list/page/vo")
        public BaseResponse<Page<QuestionVO>> listquestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                           HttpServletRequest request) {
            long current = questionQueryRequest.getCurrent();
            long size = questionQueryRequest.getPageSize();
            // 限制爬虫
            ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
            Page<Question> questionPage = questionService.page(new Page<>(current, size),
                    questionService.getQueryWrapper(questionQueryRequest));
            return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
        }

        /**
         * 分页获取当前用户创建的资源列表
         *
         * @param questionQueryRequest
         * @param request
         * @return
         */
        @PostMapping("/my/list/page/vo")
        public BaseResponse<Page<QuestionVO>> listMyquestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                             HttpServletRequest request) {
            if (questionQueryRequest == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            User loginUser = userService.getLoginUser(request);
            questionQueryRequest.setUserId(loginUser.getId());
            long current = questionQueryRequest.getCurrent();
            long size = questionQueryRequest.getPageSize();
            // 限制爬虫
            ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
            Page<Question> questionPage = questionService.page(new Page<>(current, size),
                    questionService.getQueryWrapper(questionQueryRequest));
            return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
        }
        /**
         * 编辑（用户）
         *
         * @param questionEditRequest
         * @param request
         * @return
         */
        @PostMapping("/edit")
        public BaseResponse<Boolean> editquestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
            if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            Question question = new Question();
            BeanUtils.copyProperties(questionEditRequest, question);
            List<String> tags = questionEditRequest.getTags();
            if (tags != null) {
                question.setTags(GSON.toJson(tags));
            }
            JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
            if (judgeConfig != null){
                question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
            }
            List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
            if (judgeCase != null) {
                question.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
            }
            // 参数校验
            questionService.validQuestion(question, false);
            User loginUser = userService.getLoginUser(request);
            long id = questionEditRequest.getId();
            // 判断是否存在
            Question oldquestion = questionService.getById(id);
            ThrowUtils.throwIf(oldquestion == null, ErrorCode.NOT_FOUND_ERROR);
            // 仅本人或管理员可编辑
            if (!oldquestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            boolean result = questionService.updateById(question);
            return ResultUtils.success(result);
        }

    }



