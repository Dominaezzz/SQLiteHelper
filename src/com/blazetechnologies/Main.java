package com.blazetechnologies;

import com.blazetechnologies.sql.*;
import com.blazetechnologies.sql.pragma.Pragma;
import com.blazetechnologies.sql.pragma.Synchronous;
import com.blazetechnologies.sql.table.*;

import java.util.ArrayList;

import static com.blazetechnologies.sql.Aggregate.count;
import static com.blazetechnologies.sql.DateTime.date;
import static com.blazetechnologies.sql.Query.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        test();

    }

	public static void test(){
		System.out.println(Table.create(TestEntity.class));
		System.out.println();

		createTable();

		createTrigger();

		createView();

		selectComplex();

		insert();

		update();

		delete();

		System.out.println(
				col("Artist").eq(2.75)
						.and(
								col("Album artist").gt("lady").and().col("Genre").notEq("Pop")
						)
						.or()
						.col(TestEntity.class, "Title").eq("Judas")
		);
		System.out.println();

		System.out.println(
				Table.alter("TestTable").addColumn(ColumnDef.create("Surname", DataType.TEXT))
		);
		System.out.println();

		System.out.println(
				selectValues(
						2, "Some name"
				).values(
						5, "Some other name"
				).values(
						8, "Some odd name"
				).values(
						9, "Creepy crawly or something"
				)
		);
		System.out.println();

		System.out.println(
				Case.when("Stuff == 'other stuff'").then("more stuff")
						.when("Stuff = 'Useful stuff'").then("Useful")
						.when(col("Stuff").eq("Crap")).then("Crappy stuff")
						.orElse("Unknown stuff").end()
		);
		System.out.println();

		System.out.println(
				Pragma.foreign_keys(true)
		);
		System.out.println();

		System.out.println(
				Pragma.synchronous(Synchronous.OFF)
		);
	}

	public static void createTable(){
		System.out.println(
				Create.table("TestTable").columns(
						ColumnDef.create("Id", DataType.INTEGER, ColumnConstraint.primaryKey(true, Order.ASC)),
						ColumnDef.create("Name", DataType.TEXT, ColumnConstraint.unique())
				).constraints(
						TableConstraint.check(col("Id").gtEq(0)),
						TableConstraint.foreignKey("Name").references("OtherTable", "Staff name").onDelete(Action.CASCADE)
				)
		);
		System.out.println();
	}

	public static void createTrigger(){
		System.out.println(
				Create.trigger("Test trigger")
						.after()
						.delete()
						.on("TestTable")
						.forEachRow()
						.begin(
								Update.table("TestTable").set("Id", 4).where(col("Id").eq(7))
						)
						.end()
		);
		System.out.println();
	}

	public static void createView(){
		System.out.println(
				Create.view("TestView").as(
						select(
								col("stuff"),
								date().startOfMonth().plus(5, DateTime.Mod.DAYS).unixepoch().minus(7),
								count()
						)
								.from(TestEntity.class)
								.where(
										col("Id").eq(5).and().col("Date").eq(date())
								)
				)
		);
		System.out.println();
	}

	public static void selectComplex(){
		System.out.println(
				select(true, "Title").from(
						select("Title").from(TestEntity.class)
								.join(TestEntity.class).on("[First Name]")
				)
						.where(col("Artist").eq("Lana Del Rey"))
						.groupBy("Album")
						.orderBy("Disc", Order.ASC)
						.limit(10, 3)
		);
		System.out.println();
	}

	public static void delete(){
		System.out.println(
				Delete.from("Test Entity")
						.where(
								col("_id").eq(7).or().col("_id").eq(2).and(
										col("_id").in(39, 78, 56, 0)
								)
						)
		);
		System.out.println();
	}

	public static void update(){
		System.out.println(
				Update.table(TestEntity.class)
						.set("Id", "4")
						.set("Name", "Edwin")
						.set("Birthday", date().startOfYear().plus(7, DateTime.Mod.MONTHS))
						.where(col("Stuff").eq("Other stuff"))
		);
		System.out.println();
	}

	public static void insert(){
		System.out.println(
				Insert.into("TestTable")
						.columns("Id", "Name")
						.values(1, "Dominic")
						.values(2, "Fischer")
						.values(3, "Courtney")
						.values(4, "Random name")
						.values(5, "Stuff")
		);
		System.out.println();
	}

	public static void parameters(){
		Update update = Update.table("TestTable")
				.set("Name", bind("Michael"))
				.set("Last Name", bind("Peter"))
				.set("Birthday", date(System.currentTimeMillis()/1000))
				.where(col("Id").eq(5));

		ArrayList<Object> bindValues = update.getBindings();
	}
}
