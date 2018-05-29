package com.edmi;

import com.alibaba.fastjson.JSONObject;
import com.edmi.entity.github.ICO_Github_Organization;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.request.*;
import com.edmi.utils.http.response.Response;
import com.jcabi.github.*;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;

public class Test {

    public static void main(String[] args) throws Exception {

        for(int i = 0;i<100;i++){
            System.out.println( RandomUtils.nextInt(1,3));
        }
    }
}
