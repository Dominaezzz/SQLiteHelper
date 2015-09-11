package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;

import java.util.Date;

/**
 * Created by Dominic on 10/09/2015.
 */
public class Expr extends SQL {

	Expr(){}

	Expr(String string){
		super(string);
	}

	public static Expr col(String column_name){
		return new Expr(column_name + " ");
	}

	public static Expr col(String table_name, String column_name){
		return col(table_name + "." + column_name);
	}

	public static <E extends Entity> Expr col(Class<E> table, String column_name){
		return col(Entity.getEntityName(table), column_name);
	}

	public static Expr value(String string){
		return new Expr("'" + string + "' ");
	}

	public static Expr value(long number){
		return new Expr(number + " ");
	}

	public static Expr value(double number){
		return new Expr(number + " ");
	}

	public static Expr value(float number){
		return new Expr(number + " ");
	}

	public static Expr value(boolean bool){
		return new Expr((bool? 1 : 0) + " ");
	}

	public static Expr value(char character){
		return new Expr("'" + character + "' ");
	}

	public static Expr value(Date date){
		return null;
	}

	public static Expr value(Enum value){
		return value(value.name());
	}

	public static Expr Null(){
		return new Expr("NULL ");
	}

	public static Expr current_time(){
		return new Expr("CURRENT_TIME ");
	}

	public static Expr current_timestamp(){
		return new Expr("CURRENT_TIMESTAMP ");
	}

	public static Expr current_date(){
		return new Expr("CURRENT_DATE ");
	}

	public Expr isNull(){
		builder.append("ISNULL ");
		return this;
	}

	public Expr notNull(){
		builder.append("NOT NULL ");
		return this;
	}

	public Expr is(Expr expr){
		builder.append("IS ").append(expr).append(" ");
		bindings.addAll(expr.bindings);
		return this;
	}

	public Expr isNot(Expr expr){
		builder.append("IS NOT ").append(expr).append(" ");
		bindings.addAll(expr.bindings);
		return this;
	}

	public Expr in(Expr... exprs){
		builder.append("IN(");
		for (int x = 0; x < exprs.length; x++) {
			builder.append(exprs[x]);
			bindings.add(exprs[x].bindings);
			if(x < exprs.length - 1){
				builder.append(", ");
			}
		}
		builder.append(") ");
		return this;
	}

	public Expr in(Query select_stmt){
		builder.append("IN(").append(select_stmt).append(") ");
		bindings.addAll(select_stmt.bindings);
		return this;
	}

	public Expr in(String tableOrView){
		builder.append("IN ").append(tableOrView).append(" ");
		return this;
	}

	public <E extends Entity> Expr in(Class<E> entity){
		return in(Entity.getEntityName(entity));
	}

	public Expr notIn(Expr... exprs){
		builder.append("NOT ");
		return in(exprs);
	}

	public Expr notIn(Query select_stmt){
		builder.append("NOT ");
		return in(select_stmt);
	}

	public Expr notIn(String tableOrView){
		builder.append("NOT ");
		return in(tableOrView);
	}

	public <E extends Entity> Expr notIn(Class<E> entity){
		builder.append("NOT ");
		return in(entity);
	}

	public Expr between(Expr down, Expr up){
		builder.append("BETWEEN ").append(down).append(" AND ").append(up).append(" ");
		bindings.addAll(down.bindings);
		bindings.addAll(up.bindings);
		return this;
	}

	public Expr notBetween(Expr down, Expr up){
		builder.append("NOT ");
		return between(down, up);
	}

	public Expr like(Expr expr){
		builder.append("LIKE ").append(expr).append(" ");
		bindings.addAll(expr.bindings);
		return this;
	}

	public Expr notLike(Expr expr){
		builder.append("NOT ");
		return like(expr);
	}

	public Expr glob(Expr expr){
		builder.append("GLOB ").append(expr).append(" ");
		bindings.addAll(expr.bindings);
		return this;
	}

	public Expr notGlob(Expr expr){
		builder.append("NOT ");
		return glob(expr);
	}

	public Expr match(Expr expr){
		builder.append("MATCH ").append(expr).append(" ");
		bindings.addAll(expr.bindings);
		return this;
	}

	public Expr notMatch(Expr expr){
		builder.append("NOT ");
		return match(expr);
	}

	public Expr regex(Expr expr){
		builder.append("REGEX ").append(expr).append(" ");
		bindings.addAll(expr.bindings);
		return this;
	}

	public Expr notRegex(Expr expr){
		builder.append("NOT ");
		return regex(expr);
	}

	public Expr collate(String collation_name){
		builder.append("COLLATE ").append(collation_name).append(" ");
		return this;
	}

}
