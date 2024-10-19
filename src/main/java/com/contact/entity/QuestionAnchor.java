package com.contact.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionAnchor {
    private Map<Integer, List<String>> anchorMapList = new LinkedHashMap<>();
    private Map<Integer, List<String>> qnMapList = new LinkedHashMap<>();
    private Map<Integer, List<String>> socialLinkUpdate = new LinkedHashMap<>();

    private Map<String, String> link = new LinkedHashMap<>();
    private Map<String, String> question = new LinkedHashMap<>();

    
    public QuestionAnchor() {}


    public Map<Integer, List<String>> getAnchorMapList() {
        return anchorMapList;
    }


    public void setAnchorMapList(Map<Integer, List<String>> anchorMapList) {
        this.anchorMapList = anchorMapList;
    }


    public Map<Integer, List<String>> getQnMapList() {
        return qnMapList;
    }


    public void setQnMapList(Map<Integer, List<String>> qnMapList) {
        this.qnMapList = qnMapList;
    }


    public Map<String, String> getLink() {
        return link;
    }


    public void setLink(Map<String, String> link) {
        this.link = link;
    }


    public Map<String, String> getQuestion() {
        return question;
    }


    public void setQuestion(Map<String, String> question) {
        this.question = question;
    }


    public Map<Integer, List<String>> getSocialLinkUpdate() {
        return socialLinkUpdate;
    }


    public void setSocialLinkUpdate(Map<Integer, List<String>> socialLinkUpdate) {
        this.socialLinkUpdate = socialLinkUpdate;
    }

    
    


}











