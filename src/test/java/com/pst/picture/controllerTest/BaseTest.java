package com.pst.picture.controllerTest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.ehcache.Cache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author RETURN
 * @date 2020/9/4 15:28
 */
@SpringBootTest
@TestPropertySource(locations = {"classpath:application.yml"})
@AutoConfigureMockMvc
public class BaseTest {

    @Resource
    protected MockMvc mockMvc;

    @Resource(name = "verifyCode")
    protected Cache<String, String> verifyCache;
    @Resource(name = "tokenCache")
    protected Cache<String, String> tokenCache;

    protected static final Map<String, String> params = new HashMap<>();


    protected void before() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/user/loginPwd")
                        .param("email", "543851436@qq.com")
                        .param("password", "ph123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        ).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JSONObject responseJSON = JSONUtil.parseObj(response);
        JSONObject data = responseJSON.getJSONObject("data");
        params.putIfAbsent("userId", data.getStr("id"));
    }

}