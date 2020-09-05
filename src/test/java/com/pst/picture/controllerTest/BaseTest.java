package com.pst.picture.controllerTest;

import org.ehcache.Cache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

}