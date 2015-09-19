package com.blazetechnologies.sql;

/**
 * Created by Dominic on 28/08/2015.
 */
public interface RColumn{

	String getAlias();
	Expr getExpr();
	String getStringExpr();

}
