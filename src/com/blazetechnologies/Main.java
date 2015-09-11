package com.blazetechnologies;

import com.blazetechnologies.sql.*;
import com.blazetechnologies.sql.table.*;

import static com.blazetechnologies.sql.Query.selectValues;
import static com.blazetechnologies.sql.Query.select;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println(SQLiteDatabase.createTable(TestEntity.class));

		System.out.println();

        System.out.println(
				select(true, "Title").from(
						select("Title").from(TestEntity.class).join(TestEntity.class).on("[First Name]")
				)
						.where("Artist = 'Lana Del Rey'")
						.groupBy("Album")
						.orderBy("Disc ASC")
						.limit(10, 3)
		);

		System.out.println();

		System.out.println(
				Expr.col("Artist").eq(2.75)
						.and(
								Expr.col("Album artist").gt("lady").and().col("Genre").notEq("Pop")
						).or().col("Title").eq("Judas")
		);

		System.out.println();

		System.out.println(
				Create.table("TestTable").columns(
						ColumnDef.create("Id", DataType.INTEGER, ColumnConstraint.primaryKey(Order.ASC, true)),
						ColumnDef.create("Name", DataType.TEXT, ColumnConstraint.unique())
				).constraints(
						TableConstraint.check(Expr.col("Id").gtEq(0))
				)
		);

		System.out.println();

		System.out.println(
				Table.create("TestTable").columns(
						ColumnDef.create("Id", DataType.INTEGER, ColumnConstraint.primaryKey(Order.DESC, true)),
						ColumnDef.create("Name", DataType.TEXT, ColumnConstraint.unique())
				).constraints(
						TableConstraint.foreignKey("Name").references("OtherTable", "Staff name")
				)
		);

		System.out.println();

		System.out.println(
				Insert.into("TestTable")
						.columns("Name")
						.values("Dominic")
						.values("Fischer")
						.values("Courtney")
						.values("Random name")
						.values("Stuff")
				.build()
		);

		System.out.println();

		System.out.println(
				Create.view("TestView").as(select("Stuff").from(TestEntity.class).where(Expr.col("Id").eq(5))).build()
		);

		System.out.println();

		System.out.println(
				Table.alter("TestTable").addColumn("Surname").build()
		);

		System.out.println();

		System.out.println(
				Update.table(TestEntity.class).set("Id", "4").where("Stuff == stuff").build()
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
				).build()
		);

		System.out.println();

		System.out.println(
				Case.when("Stuff == 'other stuff'").then("more stuff")
						.when("Stuff = 'Useful stuff'").then("Useful")
						.when(Expr.col("Stuff").eq("Crap")).then("Crappy stuff")
						.orElse("Unknown stuff")//.end()
		);

    }
}
