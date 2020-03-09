package com.shi.annie;

import com.shi.annie.image.Html2Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@Controller
public class AnnieApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnnieApplication.class, args);
    }

    @Autowired
    private Html2Image html2Image;

    @RequestMapping("/activity")
    public ModelAndView toActivity() {
        return new ModelAndView("activity_example");
    }
    @RequestMapping("/run")
    public void run(HttpServletResponse response) throws IOException {
        response.sendRedirect(html2Image.run());
    }
}
