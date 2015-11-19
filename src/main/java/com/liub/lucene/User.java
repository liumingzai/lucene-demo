package com.liub.lucene;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjx
 * Date: 13-6-2
 * Time: 下午7:08
 * To change this template use File | Settings | File Templates.
 */
public class User implements Serializable{

    private long id;
    private String name;
    private int age;
    private String email;
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
