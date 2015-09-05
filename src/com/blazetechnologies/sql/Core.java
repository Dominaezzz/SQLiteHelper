package com.blazetechnologies.sql;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dominic on 29/08/2015.
 */
public class Core extends Query{

	private Core(String name, String[] arguments, Object[] bindings){
		builder = new StringBuilder(name).append('(');
		for (int x = 0; x < arguments.length; x++) {
			builder.append(arguments[x]);
			if(x < arguments.length - 1){
				builder.append(", ");
			}
		}
		builder.append(')');
		this.bindings = new ArrayList<>();
		Collections.addAll(this.bindings, bindings);
	}



}
