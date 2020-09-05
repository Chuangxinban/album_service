package com.pst.picture.controllerTest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pst.picture.dao.AlbumMapper;
import com.pst.picture.entity.Album;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author RETURN
 * @date 2020/9/5 22:37
 */
@Slf4j
@DisplayName("相册测试")
@Transactional
public class AlbumTest extends BaseTest {

    @Resource
    AlbumMapper albumMapper;

    void before() throws Exception {
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

    @DisplayName("相册列表测试")
    @Test
    void test1() throws Exception {
        before();
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        ResultActions resultActions = mockMvc.perform(
                post("/albums")
                        .param("pageNum", "1")
                        .param("pageSize", "5")
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

    @DisplayName("创建相册测试")
    @Test
    void test2() throws Exception {
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        ResultActions resultActions = mockMvc.perform(
                post("/albums/create")
                        .param("name", "哈哈哈")
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
        // todo 创建相册好像未作名称校验
    }

    @DisplayName("删除相册测试")
    @Test
    void test3() throws Exception {
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","测试用相册");
        Album album = albumMapper.selectOne(queryWrapper);
        log.info("相册:{}",album);
        params.putIfAbsent("albumId", String.valueOf(album.getId()));
        String userId = params.get("userId");
        String token = tokenCache.get(userId);
        ResultActions resultActions = mockMvc.perform(
                post("/albums/delete")
                        .param("albumId", String.valueOf(album.getId()))
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
        //todo 删除相册有bug(暂时修复)
    }
}
