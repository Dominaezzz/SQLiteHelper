package com.blazetechnologies.sql.table;

import com.blazetechnologies.sql.Condition;
import com.blazetechnologies.sql.Order;
import com.blazetechnologies.sql.Query;

/**
 * Created by Dominic on 07/09/2015.
 */
public abstract class ColumnConstraint {

	private ColumnConstraint(){}

	public abstract String build();

	public static ColumnConstraint primaryKey(){
		return primaryKey(false);
	}

	public static ColumnConstraint primaryKey(boolean autoIncrement){
		return primaryKey(null, autoIncrement);
	}

	public static ColumnConstraint primaryKey(Order order){
		return primaryKey(order, false);
	}

	public static ColumnConstraint primaryKey(Order order, boolean autoIncrement){
		return new PrimaryKey(order, autoIncrement);
	}

	public static ColumnConstraint unique(){
		return new Unique();
	}

	public static ColumnConstraint notNull(){
		return new NotNull();
	}

	public static ColumnConstraint check(Condition condition){
		return check(condition.build());
	}

	public static ColumnConstraint check(String condition){
		return new Check(condition);
	}

	public static ColumnConstraint defaultValue(Long signed_number){
		return new Default(signed_number, null, null);
	}

	public static ColumnConstraint defaultValue(String literalValue_or_Query, boolean isQuery){
		if(isQuery)
			return new Default(null, null, literalValue_or_Query);
		else
			return new Default(null, literalValue_or_Query, null);
	}

	public static ColumnConstraint defaultValue(Query query){
		return new Default(null, null, query.build());
	}

	public static ForeignKey references(String table, String... columns){
		return new ForeignKey(table, columns);
	}

	public static class PrimaryKey extends ColumnConstraint{
		private Order order;
		private boolean autoIncrement;

		private PrimaryKey(Order order, boolean autoIncrement){
			this.order = order;
			this.autoIncrement = autoIncrement;
		}

		@Override
		public String build() {
			StringBuilder builder = new StringBuilder("PRIMARY KEY ");
			if(order != null){
				builder.append(order.name()).append(" ");
			}
			if(autoIncrement){
				builder.append("AUTOINCREMENT");
			}
			return builder.toString();
		}
	}

	public static class Unique extends ColumnConstraint{
		private Unique(){}

		@Override
		public String build() {
			return "UNIQUE";
		}
	}

	public static class NotNull extends ColumnConstraint{
		private NotNull(){}

		@Override
		public String build() {
			return "NOT NULL";
		}
	}

	public static class Check extends ColumnConstraint{
		private String condition;

		private Check(String condition){
			this.condition = condition;
		}

		@Override
		public String build() {
			return "CHECK(" + condition + ")";
		}
	}

	public static class Default extends ColumnConstraint{
		private Long signed_number;
		private String literal_value;
		private String expr;

		private Default(Long signed_number, String literal_value, String expr){
			this.signed_number = signed_number;
			this.literal_value = literal_value;
			this.expr = expr;
		}

		@Override
		public String build() {
			if(signed_number != null){
				return "DEFAULT " + signed_number;
			}else if(literal_value != null){
				return "DEFAULT '" + literal_value + "'";
			}else if(expr != null){
				return "DEFAULT (" + expr + ")";
			}else{
				return "DEFAULT NULL";
			}
		}
	}

	public static class ForeignKey extends ColumnConstraint{
		private String foreignTable;
		private String[] columns;

		private Action onDelete;
		private Action onUpdate;
		private String match;

		private ForeignKey(String table, String[] columns){
			this.foreignTable = table;
			this.columns = columns;
		}

		public ForeignKey onDelete(Action action){
			onDelete = action;
			return this;
		}

		public ForeignKey onUpdate(Action action){
			onUpdate = action;
			return this;
		}

		public ForeignKey match(String name){
			match = name;
			return this;
		}

		@Override
		public String build() {
			StringBuilder builder = new StringBuilder("REFERENCES ");
			builder.append(foreignTable);
			if(columns.length > 0){
				builder.append("(");
				for (int x = 0; x < columns.length; x++) {
					builder.append(columns[x]);
					if(x < columns.length - 1){
						builder.append(", ");
					}
				}
				builder.append(")");
			}
			builder.append(" ");

			if(onDelete != null){
				builder.append("ON DELETE ").append(onDelete.toString()).append(" ");
			}

			if(onUpdate != null){
				builder.append("ON UPDATE ").append(onUpdate.toString()).append(" ");
			}

			if(match != null){
				builder.append("MATCH ").append(match);
			}

			return builder.toString().trim();
		}
	}

}
