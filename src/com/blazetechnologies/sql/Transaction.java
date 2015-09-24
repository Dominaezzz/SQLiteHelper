package com.blazetechnologies.sql;

/**
 * Created by Dominic on 20/09/2015.
 */
public class Transaction extends SQL {

	public static SQL begin(){
		return SQL.raw("BEGIN TRANSACTION");
	}

	private static SQL begin(String type){
		return SQL.raw("BEGIN ", type, " TRANSACTION");
	}

	public static SQL beginDeferred(){
		return begin("DEFERRED");
	}

	public static SQL beginImmediate(){
		return begin("IMMEDIATE");
	}

	public static SQL beginExclusive(){
		return begin("EXCLUSIVE");
	}

	public static SQL commit(){
		return SQL.raw("COMMIT TRANSACTION");
	}

	public static SQL rollback(){
		return SQL.raw("ROLLBACK TRANSACTION");
	}

	public static SQL rollbackTo(String savepoint_name){
		return SQL.raw("ROLLBACK TRANSACTION TO SAVEPOINT ", savepoint_name);
	}

}
