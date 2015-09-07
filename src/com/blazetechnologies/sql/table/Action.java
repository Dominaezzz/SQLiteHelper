package com.blazetechnologies.sql.table;

/**
 * Created by Dominic on 07/09/2015.
 */
public enum Action {
	SET_NULL,
	SET_DEFAULT,
	CASCADE,
	RESTRICT,
	NO_ACTION;

	@Override
	public String toString() {
		return super.toString().replace('_', ' ');
	}
}
