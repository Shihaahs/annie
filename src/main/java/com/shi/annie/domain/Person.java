package com.shi.annie.domain;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Wuer
 * @email: syj@shushi.pro
 * @Date: 2020/1/18 10:46 上午
 */
public class Person {

    private final static String FILE_PATH = "/Users/wuer/person.json";

    private String name;

    private Integer age;

    private Boolean sex;

    private Date birthday;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Person(String name, Integer age, Boolean sex, Date birthday) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", birthday=" + birthday +
                '}';
    }

    public static Person getPerson() {
        try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
            String content = IOUtils.toString(fis, "UTF-8");
            return JSON.parseObject(content, Person.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Person> getPersonList() {
        Person p1 = new Person("乌尔", 23, false, new Date());
        Person p2 = new Person("奇奥", 45, false, new Date());
        Person p3 = new Person("拉", 47, false, new Date());
        return new ArrayList<Person>(){{
            add(p1);
            add(p2);
            add(p3);
        }};
    }

}
