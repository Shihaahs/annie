package com.shi.annie.annotation;

import java.lang.annotation.*;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/12/28 2:49 下午
 */

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadConfig {

        /**
         * 在配置文件中的命名
         */
        String value() default "";

        /**
         * 配置文件路径，相对路径于resources
         */
        String configFilePath() default "";
}
