package com.edmi.controller;

import com.alibaba.fastjson.JSONObject;

import com.edmi.utils.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class JohnsonController {



    @RequestMapping(value="/dataCrawl.do",method = {RequestMethod.GET,RequestMethod.POST},produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject index(@RequestParam Map<String, Object> param) {
        JSONObject json = new JSONObject();
        String className = "";
        Object a = SpringContextUtil.getBean(className);


        json.put("code",0);
        json.put("msg","系统已经收到您的请求，正在执行抓取，请勿重复请求，谢谢！");
        return json;
    }

}
