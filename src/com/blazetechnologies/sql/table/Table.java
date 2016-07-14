package com.blazetechnologies.sql.table;

import com.blazetechnologies.Entity;
import com.blazetechnologies.annotations.*;
import com.blazetechnologies.sql.Expr;
import com.blazetechnologies.sql.Query;
import com.blazetechnologies.sql.SQL;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dominic on 07/09/2015.
 */
public class Table extends SQL{

	private Table(){}

	public static CreateTable create(boolean temp, String tableName){
		return new CreateTable(temp, tableName);
	}

	public static CreateTable create(String tableName){
		return create(false, tableName);
	}

	public static <T extends Entity> SQL create(Class<T> entity){
		CreateTable createTable = create(Entity.getEntityName(entity));

		Field[] fields = entity.getDeclaredFields();
		ArrayList<ColumnDef> columnDefs = new ArrayList<>(fields.length);

		for (Field field : fields){
			if(field.isAnnotationPresent(Ignore.class) || Modifier.isStatic(field.getModifiers())){
				continue;
			}
			ColumnDef columnDef = ColumnDef.create(Entity.getEntityFieldName(field), getFieldType(field));
			if(field.isAnnotationPresent(PrimaryKey.class)){
				PrimaryKey key = field.getAnnotation(PrimaryKey.class);
				columnDef.addConstraint(ColumnConstraint.primaryKey(key.AutoIncrement(), key.Order()));
			}else {
				if(field.isAnnotationPresent(Unique.class)){
					columnDef.addConstraint(ColumnConstraint.unique());
				}
				if((field.isAnnotationPresent(NotNull.class) && field.getAnnotation(NotNull.class).value()) || field.getType().isPrimitive()){
					columnDef.addConstraint(ColumnConstraint.notNull());
				}
			}

			if(field.isEnumConstant()){
				columnDef.addConstraint(
						ColumnConstraint.check(
								Expr.col(Entity.getEntityFieldName(field)).in(field.getType().getEnumConstants())
						)
				);
			}

			if(field.isAnnotationPresent(ForeignKey.class)){
				ForeignKey key = field.getAnnotation(ForeignKey.class);
				columnDef.addConstraint(ColumnConstraint.references(key.table(), key.column()));
			}

			columnDefs.add(columnDef);
		}

		return createTable.columns(columnDefs.toArray(new ColumnDef[columnDefs.size()]));
	}

	private static DataType getFieldType(Field field){
		Class<?> type = field.getType();
		if(type.isAssignableFrom(Byte.TYPE)
				|| type.isAssignableFrom(Byte.class)
				|| type.isAssignableFrom(Short.TYPE)
				|| type.isAssignableFrom(Short.class)
				|| type.isAssignableFrom(Integer.TYPE)
				|| type.isAssignableFrom(Integer.class)
				|| type.isAssignableFrom(Long.TYPE)
				|| type.isAssignableFrom(Long.class)
				|| type.isAssignableFrom(Date.class)
				|| type.isAssignableFrom(Boolean.TYPE)
				|| type.isAssignableFrom(Boolean.class)){
			return DataType.INTEGER;
		}else if(type.isAssignableFrom(String.class) || type.isEnum()){
			return DataType.TEXT;
		}else if(type.isAssignableFrom(Float.TYPE) || type.isAssignableFrom(Double.TYPE)){
			return DataType.REAL;
		}else if(type.isArray() && type.getComponentType().isAssignableFrom(Byte.TYPE)){
			return DataType.BLOB;
		}else{
			return DataType.NONE;
		}
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

	public static SQL drop(String tableName){
		return SQL.raw("DROP TABLE [", tableName, "]");
	}

	public static SQL dropIfExists(String tableName){
		return SQL.raw("DROP TABLE IF EXISTS [", tableName, "]");
	}

	public static class AlterTable{
		private final String tableName;

		private AlterTable(String tableName){
			this.tableName = tableName;
		}

		public SQL renameTo(String newTableName){
			return SQL.raw("ALTER TABLE [", tableName, "] RENAME TO [", newTableName + "}");
		}

		public SQL addColumn(String column_def){
			return SQL.raw("ALTER TABLE [", tableName, "] ADD COLUMN ", column_def);
		}

		public SQL addColumn(ColumnDef column_def){
			return addColumn(column_def.build());
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
			return SQL.raw("CREATE ", temp? "TEMPORARY " : "", "TABLE [", name, "] AS ", selectStatement);
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
			builder.append("TABLE [").append(createTable.name).append("](");
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
