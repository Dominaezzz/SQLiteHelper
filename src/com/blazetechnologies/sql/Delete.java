package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;
import com.blazetechnologies.annotations.PrimaryKey;
import com.blazetechnologies.annotations.Unique;
import com.blazetechnologies.executors.Editable;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * Created by Dominic on 07/09/2015.
 */
public class Delete extends SQL {

	private Delete(){}

	public static Where from(String tableName){
		return new Where(tableName);
	}

	public static <E extends Entity> Where from(Class<E> entity){
		return from(Entity.getEntityName(entity));
	}

	public int executeDelete(Editable editable){
		return editable.executeUpdateOrDelete(toString(), getBindings().toArray());
	}

	public static class Where extends Delete{
		private Where(String tableName) {
			builder.append("DELETE FROM ").append(Utils.encaseKeyword(tableName));
		}

		public Delete where(String where, Object... args){
			builder.append(" WHERE ").append(where);
			Collections.addAll(getBindings(), args);
			return this;
		}

		public Delete where(Expr expr){
			getBindings().addAll(expr.getBindings());
			return where(expr.toString());
		}

		@SafeVarargs
		public final <E extends Entity> Delete objects(E... entities) throws Exception {
			Class<?> type = entities.getClass().getComponentType();
			Field uniqueField = null;
			for (Field field : type.getDeclaredFields()) {
				if(field.isAnnotationPresent(PrimaryKey.class)){
					uniqueField = field;
					break;
				}
				if(field.isAnnotationPresent(Unique.class)){
					if(uniqueField == null){
						uniqueField = field;
					}
				}
			}
			if(uniqueField == null){
				throw new Exception("Entity must have a Primary key field or a field.");
			}

			final String column = Entity.getEntityFieldName(uniqueField);
			Expr expr = null;
			for (E entity : entities){
				if (expr == null){
					expr = Expr.col(column).eq(uniqueField.get(entity));
				}else{
					expr = expr.or().col(column).eq(uniqueField.get(entity));
				}
			}

			return where(expr);
		}
	}

}
