package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.config.OssProperties;
import com.atguigu.guli.service.oss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author Connor
 * @date 2022/7/22
 */
@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(MultipartFile file, String module) {
        //获取上传文件的文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件名中的扩展名
        String fileName = UUID.randomUUID().toString().replace("-", "")
                + originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = module + new DateTime().toString("/yyyy/MM/dd/") + fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            InputStream inputStream = file.getInputStream();
            // 创建PutObject请求。
            ossClient.putObject(ossProperties.getBucketName(), objectName, inputStream);
            //返回对象存储中的文件路径
            return "https://" + ossProperties.getBucketName() + ".oss-cn-shanghai.aliyuncs.com/" + objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public void delete(String path, String module) {
        // 创建OSSClient实例。
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
            String objectName = path.substring(path.lastIndexOf(module));
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(ossProperties.getBucketName(), objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            throw new GuliException(ResultCodeEnum.FILE_DELETE_ERROR, oe);
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            throw new GuliException(ResultCodeEnum.FILE_DELETE_ERROR, ce);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FILE_DELETE_ERROR, e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
