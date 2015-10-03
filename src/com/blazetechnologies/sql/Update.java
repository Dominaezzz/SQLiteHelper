package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;
import com.blazetechnologies.executors.Editable;
import com.blazetechnologies.executors.JDBCExecutor;

import java.sql.Connection;

/**
 * Created by Dominic on 07/09/2015.
 */
public class Update extends SQL {

	private Update(){}

	public static Set table(Conflict conflict, String table){
		return new Set(conflict, table);
	}

	public static Set table(String table){
		return table(null, table);
	}

	public static <E extends Entity> Set table(Conflict conflict, Class<E> table){
		return table(conflict, Entity.getEntityName(table));
	}

	public static <E extends Entity> Set table(Class<E> table){
		return table(null, table);
	}

	public int executeUpdate(Editable editable){
		return editable.executeUpdateOrDelete(toString(), getBindings().toArray());
	}

	public int executeUpdate(Connection connection){
		return executeUpdate(new JDBCExecutor(connection));
	}

	public static class Set extends Where{
		protected int setCount;

		private Set(Conflict conflict, String table) {
			builder.append("UPDATE ");
			if(conflict != null){
				builder.append("OR ").append(conflict.name()).append(" ");
			}
			builder.append(Utils.encaseKeyword(table)).append(" ");
			setCount = 0;
		}

		public Set set(String column, Expr expr){
			if(setCount == 0){
				builder.append("SET ");
			}else{
				builder.append(", ");
			}
			builder.append('[').append(column).append(']').append(" = ").append(expr).append(" ");
			getBindings().addAll(expr.getBindings());
			setCount++;
			return this;
		}

		public <T> Set set(String column, T value){
			return set(column, Expr.value(value));
		}

		public Set set(String column){
			return set(column, Expr.bind());
		}
	}

	public static class Where extends Update{
		private Where() {}

		public Update where(String where){
			builder.append("WHERE ").append(where);
			return this;
		}

		public Update where(Expr expr){
			getBindings().addAll(expr.getBindings());
			return where(expr.toString());
		}
	}

}
