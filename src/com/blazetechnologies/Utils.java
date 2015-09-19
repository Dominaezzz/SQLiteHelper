package com.blazetechnologies;

import java.util.Date;

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

    public static boolean isSupported(Class<?> type){
        for (Class sup : classes){
            if(type.isAssignableFrom(sup)){
                return true;
            }
        }
        return type.isArray() && type.getComponentType().isAssignableFrom(Byte.TYPE);
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
		if(string.charAt(0) == '(' && string.charAt(string.length() - 1) == ')'){
			return string;
		}else {
			return "(" + string + ')';
		}
	}

	public static StringBuilder parenthesize(StringBuilder stringBuilder){
		if(stringBuilder.charAt(0) == '(' && stringBuilder.charAt(stringBuilder.length()) == ')'){
			return stringBuilder;
		}else {
			return stringBuilder.insert(0, '(').append(')');
		}
	}

    public static String encaseKeyword(String keyword){
		if(keyword.charAt(0) == '[' && keyword.charAt(keyword.length() - 1) == ']'){
			return keyword;
		}else {
			return '[' + keyword + ']';
		}
	}

}
