package com.pst.picture.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.pst.picture.enums.AllowPictureType;
import com.pst.picture.exception.UploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Date;

/**
 * 阿里云OSS服务的工具类
 *
 * @author RETURN
 * @date 2020/8/13 22:25
 */
@Slf4j
public class OssUtil {
    private static OSS ossClient;
    /**
     * oss bucket的名字
     */
    private static final String BUCKET_NAME = "album-pictures";
    /**
     * 个人路径host
     */
    private static final String HOST = "p.return76.top";
    /**
     * oss操作参数
     */
    private static final String OSS_PROCESS = "x-oss-process";

    /**
     * 初始化ossClient
     */
    private static void init() {
        String accessKeySecret = "NVA0p7HCd9PwoYSAQ2cqoHJnGmNGcC";
        String accessKeyId = "LTAI4G1DbXmGEhc4AGfttm5B";
        String endpoint = "oss-cn-zhangjiakou.aliyuncs.com";
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 上传图片方法
     * @param picture 图片
     * @param userId 用户id(用来确保用户路径)
     * @return 返回图片原图路径
     */
    public static String uploadPicture(MultipartFile picture, Long userId) {
        init();
        String url = null;
        String pictureName = picture.getOriginalFilename();
        try {
            //校验图片
            log.info("图片原名为:{}",pictureName);
            log.info("图片格式为:{}",picture.getContentType());
            AllowPictureType.checkType(picture.getContentType());
            //生成该用户上传的图片路径
            String pathWithName = generatePath(userId, pictureName);
            //上传到oss
            ossClient.putObject(BUCKET_NAME
                    , pathWithName
                    , new ByteArrayInputStream(picture.getBytes()));
            log.info("{}上传成功", pictureName);
            //生成自定域名的url
            url = generateCnameUrl(pathWithName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UploadException("图片上传失败");
        } finally {
            destroy();
        }
        return url;
    }

    /**
     * 生成个人域名的路径(用来替代aliyun的特长url)
     * @param path 图片相对路径
     * @return 返回url
     */
    private static String generateCnameUrl(String path) {
        return URLUtil.completeUrl(HOST, path);
    }

    /**
     * 生成图片完整路径
     * @param userId 用户id
     * @param pictureName 图片真实名字
     * @return 返回时间_图片名格式的url
     */
    private static String generatePath(Long userId, String pictureName) {
        StringBuilder pathName = new StringBuilder(String.valueOf(userId));
        String now = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        pathName.append("/")
                .append(now)
                .append("_")
                .append(pictureName);
        return pathName.toString();
    }

    /**
     * 获取缩略图路径，AliYunOss提供的图片处理的接口
     * @param url 原图路径
     * @return 缩略图路径
     */
    public static String getMiniPath(String url) {
        return UrlBuilder.ofHttp(url, null).addQuery(OSS_PROCESS, "style/mini").build();
    }

    /**
     * 获取单张图片的数据(长宽，大小，格式)，AliYunOss提供的图片信息接口
     * @param url 原图路径
     * @return 返回JSON格式的图片信息
     */
    public static JSONObject getInfo(String url) {
        RestTemplate client = new RestTemplate();
        //获取图片信息的url
        String infoUrl = UrlBuilder.ofHttp(url, null).addQuery(OSS_PROCESS, "image/info").build();
        log.info("请求图片信息的url:{}",infoUrl);
        //请求图片信息
        JSONObject jsonObject = client.getForObject(URI.create(infoUrl), JSONObject.class);
        log.info("获取图片信息成功");
        Assert.notNull(jsonObject,"请求的图片信息url错误");
        //格式化信息
        return JSONUtil.createObj()
                .set("height", jsonObject.getByPath("ImageHeight.value"))
                .set("width", jsonObject.getByPath("ImageWidth.value"))
                .set("size", jsonObject.getByPath("FileSize.value"))
                .set("format", jsonObject.getByPath("Format.value"));
    }

    /**
     * 关闭ossClient
     */
    public static void destroy() {
        if (null != ossClient) {
            ossClient.shutdown();
        }
    }

}
