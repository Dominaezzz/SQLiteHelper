package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;

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
		builder.append(table);
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

	public Update set(String column, String expr, Object... args){
		if(setCount > 0){
			builder.append(", ");
		}else{
			builder.append("SET ");
		}
		builder.append(column).append(" = ").append(expr).append(" ");
		Collections.addAll(bindings, args);
		return this;
	}

	public Update set(String column, Query query){
		return set(column, query.build(), query.getBindings().toArray());
	}

	public Update set(String column, Object arg){
		if(setCount > 0){
			builder.append(", ");
		}else{
			builder.append("SET ");
		}
		builder.append(column).append(" = ? ");
		bindings.add(arg);
		return this;
	}

	public SQL where(String where, Object... args){
		builder.append("WHERE ").append(where);
		Collections.addAll(bindings, args);
		return this;
	}

	public SQL where(Condition condition){
		return where(condition.build(), condition.getBindings().toArray());
	}

}
