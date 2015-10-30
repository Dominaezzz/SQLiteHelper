package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;
import com.blazetechnologies.sql.table.DataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
		return new Expr(Utils.encaseKeyword(column_name) + " ");
	}

	public static Expr col(String table_name, String column_name){
		return new Expr(Utils.encaseKeyword(table_name) + "." + Utils.encaseKeyword(column_name));
	}

	public static <E extends Entity> Expr col(Class<E> table, String column_name){
		return col(Entity.getEntityName(table), column_name);
	}

	public static Expr value(String string){
		return new Expr(Utils.escapeString(string) + " ");
	}

	public static <T extends Number> Expr value(T number){
		return new Expr(number + " ");
	}

	public static Expr value(boolean bool){
		return value(bool? 1 : 0);
	}

	public static <T> Expr value(T value){
		if(Utils.isText(value.getClass())){
			return value(value.toString());
		}else if(Utils.isInteger(value.getClass())){
			return value(Long.parseLong(value.toString()));
		}else if(Utils.isReal(value.getClass())){
			return value(Double.parseDouble(value.toString()));
		}else if(Utils.isBoolean(value.getClass())){
			return value(Boolean.parseBoolean(value.toString()));
		}else if(Date.class.isInstance(value)){
			return DateTime.date_time(((Date) value).getTime() / 1000);
		}else if(Utils.isBlob(value.getClass())){
			return value(Utils.bytesToHex((byte[]) value).toUpperCase());
		}else if(Expr.class.isAssignableFrom(value.getClass())){
			return Expr.class.cast(value);
		}else {
			return null;
		}
	}

	public static <T> Expr bind(T value){
		Expr expr = bind();
		if(value instanceof Date){
			expr.getBindings().add(((Date) value).getTime());
		}else if(value.getClass().isEnum()){
			expr.getBindings().add(value.toString());
		}else {
			expr.getBindings().add(value);
		}
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
		Expr cast_expr = new Expr("CAST(" + expr + " AS " + type.name() + ") ");
		cast_expr.getBindings().addAll(expr.getBindings());
		return cast_expr;
	}

	public static Expr and(Expr... exprs){
		return bin_operate("AND", exprs);
	}

	public static Expr or(Expr... exprs){
		return bin_operate("OR", exprs);
	}

	private static Expr bin_operate(String operand, Expr[] exprs){
		Expr result = new Expr("(");

		boolean first = true;
		for (Expr expr : exprs){
			result.builder.append('(').append(expr).append(')').append(' ');
			result.getBindings().addAll(expr.getBindings());
			if(first){
				result.builder.append(operand).append(' ');
				first = false;
			}
		}

		return result;
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
			delegate.getBindings().addAll(args[x].getBindings());
			if(x < args.length - 1){
				delegate.builder.append(", ");
			}
		}
		delegate.builder.append(") ");
		return this;
	}

	public Expr castTo(DataType type){
		return cast(this, type);
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
		return is(value(value));
	}

	public Expr isNot(Expr expr){
		return operate("IS NOT", expr);
	}

	public <T> Expr isNot(T value){
		return isNot(value(value));
	}

	public Expr in(Expr... exprs){
		return func("IN", exprs);
	}

	@SafeVarargs
	public final <T> Expr in(final T... values){
		Expr[] exprs = new Expr[values.length];
		for (int x = 0; x < values.length; x++) {
			exprs[x] = Expr.value(values[x]);
		}
		return in(exprs);
	}

	public <T> Expr in(Collection<T> values){
		return in(values.toArray());
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
		for (int x = 0; x < values.length; x++) {
			exprs[x] = Expr.value(values[x]);
		}
		return notIn(exprs);
	}

	public final <T> Expr notIn(Collection<T> values){
		return notIn(values.toArray());
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

	public Expr startsWith(String expr){
		return like(expr.concat("%"));
	}

	public Expr endsWith(String expr){
		return like("%".concat(expr));
	}

	public Expr contains(String expr){
		return like("%".concat(expr).concat("%"));
	}

	public Expr doesNotStartWith(String expr){
		return notLike(expr.concat("%"));
	}

	public Expr doesNotEndWith(String expr){
		return notLike("%".concat(expr));
	}

	public Expr doesNotContains(String expr){
		return notLike("%".concat(expr).concat("%"));
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

		public <T> Expr value(T value){
			return new BinaryOperator(expr, operator, Expr.value(value));
		}

		public Expr bind(){
			return new BinaryOperator(expr, operator, Expr.bind());
		}

		public Expr bind(Object object){
			return new BinaryOperator(expr, operator, Expr.bind(object));
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
