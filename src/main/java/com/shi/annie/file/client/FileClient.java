package com.shi.annie.file.client;

import com.shi.annie.file.ResourceFile;

import java.io.InputStream;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/11/28 5:28 下午
 */
public interface FileClient {


    String upload(String fileName, byte[] data);

    InputStream download(ResourceFile fileName);


}
