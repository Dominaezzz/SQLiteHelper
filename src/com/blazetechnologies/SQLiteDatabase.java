package com.blazetechnologies;

import com.blazetechnologies.annotations.Column;
import com.blazetechnologies.sql.Create;
import com.blazetechnologies.sql.Expr;
import com.blazetechnologies.sql.SQL;
import com.sun.istack.internal.NotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

import static com.blazetechnologies.sql.Query.select;

/**
 * Created by Dominic on 27/08/2015.
 */
public final class SQLiteDatabase implements Closeable{

    private final File file;

    private SQLiteDatabase(File databaseFile){
        file = databaseFile;
    }

    public static  <T extends Entity> String createTable(Class<T> table){
        StringBuilder builder = new StringBuilder("CREATE TABLE ");
        builder.append(table.getSimpleName());
        builder.append("(");

        Field[] fields = table.getDeclaredFields();
        for(int x = 0; x < fields.length; x++){
            Field field = fields[x];
            //field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if(column == null){
                builder.append(field.getName());
                builder.append(" ");
                builder.append(getFieldType(field));
            }else {
                builder.append('[');
                builder.append(column.Name());
                builder.append("] ");
                builder.append(getFieldType(field));
                if (column.PrimaryKey()) {
                    builder.append(" PRIMARY KEY");
                    if (column.AutoIncrement()) {
                        builder.append(" AUTOINCREMENT");
                    }
                } else {
                    if (column.Unique()) {
                        builder.append(" UNIQUE");
                    }
                    if (column.Null()) {
                        builder.append(" NULL");
                    } else {
                        builder.append(" NOT NULL");
                    }
                }
            }

            if(x < fields.length - 1){
                builder.append(", ");
            }
        }

        builder.append(");");

        return builder.toString();
        //execute(builder.toString());
    }

    public void createView(@NotNull String name, @NotNull String selectStatement){
        createView(false, name, selectStatement);
    }

    public void createView(boolean temporary, @NotNull String name, @NotNull String selectStatement){
        if(Utils.isEmpty(selectStatement)){
            throw new NullPointerException("selectStatement can not be null or empty");
        }
        if(Utils.isEmpty(selectStatement)){
            throw new NullPointerException("name can not be null or empty");
        }

		execute(
				Create.view(temporary, name)
				.as(selectStatement)
		);
    }

    public void createIndex(String name, boolean unique, Class<Entity> table, String[] indexedColumns, String where){
        String tableName = Entity.getEntityName(table);
        createIndex(name, unique, tableName, indexedColumns, where);
    }

    public void createIndex(String name, boolean unique, String table, String[] indexedColumns, String where){
		execute(
				Create.index(unique, name)
				.on(table, indexedColumns)
				.where(where)
		);
    }

	public void execute(String sql, Object... args){
		select("title", "artist").from(TestEntity.class)
				.where(Expr.col("title").eq("blue jeans"))
				.groupBy("artist")
				.orderBy("artist")
				.limit(1)
				.build();
	}

	public void execute(SQL sql){
		execute(sql.build(), sql.getBindings());
	}

    private static String getFieldType(Field field){
        Class<?> type = field.getType();
        if(type.isAssignableFrom(Integer.TYPE)
                || type.isAssignableFrom(Byte.TYPE)
                || type.isAssignableFrom(Long.TYPE)
                || type.isAssignableFrom(Date.class)
                || type.isAssignableFrom(Boolean.TYPE)
                || type.isAssignableFrom(Short.TYPE)){
            return "INTEGER";
        }else if(type.isAssignableFrom(String.class) || type.isEnum()){
            return "TEXT";
        }else if(type.isAssignableFrom(Float.TYPE) || type.isAssignableFrom(Double.TYPE)){
            return "REAL";
        }else if(type.isArray() && type.getComponentType().isAssignableFrom(Byte.TYPE)){
            return "BLOB";
        }else{
            return "NULL";
        }
    }

    @Override
    public void close() throws IOException {

    }
}
