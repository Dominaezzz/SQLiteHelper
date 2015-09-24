package com.blazetechnologies.sql;

/**
 * Created by Dominic on 28/08/2015.
 */
public class Aggregate extends Expr{

    private Aggregate(boolean distinct, String name, String column){
		super(name);
        builder.append('(');
		if(distinct){
			builder.append("DISTINCT ");
		}
		if(column != null) {
			builder.append("[").append(column).append("]");
		}else {
			builder.append("*");
		}
		builder.append(')').append(' ');
	}

	private Aggregate(boolean distinct, String name, String column, String extra){
		super(name);
		builder.append('(');
		if(distinct){
			builder.append("DISTINCT ");
		}
		builder.append("[").append(column).append("]").append(", ").append(extra);
		builder.append(')').append(" ");
	}

    public static Aggregate max(String column_name){
		return new Aggregate(false, "MAX", column_name);
	}

	public static Aggregate min(String column_name){
		return new Aggregate(false, "MIN", column_name);
	}

	public static Aggregate group_concat(String column_name){
		return group_concat(false, column_name);
	}

	public static Aggregate group_concat(boolean distinct, String column_name){
		return new Aggregate(distinct, "GROUP_CONCAT", column_name);
	}

	public static Aggregate group_concat(String column_name, String sep){
		return group_concat(false, column_name, sep);
	}

	public static Aggregate group_concat(boolean distinct, String column_name, String sep){
		return new Aggregate(distinct, "GROUP_CONCAT", column_name, sep);
	}

	public static Aggregate sum(String column_name){
		return sum(false, column_name);
	}

	public static Aggregate sum(boolean distinct, String column_name){
		return new Aggregate(distinct, "SUM", column_name);
	}

	public static Aggregate total(String column_name){
		return total(false, column_name);
	}

	public static Aggregate total(boolean distinct, String column_name){
		return new Aggregate(distinct, "TOTAL", column_name);
	}

	public static Aggregate count(){
		return count(null);
	}

	public static Aggregate count(String column_name){
		return count(false, column_name);
	}

	public static Aggregate count(boolean distinct, String column_name){
		return new Aggregate(distinct, "COUNT", column_name);
	}

	public static Aggregate avg(String column_name){
		return avg(false, column_name);
	}

	public static Aggregate avg(boolean distinct, String column_name){
		return new Aggregate(distinct, "AVG", column_name);
	}

}
