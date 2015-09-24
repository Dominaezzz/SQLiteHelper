package com.blazetechnologies.sql;

/**
 * Created by Dominic on 12/09/2015.
 */
public class Pragma extends SQL {

	private Pragma(String name, Object value){
		super("PRAGMA ");
		builder.append(name);
		if (value != null) {
			builder.append('(').append(value).append(')');
		}
		builder.append(";");
	}

	/**
	 * The application_id PRAGMA is used to query or set the 32-bit unsigned big-endian "Application ID" integer located at offset 68 into the database header.
	 * Applications that use SQLite as their application file-format should set the Application ID integer
	 * to a unique integer so that utilities such as file(1) can determine the specific file type
	 * rather than just reporting "SQLite3 Database".
	 * A list of assigned application IDs can be seen by consulting the magic.txt file in the SQLite source repository.
	 * */
	public static Pragma application_id(int id){
		return new Pragma("application_id", id);
	}

	public static Pragma application_id(){
		return new Pragma("application_id", null);
	}

	public static Pragma auto_vacuum(){
		return new Pragma("auto_vacuum", null);
	}

	/**
	 * Query or set the auto-vacuum status in the database.

	 The default setting for auto-vacuum is 0 or "none", unless the SQLITE_DEFAULT_AUTOVACUUM compile-time option is used. The "none" setting means that auto-vacuum is disabled. When auto-vacuum is disabled and data is deleted data from a database, the database file remains the same size. Unused database file pages are added to a "freelist" and reused for subsequent inserts. So no database file space is lost. However, the database file does not shrink. In this mode the VACUUM command can be used to rebuild the entire database file and thus reclaim unused disk space.

	 When the auto-vacuum mode is 1 or "full", the freelist pages are moved to the end of the database file and the database file is truncated to remove the freelist pages at every transaction commit. Note, however, that auto-vacuum only truncates the freelist pages from the file. Auto-vacuum does not defragment the database nor repack individual database pages the way that the VACUUM command does. In fact, because it moves pages around within the file, auto-vacuum can actually make fragmentation worse.

	 Auto-vacuuming is only possible if the database stores some additional information that allows each database page to be traced backwards to its referrer. Therefore, auto-vacuuming must be turned on before any tables are created. It is not possible to enable or disable auto-vacuum after a table has been created.

	 When the value of auto-vacuum is 2 or "incremental" then the additional information needed to do auto-vacuuming is stored in the database file but auto-vacuuming does not occur automatically at each commit as it does with auto_vacuum=full. In incremental mode, the separate incremental_vacuum pragma must be invoked to cause the auto-vacuum to occur.

	 The database connection can be changed between full and incremental autovacuum mode at any time. However, changing from "none" to "full" or "incremental" can only occur when the database is new (no tables have yet been created) or by running the VACUUM command. To change auto-vacuum modes, first use the auto_vacuum pragma to set the new desired mode, then invoke the VACUUM command to reorganize the entire database file. To change from "full" or "incremental" back to "none" always requires running VACUUM even on an empty database.

	 When the auto_vacuum pragma is invoked with no arguments, it returns the current auto_vacuum mode.

	 * */
	public static Pragma auto_vacuum(AUTO_VACUUM auto_vacuum){
		return new Pragma("auto_vacuum", auto_vacuum);
	}

	public enum AUTO_VACUUM{
		NONE,
		FULL,
		INCREMENTAL
	}

	public static Pragma automatic_index(){
		return new Pragma("automatic_index", null);
	}

	public static Pragma automatic_index(boolean bool){
		return new Pragma("automatic_index", bool);
	}

	public static Pragma busy_timeout(){
		return new Pragma("busy_timeout", null);
	}

	/**
	 * Query or change the setting of the busy timeout. This pragma is an alternative to the sqlite3_busy_timeout() C-language interface which is made available as a pragma for use with language bindings that do not provide direct access to sqlite3_busy_timeout().
	 * Each database connection can only have a single busy handler. This PRAGMA sets the busy handler for the process, possibly overwriting any previously set busy handler.
	 * */
	public static Pragma busy_timeout(long milliSeconds){
		return new Pragma("busy_timeout", milliSeconds);
	}

	public static Pragma cache_size(){
		return new Pragma("cache_size", null);
	}

	public static Pragma cache_size(int size){
		return new Pragma("cache_size", size);
	}

}
