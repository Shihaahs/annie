package com.shi.annie.file;

import java.util.Date;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/11/28 6:00 下午
 */
public class ResourceFile {

    private String fileName;   //文件名   test.png
    private String filePrefixName;  //文件名，不带格式   test
    private String fileSuffixName;  //文件格式名，带DOT   .png

    private String fileKey;  //文件全路径名

    private String bucket;

    private Long baseFileSize;    //文件流大小  按位计算
    private String fileSize;    //文件转换大小
    private String fileSizeUnit;   //文件大小单位 GB、MB、KB、B，对应fileSize

    private String downloadUrl;    //文件服务器对外输出的下载地址

    private Date createTime;   //文件在服务器上 初次上传的时间

    private Date writeTime;   //文件被覆盖或修改的时间

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePrefixName() {
        return filePrefixName;
    }

    public void setFilePrefixName(String filePrefixName) {
        this.filePrefixName = filePrefixName;
    }

    public Long getBaseFileSize() {
        return baseFileSize;
    }

    public void setBaseFileSize(Long baseFileSize) {
        this.baseFileSize = baseFileSize;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSizeUnit() {
        return fileSizeUnit;
    }

    public void setFileSizeUnit(String fileSizeUnit) {
        this.fileSizeUnit = fileSizeUnit;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFileSuffixName() {
        return fileSuffixName;
    }

    public void setFileSuffixName(String fileSuffixName) {
        this.fileSuffixName = fileSuffixName;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

}
