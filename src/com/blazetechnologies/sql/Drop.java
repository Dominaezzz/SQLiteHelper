package com.blazetechnologies.sql;

import com.blazetechnologies.sql.table.Table;

/**
 * Created by Dominic on 11/09/2015.
 */
public final class Drop extends SQL {

	private Drop(String type, boolean shouldThrow, String name){
		super("DROP ");
		builder.append(type).append(" ");
		if(!shouldThrow){
			builder.append("IF EXISTS ");
		}
		builder.append('[').append(name).append(']');
	}

	public SQL tableIfExists(String table_name){
		return Table.dropIfExists(table_name);
	}

	public SQL table(String table_name){
		return Table.drop(table_name);
	}

	public SQL viewIfExists(String view_name){
		return new Drop("VIEW", false, view_name);
	}

	public SQL view(String view_name){
		return new Drop("VIEW", true, view_name);
	}

	public SQL index(String index_name){
		return new Drop("INDEX", true, index_name);
	}

	public SQL indexIfExists(String index_name){
		return new Drop("INDEX", false, index_name);
	}

	public SQL trigger(String index_name){
		return new Drop("TRIGGER", true, index_name);
	}

	public SQL triggerIfExists(String trigger_name){
		return new Drop("TRIGGER", false, trigger_name);
	}

}
