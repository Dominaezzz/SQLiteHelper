package com.blazetechnologies.sql.table;

import com.blazetechnologies.Utils;
import com.blazetechnologies.sql.Create;
import com.blazetechnologies.sql.Expr;

/**
 * Created by Dominic on 07/09/2015.
 */
public abstract class TableConstraint {

	private TableConstraint(){}

	public abstract String build();

	public static TableConstraint primaryKey(String... columns){
		return new PrimaryKey(columns);
	}

	public static TableConstraint primaryKey(Create.IndexedColumn... indexedColumns){
		String[] columns = new String[indexedColumns.length];
		for (int x = 0; x < indexedColumns.length; x++) {
			columns[x] = indexedColumns[x].build();
		}
		return primaryKey(columns);
	}

	public static TableConstraint unique(String... columns){
		return new Unique(columns);
	}

	public static TableConstraint unique(Create.IndexedColumn... indexedColumns){
		String[] columns = new String[indexedColumns.length];
		for (int x = 0; x < indexedColumns.length; x++) {
			columns[x] = indexedColumns[x].build();
		}
		return unique(columns);
	}

	public static TableConstraint check(Expr condition){
		return check(condition.build());
	}

	public static TableConstraint check(String condition){
		return new Check(condition);
	}

	public static ForeignKey foreignKey(String... columns){
		return new ForeignKey(columns);
	}

	public static class PrimaryKey extends TableConstraint{
		private String[] indexed_columns;

		private PrimaryKey(String[] indexed_columns){
			this.indexed_columns = indexed_columns;
		}

		@Override
		public String build() {
			StringBuilder builder = new StringBuilder("PRIMARY KEY (");
			for (int x = 0; x < indexed_columns.length; x++) {
				builder.append(indexed_columns[x]);
				if(x < indexed_columns.length - 1){
					builder.append(", ");
				}
			}
			return builder.append(")").toString();
		}
	}

	public static class Unique extends TableConstraint{
		private String[] indexed_columns;

		private Unique(String[] indexed_columns){
			this.indexed_columns = indexed_columns;
		}

		@Override
		public String build() {
			StringBuilder builder = new StringBuilder("UNIQUE (");
			for (int x = 0; x < indexed_columns.length; x++) {
				builder.append(indexed_columns[x]);
				if(x < indexed_columns.length - 1){
					builder.append(", ");
				}
			}
			return builder.append(")").toString();
		}
	}

	public static class Check extends TableConstraint{
		private String condition;

		private Check(String condition){
			this.condition = condition;
		}

		@Override
		public String build() {
			return "CHECK(" + condition + ")";
		}
	}

	public static class ForeignKey{
		private String[] columns;

		private ForeignKey(String[] columns){
			this.columns = columns;
		}

		public ForeignKey2 references(String foreignTable, String... foreignColumns){
			return new ForeignKey2(columns, foreignTable, foreignColumns);
		}
	}

	public static class ForeignKey2 extends TableConstraint{
		private String[] columns;
		private String foreignTable;
		private String[] foreignColumns;

		private Action onDelete;
		private Action onUpdate;
		private String match;

		private ForeignKey2(String[] columns, String foreignTable, String[] foreignColumns){
			this.columns = columns;
			this.foreignTable = foreignTable;
			this.foreignColumns = foreignColumns;
		}

		public ForeignKey2 onDelete(Action action){
			onDelete = action;
			return this;
		}

		public ForeignKey2 onUpdate(Action action){
			onUpdate = action;
			return this;
		}

		public ForeignKey2 match(String name){
			match = name;
			return this;
		}

		@Override
		public String build() {
			StringBuilder builder = new StringBuilder("FOREIGN KEY (");
			for (int x = 0; x < columns.length; x++) {
				builder.append(columns[x]);
				if(x < columns.length - 1){
					builder.append(", ");
				}
			}
			builder.append(") ");
			builder.append("REFERENCES ");
			builder.append(foreignTable);
			if(foreignColumns.length > 0){
				builder.append("(");
				for (int x = 0; x < foreignColumns.length; x++) {
					builder.append(Utils.encaseKeyword(foreignColumns[x]));
					if(x < foreignColumns.length - 1){
						builder.append(", ");
					}
				}
				builder.append(")");
			}
			builder.append(" ");

			if(onDelete != null){
				builder.append("ON DELETE ").append(onDelete.toString()).append(" ");
			}

			if(onUpdate != null){
				builder.append("ON UPDATE ").append(onUpdate.toString()).append(" ");
			}

			if(match != null){
				builder.append("MATCH ").append(match);
			}

			return builder.toString().trim();
		}
	}

}
