package com.shi.annie.handler;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2019/12/28 2:54 下午
 */
public class ReadConfigHandler {

    private final static String DOT = ".";
    private final static String DOT_WITH_REGEX = "\\.";
    private final static String COLON = ":";
    private final static String SPACE = " ";


    public static void main(String[] args) throws FileNotFoundException {

        String value = "person.age";
        String configFilePath = "some.yml";

        Yaml yaml = new Yaml();
        InputStream in = ReadConfigHandler.class.getClassLoader().getResourceAsStream(configFilePath);
        if (null == in) {
            throw new FileNotFoundException("cannot find file, file path:[ " + configFilePath + " ]");
        }
        Map<String, Object> map = yaml.loadAs(in, Map.class);
        StringBuffer path = new StringBuffer();
        for (String s : value.split(DOT_WITH_REGEX)) {
            path.append(s).append(DOT);
            Object nextMap = map.get(s);
            if (nextMap instanceof Map) {
                map = (Map<String, Object>) nextMap;
            } else if (nextMap instanceof String || nextMap instanceof Number ) {
                System.out.println(deleteLastChat(path) + COLON + SPACE + nextMap.toString());
            } else {
                System.out.println("配置文件中参数不存在, path: " + deleteLastChat(path));
            }
        }
    }

    private static String deleteLastChat(StringBuffer stringBuffer) {
        return stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
    }

}
