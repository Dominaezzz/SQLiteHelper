package com.blazetechnologies.sql;

import java.util.ArrayList;

/**
 * Created by Dominic on 04/09/2015.
 */
public abstract class SQL {

	StringBuilder builder;
	ArrayList<Object> bindings;

	SQL(){
		builder = new StringBuilder();
		bindings = new ArrayList<>();
	}

	public String build(){
		return builder.toString();
	}

	public ArrayList<Object> getBindings(){
		return bindings;
	}

}
