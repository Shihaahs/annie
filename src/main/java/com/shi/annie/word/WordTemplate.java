package com.shi.annie.word;

import com.shi.annie.utils.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/4/2 10:35 上午
 */
public class WordTemplate {

    
    /**
     * 使用Apache Freemarker简单实现word模板生成，速度还可以
     * 44KB的word生成时间在200ms左右
     */
    public static void main(String[] args) throws Exception {
        String docPath = "test-doc.ftl";

        Map<String, String> params = new HashMap<>();
        params.put("name", "乌尔123");
        params.put("address", "杭州");
        params.put("weight", "12.2");

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("utf-8");

        configuration.setClassForTemplateLoading(DateUtil.class, "/");
        Template t = configuration.getTemplate(docPath);
        // 输出文档路径及名称
        File outFile = new File("/Users/wuer/Desktop/doc-2.doc");
        FileOutputStream fos = new FileOutputStream(outFile);
        OutputStreamWriter oWriter = new OutputStreamWriter(fos, "utf-8");
        Writer out = new BufferedWriter(oWriter);
        t.process(params, out);

        out.close();
        fos.close();

    }
}
