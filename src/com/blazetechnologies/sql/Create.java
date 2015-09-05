package com.blazetechnologies.sql;

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


	public static class Table extends Create{
		StringBuilder builder;
		Table(boolean temp, String name){
			builder = new StringBuilder("CREATE ");
			if(temp){
				builder.append("TEMPORARY ");
			}
			builder.append("TABLE ").append(name).append(" ");
		}

		public Create as(Query query){
			return as(query.build());
		}

		public Create as(String select){
			builder.append("AS ").append(select);
			return this;
		}

		public Create columns(String... columnDefinitions){
			builder.append("(");
			for (int x = 0; x < columnDefinitions.length; x++) {
				builder.append(columnDefinitions[x]);
				if(x < columnDefinitions.length - 1){
					builder.append(", ");
				}
			}
			builder.append(") ");
			return this;
		}

		public Create columns(Column... columns){
			builder.append("(");
			for (int x = 0; x < columns.length; x++) {
				builder.append(columns[x].build());
				if(x < columns.length - 1){
					builder.append(", ");
				}
			}
			builder.append(") ");
			return this;
		}

		public static Column column(String name, ColumnType type, ColumnConstraint... columnConstraints){
			return new Column(name).setType(type).addConstraints(columnConstraints);
		}
	}

	public enum TableContraints{
		FOREIGN_KEY
	}

	public enum ColumnType{
		INTEGER,
		FLOAT,
		BLOB,
		TEXT
	}

	public enum ColumnConstraint{
		PRIMARY_KEY,
		AUTOINCREMENT,
		UNIQUE,
		NOT_NULL;

		@Override
		public String toString() {
			return super.toString().replace('_', ' ');
		}
	}

	public static class Column extends SQL{
		Column(String name){
			builder.append(name).append(" ");
		}

		Column setType(ColumnType columnType){
			builder.append(columnType.name()).append(" ");
			return this;
		}

		Column addConstraints(ColumnConstraint[] columnConstraints){
			for (ColumnConstraint constraint : columnConstraints){
				builder.append(constraint.toString()).append(" ");
			}
			return this;
		}
	}

	public static class View extends Create{
		View(boolean temp, String name){
			builder.append("CREATE ");
			if(temp){
				builder.append("TEMPORARY ");
			}
			builder.append(name).append(" ");
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
		Index(boolean unique, String name){
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

	public static class IndexedColumn extends SQL{
		private IndexedColumn(String name){
			builder.append(name).append(" ");
		}

		public static IndexedColumn name(String name){
			return new IndexedColumn(name);
		}

		public IndexedColumn collate(String collation_name){
			builder.append("COLLATE ").append(collation_name).append(" ");
			return this;
		}

		public IndexedColumn asc(){
			builder.append("ASC");
			return this;
		}

		public IndexedColumn desc(){
			builder.append("DESC");
			return this;
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

		public Create where(Condition condition){
			return where(condition.build());
		}
	}



}
