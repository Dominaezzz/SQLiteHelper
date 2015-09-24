package com.blazetechnologies.sql;

/**
 * Created by Dominic on 29/08/2015.
 */
public class Core extends Expr {

	private Core(String name) {
		super(name);
		builder.append("() ");
	}

	private Core(String name, Expr... arguments) {
		builder.append(name);
		builder.append('(');
		for (int x = 0; x < arguments.length; x++) {
			builder.append(arguments[x]);
			getBindings().addAll(arguments[x].getBindings());
			if (x < arguments.length - 1) {
				builder.append(", ");
			}
		}
		builder.append(") ");
	}

	/**
	 * The abs(X) function returns the absolute value of the numeric argument X.
	 * Abs(X) returns NULL if X is NULL.
	 * Abs(X) returns 0.0 if X is a string or blob that cannot be converted to a numeric value.
	 * If X is the integer -9223372036854775808 then abs(X) throws an integer overflow error since there is no equivalent positive 64-bit two complement value.
	 *
	 * */
	public static Core abs(Expr expr) {
		return new Core("ABS", expr);
	}

	/**
	 * The changes() function returns the number of database rows that were changed or inserted or deleted by the most recently completed INSERT, DELETE, or UPDATE statement, exclusive of statements in lower-level triggers.
	 * The changes() SQL function is a wrapper around the sqlite3_changes() C/C++ function and hence follows the same rules for counting changes.
	 *
	 * */
	public static Core changes() {
		return new Core("CHANGES");
	}

	/**
	 * The coalesce() function returns a copy of its first non-NULL argument, or NULL if all arguments are NULL. Coalesce() must have at least 2 arguments.
	 * */
	public static Core coalesce(Expr... exprs) {
		return new Core("COALESCE", exprs);
	}

	/**
	 *  The glob(X,Y) function is equivalent to the expression "Y GLOB X".
	 *  Note that the X and Y arguments are reversed in the glob() function relative to the infix GLOB operator.
	 *  If the sqlite3_create_function() interface is used to override the glob(X,Y) function with an alternative implementation then the
	 *  GLOB operator will invoke the alternative implementation.
	 * */
	public static Core glob(Expr x, Expr y) {
		return new Core("GLOB", x, y);
	}

	/**
	 * The ifnull() function returns a copy of its first non-NULL argument, or NULL if both arguments are NULL.
	 * Ifnull() must have exactly 2 arguments.
	 * The ifnull() function is equivalent to coalesce() with two arguments.
	 * */
	public static Core ifNull(Expr x, Expr y) {
		return new Core("IFNULL", x, y);
	}

	public static <T> Core ifNull(Expr x, T y){
		return ifNull(x, value(y));
	}

	/**
	 * The instr(X,Y) function finds the first occurrence of string Y within string X and returns the number of prior characters plus 1,
	 * or 0 if Y is nowhere found within X. Or, if X and Y are both BLOBs, then instr(X,Y) returns one more than the number bytes prior to the first occurrence of Y,
	 * or 0 if Y does not occur anywhere within X.
	 * If both arguments X and Y to instr(X,Y) are non-NULL and are not BLOBs then both are interpreted as strings.
	 * If either X or Y are NULL in instr(X,Y) then the result is NULL.
	 * */
	public static Core instr(Expr x, Expr y) {
		return new Core("INSTR", x, y);
	}

	public static Core instr(Expr x, String y){
		return instr(x, value(y));
	}

	/**
	 * The hex() function interprets its argument as a BLOB and returns a string which is the upper-case hexadecimal rendering of the content of that blob.
	 * */
	public static Core hex(Expr expr) {
		return new Core("HEX", expr);
	}

	public static Core last_insert_rowid() {
		return new Core("LAST_INSERT_ROWID");
	}

	public static Core length(Expr expr) {
		return new Core("LENGTH", expr);
	}

	public static Core like(Expr x, Expr y) {
		return new Core("LIKE", x, y);
	}

	public static Core like(String column, Expr y){
		return like(col(column), y);
	}

	public static Core like(Expr x, Expr y, Expr z) {
		return new Core("LIKE", x, y, z);
	}

