package com.blazetechnologies.sql;

/**
 * Created by Dominic on 10/09/2015.
 */
public class Raise extends Expr {

	private Raise(String name, String message){
		builder.append("RAISE(").append(name);
		if(message != null){
			builder.append(", ").append(message);
		}
		builder.append(") ");
	}

	public Raise ignore(){
		return new Raise("IGNORE", null);
	}

	public Raise abort(String message){
		return new Raise("ABORT", message);
	}

	public Raise rollback(String message){
		return new Raise("ROLLBACK", message);
	}

	public Raise fail(String message){
		return new Raise("FAIL", message);
	}

}
