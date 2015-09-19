package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;
import com.blazetechnologies.annotations.PrimaryKey;
import com.blazetechnologies.annotations.Unique;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * Created by Dominic on 07/09/2015.
 */
public class Delete extends SQL {

	private Delete(String tableName){
		super("DELETE FROM ");
		builder.append(Utils.encaseKeyword(tableName));
	}

	public static Delete from(String tableName){
		return new Delete(tableName);
	}

	public static <E extends Entity> Delete from(Class<E> entity){
		return from(Entity.getEntityName(entity));
	}

	public SQL where(String where, Object... args){
		builder.append(" WHERE ").append(where);
		Collections.addAll(getBindings(), args);
		return this;
	}

	public SQL where(Expr expr){
		getBindings().addAll(expr.getBindings());
		return where(expr.toString());
	}

	@SafeVarargs
	public final <E extends Entity> SQL objects(E... entities) throws Exception {
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