	public static Core lower(Expr expr){
		return new Core("LOWER", expr);
	}

	public static Core lower(String column){
		return lower(col(column));
	}

	public static Core lTrim(Expr expr){
		return new Core("LTRIM", expr);
	}

	public static Core lTrim(String column){
		return lTrim(col(column));
	}

	public static Core lTrim(Expr x, Expr y){
		return new Core("LTRIM", x, y);
	}

	public static Core lTrim(Expr x, String y){
		return lTrim(x, Expr.value(y));
	}

	public static Core lTrim(String column, String y){
		return lTrim(col(column), y);
	}

	public static Core max(Expr... exprs){
		return new Core("MAX", exprs);
	}

	/** The multi-argument min() function returns the argument with the minimum value.
	 *  The multi-argument min() function searches its arguments from left to right for an argument that defines a
	 *  collating function and uses that collating function for all string comparisons.
	 *  If none of the arguments to min() define a collating function, then the BINARY collating function is used.
	 * **/
	public static Core min(Expr... exprs){
		return new Core("MIN", exprs);
	}

	/**
	 * The nullif(X,Y) function returns its first argument if the arguments are different and NULL if the arguments are the same.
	 * The nullif(X,Y) function searches its arguments from left to right for an argument that defines a collating function and
	 * uses that collating function for all string comparisons.
	 * If neither argument to nullif() defines a collating function then the BINARY is used.
	 * **/
	public static Core nullIf(Expr x, Expr y){
		return new Core("NULLIF", x, y);
	}

	/**
	 * The printf(FORMAT,...) SQL function works like the sqlite3_mprintf() C-language function and the printf() function from the standard C library.
	 * The first argument is a format string that specifies how to construct the output string using values taken from subsequent arguments.
	 * If the FORMAT argument is missing or NULL then the result is NULL.
	 * The %n format is silently ignored and does not consume an argument.
	 * The %p format is an alias for %X. The %z format is interchangeable with %s.
	 * If there are too few arguments in the argument list, missing arguments are assumed to have a NULL value,
	 * which is translated into 0 or 0.0 for numeric formats or an empty string for %s.
	 * **/
	public static Core printF(Expr format, Expr... args){
		Expr[] exprs = new Expr[args.length + 1];
		exprs[0] = format;
		System.arraycopy(args, 0, exprs, 1, args.length);
		return new Core("PRINTF", exprs);
	}

	public static Core printF(String format, Expr... args){
		return printF(value(format), args);
	}

	/**
	 * The quote(X) function returns the text of an SQL literal which is the value of its argument suitable for inclusion into an SQL statement.
	 * Strings are surrounded by single-quotes with escapes on interior quotes as needed. BLOBs are encoded as hexadecimal literals.
	 * Strings with embedded NUL characters cannot be represented as string literals in SQL and hence the returned string literal is truncated prior to the first NUL.
	 * **/
	public static Core quote(Expr expr){
		return new Core("QUOTE", expr);
	}

	/**
	 * The random() function returns a pseudo-random integer between -9223372036854775808 and +9223372036854775807.
	 * */
	public static Core random(){
		return new Core("RANDOM");
	}

	/**
	 * The randomblob(N) function return an N-byte blob containing pseudo-random bytes. If N is less than 1 then a 1-byte random blob is returned.
	 * Hint: applications can generate globally unique identifiers using this function together with hex() and/or lower() like this:
	 * 		hex(randomblob(16))
	 * 		lower(hex(randomblob(16)))
	 * **/
	public static Core randomBlob(Expr expr){
		return new Core("RANDOMBLOB", expr);
	}

	public static Core randomBlob(long N){
		return randomBlob(Expr.value(N));
	}

	/**
	 * The replace(X,Y,Z) function returns a string formed by substituting string Z for every occurrence of string Y in string X.
	 * The BINARY collating sequence is used for comparisons.
	 * If Y is an empty string then return X unchanged.
	 * If Z is not initially a string, it is cast to a UTF-8 string prior to processing.
	 * */
	public static Core replace(Expr x, Expr y, Expr z){
		return new Core("REPLACE", x, y, z);
	}

