package com.blazetechnologies.sql;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dominic on 10/09/2015.
 */
public final class Case extends Expr{

	private Expr baseCase;
	private Map<Expr, Expr> cases;
	private Expr elseExpr;
	private boolean ended;

	private Case(Expr baseCase){
		cases = new LinkedHashMap<>();
		ended = false;
		this.baseCase = baseCase;
	}

	public static Case base(Expr baseCase){
		return new Case(baseCase);
	}

	public static <T> Case base(T value){
		return base(value(value));
	}

	public static Case base(){
		return base(null);
	}

	public Case when(Expr when, Expr then){
		getBindings().addAll(when.getBindings());
		getBindings().addAll(then.getBindings());

		cases.put(when, then);
		return this;
	}

	public <T> Case when(Expr when, T then){
		return when(when, value(then));
	}

	public <T> Case when(T when, Expr then){
		return when(value(when), then);
	}

	public <T> Case when(T when, T then){
		return when(value(when), value(then));
	}

	public Case orElse(Expr expr){
		elseExpr = expr;
		return this;
	}

	public <T> Case orElse(T expr){
		return orElse(value(expr));
	}

	public Expr end(){
		ended = true;
		return this;
	}

	@Override
	public String build() {
		if(!ended || builder.length() == 0){
			builder.append("CASE ");
			if(baseCase != null){
				builder.append(baseCase).append(" ");
			}
			for (Map.Entry<Expr, Expr> pair : cases.entrySet()){
				builder.append("WHEN ").append(pair.getKey()).append(" ")
						.append("THEN ").append(pair.getValue()).append(" ");
			}
			if(elseExpr != null){
				builder.append("ELSE ").append(elseExpr).append(' ');
			}
			builder.append("END");
		}
		try {
			return super.build();
		}finally {
			if(!ended){
				builder.delete(0, builder.length());
			}
		}

	}
}
