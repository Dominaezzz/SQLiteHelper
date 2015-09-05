package com.blazetechnologies;

import com.blazetechnologies.sql.Condition;
import com.blazetechnologies.sql.Query;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println(SQLiteDatabase.createTable(TestEntity.class));

        System.out.println(
                Query.select(true, "Title").from(Query.select("").join("").on(""))
                .where("Artist = 'lana del rey'")
                .groupBy("Album")
                .orderBy("Disc ASC")
                .limit(10, 3).build()
        );
        Condition.col("Artist").eq(2).and(Condition.col("Album artist").gt("lady").and("Genre").notEq("Pop"));

    }
}
