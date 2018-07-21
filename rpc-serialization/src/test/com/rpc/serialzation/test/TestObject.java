package com.rpc.serialzation.test;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/21_6:33 PM
 */
public class TestObject {
    private String name;
    private int id;

    public TestObject(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
