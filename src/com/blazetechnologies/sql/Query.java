package com.blazetechnologies.sql;

import com.blazetechnologies.Entity;
import com.blazetechnologies.Utils;

/**
 * Created by Dominic on 27/08/2015.
 */
public class Query extends SQL{

    Query(){}

    public static Select select(boolean distinct, String... columns){
        return new Select(distinct, columns);
    }

    public static Select select(String... columns){
        return select(false, columns);
    }

	public static Values selectValues(Object... values){
		return new Values().values(values);
	}

	public static Values selectValues(Query... values){
		return new Values().values(values);
	}

    private String buildSubQuery(){
        return "(" + builder.toString() + ")";
    }

	public static class Values extends GroupBy{
		int valuesCount;

		private Values(){
			valuesCount = 0;
		}

		public Values values(Object... values){
			if(valuesCount > 0){
				builder.append(", ");
			}else{
				builder.append("VALUES ");
			}

			if(valuesCount < 500){
				builder.append("(");
				for (int x = 0; x < values.length; x++) {
					builder.append("?");
					bindings.add(values[x]);
					if(x < values.length - 1){
						builder.append(", ");
					}
				}
				builder.append(")");
			}else{
				throw new UnsupportedOperationException("VALUES limit is 500");
			}
			builder.append(" ");
			valuesCount++;
			return this;
		}

		public Values values(Query... values){
			if(valuesCount > 0){
				builder.append(", ");
			}else{
				builder.append("VALUES ");
			}

			if(valuesCount < 500){
				builder.append("(");
				for (int x = 0; x < values.length; x++) {
					builder.append(values[x].buildSubQuery());
					bindings.addAll(values[x].getBindings());
					if(x < values.length - 1){
						builder.append(", ");
					}
				}
				builder.append(")");
			}else{
				throw new UnsupportedOperationException("VALUES limit is 500");
			}
			builder.append(" ");
			valuesCount++;
			return this;
		}
	}

    public static class Select extends From{
        private Select(boolean distinct, String... columns){
            //builder = new StringBuilder();
            //bindings = new ArrayList<>();
            builder.append("SELECT ");
            if(distinct){
                builder.append("DISTINCT ");
            }
            if(columns == null || columns.length < 1 || columns[0] == null || columns[0].equals("*")){
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
            bindings.addAll(subQuery.bindings);
            return from(subQuery.buildSubQuery());
        }

        public <E extends Entity> From from(Class<E> table){
            return from(Utils.getTableName(table));
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

        public Where where(Condition condition){
            bindings.addAll(condition.getBindings());
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
            bindings.addAll(subQuery.bindings);
            return join(subQuery.buildSubQuery(), operator);
        }

        public Join join(String tableViewOrSubquery, JoinOperator operator){
            if(operator == null)operator = JoinOperator.NONE;
            if(operator != JoinOperator.NONE){
                switch (operator){
                    case LEFT:
                        builder.append("LEFT ");
                        break;
                    case LEFT_OUTER:
                        builder.append("LEFT OUTER ");
                        break;
                    case INNER:
                        builder.append("INNER ");
                        break;
                    case CROSS:
                        builder.append("CROSS ");
                        break;
                }
            }
            builder.append("JOIN ").append(tableViewOrSubquery).append(" ");
            return new Join(this);
        }

        public <E extends Entity> Join join(Class<E> table, JoinOperator operator){
            return join(Utils.getTableName(table), operator);
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
    }

    public static class Having extends GroupBy{
        private Having(Where where){
            bindings = where.bindings;
            builder = where.builder;
        }

        public GroupBy having(Condition condition){
            bindings.addAll(condition.getBindings());
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
		private static final String EXCEXPT = "EXCEPT";

		private Query query;
		private String operator;

		private Compound(Query query, String operator){
			this.query = query;
			this.operator = operator;
		}

		public Select select(boolean distinct, String... columns){
			Select select = Query.select(distinct, columns);
			select.builder.append(query.build()).append(" ").append(operator).append(" ");
			select.bindings.addAll(query.bindings);
			return select;
		}

		public Select select(String... columns){
			return select(false, columns);
		}

		public Values selectValues(Object... values){
			Values nextValues = new Values().values(values);
			nextValues.builder.append(query.build()).append(" ").append(operator).append(" ");
			nextValues.bindings.addAll(query.bindings);
			return nextValues;
		}

		public Values selectValues(Query... values){
			Values nextValues = new Values().values(values);
			nextValues.builder.append(query.build()).append(" ").append(operator).append(" ");
			nextValues.bindings.addAll(query.bindings);
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
			return new Compound(this, Compound.EXCEXPT);
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

        public OrderBy orderBy(String column, Order order){
			builder.append("ORDER BY ").append(column).append(" ").append(order.name()).append(" ");
			return this;
		}
    }

    public static class OrderBy extends Query{
        private OrderBy(){}

        public Query limit(long limit){
            return limit(limit, 0);
        }

        public Query limit(long limit, long offset){
            builder.append("LIMIT ").append(limit).append(" ");
            builder.append("OFFSET ").append(offset).append(" ");
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

        public From on(Condition condition){
            from.bindings.addAll(condition.getBindings());
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
        CROSS
    }

}
