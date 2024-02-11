package com.cos.photogramstart.utills;

import java.util.Map;

public class Script {

    public static String back(String message){
        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert("+message+");");
        sb.append("history.back();");
        sb.append("</script>");
        return sb.toString();
    }
}
