package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;
import com.blazetechnologies.executors.JDBCExecutor;
import com.blazetechnologies.executors.Selectable;
import com.sun.istack.internal.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Dominic on 27/08/2015.
 */
public class Query extends Expr{

    private Query(){}

    public static Select select(boolean distinct, String... columns){
        return new Select(distinct, columns);
    }

    public static Select select(String... columns){
        return select(false, columns);
    }

	public static Select select(boolean distinct, RColumn... rColumns){
		String[] columns = new String[rColumns.length];
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Object> bindings = new ArrayList<>();
		for (int x = 0; x < rColumns.length; x++) {
			RColumn rColumn = rColumns[x];
			if(rColumn.getExpr() != null){
				stringBuilder.append(Utils.parenthesize(rColumn.getExpr().toString())).append(" ");
                bindings.addAll(rColumn.getExpr().getBindings());
			}else if(rColumn.getStringExpr() != null){
				stringBuilder.append(Utils.parenthesize(rColumn.getStringExpr())).append(" ");
			}else {
				stringBuilder.append(rColumn.getAlias());
				columns[x] = stringBuilder.toString();
				continue;
			}
			if(rColumn.getAlias() != null){
				stringBuilder.append("AS ").append(rColumn.getAlias());
			}
			columns[x] = stringBuilder.toString();
            stringBuilder.delete(0, stringBuilder.length());
		}
        Select select = select(distinct, columns);
        select.getBindings().addAll(bindings);
        return select;
	}

	public static Select select(RColumn... rColumns){
		return select(false, rColumns);
	}

	public static Select select(boolean distinct){
		return new Select(distinct, new String[]{"*"});
	}

	public static Select select(){
		return select(false);
	}

	public static Values selectValues(Object... values){
		return new Values().values(values);
	}

	public static Values selectValues(Expr... values){
		return new Values().values(values);
	}

    private String buildSubQuery(){
        return "(" + builder.toString() + ")";
    }

    public <T> T executeQuery(Selectable<T> selectable){
        return selectable.executeQuery(toString(), getBindings().toArray());
    }

    public ResultSet executeQuery(Connection connection){
        return executeQuery(new JDBCExecutor(connection));
    }

	public static class Values extends GroupBy{
		int valuesCount;

		private Values(){
			valuesCount = 0;
		}

		@SafeVarargs
		public final <T> Values values(T... values){
			Expr[] exprs = new Expr[values.length];
			for (int x = 0; x < values.length; x++) {
				exprs[x] = value(values[x]);
			}
			return values(exprs);
		}

		public Values values(Expr... values){
			if(valuesCount > 0){
				builder.deleteCharAt(builder.length() - 1);
				builder.append(", ");
			}else{
				builder.append("VALUES ");
			}

			builder.append("(");
			for (int x = 0; x < values.length; x++) {
				builder.append(values[x]);
				getBindings().addAll(values[x].getBindings());
				if(x < values.length - 1){
					builder.append(", ");
				}
			}
			builder.append(") ");
			valuesCount++;
			return this;
		}
	}

    public static class Select extends From{
        private Select(boolean distinct, String[] columns){
            builder.append("SELECT ");
            if(distinct){
                builder.append("DISTINCT ");
            }
            if(columns.length < 1){
                builder.append("* ");
            }else{
                for(int x = 0; x < columns.length; x++) {
                    builder.append(columns[x]);
                    if(x < columns.length - 1){
                        builder.append(", ");
                    }
                }
                builder.append(" ");
            }
        }

        public From from(Query subQuery){
            getBindings().addAll(subQuery.getBindings());
            return from(subQuery.build());
        }

        public <E extends Entity> From from(Class<E> table){
            return from(Entity.getEntityName(table));
        }

        public From from(String tableViewOrSubQuery){
            builder.append("FROM ");
            if(tableViewOrSubQuery.contains(" ")){
                if(tableViewOrSubQuery.toLowerCase().contains("select")){
                    builder.append('(').append(tableViewOrSubQuery).append(')');
                }else{
                    builder.append('[').append(tableViewOrSubQuery).append(']');
                }
            }else{
                builder.append(tableViewOrSubQuery);
            }
            builder.append(" ");
            return this;
        }
    }

    public static class From extends Where{
        private From(){}

        public Where where(String expr){
            builder.append("WHERE ");
            if(expr.contains(" ")){
                builder.append('(').append(expr).append(')');
            }else{
                builder.append(expr);
            }
            builder.append(" ");
            return this;
        }

        public Where where(Expr condition){
            getBindings().addAll(condition.getBindings());
            return where(condition.build());
        }

        public <E extends Entity> Join join(Class<E> table){
            return join(table, JoinOperator.NONE);
        }

        public Join join(String tableViewOrSubquery){
            return join(tableViewOrSubquery, JoinOperator.NONE);
        }

        public Join join(Query subQuery){
            return join(subQuery, JoinOperator.NONE);
        }

        public Join join(Query subQuery, JoinOperator operator){
            getBindings().addAll(subQuery.getBindings());
            return join(subQuery.buildSubQuery(), operator);
        }

        public Join join(String tableViewOrSubquery, @NotNull JoinOperator operator){
            if(operator != JoinOperator.NONE){
				builder.append(operator.toString()).append(" ");
            }
            builder.append("JOIN ").append(tableViewOrSubquery).append(" ");
            return new Join(this);
        }

        public <E extends Entity> Join join(Class<E> table, JoinOperator operator){
            return join(Entity.getEntityName(table), operator);
        }
    }

