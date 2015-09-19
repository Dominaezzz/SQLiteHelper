package com.blazetechnologies.sql;

/**
 * Created by Dominic on 19/09/2015.
 */
public class IndexedColumn extends SQL {

	private IndexedColumn(String name){
		builder.append(name).append(" ");
	}

	public static IndexedColumn name(String name){
		return new IndexedColumn(name);
	}

	public IndexedColumn collate(String collation_name){
		builder.append("COLLATE ").append(collation_name).append(" ");
		return this;
	}

	public IndexedColumn asc(){
		builder.append("ASC");
		return this;
	}

	public IndexedColumn desc(){
		builder.append("DESC");
		return this;
	}

}
