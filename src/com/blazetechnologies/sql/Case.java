package com.blazetechnologies.sql;

import java.util.ArrayList;

/**
 * Created by Dominic on 10/09/2015.
 */
public final class Case {

	private Case(){
	}

	public static When when(String expr){
		return when(new Expr(expr));
	}

	public static When when(Expr condition){
		return new When(condition);
	}

	public static class Base{
		private StringBuilder builder;
		private ArrayList<Object> bindings;

		private Base(Expr expr){
			builder = new StringBuilder();
			bindings = new ArrayList<>(expr.getBindings());
			builder.append("CASE ").append(expr).append(" WHEN ").append(" ");
		}

		public When when(String expr){
			return when(new Expr(expr));
		}

		public When when(Expr expr){
			When when = Case.when(expr);
			builder.append(expr).append(" ");
			bindings.addAll(expr.getBindings());
			when.builder = builder;
			when.bindings = bindings;
			return when;
		}
	}

	public static class When{
		private StringBuilder builder;
		private ArrayList<Object> bindings;
		private Then then;

		private When(Expr expr){
			builder = new StringBuilder();
			bindings = new ArrayList<>(expr.getBindings());
			builder.append("CASE WHEN ").append(expr).append(" ");
			then = new Then(this);
		}

		public Then then(String expr){
			builder.append("THEN ").append(expr).append(" ");
			return then(Expr.value(expr));
		}

		public Then then(Expr expr){
			builder.append("THEN ").append(expr).append(" ");
			return then;
		}
	}

	public static class Then extends Else{
		private Then(When when){
			super(when);
		}

		public When when(String expr){
			when.builder.append("WHEN ").append(expr).append(" ");
			return when;
		}

		public When when(Expr condition){
			when.bindings.addAll(condition.getBindings());
			return when(condition.build());
		}

		public Else orElse(String expr){
			when.builder.append("ELSE ").append(expr).append(" ");
			return this;
		}

		public Else orElse(Expr expr){
			when.bindings.addAll(expr.getBindings());
			return orElse(expr.build());
		}
	}

	public static class Else{
		protected When when;

		private Else(When when){
			this.when = when;
		}

		public Expr end(){
			Expr expr = new Expr(when.builder.append("END").toString());
			expr.getBindings().addAll(when.bindings);
			return expr;
		}
	}

}
