package com.shi.annie.file.client;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/11/28 5:55 下午
 */
public class FileClientFactory {

    public static FileClient getFileClient(){
        //todo 目前没有别的文件服务，开源minio的用用
        return new MinioClient();
    }

}
