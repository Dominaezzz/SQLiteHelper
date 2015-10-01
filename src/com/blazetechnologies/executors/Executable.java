package com.blazetechnologies.executors;

/**
 * Created by Dominic on 01/10/2015.
 */
public interface Executable {
	void executeSQL(String sql, Object[] args);
}
