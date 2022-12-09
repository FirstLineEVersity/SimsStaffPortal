package com.sims.staffportal.properties;

import java.util.List;

public class AnswerModel {
    int answerId;
    int answerMode;//1 - single selection, 2 multiple selection, 3 input text
    String answerDesc;
    List<SubAnswerModel> subAnswerModelList;
    int selectedsubanswerid;

    public AnswerModel(int answerId, int answerMode, String answerDesc, List<SubAnswerModel> subAnswerModelList, int selectedsubanswerid) {
        this.answerId = answerId;
        this.answerMode = answerMode;
        this.answerDesc = answerDesc;
        this.subAnswerModelList = subAnswerModelList;
        this.selectedsubanswerid = selectedsubanswerid;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getAnswerMode() {
        return answerMode;
    }

    public void setAnswerMode(int answerMode) {
        this.answerMode = answerMode;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public List<SubAnswerModel> getSubAnswerModelList() {
        return subAnswerModelList;
    }

    public void setSubAnswerModelList(List<SubAnswerModel> subAnswerModelList) {
        this.subAnswerModelList = subAnswerModelList;
    }

    public int getSelectedsubanswerid() {
        return selectedsubanswerid;
    }

    public void setSelectedsubanswerid(int selectedsubanswerid) {
        this.selectedsubanswerid = selectedsubanswerid;
    }
}
