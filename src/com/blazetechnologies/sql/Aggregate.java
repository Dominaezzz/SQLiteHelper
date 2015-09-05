package com.blazetechnologies.sql;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dominic on 28/08/2015.
 */
public class Aggregate extends Query{

    private Aggregate(String name, String query, Object[] bindings){
        builder = new StringBuilder(name).append('(').append(query).append(')');
		this.bindings = new ArrayList<>();
		Collections.addAll(this.bindings, bindings);
	}

    public static Aggregate max(Query query){
		return max(query.build(), query.bindings.toArray());
	}

	public static Aggregate max(String expr, Object... bindings){
		return new Aggregate("MAX", expr, bindings);
	}

	public static Aggregate min(Query query){
		return min(query.build(), query.bindings.toArray());
	}

	public static Aggregate min(String expr, Object... bindings){
		return new Aggregate("MIN", expr, bindings);
	}

	public static Aggregate group_concat(Query query){
		return group_concat(query.build(), query.bindings.toArray());
	}

	public static Aggregate group_concat(String expr, Object... bindings){
		return new Aggregate("GROUP_CONCAT", expr, bindings);
	}

	public static Aggregate group_concat(Query query, String sep){
		return group_concat(query.build(), sep, query.bindings.toArray());
	}

	public static Aggregate group_concat(String expr, String sep, Object... bindings){
		return new Aggregate("GROUP_CONCAT", expr + ", " + sep, bindings);
	}

	public static Aggregate sum(Query query){
		return sum(query.build(), query.bindings.toArray());
	}

	public static Aggregate sum(String expr, Object... bindings){
		return new Aggregate("SUM", expr, bindings);
	}

	public static Aggregate total(Query query){
		return total(query.build(), query.bindings.toArray());
	}

	public static Aggregate total(String expr, Object... bindings){
		return new Aggregate("TOTAL", expr, bindings);
	}

	public static Aggregate count(Query query){
		return count(query.build(), query.bindings.toArray());
	}

	public static Aggregate count(String expr, Object... bindings){
		return new Aggregate("COUNT", expr, bindings);
	}

	public static Aggregate avg(Query query){
		return avg(query.build(), query.bindings.toArray());
	}

	public static Aggregate avg(String expr, Object... bindings){
		return new Aggregate("AVG", expr, bindings);
	}

}
