package com.pst.picture.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.pst.picture.annotation.PassToken;
import com.pst.picture.exception.AuthenticationException;
import com.pst.picture.utils.JwtUtil;
import com.pst.picture.utils.TokenCacheUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * token拦截器，拦截所有请求
 * 放行未被controller捕获的请求和含有PassToken注解的请求
 * @author RETURN
 * @date 2020/8/13 22:26
 */
public class TokenInterceptor implements HandlerInterceptor {

    private static final String TOKEN_NAME = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果该url没有进入controller的方法
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //如果有不需要验证token的注解
        if (handlerMethod.hasMethodAnnotation(PassToken.class)){
            return true;
        }

        String token = request.getHeader(TOKEN_NAME);
        if (null == token){
            throw new AuthenticationException("无token,请重新登录");
        }
        //验证token
        Map<String, Claim> verify = JwtUtil.verify(token);
        String userId = verify.get("userId").asString();
        // todo 判断userId是否是数字
        String cacheToken = TokenCacheUtil.get(userId);
        System.out.println("token:"+token);
        System.out.println("cacheToken:"+cacheToken);
        if (!token.equals(cacheToken)){
            throw new AuthenticationException("token已失效,请重新登录");
        }
        request.setAttribute("userId",userId);
        return true;
    }
}