	public static Core replace(Expr x, String y, String z){
		return replace(x, Expr.value(y), Expr.value(z));
	}

	public static Core replace(String column, String y, String z){
		return replace(col(column), y, z);
	}

	/**
	 * The round(X,Y) function returns a floating-point value X rounded to Y digits to the right of the decimal point.
	 * If the Y argument is omitted, it is assumed to be 0.
	 * */
	public static Core round(Expr expr){
		return new Core("ROUND", expr);
	}

	public static Core round(String column){
		return round(col(column));
	}

	/**
	 * The rtrim(X,Y) function returns a string formed by removing any and all characters that appear in Y from the right side of X.
	 * If the Y argument is omitted, rtrim(X) removes spaces from the right side of X.
	 * */
	public static Core rTrim(Expr expr){
		return new Core("RTRIM", expr);
	}

	public static Core rTrim(String column){
		return rTrim(col(column));
	}

	public static Core rTrim(Expr x, Expr y){
		return new Core("RTRIM", x, y);
	}

	public static Core rTrim(Expr x, String y){
		return rTrim(x, Expr.value(y));
	}

	public static Core rTrim(String column, Expr y){
		return rTrim(col(column), y);
	}

	public static Core rTrim(String column, String y){
		return rTrim(column, value(y));
	}

	public static Core substr(Expr x, Expr start){
		return new Core("SUBSTR", x, start);
	}

	public static Core substr(Expr x, long start){
		return substr(x, Expr.value(start));
	}

	public static Core substr(String column, long start){
		return substr(col(column), start);
	}

	/**
	 * The substr(X,Y,Z) function returns a substring of input string X that begins with the Y-th character and which is Z characters long.
	 * If Z is omitted then substr(X,Y) returns all characters through the end of the string X beginning with the Y-th.
	 * The left-most character of X is number 1.
	 * If Y is negative then the first character of the substring is found by counting from the right rather than the left.
	 * If Z is negative then the abs(Z) characters preceding the Y-th character are returned.
	 * If X is a string then characters indices refer to actual UTF-8 characters. If X is a BLOB then the indices refer to bytes.
	 * */
	public static Core substr(Expr x, Expr start, Expr length){
		return new Core("SUBSTR", x, start, length);
	}

	public static Core substr(Expr x, long start, long length){
		return substr(x, Expr.value(start), Expr.value(length));
	}

	public static Core substr(String column, long start, long length){
		return substr(col(column), start, length);
	}

	/**
	 * The total_changes() function returns the number of row changes caused by INSERT, UPDATE or DELETE statements since the current database connection was opened.
	 * This function is a wrapper around the sqlite3_total_changes() C/C++ interface.
	 * */
	public static Core total_changes(){
		return new Core("TOTAL_CHANGES");
	}

	public static Core trim(Expr expr){
		return new Core("TRIM", expr);
	}

	public static Core trim(String column){
		return trim(col(column));
	}

	/**
	 * The trim(X,Y) function returns a string formed by removing any and all characters that appear in Y from both ends of X.
	 * If the Y argument is omitted, trim(X) removes spaces from both ends of X.
	 * */
	public static Core trim(Expr x, Expr y){
		return new Core("TRIM", x, y);
	}

	public static Core trim(Expr expr, String y){
		return trim(expr, Expr.value(y));
	}

	public static Core trim(String column, Expr y){
		return trim(col(column), y);
	}

	public static Core trim(String column, String y){
		return trim(column, value(y));
	}

	/**
	 * The typeof(X) function returns a string that indicates the datatype of the expression X: "null", "integer", "real", "text", or "blob".
	 * */
	public static Core typeOf(Expr expr){
		return new Core("TYPEOF", expr);
	}

	public static Core upper(Expr expr){
		return new Core("UPPER", expr);
	}

	public static Core upper(String column){
		return upper(col(column));
	}

	public static Core zeroBlob(Expr expr){
		return new Core("ZEROBLOB", expr);
	}

	public static Core zeroBlob(long length){
		return zeroBlob(Expr.value(length));
	}

}
