package com.edmi.utils.http;


import com.edmi.utils.http.request.MultiPartFormRequest;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.request.UrlEncodedFormRequest;
import com.edmi.utils.http.response.Response;
import org.junit.Test;

/**
 * mailto:xiaobenma020@gmail.com
 */
public class HttpClientUtilTest {

    @Test
    public void doResponse() throws Exception {
        MultiPartFormRequest request = new MultiPartFormRequest("https://api.github.com/orgs/alibaba", RequestMethod.GET);
        Response response = HttpClientUtil.doRequest(request);
        System.out.println(response.getResponseText()); //response text
        System.out.println(response.getCode()); //response code
        System.out.println(response.getHeader("Set-Cookie"));
    }

}