package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;

import java.util.Collections;

/**
 * Created by Dominic on 04/09/2015.
 */
public class Insert extends SQL{

	private Insert(){}

	public static Conflict orReplace(){
		return new Conflict("REPLACE");
	}

	public static Conflict orIgnore(){
		return new Conflict("IGNORE");
	}

	public static Conflict orAbort(){
		return new Conflict("ABORT");
	}

	public static Conflict orFail(){
		return new Conflict("FAIL");
	}

	public static Conflict orRollback(){
		return new Conflict("ROLLBACK");
	}

	public static class Conflict extends Into {
		Conflict(){
			builder.append("INSERT INTO ");
		}

		Conflict(String onConflict){
			builder.append("INSERT OR ").append(onConflict).append(" INTO ");
		}

		public Into into(String databaseName, String table){
			builder.append(databaseName).append(".").append(table).append(" ");
			return this;
		}

		public Into into(String tableName){
			builder.append(tableName).append(" ");
			return this;
		}

		public <E extends Entity> Into into(String databaseName, Class<E> table){
			return into(databaseName, Entity.getEntityName(table));
		}

		public <E extends Entity> Into into(Class<E> table){
			return into(Entity.getEntityName(table));
		}
	}

	public static class Into extends Columns {
		private Into(){}

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
			return stmt(query.build(), query.bindings.toArray());
		}

		public Insert stmt(String query, Object... args){
			Collections.addAll(bindings, args);
			builder.append(query);
			return this;
		}

		public Insert defaultValues(){
			builder.append("DEFAULT VALUES");
			return this;
		}
	}

	public static class Values extends Insert{
		int valuesCount = 0;

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
					bindings.add(values[x]);
					if(x < values.length - 1){
						builder.append(", ");
					}
				}
				builder.append(")");
			}else{
				throw new UnsupportedOperationException("VALUES limit is 500");
			}
			return this;
		}
	}

}
