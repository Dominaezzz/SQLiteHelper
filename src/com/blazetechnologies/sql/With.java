package com.blazetechnologies.sql;

/**
 * Created by Dominic on 12/09/2015.
 */
public class With extends SQL{

	private With(boolean isRecursive, String cte_table_name, String selectStatement){

	}

	public static With ordinary(String cte_table_name, String selectStatement){
		return new With(false, cte_table_name, selectStatement);
	}

	public static With recursive(String cte_table_name, String selectStatement){
		return new With(true, cte_table_name, selectStatement);
	}

	public With and(String cte_table_name, String selectStatement){
		return this;
	}

	public <T extends SQL> T then(T statement){

		return statement;
	}

}