    public static class Where extends GroupBy{
        private Where(){}

        public Having groupBy(String... columns){
            if(columns.length > 0){
                builder.append("GROUP BY ");
                for (int x = 0; x < columns.length; x++) {
                    builder.append(columns[x]);
                    if(x < columns.length - 1) {
                        builder.append(", ");
                    }
                }
                builder.append(" ");
            }
            return new Having(this);
        }

		public Having groupBy(Expr... exprs){
			if(exprs.length > 0){
				builder.append("GROUP BY ");
				for (int x = 0; x < exprs.length; x++) {
					builder.append(exprs[x]);
					getBindings().addAll(exprs[x].getBindings());
					if(x < exprs.length - 1) {
						builder.append(", ");
					}
				}
				builder.append(" ");
			}
			return new Having(this);
		}
    }

    public static class Having extends GroupBy{
        private Having(Where where){
            builder = where.builder;
			getBindings().addAll(where.getBindings());
        }

        public GroupBy having(Expr condition) {
			getBindings().addAll(condition.getBindings());
            return having(condition.build());
        }

        public GroupBy having(String expr){
            builder.append("HAVING ").append(expr).append(" ");
            return this;
        }
    }

	public static class Compound{
		private static final String UNION = "UNION";
		private static final String UNION_ALL = "UNION ALL";
		private static final String INTERSECT = "INTERSECT";
		private static final String EXCEPT = "EXCEPT";

		private Query query;
		private String operator;

		private Compound(Query query, String operator){
			this.query = query;
			this.operator = operator;
		}

		public Select select(boolean distinct, String... columns){
			Select select = Query.select(distinct, columns);
			select.builder.append(query.build()).append(" ").append(operator).append(" ");
			select.getBindings().addAll(query.getBindings());
			return select;
		}

		public Select select(String... columns){
			return select(false, columns);
		}

		public Values selectValues(Object... values){
			Values nextValues = new Values().values(values);
			nextValues.builder.append(query.build()).append(" ").append(operator).append(" ");
			nextValues.getBindings().addAll(query.getBindings());
			return nextValues;
		}

		public Values selectValues(Expr... values){
			Values nextValues = new Values().values(values);
			nextValues.builder.append(query.build()).append(" ").append(operator).append(" ");
			nextValues.getBindings().addAll(query.getBindings());
			return nextValues;
		}
	}

    public static class GroupBy extends OrderBy{
        private GroupBy(){}

		public Compound union(){
			return new Compound(this, Compound.UNION);
		}

		public Compound unionAll(){
			return new Compound(this, Compound.UNION_ALL);
		}

		public Compound intersect(){
			return new Compound(this, Compound.INTERSECT);
		}

		public Compound except(){
			return new Compound(this, Compound.EXCEPT);
		}

        public OrderBy orderBy(String... terms){
            if(terms != null && terms.length > 0){
                builder.append("ORDER BY ");
                for (int x = 0; x < terms.length; x++) {
                    builder.append(terms[x]);
                    if(x < terms.length - 1){
                        builder.append(',');
                    }
                    builder.append(' ');
                }
            }
            return this;
        }

        public OrderBy orderBy(Expr... exprs){
			if(exprs.length > 0){
				builder.append("ORDER BY ");
				for (int x = 0; x < exprs.length; x++) {
					builder.append(exprs[x]);
					getBindings().addAll(exprs[x].getBindings());
					if(x < exprs.length - 1){
						builder.append(',');
					}
					builder.append(' ');
				}
			}
			return this;
		}

        public OrderBy orderBy(String column, Order order){
			builder.append("ORDER BY ").append(column).append(" ").append(order.name()).append(" ");
			return this;
		}//add order builder
    }

    public static class OrderBy extends Query{
        private OrderBy(){}

        public Query limit(long limit){
            return limit(Expr.value(limit));
        }

        public Query limit(long limit, long offset){
            return limit(Expr.value(limit), Expr.value(offset));
        }

		public Query limit(Expr expr){
			builder.append("LIMIT (").append(expr).append(") ");
			getBindings().addAll(expr.getBindings());
			return this;
		}

		public Query limit(Expr limit, Expr offset){
			builder.append("LIMIT (").append(limit).append(") ");
			getBindings().addAll(limit.getBindings());
			builder.append("OFFSET (").append(offset).append(") ");
			getBindings().addAll(offset.getBindings());
			return this;
		}
    }

    public static class Join{
        private From from;

        private Join(From from){
            this.from = from;
        }

        public From on(String expr){
            from.builder.append("ON ").append(expr).append(" ");
            return from;
        }

        public From on(Expr condition){
            from.getBindings().addAll(condition.getBindings());
            return on(condition.build());
        }

        public From using(String... columns) {
            if(columns.length > 0){
                from.builder.append("USING (");
                for (int x = 0; x < columns.length; x++) {
                    from.builder.append(columns[x]);
                    if(x < columns.length - 1){
                        from.builder.append(", ");
                    }
                }
                from.builder.append(") ");
            }
            return from;
        }
    }

    public enum JoinOperator{
        NONE,
        LEFT,
        LEFT_OUTER,
        INNER,
        CROSS,
		NATURAL_LEFT,
		NATURAL_LEFT_OUTER,
		NATURAL_INNER,
		NATURAL_CROSS;

		@Override
		public String toString() {
			return super.toString().replace('_', ' ');
		}
	}

}
