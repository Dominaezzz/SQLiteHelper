package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;
import com.blazetechnologies.executors.Insertable;
import com.blazetechnologies.executors.JDBCExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collections;

/**
 * Created by Dominic on 04/09/2015.
 */
public class Insert extends SQL{

	private Insert(){}

	public static Into into(Conflict conflict, String tableName){
		return new Into(conflict, tableName);
	}

	public static Into into(String tableName){
		return into(null, tableName);
	}

	public static <E extends Entity> Into into(Conflict conflict, Class<E> table){
		return into(conflict, Entity.getEntityName(table));
	}

	public static <E extends Entity> Into into(Class<E> table){
		return into(null, table);
	}

	public int executeInsert(Insertable insertable) {
		return insertable.executeInsert(toString(), getBindings().toArray());
	}

	public int executeInsert(Connection connection) {
		return executeInsert(new JDBCExecutor(connection));
	}

	public static class Into extends Columns {
		private Into(Conflict conflict, String tableName){
			builder.append("INSERT ");
			if(conflict != null){
				builder.append("OR ").append(conflict.name()).append(" ");
			}
			builder.append("INTO ").append(tableName).append(" ");
		}

		public Columns columns(String... columns){
			if(columns.length > 0){
				builder.append("(");
				for (int x = 0; x < columns.length; x++) {
					builder.append('[').append(columns[x]).append(']');
					if(x < columns.length - 1){
						builder.append(", ");
					}
				}
				builder.append(") ");
			}
			return this;
		}

		public Insert bindColumns(String... columns){
			columns(columns);
			Expr[] exprs = new Expr[columns.length];
			for (int x = 0; x < columns.length; x++) {
				exprs[x] = Expr.bind();
			}
			return values(exprs);
		}
	}

	public static class Columns extends Values{
		private Columns(){}

		public Insert stmt(Query query){
			return stmt(query.build(), query.getBindings().toArray());
		}

		public Insert stmt(String query, Object... args){
			Collections.addAll(getBindings(), args);
			builder.append(query);
			return this;
		}

		public Insert defaultValues(){
			builder.append("DEFAULT VALUES");
			return this;
		}
	}

	public static class Values extends Insert{
		private int valuesCount = 0;

		private Values(){}

		@SafeVarargs
		public final <T> Values values(T... values){
			return values(Utils.valuesToExpressions(values));
		}

		public Values values(Expr... values){
			if(valuesCount == 0){
				builder.append("VALUES ");
			}
			if(valuesCount > 0){
				builder.append(", ");
			}

			builder.append("(");
			for (int x = 0; x < values.length; x++) {
				builder.append(values[x]);
				getBindings().add(values[x].getBindings());
				if(x < values.length - 1){
					builder.append(", ");
				}
			}
			builder.append(") ");

			valuesCount++;
			return this;
		}

		public <E extends Entity> Insert values(E... entities) throws IllegalAccessException {
			Class<?> type = entities.getClass().getComponentType();
			Field[] fields = type.getDeclaredFields();

			builder.append("VALUES ");

			for (int x = 0; x < entities.length; x++){
				builder.append("(");
				for (int y = 0; y < fields.length; y++){
					builder.append(Expr.value(fields[y].get(entities[x])));
					if(y < fields.length - 1){
						builder.append(", ");
					}
				}
				builder.append(')');
				if(x < entities.length - 1){
					builder.append(',');
				}
				builder.append(' ');
			}

			return this;
		}
	}

}
