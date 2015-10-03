package com.blazetechnologies.executors;

import java.sql.*;

/**
 * Created by Dominic on 03/10/2015.
 */
public class JDBCExecutor implements SQLiteWrapper<ResultSet> {

	private Connection connection;

	public JDBCExecutor(Connection connection){
		this.connection = connection;
	}

	@Override
	public int executeUpdateOrDelete(String sql, Object[] args) {
		try {
			PreparedStatement statement = getPreparedStatement(sql, args);
			try {
				statement.execute();
				return statement.getUpdateCount();
			}finally {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void executeSQL(String sql, Object[] args) {
		try {
			PreparedStatement statement = getPreparedStatement(sql, args);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int executeInsert(String sql, Object[] args) {
		executeSQL(sql, args);
		return 0;
	}

	@Override
	public ResultSet executeQuery(String sql, Object[] args) {
		try {
			PreparedStatement statement = getPreparedStatement(sql, args);
			statement.closeOnCompletion();
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private PreparedStatement getPreparedStatement(String sql, Object[] args) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sql);
		for (int x = 0; x < args.length; x++) {
			statement.setObject(x + 1, args[x]);
		}
		return statement;
	}
}
