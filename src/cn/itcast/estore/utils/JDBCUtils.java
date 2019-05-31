package cn.itcast.estore.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 使用C3P0改造的JDBC的工具类:
 */
public class JDBCUtils {
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	/**
	 * 获得连接的方法
	 */
	public static Connection getConnection(){
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 获得连接池:
	 */
	public static DataSource getDataSource(){
		return dataSource;
	}
	
}
