package com.shi.annie.file.client;

import com.shi.annie.file.ResourceFile;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/11/28 5:28 下午
 */
public class MinioClient extends AbstractFileClient {

    private io.minio.MinioClient minioClient;

    public MinioClient() {
        String endPoint = HTTP + MINIO_SERVER_HOST + COLON + MINIO_SERVER_PORT;
        String accessKey = MINIO_SERVER_ACCESS_KEY;
        String secretKey = MINIO_SERVER_SECRET_KEY;
        try {
            this.minioClient = new io.minio.MinioClient(endPoint, accessKey, secretKey);
            // todo bucketName 应该跟着业务走
            String bucketName = MINIO_SERVER_DEFAULT_BUCKET;
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                minioClient.makeBucket(bucketName);
            }
        } catch (InvalidEndpointException | InvalidPortException e) {
            System.out.println("Minio 连接主机异常");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(("Minio 创建bucket异常，bucketName: " + MINIO_SERVER_DEFAULT_BUCKET));
            e.printStackTrace();
        }
    }


    @Override
    public String upload(String fileName, byte[] data) {

        String downloadUrl = StringUtils.EMPTY;
        String fileKey = getFileDir(SLASH) + SLASH + fileName;
        String bucket = MINIO_SERVER_DEFAULT_BUCKET;
        try {
            minioClient.putObject(bucket, fileKey, new ByteArrayInputStream(data), "application/octet-stream");
            downloadUrl = minioClient.presignedGetObject(bucket, fileKey, 100);
            //todo 后期完善文件上传信息到DB做记录
            //insertResourceFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadUrl;
    }


    /**
     * 将文件上传信息写入DB
     */
    private void insertResourceFile() {
        //ResourceFile file = new ResourceFile();
    }

    @Override
    public InputStream download(ResourceFile file){
        //fixme 后期入参应该是resourceFile，根据业务bucket去拿文件流，有后台Controller封一层给出下载联动
        try {
            return minioClient.getObject(MINIO_SERVER_DEFAULT_BUCKET, file.getDownloadUrl());
        } catch (Exception e) {
        }
        return null;
    }
}
