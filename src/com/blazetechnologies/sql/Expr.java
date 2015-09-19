package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;
import com.blazetechnologies.sql.table.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.IntFunction;

/**
 * Created by Dominic on 10/09/2015.
 */
public class Expr extends SQL implements RColumn{

	private String alias = null;

	protected Expr(){}

	protected Expr(String string){
		super(string);
	}

	public static Expr col(String column_name){
		return new Expr("[" + column_name + "] ");
	}

	public static Expr col(String table_name, String column_name){
		return col("[" + table_name + "].[" + column_name + "]");
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

	public static Expr value(byte[] value){
		return value(Utils.bytesToHex(value).toUpperCase());
	}

	public static <T> Expr value(T value){
		if(Utils.isSupported(value.getClass())){
			if(String.class.isInstance(value)){
				return value(String.class.cast(value));
			}else if(Date.class.isInstance(value)){
				return value(Date.class.isInstance(value));
			}else if(Enum.class.isInstance(value)){
				return value(Enum.class.cast(value));
			}else if(Character.TYPE.isInstance(value) || Character.class.isInstance(value)){
				return value(Character.class.cast(value));
			}else if(value.getClass().isArray() && value.getClass().getComponentType().isAssignableFrom(byte.class)){
				return value((byte[]) value);
			}else{
				try {
					return value(Long.parseLong(value.toString()));
				}catch (Exception e){
					return value(Number.class.cast(value).doubleValue());
				}
			}
		}else if(Expr.class.isAssignableFrom(value.getClass())){
			return Expr.class.cast(value);
		}
		return null;
	}

	public static <T> Expr bind(T value){
		Expr expr = new Expr("? ");
		expr.getBindings().add(value);
		return expr;
	}

	public static Expr bind(){
		return new Expr("? ");
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

	public static Expr cast(Expr expr, DataType type){
		return new Expr("CAST(" + expr + " AS " + type.name() + ") ");
	}

	public static Expr and(Expr x, Expr y){
		return operate("AND", x, y);
	}

	public static Expr or(Expr x, Expr y){
		return operate("OR", x, y);
	}

	private static Expr operate(String operator, Expr x, Expr y){
		return new Expr("((" + x + ") " + operator + " (" + y + ")) ");
	}

	private Expr operate(String name, Expr expr){
		return operate(this, name, expr);
	}

	protected Expr operate(Expr delegate, String name, Expr expr){
		delegate.builder.append(name).append(" ");
		if(expr != null) {
			delegate.builder.append(expr).append(" ");
			delegate.getBindings().addAll(expr.getBindings());
		}
		return this;
	}

	private Expr func(String name, Expr... args){
		return func(this, name, args);
	}

	protected Expr func(Expr delegate, String name, Expr... args){
		delegate.builder.append(name).append("(");
		for (int x = 0; x < args.length; x++) {
			delegate.builder.append(args[x]);
			delegate.getBindings().add(args[x].getBindings());
			if(x < args.length - 1){
				delegate.builder.append(", ");
			}
		}
		delegate.builder.append(") ");
		return this;
	}

	public Expr and(Expr expr){
		return func("AND ", expr);
	}

	public Expr or(Expr expr){
		return func("OR ", expr);
	}

	public Joint and(){
		return new Joint(this, "AND");
	}

	public Joint or(){
		return new Joint(this, "OR");
	}

	public Joint plus(){
		return new Joint(this, "+");
	}

	public Expr plus(Expr expr){
		return func("+ ", expr);
	}

	public <T> Expr plus(T value){
		return plus(Expr.value(value));
	}

	public Joint minus(){
		return new Joint(this, "-");
	}

	public Expr minus(Expr expr){
		return func("- ", expr);
	}

	public <T> Expr minus(T value){
		return minus(Expr.value(value));
	}

	public Joint multiplyBy(){
		return new Joint(this, "*");
	}

	public Expr multiplyBy(Expr expr){
		return func("* ", expr);
	}

	public <T> Expr multiplyBy(T value){
		return multiplyBy(Expr.value(value));
	}

	public Joint divideBy(){
		return new Joint(this, "/");
	}

	public Expr divideBy(Expr expr){
		return func("/ ", expr);
	}

	public <T> Expr divideBy(T value){
		return divideBy(Expr.value(value));
	}

	public Joint modulus(){
		return new Joint(this, "%");
	}

	public Expr modulus(Expr expr){
		return func("% ", expr);
	}

	public <T> Expr modulus(T value){
		return modulus(Expr.value(value));
	}

	public Expr eq(Expr value){
		return operate("=", value);
	}

	public <T> Expr eq(T value){
		return eq(value(value));
	}

	public Expr notEq(Expr value){
		return operate("!=", value);
	}

	public <T> Expr notEq(T value){
		return notEq(value(value));
	}

	public Expr gt(Expr value){
		return operate(">", value);
	}

	public <T> Expr gt(T value){
		return gt(value(value));
	}

	public Expr gtEq(Expr expr){
		return operate(">=", value(expr));
	}

	public <T> Expr gtEq(T value){
		return gtEq(value(value));
	}

	public Expr lt(Expr value){
		return operate("<", value);
	}

	public <T> Expr lt(T value){
		return lt(value(value));
	}

	public Expr ltEq(Expr value){
		return operate("<=", value);
	}

	public <T> Expr ltEq(T value){
		return ltEq(value(value));
	}

	public Expr isNull(){
		return operate("ISNULL", null);
	}

	public Expr notNull(){
		return operate("NOT NULL", null);
	}

	public Expr is(Expr expr){
		return operate("IS", expr);
	}

	public <T> Expr is(T value){
		return is(Expr.value(value));
	}

	public Expr isNot(Expr expr){
		return operate("IS NOT", expr);
	}

	public <T> Expr isNot(T value){
		return isNot(Expr.value(value));
	}

	public Expr in(Expr... exprs){
		return func("IN", exprs);
	}

	@SafeVarargs
	public final <T> Expr in(final T... values){
		Expr[] exprs = new Expr[values.length];
		Arrays.setAll(exprs, new IntFunction<Expr>() {
			@Override
			public Expr apply(int value) {
				return Expr.value(values[value]);
			}
		});
		return in(exprs);
	}

	public Expr in(String tableOrView){
		return operate("IN", new Expr(tableOrView));
	}

	public <E extends Entity> Expr in(Class<E> entity){
		return in(Entity.getEntityName(entity));
	}

	public Expr notIn(Expr... exprs){
		return func("NOT IN", exprs);
	}

	@SafeVarargs
	public final <T> Expr notIn(final T... values){
		Expr[] exprs = new Expr[values.length];
		Arrays.setAll(exprs, new IntFunction<Expr>() {
			@Override
			public Expr apply(int value) {
				return Expr.value(values[value]);
			}
		});
		return notIn(exprs);
	}

	public Expr notIn(String tableOrView){
		return operate("NOT IN", new Expr(tableOrView));
	}

	public <E extends Entity> Expr notIn(Class<E> entity){
		return notIn(Entity.getEntityName(entity));
	}

	public Expr between(Expr down, Expr up){
		return operate("BETWEEN", down).operate("AND", up);
	}

	public <T> Expr between(T down, T up){
		return between(Expr.value(down), Expr.value(up));
	}

	public Expr notBetween(Expr down, Expr up){
		return operate("NOT BETWEEN", down).operate("AND", up);
	}

	public <T> Expr notBetween(T down, T up){
		return notBetween(Expr.value(down), Expr.value(up));
	}

	public Expr like(Expr expr){
		return operate("LIKE", expr);
	}

	public Expr like(String expr){
		return like(Expr.value(expr));
	}

	public Expr notLike(Expr expr){
		return operate("NOT LIKE", expr);
	}

	public Expr notLike(String expr){
		return notLike(Expr.value(expr));
	}

	public Expr glob(Expr expr){
		return operate("GLOB", expr);
	}

	public Expr notGlob(Expr expr){
		return operate("NOT GLOB", expr);
	}

	public Expr match(Expr expr){
		return operate("MATCH", expr);
	}

	public Expr match(String expr){
		return match(Expr.value(expr));
	}

	public Expr notMatch(Expr expr){
		return operate("NOT MATCH", expr);
	}

	public Expr notMatch(String expr){
		return notMatch(Expr.value(expr));
	}

	public Expr regex(Expr expr){
		return operate("REGEX", expr);
	}

	public Expr regex(String regex){
		return regex(Expr.value(regex));
	}

	public Expr notRegex(Expr expr){
		return operate("NOT REGEX", expr);
	}

	public Expr notRegex(String regex){
		return notRegex(Expr.value(regex));
	}

	public Expr collate(String collation_name){
		return operate("COLLATE", new Expr(collation_name));
	}

	public RColumn as(String alias){
		this.alias = alias;
		return this;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public Expr getExpr() {
		return this;
	}

	@Override
	public String getStringExpr() {
		return toString();
	}

	public static class Joint{
		private Expr expr;
		private String operator;

		private Joint(Expr a, String operator){
			this.expr = a;
			this.operator = operator;
		}

		public Expr col(String column_name){
			return new BinaryOperator(expr, operator, Expr.col(column_name));
		}

		public Expr col(String table_name, String column_name){
			return new BinaryOperator(expr, operator, Expr.col(table_name, column_name));
		}

		public <E extends Entity> Expr col(Class<E> table, String column_name){
			return new BinaryOperator(expr, operator, Expr.col(table, column_name));
		}

		public Expr value(String string){
			return new BinaryOperator(expr, operator, Expr.value(string));
		}

		public Expr value(long number){
			return new BinaryOperator(expr, operator, Expr.value(number));
		}

		public Expr value(double number){
			return new BinaryOperator(expr, operator, Expr.value(number));
		}

		public Expr value(float number){
			return new BinaryOperator(expr, operator, Expr.value(number));
		}

		public Expr value(boolean bool){
			return new BinaryOperator(expr, operator, Expr.value(bool));
		}

		public Expr value(char character){
			return new BinaryOperator(expr, operator, Expr.value(character));
		}

		public Expr value(Date date){
			return new BinaryOperator(expr, operator, Expr.value(date));
		}

		public Expr value(Enum value){
			return new BinaryOperator(expr, operator, Expr.value(value));
		}

		public Expr value(byte[] value){
			return new BinaryOperator(expr, operator, Expr.value(value));
		}

		public <T> Expr value(T value){
			return new BinaryOperator(expr, operator, Expr.value(value));
		}

	}

	public static class BinaryOperator extends Expr{
		private Expr a;
		private String operator;
		private Expr b;

		private BinaryOperator(Expr a, String operator, Expr b){
			this.a = a;
			this.operator = operator;
			this.b = b;
		}

		@Override
		protected Expr operate(Expr delegate, String name, Expr expr) {
			return super.operate(b, name, expr);
		}

		@Override
		protected Expr func(Expr delegate, String name, Expr... args) {
			return super.func(b, name, args);
		}

		@Override
		public ArrayList<Object> getBindings() {
			ArrayList<Object> bindings = new ArrayList<>(a.getBindings().size() + b.getBindings().size());
			bindings.addAll(a.getBindings());
			bindings.addAll(b.getBindings());
			return bindings;
		}

		@Override
		public String build() {
			return a + " " + operator + " " + b;
		}
	}

}
