package com.hit.server;

import Algorithems.IAlgoScheduling;

import java.util.Map;

public class Request {

    private String action;

    private Map<String, Object> body;
    //private IAlgoScheduling algorithem;

    public Request(String action, Map<String, Object> body) {
        this.action = action;
        this.body = body;
        //this.algorithem=algorithem;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }


//    public IAlgoScheduling getAlgorithem() {
//        return algorithem;
//    }
//
//    public void setAlgorithem(IAlgoScheduling algorithem) {
//        this.algorithem = algorithem;
//    }
}
