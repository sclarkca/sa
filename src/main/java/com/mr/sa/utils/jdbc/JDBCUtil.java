//package com.mr.sa.utils.jdbc;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.apache.commons.dbcp.BasicDataSourceFactory;
//import org.apache.commons.dbutils.QueryRunner;
//import org.apache.commons.dbutils.handlers.ArrayListHandler;
//import org.apache.commons.lang.StringUtils;
//
//public class JDBCUtil {
//
//	private static DataSource dataSource = null;
//
//	public JDBCUtil(String driverClassName, String url, String username, String password) {
//
//		try {
//			Properties pro = new Properties();
//			pro.setProperty("driverClassName", driverClassName);
//			pro.setProperty("url", url);
//			if (StringUtils.isNotBlank(username)) {
//				pro.setProperty("username", username);
//			}
//			if (StringUtils.isNotBlank(password)) {
//				pro.setProperty("password", password);
//			}
//			dataSource = BasicDataSourceFactory.createDataSource(pro);
//			System.out.println("");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public int update(String sql) {
//		try {
//			QueryRunner run = new QueryRunner(this.getDataSource());
//			run.update(sql);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//		return 0;
//	}
//
//	public Connection getConnection() throws SQLException {
//		return dataSource.getConnection();
//	}
//
//	public DataSource getDataSource() {
//		return dataSource;
//	}
//
//	public void setDataSource(DataSource dataSource) {
//		JDBCUtil.dataSource = dataSource;
//	}
//
//	public static void main1(String[] args) {
//		JDBCUtil jdbcUtil = new JDBCUtil("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@3.14.11.101:1521:mmdb1",
//				"mmualert", "mmpalert1");
//
//		/*
//		 * JDBCUtil jdbcUtil = new JDBCUtil("oracle.jdbc.driver.OracleDriver",
//		 * "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "omdb", "omdb");
//		 */
//		QueryRunner run = new QueryRunner(jdbcUtil.getDataSource());
//		String remoteTotalSql = "select * from alert_status";
//		try {
//			List<Object[]> result = run.query(remoteTotalSql, new ArrayListHandler());
//			System.out.println(1);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public static void mainSqlserver(String[] args) {
//		/*
//		 * JDBCUtil jdbcUtil = new JDBCUtil(
//		 * "com.microsoft.sqlserver.jdbc.SQLServerDriver",
//		 * "jdbc:sqlserver://10.10.70.231:1433;DatabaseName=SolarWindsOrion",
//		 * "sa", "cisco!123");
//		 */
//
//		/*
//		 * JDBCUtil jdbcUtil = new JDBCUtil("oracle.jdbc.driver.OracleDriver",
//		 * "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "omdb", "omdb");
//		 */
//
//		JDBCUtil jdbcUtil = new JDBCUtil("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@3.14.11.101:1521:mmdb1",
//				"sysman", "weblogic1");
//
//		QueryRunner run = new QueryRunner(jdbcUtil.getDataSource());
//		// String remoteTotalSql = "select * from dbo.Nodes";
//		// String remoteTotalSql =
//		// "select
//		// MACHINETYPE,IP_ADDRESS,IOSVERSION,LOCATION,SERVICE,CAPTION,AREA,
//		// CITY,VENDOR,DESCRIPTION from dbo.Nodes";
//		String remoteTotalSql = "select * from MGMT$DB_DBNINSTANCEINFO";
//		try {
//			List<Object[]> result = run.query(remoteTotalSql, new ArrayListHandler());
//			System.out.println(1);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) {
//
//		/*
//		 * String sql = "select * from plocation"; List<Msisdn> msisdnList =
//		 * this.jdbcTemplateBcp.query(sql, new
//		 * BeanPropertyRowMapper<Msisdn>(Msisdn.class));
//		 */
//
//		// Sqlite
//		JDBCUtil jdbcUtil = new JDBCUtil("org.sqlite.JDBC", "jdbc:sqlite:e:/test.db", "", "");
//
//		QueryRunner run = new QueryRunner(jdbcUtil.getDataSource());
//		String remoteTotalSql = "select * from plocation";
//		try {
//			long startTime = Calendar.getInstance().getTimeInMillis();
//			List<Object[]> result = run.query(remoteTotalSql, new ArrayListHandler());
//			long endTime = Calendar.getInstance().getTimeInMillis();
//			System.out.println(endTime - startTime);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//}
