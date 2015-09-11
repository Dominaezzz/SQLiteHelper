package com.blazetechnologies.sql;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dominic on 28/08/2015.
 */
public final class Condition {

    private static Class[] classes = new Class[]{
            Byte.TYPE,
            Byte.class,

            Short.TYPE,
			Short.class,

            Integer.TYPE,
			Integer.class,

            Long.TYPE,
			Long.class,

            Float.TYPE,
			Float.class,

            Double.TYPE,
			Double.class,

            Boolean.TYPE,
			Boolean.class,

            Character.TYPE,
			Character.class,

            Date.class,

			String.class
    };

    private StringBuilder builder;
    private ArrayList<Object> bindings;

    private Condition(String column){
        builder = new StringBuilder(column).append(" ");
        bindings = new ArrayList<>();
    }

    public static class Col{
        private Condition condition;

        private Col(Condition condition){
            this.condition = condition;
        }

        public  <T> Condition eq(T value){
            return condition.eq(value);
        }

        public <T> Condition notEq(T value){
            return condition.notEq(value);
        }

        public <T> Condition gt(T value){
            return condition.gt(value);
        }

        public <T> Condition gtEq(T value){
            return condition.gtEq(value);
        }

        public <T> Condition lt(T value){
            return condition.lt(value);
        }

        public <T> Condition ltEq(T value){
            return condition.ltEq(value);
        }

        public <T> Condition is(T value){
            return condition.is(value);
        }

        public <T> Condition isNot(T value){
            return condition.isNot(value);
        }

        public <T> Condition like(T value){
            return condition.like(value);
        }

        public <T> Condition notLike(T value){
            return condition.notLike(value);
        }

        public <T> Condition in(T... value){
            return condition.in(value);
        }

        public <T> Condition notIn(T... value){
            return condition.notIn(value);
        }

        public <T> Condition match(T value){
            return condition.match(value);
        }

        public <T> Condition between(T lower, T upper){
            return condition.between(lower, upper);
        }

        public Condition regex(String regex){
            return condition.regex(regex);
        }

        public <T> Condition notBetween(T lower, T upper){
            return condition.notBetween(lower, upper);
        }
    }

    public Col and(String column){
        builder.insert(0, "(").append(") AND (").append(column).append(" ");
        return new Col(this);
    }

    public Condition and(Condition condition){
        builder.append(" AND (").append(condition.build()).append(") ");
        bindings.add(condition.bindings);
        return this;
    }

    public Col or(String column){
        builder.insert(0, "(").append(") OR (").append(column).append(" ");
        return new Col(this);
    }

    public Condition or(Condition condition){
        builder.append(" OR (").append(condition.build()).append(") ");
        bindings.addAll(condition.bindings);
        return this;
    }

    public static Col col(String column){
        return new Col(new Condition(column));
    }

    private <T> Condition eq(T value){
        if(isSupported(value.getClass())){
            builder.append("= ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition notEq(T value){
        if(isSupported(value.getClass())){
            builder.append("!= ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition gt(T value){
        if(isSupported(value.getClass())){
            builder.append("> ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition gtEq(T value){
        if(isSupported(value.getClass())){
            builder.append(">= ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition lt(T value){
        if(isSupported(value.getClass())){
            builder.append("< ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition ltEq(T value){
        if(isSupported(value.getClass())){
            builder.append("<= ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition is(T value){
        if(isSupported(value.getClass())){
            builder.append("IS ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition isNot(T value){
        if(isSupported(value.getClass())){
            builder.append("IS NOT ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition like(T value){
        if(isSupported(value.getClass())){
            builder.append("LIKE ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition notLike(T value){
        if(isSupported(value.getClass())){
            builder.append("NOT LIKE ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    @SafeVarargs
    private final <T> Condition in(T... values){
        if(isSupported(values.getClass().getComponentType())){
            builder.append("IN(");
            for (int x = 0; x < values.length; x++) {
                builder.append("?");
                bindings.add(values[x]);
                if(x < values.length - 1){
                    builder.append(", ");
                }
            }
            builder.append(") ");
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(values.getClass().getSimpleName() + " is not supported");
    }

    @SafeVarargs
    private final <T> Condition notIn(T... values){
        if(isSupported(values.getClass().getComponentType())){
            builder.append("NOT IN(");
            for (int x = 0; x < values.length; x++) {
                builder.append("?");
                bindings.add(values[x]);
                if(x < values.length - 1){
                    builder.append(", ");
                }
            }
            builder.append(") ");
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(values.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition match(T value){
        if(isSupported(value.getClass())){
            builder.append("MATCH ? ");
            bindings.add(value);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition between(T lower, T upper){
        if(!lower.getClass().isAssignableFrom(upper.getClass())){
            throw new IllegalArgumentException("Both lower and upper should be of the same type");
        }
        if(isSupported(lower.getClass()) && isSupported(upper.getClass())){
            builder.append("BETWEEN ? AND ? ");
            bindings.add(lower);
            bindings.add(upper);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(lower.getClass().getSimpleName() + " is not supported");
    }

    private <T> Condition notBetween(T lower, T upper){
        if(!lower.getClass().isAssignableFrom(upper.getClass())){
            throw new IllegalArgumentException("Both lower and upper should be of the same type");
        }
        if(isSupported(lower.getClass()) && isSupported(upper.getClass())){
            builder.append("NOT BETWEEN ? AND ? ");
            bindings.add(lower);
            bindings.add(upper);
            fixBrackets();
            return this;
        }
        throw new IllegalArgumentException(lower.getClass().getSimpleName() + " is not supported");
    }

    private Condition regex(String regex){
        builder.append("REGEX ? ");
        bindings.add(regex);
        fixBrackets();
        return this;
    }

    private void fixBrackets(){
        int open = 0;
        int close = 0;
        for (int x = 0; x < builder.length(); x++) {
            char c = builder.charAt(x);
            if(c == '('){
                open++;
            }
            if(c == ')'){
                close++;
            }
        }
        if(close < open){
            for (int x = 0; x < open - close; x++) {
                builder.append(')');
            }
        }
    }

    public String build(){
        return builder.toString();
    }

    ArrayList<Object> getBindings(){
        return bindings;
    }

    private static boolean isSupported(Class<?> type){
        for (Class sup : classes){
            if(type.isAssignableFrom(sup)){
                return true;
            }
        }
        return type.isArray() && type.getComponentType().isAssignableFrom(Byte.TYPE);
    }

}
