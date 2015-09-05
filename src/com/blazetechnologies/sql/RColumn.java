package com.blazetechnologies.sql;

import java.util.ArrayList;

/**
 * Created by Dominic on 28/08/2015.
 */
public class RColumn {

	String alias;
	String query;
	ArrayList<Object> bindings;

	private RColumn(String alias, String expr){
		this.alias = alias;
		this.query = expr;
		bindings = new ArrayList<>();
	}

	public static RColumn def(Query query, String alias){
		RColumn column = def(query.build(), alias);
		column.bindings.addAll(query.bindings);
		return column;
	}

	public static RColumn def(String expr, String alias){
		return new RColumn(alias, expr);
	}

}
