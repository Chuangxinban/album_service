package com.pst.picture.controllerTest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pst.picture.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@DisplayName("用户测试")
public class UserTest extends BaseTest {

    @Resource
    private PictureService pictureService;

    @DisplayName("获取验证码测试")
    @Test
    void test1() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/user/verifyCode")
                .param("email", "543851436@qq.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        );
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(
                print()
        );
    }

    @DisplayName("邮箱登录测试")
    @Test
    void test2() throws Exception {
        String verifyCode = verifyCache.get("543851436@qq.com");

        ResultActions resultActions = mockMvc.perform(post("/user/loginEmail")
                .param("email", "543851436@qq.com")
                .param("verifyCode", verifyCode)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        );
        MvcResult mvcResult = resultActions.andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(
                print()
        );

        String response = mvcResult.getResponse().getContentAsString();
        JSONObject responseJSON = JSONUtil.parseObj(response);
        JSONObject data = responseJSON.getJSONObject("data");
        params.putIfAbsent("userId", data.getStr("id"));
    }

    @DisplayName("邮箱密码登录测试")
    @Test
    void test3() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                post("/user/loginPwd")
                        .param("email", "543851436@qq.com")
                        .param("password", "ph123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        );
        MvcResult mvcResult = resultActions.andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(
                print()
        );

        String response = mvcResult.getResponse().getContentAsString();
        JSONObject responseJSON = JSONUtil.parseObj(response);
        JSONObject data = responseJSON.getJSONObject("data");
        params.putIfAbsent("userId", data.getStr("id"));
    }

    @DisplayName("修改密码")
    @Test
    void test4() throws Exception {
        test1();
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        String verifyCode = verifyCache.get("543851436@qq.com");

        ResultActions resultActions = mockMvc.perform(
                post("/user/changePwd")
                        .param("email", "543851436@qq.com")
                        .param("password", "ph123")
                        .param("verifyCode",verifyCode)
                        .header("token", token)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        );
        MvcResult mvcResult = resultActions.andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(
                print()
        );
    }

    @DisplayName("修改昵称")
    @Test
    void test5() throws Exception {
        String userId = params.get("userId");
        String token = tokenCache.get(userId);

        ResultActions resultActions = mockMvc.perform(
                post("/user/changeNickname")
                        .param("nickname", "匿名用户")
                        .header("token",token)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        );
        MvcResult mvcResult = resultActions.andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(
                print()
        );
    }

    @DisplayName("修改头像")
    @Test
    void test6() throws Exception {
        String userId = params.get("userId");
        String token = tokenCache.get(userId);

        MockMultipartFile multipartFile = new MockMultipartFile("picture", "test.jpg",
                "image/jpeg", "test image content".getBytes());

        ResultActions resultActions = mockMvc.perform(
                multipart("/user/changeAvatar")
                        .file(multipartFile)
                        .header("token",token)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        );
        MvcResult mvcResult = resultActions.andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(
                print()
        );
    }
}
