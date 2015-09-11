package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;

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
					builder.append(columns[x]);
					if(x < columns.length - 1){
						builder.append(", ");
					}
				}
				builder.append(") ");
			}
			return this;
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

		public Values values(Object... values){
			if(valuesCount == 0){
				builder.append("VALUES ");
			}
			if(valuesCount > 0){
				builder.append(", ");
			}

			if(valuesCount < 500){
				builder.append("(");
				for (int x = 0; x < values.length; x++) {
					builder.append("?");
					getBindings().add(values[x]);
					if(x < values.length - 1){
						builder.append(", ");
					}
				}
				builder.append(")");
			}else{
				throw new UnsupportedOperationException("VALUES limit is 500");
			}
			builder.append(" ");

			valuesCount++;
			return this;
		}
	}

}
