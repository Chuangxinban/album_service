package com.pst.picture.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pst.picture.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * token工具类，包括token的签发和认证
 * @author RETURN
 * @date 2020/8/13 22:27
 */
@Slf4j
public class JwtUtil {

    private static final String SECRET = "secret";

    public static String sign(String claimName,String payLoad){
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        Map<String,Object> header = new HashMap<>(2);
        header.put("alg","HS256");
        header.put("typ","JWT");
        return JWT.create()
                .withHeader(header)
                .withJWTId(UUID.randomUUID().toString())
                .withClaim(claimName,payLoad)
                .sign(algorithm);
    }

    public static Map<String, Claim> verify(String token){
        DecodedJWT jwt = null;
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        try {
            log.debug("尝试验证token：{}",token);
            jwt = JWT.require(algorithm).build().verify(token);
        }catch (JWTVerificationException e){
            throw new AuthenticationException("token验证失败");
        }

        return jwt.getClaims();
    }

    public static String signUser(Long userId){
        return sign("userId", String.valueOf(userId));
    }


}
