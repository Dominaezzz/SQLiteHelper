package com.blazetechnologies;

import com.blazetechnologies.annotations.Field;
import com.blazetechnologies.annotations.PrimaryKey;
import com.blazetechnologies.annotations.Table;

import java.util.Date;

/**
 * Created by Dominic on 27/08/2015.
 */
@Table(Name = "Test Table")
public class TestEntity extends Entity {

    @Field(Name = "Id")
    @PrimaryKey(AutoIncrement = true, Asc = true)
    private int _id;

    @Field(Name = "First Name")
    private String firstname;
    private String lastname;
    private Date birthday;
    private String occupation;
	private String nationality;

}
