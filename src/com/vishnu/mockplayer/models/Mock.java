package com.vishnu.mockplayer.models;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 16/9/13
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mock {
    private int id;
    private String name;

    public Mock(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
