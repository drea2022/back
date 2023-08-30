package com.xuhui.OJ.judge.strategy;

import com.xuhui.OJ.model.dto.question.JudgeCase;
import com.xuhui.OJ.model.dto.questionSubmit.JudgeInfo;
import com.xuhui.OJ.model.entity.Question;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略模式中需要的参数）
 */
@Data
public class JudgeContext {
   private JudgeInfo judgeInfo;

   private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit questionSubmit;
 }
