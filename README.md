# SQLiteHelper
A simple java library to help build sql statements for SQLite.
Nothing serious, I started it for learning purposes and I think it's an interesting project and it tasks me in a certain way.
I uploaded it for anyone who might find it useful and requires an sql builder.

# Select
```java
select(true, "Title")
.from("Source Table")
.where(col("Artist").eq("Lana Del Rey"))
.groupBy("Album")
.orderBy("Disc", Order.ASC)
.limit(10, 3)
```

The output is...
```sql
SELECT DISTINCT Title
FROM [Source Table] 
WHERE ([Artist] = 'Lana Del Rey') 
GROUP BY Album 
ORDER BY Disc ASC 
LIMIT (10) OFFSET (3)
```

# Insert
```java
Insert.into("TestTable")
	.columns("Id", "Name")
	.values(1, "Dominic")
	.values(2, "Fischer")
	.values(3, "Courtney")
	.values(4, "Random name")
	.values(5, "Stuff")
```

The output is...
```sql
INSERT INTO TestTable ([Id], [Name]) 
VALUES (1, 'Dominic') ,
   (2, 'Fischer') ,
   (3, 'Courtney') ,
   (4, 'Random name') ,
   (5, 'Stuff')
```

# Update
```java
Update.table(TestEntity.class)
	.set("Id", "4")
	.set("Name", "Edwin")
	.set("Birthday", date().startOfYear().plus(7, DateTime.Mod.MONTHS))
	.where(col("Stuff").eq("Other stuff"))
```

The output is...
```sql
UPDATE [Test Table] 
SET [Id] = '4' ,
[Name] = 'Edwin' ,
[Birthday] = date('now', 'start of year', '+7 months') 
WHERE [Stuff] = 'Other stuff'
```

# Delete
```java
Delete.from("Test Entity")
	.where(
			col("_id").eq(7).or().col("_id").eq(2).and(
					col("_id").in(39, 78, 56, 0)
			)
	)
```

The output is...
```sql
DELETE FROM [Test Entity] 
WHERE [_id] = 7 OR [_id] = 2 AND ([_id] IN(39, 78, 56, 0))
```

# Create Table
```java
Create.table("TestTable")
    .columns(
        ColumnDef.create("Id", DataType.INTEGER, ColumnConstraint.primaryKey(true, Order.ASC)),
        ColumnDef.create("Name", DataType.TEXT, ColumnConstraint.unique())
    ).constraints(
        TableConstraint.check(col("Id").gtEq(0)),
        TableConstraint.foreignKey("Name").references("OtherTable", "Staff name").onDelete(Action.CASCADE)
    )
```

The output is...
```sql
CREATE TABLE [TestTable](
    [Id] INTEGER PRIMARY KEY ASC AUTOINCREMENT, 
    [Name] TEXT UNIQUE, 
    CHECK([Id] >= 0), 
    FOREIGN KEY (Name) REFERENCES OtherTable([Staff name]) ON DELETE CASCADE);
```

# Create View
```java
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
```

The output is...
```sql
CREATE VIEW [TestView]
AS
SELECT [stuff] , (date('now', 'start of month', '+5 days', 'unixepoch') - (7)) , COUNT(*)
FROM [Test Table]
WHERE ([Id] = 5 AND [Date] = date('now'))
```

# Alter Table
```java
Table.alter("TestTable")
.addColumn(ColumnDef.create("Surname", DataType.TEXT))
```

The output is...
```sql
ALTER TABLE [TestTable] ADD COLUMN [Surname] TEXT
```

# Statements with parameters
```java
Update update = Update.table("TestTable")
				.set("Name", bind("Michael"))
				.set("Last Name", bind("Peter"))
				.set("Birthday", date(System.currentTimeMillis()/1000))
				.where(col("Id").eq(5));
		
ArrayList<Object> bindValues = update.getBindings();
```

The output is...
```sql
UPDATE [TestTable] SET Name = ?, [Last Name] = ?, BirthDay = date(1837293792) WHERE Id = 5

bindValues = [Michael, Peter]
```