package com.blazetechnologies;

import com.blazetechnologies.sql.Expr;

import java.util.Date;
import java.util.Stack;

/**
 * Created by Dominic on 27/08/2015.
 */
public class Utils {

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

	private static Class[] integerClasses = new Class[]{
			Byte.TYPE,
			Byte.class,

			Short.TYPE,
			Short.class,

			Integer.TYPE,
			Integer.class,

			Long.TYPE,
			Long.class
	};

	private static Class[] realClasses = new Class[]{
			Float.TYPE,
			Float.class,
			Double.TYPE,
			Double.class
	};

	private static Class[] textClasses = new Class[]{
			Character.TYPE,
			Character.class,
			String.class,
			Enum.class
	};

	public static boolean isInteger(Class<?> type){
		for (Class sup : integerClasses){
			if(type.isAssignableFrom(sup)){
				return true;
			}
		}
		return false;
	}

	public static boolean isReal(Class<?> type){
		for (Class sup : realClasses){
			if(type.isAssignableFrom(sup)){
				return true;
			}
		}
		return false;
	}

	public static boolean isText(Class<?> type){
		for (Class sup : textClasses){
			if(type.isAssignableFrom(sup)){
				return true;
			}
		}
		return false;
	}

	public static boolean isBoolean(Class<?> type){
		return type.isAssignableFrom(Boolean.TYPE) || type.isAssignableFrom(Boolean.class);
	}

	public static boolean isBlob(Class<?> type){
		return type.isArray() && type.getComponentType().isAssignableFrom(byte.class);
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static String parenthesize(String string){
		if(isWrapped(string)){
			return string;
		}else {
			return "(" + string + ')';
		}
	}

	public static StringBuilder parenthesize(StringBuilder stringBuilder){
		if(isWrapped(stringBuilder)){
			return stringBuilder;
		}else {
			return stringBuilder.insert(0, '(').append(')');
		}
	}

    public static String encaseKeyword(String keyword){
		if(isWrapped(keyword)){
			return keyword;
		}else {
			return '[' + keyword + ']';
		}
	}

    private static boolean isWrapped(CharSequence sequence){
		Stack<Character> characterStack = new Stack<>();
		char b = sequence.charAt(0);
		char e = sequence.charAt(sequence.length() - 1);

		boolean hasSpace = false;

		if((b == '(' && e == ')') || (b == '[' && e == ']')){
			for (int x = 0; x < sequence.length(); x++) {

				char c = sequence.charAt(x);
				if(c == ' '){
					hasSpace = true;
				}

				if(c == '(' || c == '['){
					characterStack.push(c);
				}
				if(c == ')' || c == ']'){
					characterStack.pop();
					if(x != sequence.length() - 1 && characterStack.empty()){
						return !hasSpace;
					}
				}
			}

			return true;
		}

		return !sequence.toString().contains(" ");
	}

	public static Expr[] valuesToExpressions(Object[] values){
		Expr[] exprs = new Expr[values.length];
		for (int x = 0; x < values.length; x++) {
			exprs[x] = Expr.value(values[x]);
		}
		return exprs;
	}

	public static String escapeString(String string){
		StringBuilder sqlString = new StringBuilder();
		sqlString.append('\'');

		if(string.contains("'")){
			for (int x = 0; x < string.length(); x++) {
				char c = string.charAt(x);
				if(c == '\''){
					sqlString.append('\'');
				}
				sqlString.append(c);
			}
		}else {
			sqlString.append(string);
		}

		sqlString.append('\'');
		return sqlString.toString();
	}

}
