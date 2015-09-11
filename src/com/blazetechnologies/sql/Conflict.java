package com.blazetechnologies.sql;

/**
 * Created by Dominic on 07/09/2015.
 */
public enum Conflict {
	ROLLBACK,
	ABORT,
	REPLACE,
	FAIL,
	IGNORE
}
