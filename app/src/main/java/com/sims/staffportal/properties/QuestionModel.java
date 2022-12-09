package com.sims.staffportal.properties;

import java.util.List;

public class QuestionModel {
    int id;
    int questionId;
    String question;
    int nubOfOptions;
    List<AnswerModel> answerModelList;
    int selectedOption = 0;
    int selectedOptionMode = 0;
    int selectedOptionSub = 0;
    int selectedSubAnsPos = 0;
    String inputAns ="";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuestionModel(int id, int questionId, String question, int nubOfOptions, List<AnswerModel> answerModelList, int selectedOption, int selectedOptionMode, int selectedOptionSub, int selectedSubAnsPos, String inputAns) {
        this.id = id;
        this.questionId = questionId;
        this.question = question;
        this.nubOfOptions = nubOfOptions;
        this.answerModelList = answerModelList;
        this.selectedOption = selectedOption;
        this.selectedOptionMode = selectedOptionMode;
        this.selectedOptionSub = selectedOptionSub;
        this.selectedSubAnsPos = selectedSubAnsPos;
        this.inputAns = inputAns;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNubOfOptions() {
        return nubOfOptions;
    }

    public void setNubOfOptions(int nubOfOptions) {
        this.nubOfOptions = nubOfOptions;
    }

    public List<AnswerModel> getAnswerModelList() {
        return answerModelList;
    }

    public void setAnswerModelList(List<AnswerModel> answerModelList) {
        this.answerModelList = answerModelList;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

    public int getSelectedOptionMode() {
        return selectedOptionMode;
    }

    public void setSelectedOptionMode(int selectedOptionMode) {
        this.selectedOptionMode = selectedOptionMode;
    }

    public int getSelectedOptionSub() {
        return selectedOptionSub;
    }

    public void setSelectedOptionSub(int selectedOptionSub) {
        this.selectedOptionSub = selectedOptionSub;
    }

    public int getSelectedSubAnsPos() {
        return selectedSubAnsPos;
    }

    public void setSelectedSubAnsPos(int selectedSubAnsPos) {
        this.selectedSubAnsPos = selectedSubAnsPos;
    }

    public String getInputAns() {
        return inputAns;
    }

    public void setInputAns(String inputAns) {
        this.inputAns = inputAns;
    }
}
