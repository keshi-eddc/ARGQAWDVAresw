package com.edmi;

import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.github.ICO_Github_Organization;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.request.*;
import com.edmi.utils.http.response.Response;
import com.jcabi.github.*;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class Test{



    public static void main(String[] args) throws Exception {

        String url = "http://ec2-18-216-242-223.us-east-2.compute.amazonaws.com:8091/blocktest/api/v1/icocrunch.io/ico/detail";
        String data = "{'https://icocrunch.io/ico/metal/':'2'}";
        JSONObject json = JSONObject.parseObject(data);

        Request request = new Request(url, RequestMethod.GET);
        request.addUrlParam("solution_data",json);

        /*MultiPartFormRequest request = new MultiPartFormRequest(url,RequestMethod.POST);
        request.addPart("solution_data",json);*/

        Response response = HttpClientUtil.doRequest(request);
        System.out.println(response.getResponseText());
    }

}
