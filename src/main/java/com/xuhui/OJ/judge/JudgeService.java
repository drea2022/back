package com.xuhui.OJ.judge;


import com.xuhui.OJ.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
