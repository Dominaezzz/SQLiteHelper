package com.blazetechnologies.sql.table;

import com.blazetechnologies.sql.Query;
import com.blazetechnologies.sql.SQL;

/**
 * Created by Dominic on 07/09/2015.
 */
public class Table {

	private Table(){}

	public static CreateTable create(boolean temp, String tableName){
		return new CreateTable(temp, tableName);
	}

	public static CreateTable create(String tableName){
		return create(false, tableName);
	}

	public static AlterTable alter(String tableName){
		return new AlterTable(tableName);
	}

	public static SQL rename(String from, String to){
		return new AlterTable(from).renameTo(to);
	}

	public static SQL addColumn(String tableName, String column_def){
		return new AlterTable(tableName).addColumn(column_def);
	}

	public static SQL dropOrThrow(String tableName){
		return SQL.raw("DROP TABLE ", tableName);
	}

	public static SQL drop(String tableName){
		return SQL.raw("DROP TABLE IF EXISTS ", tableName);
	}

	public static class AlterTable{
		private final String tableName;

		private AlterTable(String tableName){
			this.tableName = tableName;
		}

		public SQL renameTo(String newTableName){
			return SQL.raw("ALTER TABLE ", tableName, " RENAME TO ", newTableName);
		}

		public SQL addColumn(String column_def){
			return SQL.raw("ALTER TABLE ", tableName, " ADD COLUMN ", column_def);
		}
	}

	public static class CreateTable{
		boolean temp;
		String name;

		private CreateTable(boolean temp, String name){
			this.name = name;
			this.temp = temp;
		}

		public CreateTable2 columns(String... column_defs){
			return new CreateTable2(this, column_defs);
		}

		public CreateTable2 columns(ColumnDef... column_defs){
			String[] defs = new String[column_defs.length];
			for (int x = 0; x < column_defs.length; x++) {
				defs[x] = column_defs[x].build();
			}
			return new CreateTable2(this, defs);
		}

		public SQL as(String selectStatement){
			return SQL.raw("CREATE ", temp? "TEMPORARY " : "", "TABLE AS ", selectStatement);
		}

		public SQL as(Query query){
			return as(query.build());
		}
	}

	public static class CreateTable2 extends SQL{
		private CreateTable2(CreateTable createTable, String[] column_defs){
			super("CREATE ");
			if(createTable.temp){
				builder.append("TEMPORARY ");
			}
			builder.append("TABLE ").append(createTable.name).append("(");
			for (int x = 0; x < column_defs.length; x++) {
				builder.append(column_defs[x]);
				if(x < column_defs.length - 1){
					builder.append(", ");
				}
			}
		}

		public SQL constraints(TableConstraint... constraints){
			for (TableConstraint constraint : constraints){
				builder.append(", ").append(constraint.build());
			}
			return this;
		}

		public SQL constraints(String... constraints){
			for (String constraint : constraints){
				builder.append(", ").append(constraint.trim());
			}
			return this;
		}

		@Override
		public String build() {
			return super.build() + ");";
		}
	}

}
