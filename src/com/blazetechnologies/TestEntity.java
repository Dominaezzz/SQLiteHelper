package com.blazetechnologies;

import com.blazetechnologies.annotations.Column;
import com.blazetechnologies.annotations.PrimaryKey;
import com.blazetechnologies.annotations.Table;
import com.blazetechnologies.sql.Order;

import java.util.Date;

/**
 * Created by Dominic on 27/08/2015.
 */
@Table(Name = "Test Table")
public class TestEntity extends Entity {

    @Column(Name = "Id")
    @PrimaryKey(AutoIncrement = true, Order = Order.ASC)
    private int _id;

    @Column(Name = "First Name")
    private String firstname;

    private String lastname;

    private Date birthday;

    private String occupation;

    private String nationality;

}
