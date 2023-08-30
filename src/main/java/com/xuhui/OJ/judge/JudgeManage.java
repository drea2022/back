package com.xuhui.OJ.judge;

import com.xuhui.OJ.judge.strategy.DefaultJudgeStrategy;
import com.xuhui.OJ.judge.strategy.JavaJudgeStrategy;
import com.xuhui.OJ.judge.strategy.JudgeContext;
import com.xuhui.OJ.judge.strategy.JudgeStrategy;
import com.xuhui.OJ.model.dto.questionSubmit.JudgeInfo;
import com.xuhui.OJ.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

@Service
public class JudgeManage {

    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy=new DefaultJudgeStrategy();
        if ("java".equals(language)){
            judgeStrategy=new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
