package com.blazetechnologies.sql;

import com.blazetechnologies.sql.table.Table;

/**
 * Created by Dominic on 04/09/2015.
 */
public class Create extends SQL {

	private Create(){}

	public static View view(boolean temp, String name){
		return new View(temp, name);
	}

	public static View view(String name){
		return view(false, name);
	}

	public static Index index(boolean unique, String name){
		return new Index(unique, name);
	}

	public static Index index(String name){
		return index(false, name);
	}

	public static Table.CreateTable table(boolean temp, String name){
		return Table.create(temp, name);
	}

	public static Table.CreateTable table(String name){
		return table(false, name);
	}

	public static class View extends Create{
		View(boolean temp, String name){
			builder.append("CREATE ");
			if(temp){
				builder.append("TEMPORARY ");
			}
			builder.append("VIEW ").append(name).append(" ");
		}

		public Create as(Query query){
			return as(query.build());
		}

		public Create as(String query){
			builder.append("AS ").append(query);
			return this;
		}
	}

	public static class Index{
		StringBuilder builder;

		private Index(boolean unique, String name){
			builder = new StringBuilder("CREATE ");
			if(unique){
				builder.append("UNIQUE ");
			}
			builder.append("INDEX ").append(name);
		}

		public Where on(String table, String... indexed_columns){
			builder.append("ON ").append(table);
			builder.append("(");
			for (int x = 0; x < indexed_columns.length; x++) {
				builder.append(indexed_columns[x]);
				if(x < indexed_columns.length - 1){
					builder.append(", ");
				}
			}
			builder.append(")");
			return new Where(builder);
		}

		public Where on(String table, IndexedColumn... indexed_columns){
			builder.append("ON ").append(table);
			builder.append("(");
			for (int x = 0; x < indexed_columns.length; x++) {
				builder.append(indexed_columns[x].build());
				if(x < indexed_columns.length - 1){
					builder.append(", ");
				}
			}
			builder.append(")");
			return new Where(builder);
		}
	}

	public static class Where extends Create{
		Where(StringBuilder builder){
			this.builder = builder;
		}

		public Create where(String where){
			builder.append("WHERE ").append(where);
			return this;
		}

		public Create where(Expr condition){
			return where(condition.build());
		}
	}

}
