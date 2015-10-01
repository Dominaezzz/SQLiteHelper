package com.blazetechnologies.sql.pragma;

import com.blazetechnologies.Utils;
import com.blazetechnologies.sql.SQL;

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

	 When the value of auto-vacuum is 2 or "incremental" then the additional information needed to do auto-vacuuming is stored in the database file but auto-vacuuming does not occur automatically at each commit as it does with autoVacuum=full. In incremental mode, the separate incremental_vacuum pragma must be invoked to cause the auto-vacuum to occur.

	 The database connection can be changed between full and incremental autovacuum mode at any time. However, changing from "none" to "full" or "incremental" can only occur when the database is new (no tables have yet been created) or by running the VACUUM command. To change auto-vacuum modes, first use the autoVacuum pragma to set the new desired mode, then invoke the VACUUM command to reorganize the entire database file. To change from "full" or "incremental" back to "none" always requires running VACUUM even on an empty database.

	 When the autoVacuum pragma is invoked with no arguments, it returns the current autoVacuum mode.

	 * */
	public static Pragma auto_vacuum(AutoVacuum autoVacuum){
		return new Pragma("auto_vacuum", autoVacuum);
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

	public static Pragma cache_spill(){
		return new Pragma("cache_spill", null);
	}

	public static Pragma cache_spill(boolean value){
		return new Pragma("cache_spill", value);
	}

	public static Pragma case_sesnsititve_like(){
		return new Pragma("case_sensitive_like", null);
	}

	public static Pragma case_senesitive_like(boolean value){
		return new Pragma("case_sensitive_like", value);
	}

	public static Pragma checkPoint_fullFSync(){
		return new Pragma("checkpoint_fullfsync", null);
	}

	public static Pragma checkPoint_fullFSync(boolean value){
		return new Pragma("checkpoint_fullfsync", value);
	}

	public static Pragma collasion_list(){
		return new Pragma("collation_list", null);
	}

	public static Pragma compile_options(){
		return new Pragma("compile_options", null);
	}

	@Deprecated
	public static Pragma count_changes(){
		return new Pragma("count_changes", null);
	}

	@Deprecated
	public static Pragma count_changes(boolean value){
		return new Pragma("count_changes", value);
	}

	@Deprecated
	public static Pragma data_store_directory(){
		return new Pragma("data_store_directory", null);
	}

	@Deprecated
	public static Pragma data_store_diraectory(String directory_name){
		return new Pragma("data_store_directory", directory_name);
	}

	public static Pragma data_version(){
		return new Pragma("data_version", null);
	}

	public static Pragma database_list(){
		return new Pragma("database_list", null);
	}

	public static Pragma default_cache_size(){
		return new Pragma("default_cache_size", null);
	}

	public static Pragma default_cache_size(int size){
		return new Pragma("default_cache_size", size);
	}

	public static Pragma defer_foreign_keys(){
		return new Pragma("defer_foreign_keys", null);
	}

	public static Pragma defer_foreign_keys(boolean value){
		return new Pragma("defer_foreign_keys", value);
	}

	public static Pragma empty_result_callbacks(){
		return new Pragma("empty_result_callbacks", null);
	}

	public static Pragma empty_result_callbacks(boolean value){
		return new Pragma("empty_result_callbacks", value);
	}

	public static Pragma encoding(){
		return new Pragma("encoding", null);
	}

	public static Pragma encoding(String encoding){
		return new Pragma("encoding", Utils.escapeString(encoding));
	}

	public static Pragma foreign_key_check(){
		return new Pragma("foreign_key_check", null);
	}

	public static Pragma foreign_key_check(String table_name){
		return new Pragma("foreign_key_check", table_name);
	}

	public static Pragma foreign_key_list(String table_name){
		return new Pragma("foreign_key_list", table_name);
	}

	public static Pragma foreign_keys(){
		return new Pragma("foreign_keys", null);
	}

	public static Pragma foreign_keys(boolean value){
		return new Pragma("foreign_keys", value);
	}

	public static Pragma freelist_count(){
		return new Pragma("freelist_count", null);
	}

	public static Pragma full_column_names(){
		return new Pragma("full_column_names", null);
	}

	public static Pragma full_column_names(boolean value){
		return new Pragma("full_column_names", value);
	}

	public static Pragma fullfsync(){
		return new Pragma("fullfsync", null);
	}

	public static Pragma ignore_check_contraints(boolean value){
		return new Pragma("ignore_check_constraints", value);
	}

	public static Pragma incremental_vacuum(int N){
		return new Pragma("incremental_vacuum", N);
	}

	public static Pragma index_info(String index_name){
		return new Pragma("index_info", index_name);
	}

	public static Pragma index_list(String table_name){
		return new Pragma("index_list", table_name);
	}

	public static Pragma index_xinfo(String index_name){
		return new Pragma("index_xinfo", index_name);
	}

	public static Pragma integrity_check(){
		return new Pragma("integrity_check", null);
	}

	public static Pragma integrity_check(int N){
		return new Pragma("integrity_check", N);
	}

	public static Pragma journal_mode(){
		return new Pragma("journal_mode", null);
	}

	public static Pragma journal_mode(JournalMode mode){
		return new Pragma("journal_mode", mode);
	}

	public static Pragma journal_size_limit(){
		return new Pragma("journal_size_limit", null);
	}

	public static Pragma journal_size_limit(int size){
		return new Pragma("journal_size_limit", size);
	}

	public static Pragma legacy_file_format(){
		return new Pragma("legacy_file_format", null);
	}

	public static Pragma legacy_file_format(boolean value){
		return new Pragma("legacy_file_format", value);
	}

	public static Pragma locking_mode(){
		return new Pragma("locking_mode", null);
	}

	public static Pragma locking_mode(LockingMode mode){
		return new Pragma("locking_mode", mode);
	}

	public static Pragma max_page_count(){
		return new Pragma("max_page_count", null);
	}

	public static Pragma max_page_count(int N){
		return new Pragma("max_page_count", N);
	}

	public static Pragma mmap_size(){
		return new Pragma("mmap_size", null);
	}

	public static Pragma mmap_size(int N){
		return new Pragma("mmap_size", N);
	}

	public static Pragma page_count(){
		return new Pragma("page_count", null);
	}

	public static Pragma page_size(){
		return new Pragma("page_size", null);
	}

	public static Pragma page_size(int bytes){
		return new Pragma("page_size", bytes);
	}

	public static Pragma parser_trace(boolean value){
		return new Pragma("parser_trace", value);
	}

	public static Pragma query_only(){
		return new Pragma("query_only", null);
	}

	public static Pragma query_only(boolean value){
		return new Pragma("query_only", value);
	}

	public static Pragma quick_check(){
		return new Pragma("quick_check", null);
	}

	public static Pragma quick_check(int N){
		return new Pragma("quick_check", N);
	}

	public static Pragma read_uncommitted(){
		return new Pragma("read_uncommitted", null);
	}

	public static Pragma read_uncommitted(boolean value){
		return new Pragma("read_uncommitted", value);
	}

	public static Pragma recursive_triggers(){
		return new Pragma("recursive_triggers", null);
	}

	public static Pragma recursive_triggers(boolean value){
		return new Pragma("recursive_triggers", value);
	}

	public static Pragma reverse_unordered_selects(){
		return new Pragma("reverse_unordered_selects", null);
	}

	public static Pragma reverse_unordered_selects(boolean value){
		return new Pragma("reverse_unordered_selects", value);
	}

	public static Pragma schema_version(){
		return new Pragma("schema_version", null);
	}

	public static Pragma schema_version(int N){
		return new Pragma("schema_version", N);
	}

	public static Pragma user_version(){
		return new Pragma("user_version", null);
	}

	public static Pragma user_version(int N){
		return new Pragma("user_version", N);
	}

	public static Pragma secure_delete(){
		return new Pragma("secure_delete", null);
	}

	public static Pragma secure_delete(boolean value){
		return new Pragma("secure_delete", value);
	}

	@Deprecated
	public static Pragma short_column_names(){
		return new Pragma("short_column_names", null);
	}

	@Deprecated
	public static Pragma short_column_names(boolean value){
		return new Pragma("short_column_names", value);
	}

	public static Pragma shrink_memory(){
		return new Pragma("shrink_memory", null);
	}

	public static Pragma soft_heap_limit(){
		return new Pragma("soft_heap_limit", null);
	}

	public static Pragma soft_heap_limit(int N){
		return new Pragma("soft_heap_limit", N);
	}

	public static Pragma stats(){
		return new Pragma("stats", null);
	}

	public static Pragma synchronous(){
		return new Pragma("synchronous", null);
	}

	public static Pragma synchronous(Synchronous synchronous){
		return new Pragma("synchronous", synchronous);
	}

	public static Pragma table_info(String table_name){
		return new Pragma("table_info", table_name);
	}

	public static Pragma temp_store(){
		return new Pragma("temp_store", null);
	}

	public static Pragma temp_store(TempStore tempStore){
		return new Pragma("temp_store", tempStore);
	}

	@Deprecated
	public static Pragma temp_store_directory(){
		return new Pragma("temp_store_directory", null);
	}

	@Deprecated
	public static Pragma temp_store_directory(String directory_name){
		return new Pragma("temp_store_directory", directory_name);
	}

	public static Pragma threads(){
		return new Pragma("threads", null);
	}

	public static Pragma threads(int N){
		return new Pragma("threads", N);
	}

	public static Pragma wal_autocheckpoint(){
		return new Pragma("wal_autocheckpoint", null);
	}

	public static Pragma wal_autocheckpoint(int N){
		return new Pragma("wal_autocheckpoint", N);
	}

	public static Pragma wal_checkpoint(){
		return new Pragma("wal_checkpoint", null);
	}

	public static Pragma wal_checkpoint(WalCheckPoint walCheckPoint){
		return new Pragma("wal_checkpoint", walCheckPoint);
	}

	public static Pragma writable_schema(boolean value){
		return new Pragma("writable_schema", value);
	}

}
