package com.blazetechnologies.executors;

/**
 * Created by Dominic on 01/10/2015.
 */
public interface Editable {
	/**
	 * @param sql The update/delete statement.
	 *
	 * @param args The arguments for the parameters in sql.
	 *
	 * @return the number of rows affected.
	 * */
	int executeUpdateOrDelete(String sql, Object[] args);
}
