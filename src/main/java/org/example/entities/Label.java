package org.example.entities;

import java.util.Date;

public class Label {
    private Integer id;
    private String name;
    private Date establishingDate;

    public Label(Integer id, String name, Date establishingDate) {
        this.id = id;
        this.name = name;
        this.establishingDate = establishingDate;
    }
}
