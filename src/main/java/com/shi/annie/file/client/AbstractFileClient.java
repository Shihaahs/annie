package com.shi.annie.file.client;

import com.shi.annie.file.config.FileServerConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;

import java.util.Date;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/11/28 9:42 下午
 */
public abstract class AbstractFileClient implements FileClient, FileServerConfig {

    private static final String FOLD_DATE_FORMAT = "yyyy/MM/dd";

    String getFileDir(String mainDir) {
        if (StringUtils.isBlank(mainDir)) {
            mainDir = "";
        }
        String dateStr = DateUtils.formatDate(new Date(), FOLD_DATE_FORMAT);
        return mainDir + dateStr;
    }

    protected String getFileKey(String mainDir, String fileName) {
        return getFileDir(mainDir) + SLASH + fileName;
    }

    protected String getFileKeyWithTime(String mainDir, String fileName) {
        String newFileName = new Date().getTime() + "-" + fileName;
        return getFileDir(mainDir) + SLASH + newFileName;
    }
}
