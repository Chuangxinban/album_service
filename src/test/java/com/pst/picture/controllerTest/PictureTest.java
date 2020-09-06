package com.pst.picture.controllerTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author RETURN
 * @date 2020/9/6 11:54
 */
@DisplayName("图片测试")
@Slf4j
public class PictureTest extends BaseTest {

    @DisplayName("获取图片列表测试")
    @Test
    void test1() throws Exception {
        before();

        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        ResultActions resultActions = mockMvc.perform(
                post("/pictures")
                        .param("pageNum", "1")
                        .param("pageSize", "5")
                        .param("albumId","1")
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

    @DisplayName("图片上传测试")
    @Test
    void test2() throws Exception {
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        MockMultipartFile multipartFile1 = new MockMultipartFile("pictures", "test1.jpg",
                "image/jpeg", "图片上传测试".getBytes());
        MockMultipartFile multipartFile2 = new MockMultipartFile("pictures", "test2.jpg",
                "image/jpeg", "图片上传测试".getBytes());

        ResultActions resultActions = mockMvc.perform(
                multipart("/pictures/upload")
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .param("albumId","1")
                        .header("token",token)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @DisplayName("图片移动，删除，恢复测试")
    @Test
    void test3() throws Exception {
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        ResultActions resultActions = mockMvc.perform(
                post("/pictures/move")
                        .param("ids","2,3,4")
                        .param("albumId","1")
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

    @DisplayName("获取图片详情测试")
    @Test
    void test4() throws Exception {
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        ResultActions resultActions = mockMvc.perform(
                post("/pictures/detailed")
                        .param("pictureId","2")
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

    @DisplayName("图片描述更新测试")
    @Test
    void test5() throws Exception {
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        ResultActions resultActions = mockMvc.perform(
                post("/pictures/descUpdate")
                        .param("pictureId","2")
                        .param("newDescription","测试描述")
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

}
