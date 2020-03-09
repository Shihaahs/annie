package com.shi.annie.image;

import com.shi.annie.AnnieApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/3/9 5:14 下午
 */
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AnnieApplication.class})
class Html2ImageTest {

    @Autowired
    private Html2Image html2Image;

    @Test
    public void testRun() {
        html2Image.run();
    }

}