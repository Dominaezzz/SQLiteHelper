package com.blazetechnologies.sql;

import com.blazetechnologies.Utils;
import com.blazetechnologies.sql.table.Table;

/**
 * Created by Dominic on 04/09/2015.
 */
public class Create extends SQL {

	private Create(){}

	public static View view(boolean temp, String name){
		return new View(temp, name);
	}

	public static View view(String name){
		return view(false, name);
	}

	public static Index index(boolean unique, String name){
		return new Index(unique, name);
	}

	public static Index index(String name){
		return index(false, name);
	}

	public static Table.CreateTable table(boolean temp, String name){
		return Table.create(temp, name);
	}

	public static Table.CreateTable table(String name){
		return table(false, name);
	}

	public static Trigger trigger(boolean temp, String name){
		return new Trigger(temp, name);
	}

	public static Trigger trigger(String name){
		return trigger(false, name);
	}

	public static class View extends Create{
		View(boolean temp, String name){
			builder.append("CREATE ");
			if(temp){
				builder.append("TEMPORARY ");
			}
			builder.append("VIEW [").append(name).append("] ");
		}

		public Create as(Query query){
			return as(query.build());
		}

		public Create as(String query){
			builder.append("AS ").append(query);
			return this;
		}
	}

	public static class Index{
		StringBuilder builder;

		private Index(boolean unique, String name){
			builder = new StringBuilder("CREATE ");
			if(unique){
				builder.append("UNIQUE ");
			}
			builder.append("INDEX ").append(name);
		}

		public Where on(String table, String... indexed_columns){
			builder.append("ON ").append(table);
			builder.append("(");
			for (int x = 0; x < indexed_columns.length; x++) {
				builder.append(indexed_columns[x]);
				if(x < indexed_columns.length - 1){
					builder.append(", ");
				}
			}
			builder.append(")");
			return new Where(builder);
		}

		public Where on(String table, IndexedColumn... indexed_columns){
			builder.append("ON ").append(table);
			builder.append("(");
			for (int x = 0; x < indexed_columns.length; x++) {
				builder.append(indexed_columns[x].build());
				if(x < indexed_columns.length - 1){
					builder.append(", ");
				}
			}
			builder.append(")");
			return new Where(builder);
		}
	}

	public static class Where extends Create{
		Where(StringBuilder builder){
			this.builder = builder;
		}

		public Create where(String where){
			builder.append("WHERE ").append(where);
			return this;
		}

		public Create where(Expr condition){
			return where(condition.build());
		}
	}

	public static class Trigger extends Trigger2{
		private Trigger(boolean temp, String name){
			builder.append("CREATE ");
			if(temp){
				builder.append("TEMPORARY ");
			}
			builder.append("TRIGGER ").append(Utils.encaseKeyword(name)).append(" ");
		}

		public Trigger2 before(){
			builder.append("BEFORE ");
			return this;
		}

		public Trigger2 after(){
			builder.append("AFTER ");
			return this;
		}

		public Trigger2 insteadOf(){
			builder.append("INSTEAD OF ");
			return this;
		}
	}

	public static class Trigger2{
		protected StringBuilder builder;

		private Trigger2(){
			builder = new StringBuilder();
		}

		public Trigger3 delete(){
			builder.append("DELETE ");
			return new Trigger3(this);
		}

		public Trigger3 insert(){
			builder.append("INSERT ");
			return new Trigger3(this);
		}

		public Trigger3 update(){
			builder.append("UPDATE ");
			return new Trigger3(this);
		}

		public Trigger3 updateOf(String... columns){
			builder.append("UPDATE OF ");
			for (int x = 0; x < columns.length; x++) {
				builder.append(columns[x]);
				if(x < columns.length - 1){
					builder.append(", ");
				}
			}
			builder.append(" ");
			return new Trigger3(this);
		}
	}

	public static class Trigger3{
		private StringBuilder builder;

		private Trigger3(Trigger2 trigger2){
			builder = trigger2.builder;
		}

		public Trigger4 on(String table_name){
			builder.append("ON ").append(Utils.encaseKeyword(table_name)).append(" ");
			return new Trigger4(this);
		}
	}

	public static class Trigger4 extends Trigger5{
		private Trigger4(Trigger3 trigger3){
			super(trigger3.builder);
		}

		public Trigger5 forEachRow(){
			builder.append("FOR EACH ROW ");
			return this;
		}

		public Trigger5 forEachRow(Expr when_expr){
			builder.append("FOR EACH ROW WHEN ").append(when_expr).append(" ");
			return this;
		}

		public Trigger5 forEachRow(String when_expr){
			builder.append("FOR EACH ROW WHEN ").append(when_expr).append(" ");
			return this;
		}
	}

	public static class Trigger5 {
		protected StringBuilder builder;
		private boolean hasNotBegun;

		private Trigger5(CharSequence sequence) {
			builder = new StringBuilder(sequence);
			hasNotBegun = true;
		}

		public Trigger5 addStmt(SQL stmt){
			if(hasNotBegun){
				builder.append("BEGIN ");
				hasNotBegun = false;
			}
			builder.append(stmt).append("; ");
			return this;
		}

		public Trigger5 begin(Update update){
			return addStmt(update);
		}

		public Trigger5 begin(Insert insert){
			return addStmt(insert);
		}

		public Trigger5 begin(Delete delete){
			return addStmt(delete);
		}

		public Trigger5 begin(Query query){
			return addStmt(query);
		}

		public Trigger5 begin(String stmt){
			if(hasNotBegun){
				builder.append("BEGIN ");
				hasNotBegun = false;
			}
			builder.append(stmt).append("; ");
			return this;
		}

		public SQL end(){
			builder.append("END");
			return SQL.raw(builder);
		}
	}
}
