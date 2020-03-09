package com.shi.annie.image;

import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/3/9 3:54 下午
 * 网页转图片
 */
@Component
public class Html2Image {


    @Autowired
    ApplicationContext app;

    public String run() {
        //获取本地templates下的html文件  ->  png
        //String htmlContent = initHtmlContent("activity");
        //createAndUploadPNG(htmlContent, 600, 700, "activity.png");
        //获取本机首页地址  ->  png
        String indexHtml = "https://www.baidu.com/";
        return createAndUploadPNG(indexHtml, 600, 700, "activity.png");
    }

    public String initHtmlContent(String fileName) {
        Context context = new Context();
        //本地图片
        //URL wuer = Html2Image.class.getResource("/static/wuer.jpeg");
        //URL brand = Html2Image.class.getResource("/static/brand.jpg");
        context.setVariable("brand", "http://localhost:8080/brand.jpg");
        context.setVariable("qrCode", "http://localhost:8080/wuer.jpeg");
        context.setVariable("name", "乌尔提供原创PS服务");
        context.setVariable("count", "共 2 款商品");
        context.setVariable("status", "ACTIVITY");
        context.setVariable("date", LocalDate.now().toString());

        List<String> imgs = new ArrayList<>();
        imgs.add("http://devdev.oss-cn-shanghai.aliyuncs.com/1b90dfd4c45ed0ef8ad78adb7445c203690fceb9_1554367041112_29.jpg");
        imgs.add("http://devdev.oss-cn-shanghai.aliyuncs.com/1b90dfd4c45ed0ef8ad78adb7445c203690fceb9_1554367041112_29.jpg");
        context.setVariable("imgs", imgs);
        return getEngine().process(fileName, context);
    }

    private SpringTemplateEngine getEngine() {

        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(Boolean.FALSE);
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setApplicationContext(app);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        IDialect dialect = new SpringStandardDialect();
        templateEngine.setDialect(dialect);
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }


    private String createAndUploadPNG(String htmlStr, int width, int height, String ossName) {

        Document doc = null;
        if (!htmlStr.startsWith("http://") && !htmlStr.startsWith("https://")) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new ByteArrayInputStream(htmlStr.getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DocumentSource docSource = null;
            try {
                docSource = new DefaultDocumentSource(htmlStr);
                DOMSource parser = new DefaultDOMSource(docSource);
                doc = parser.parse();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //解析 css
        DOMAnalyzer da = new DOMAnalyzer(doc, null);
        da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
        da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
        da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
        da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css
        da.getStyleSheets();

        //生成canvas画板
        BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, null);
        contentCanvas.setAutoMediaUpdate(false); //we have a correct media specification, do not update
        contentCanvas.getConfig().setClipViewport(Boolean.TRUE);
        contentCanvas.getConfig().setLoadImages(Boolean.TRUE);
        contentCanvas.getConfig().setLoadBackgroundImages(Boolean.TRUE);
        contentCanvas.createLayout(new Dimension(width, height));

        //上传图片到云端
//        ResourceFile upload;
//        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
//            ImageIO.write(contentCanvas.getImage(), "png", bos);
//            byte [] bytes = bos.toByteArray();
//            FileClient fileClient = FileClientFactory.getClient();
//            upload = fileClient.upload(ossName, bytes);
//        }

        //生成本地图片  项目目录下
        File file = new File(ossName);
        if (!file.exists())
            file.mkdirs();

        try {
            ImageIO.write(contentCanvas.getImage(), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(file.getAbsolutePath());
        return file.getAbsolutePath();
    }

}
