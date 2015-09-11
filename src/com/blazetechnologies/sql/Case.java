package com.blazetechnologies.sql;

import java.util.ArrayList;

/**
 * Created by Dominic on 10/09/2015.
 */
public class Case {

	private Case(){
	}

	public static When when(String expr){
		return when(Expr.value(expr));
	}

	public static When when(Expr condition){
		return new When(condition, condition.getBindings());
	}

	public static class When{
		private StringBuilder builder;
		private Then then;

		private When(Expr expr, ArrayList<Object> bindings){
			builder = new StringBuilder();
			builder.append("CASE WHEN ").append(expr).append(" ");
			then = new Then(this);
		}

		public Then then(String expr){
			builder.append("THEN ").append(expr).append(" ");
			return then;
		}

		public Then then(Query query){
			return then(query.build());
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

			return when(condition.build());
		}

		public Else orElse(String expr){
			when.builder.append("ELSE ").append(expr).append(" ");
			return this;
		}
	}

	public static class Else{
		protected When when;

		private Else(When when){
			this.when = when;
		}

		public String end(){
			when.builder.append("END");
			return when.builder.toString();
		}
	}

}
