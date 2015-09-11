package com.blazetechnologies.sql;

import java.util.Collections;

/**
 * Created by Dominic on 07/09/2015.
 */
public class Delete extends SQL {

	private Delete(String tableName){
		super("DELETE FROM ");
		builder.append(tableName);
	}

	public static Delete from(String tableName){
		return new Delete(tableName);
	}

	public SQL where(String where, Object... args){
		builder.append(" WHERE ").append(where);
		Collections.addAll(getBindings(), args);
		return this;
	}

	public SQL where(Expr condition){
		return where(condition.build(), condition.getBindings().toArray());
	}

}
