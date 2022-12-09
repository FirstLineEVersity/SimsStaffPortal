package com.sims.staffportal.properties;

public class SubAnswerModel {
    int answerId;
    int subAnswerId;
    String subAnswerDesc;
    int selectedsubanswerid;
    String inputanswer;

    public SubAnswerModel(int answerId, int subAnswerId, String subAnswerDesc, int selectedsubanswerid, String inputanswer) {
        this.answerId = answerId;
        this.subAnswerId = subAnswerId;
        this.subAnswerDesc = subAnswerDesc;
        this.selectedsubanswerid = selectedsubanswerid;
        this.inputanswer = inputanswer;
    }

    public int getSelectedsubanswerid() {
        return selectedsubanswerid;
    }

    public void setSelectedsubanswerid(int selectedsubanswerid) {
        this.selectedsubanswerid = selectedsubanswerid;
    }

    public String getInputanswer() {
        return inputanswer;
    }

    public void setInputanswer(String inputanswer) {
        this.inputanswer = inputanswer;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getSubAnswerId() {
        return subAnswerId;
    }

    public void setSubAnswerId(int subAnswerId) {
        this.subAnswerId = subAnswerId;
    }

    public String getSubAnswerDesc() {
        return subAnswerDesc;
    }

    public void setSubAnswerDesc(String subAnswerDesc) {
        this.subAnswerDesc = subAnswerDesc;
    }
}
