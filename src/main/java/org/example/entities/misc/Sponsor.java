package org.example.entities.misc;

import org.example.entities.enums.CompanyType;

public class Sponsor {
    private Integer id;
    private String name;
    private CompanyType companyType;

    public Sponsor(Integer id, String name, CompanyType companyType) {
        this.id = id;
        this.name = name;
        this.companyType = companyType;
    }
}
