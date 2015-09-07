package com.blazetechnologies.sql;

import java.util.ArrayList;

/**
 * Created by Dominic on 04/09/2015.
 */
public class SQL {

	protected StringBuilder builder;
	protected ArrayList<Object> bindings;

	protected SQL(){
		builder = new StringBuilder();
		bindings = new ArrayList<>();
	}

	protected SQL(String prefix){
		builder = new StringBuilder(prefix);
		bindings = new ArrayList<>();
	}

	public String build(){
		return builder.toString();
	}

	public ArrayList<Object> getBindings(){
		return bindings;
	}

	public static SQL raw(String... strings){
		SQL sql = new SQL();
		for (String string : strings){
			sql.builder.append(string);
		}
		return sql;
	}

}
