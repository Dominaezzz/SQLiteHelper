package com.blazetechnologies;

import com.blazetechnologies.sql.Condition;
import com.blazetechnologies.sql.Create;
import com.blazetechnologies.sql.Order;
import com.blazetechnologies.sql.Query;
import com.blazetechnologies.sql.table.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println(SQLiteDatabase.createTable(TestEntity.class));

        System.out.println(
				Query.select(true, "Title").from(Query.select("").join("").on(""))
						.where("Artist = 'Lana Del Rey'")
						.groupBy("Album")
						.orderBy("Disc ASC")
						.limit(10, 3).build()
		);
        Condition.col("Artist").eq(2).and(Condition.col("Album artist").gt("lady").and("Genre").notEq("Pop"));

		Create.table("TestTable").columns(
				ColumnDef.create("Id", DataType.INTEGER, ColumnConstraint.primaryKey(Order.ASC, true)),
				ColumnDef.create("Name", DataType.TEXT, ColumnConstraint.unique())
		).constraints(
				TableConstraint.check(Condition.col("Id").gtEq(0))
		);

		Table.create("TestTable").columns(
				ColumnDef.create("Id", DataType.INTEGER, ColumnConstraint.primaryKey(Order.DESC, true)),
				ColumnDef.create("Name", DataType.TEXT, ColumnConstraint.unique())
		).constraints(
				TableConstraint.foreignKey("Name").references("OtherTable", "Staff name")
		);

		Create.view("TestView").as(Query.select("Stuff").from(TestEntity.class).where(Condition.col("Id").eq(5)));

		Table.alter("TestTable").addColumn("");

    }
}
