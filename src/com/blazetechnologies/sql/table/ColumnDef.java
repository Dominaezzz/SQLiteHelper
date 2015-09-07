package com.blazetechnologies.sql.table;

import java.util.ArrayList;

/**
 * Created by Dominic on 07/09/2015.
 */
public class ColumnDef {

	private String name;
	private DataType type;
	private ArrayList<ColumnConstraint> constraints;

	private ColumnDef(String name, DataType type){
		this.name = name;
		this.type = type;
		constraints = new ArrayList<>();
	}

	public ColumnDef addConstraint(ColumnConstraint constraint){
		constraints.add(constraint);
		return this;
	}

	public String build(){
		StringBuilder builder = new StringBuilder(name).append(" ");
		if(type != DataType.NONE){
			builder.append(type.name()).append(" ");
		}
		for(ColumnConstraint constraint : constraints){
			builder.append(constraint.build()).append(" ");
		}
		return builder.toString().trim();
	}

	public static ColumnDef create(String name){
		return create(name, DataType.NONE);
	}

	public static ColumnDef create(String name, DataType type){
		return new ColumnDef(name, type);
	}

	public static ColumnDef create(String name, ColumnConstraint constraint){
		return create(name, DataType.NONE, constraint);
	}

	public static ColumnDef create(String name, DataType type, ColumnConstraint constraint){
		return create(name, type).addConstraint(constraint);
	}

}
