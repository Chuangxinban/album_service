package com.pst.picture.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pst.picture.exception.AuthenticationException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * token工具类，包括token的签发和认证
 * @author RETURN
 * @date 2020/8/13 22:27
 */
public class JwtUtil {

    private static final String SECRET = "secret";
    public static final long EXP =15 * 60 * 1000;

    public static String sign(String claimName,String payLoad){
        Date exp = new Date(System.currentTimeMillis() + EXP);
        Date iat = new Date(System.currentTimeMillis());
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        Map<String,Object> header = new HashMap<>(2);
        header.put("alg","HS256");
        header.put("typ","JWT");
        return JWT.create()
                .withHeader(header)
                .withJWTId(UUID.randomUUID().toString())
                .withExpiresAt(exp)
                .withIssuedAt(iat)
                .withClaim(claimName,payLoad)
                .sign(algorithm);
    }

    public static Map<String, Claim> verify(String token){
        DecodedJWT jwt = null;
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        try {
            jwt = JWT.require(algorithm).build().verify(token);
        }catch (JWTVerificationException e){
            throw new AuthenticationException("token验证失败");
        }

        return jwt.getClaims();
    }

    public static String signUser(String userId){
        return sign("userId",userId);
    }


}
