package com.wang.spring_security_framework.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RestfulJumpController {
    @RequestMapping("/toR1")
    public String toR1Page() {
        String url = "/r/r1";
        String code = "204";
        String msg = "即将跳转!";
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("url", url);
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return JSON.toJSONString(resultMap);
    }

    @RequestMapping("/toR2")
    public String toR2Page() {
        String url = "/r/r2";
        String code = "204";
        String msg = "即将跳转!";
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("url", url);
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return JSON.toJSONString(resultMap);
    }

    @RequestMapping("/toR3")
    public String toR3Page() {
        String url = "/r/r3";
        String code = "204";
        String msg = "即将跳转!";
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("url", url);
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return JSON.toJSONString(resultMap);
    }

}
