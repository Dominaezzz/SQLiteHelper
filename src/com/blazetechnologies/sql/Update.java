package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;

import java.util.Collections;

/**
 * Created by Dominic on 07/09/2015.
 */
public class Update extends SQL {

	private int setCount;

	private Update(Conflict conflict, String table){
		builder.append("UPDATE ");
		if(conflict != null){
			builder.append("OR ").append(conflict.name()).append(" ");
		}
		builder.append(Utils.encaseKeyword(table)).append(" ");
		setCount = 0;
	}

	public static Update table(Conflict conflict, String table){
		return new Update(conflict, table);
	}

	public static Update table(String table){
		return table(null, table);
	}

	public static <E extends Entity> Update table(Conflict conflict, Class<E> table){
		return table(conflict, Entity.getEntityName(table));
	}

	public static <E extends Entity> Update table(Class<E> table){
		return table(null, table);
	}

	public Update set(String column, Expr expr){
		if(setCount > 0){
			builder.append(", ");
		}else{
			builder.append("SET ");
		}
		builder.append(column).append(" = ").append(expr).append(" ");
		getBindings().addAll(expr.getBindings());
		return this;
	}

	public <T> Update set(String column, T value){
		return set(column, Expr.value(value));
	}

	public SQL where(String where, Object... args){
		builder.append("WHERE ").append(where);
		Collections.addAll(getBindings(), args);
		return this;
	}

	public SQL where(Expr expr){
		getBindings().addAll(expr.getBindings());
		return where(expr.toString());
	}

}
