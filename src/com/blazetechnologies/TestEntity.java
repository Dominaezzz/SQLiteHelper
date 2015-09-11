package com.blazetechnologies;

import com.blazetechnologies.annotations.Column;

import java.util.Date;

/**
 * Created by Dominic on 27/08/2015.
 */
public class TestEntity extends Entity {

    @Column(Name = "id", PrimaryKey = true, AutoIncrement = true)
    private int id;

    @Column(Name = "First Name")
    private String firstname;
    private String lastname;
    private Date birthday;

}
