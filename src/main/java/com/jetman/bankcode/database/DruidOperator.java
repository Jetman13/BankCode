package com.jetman.bankcode.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jetman.bankcode.util.StringUtil;

public class DruidOperator {
	private static Logger log = LogManager.getLogger(DruidOperator.class.getName());
	
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public DruidOperator() {

	}
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException("Get Connection Fail", e);
		}
		return conn;
	}

	public int executeUpdate(String sql, Object... objects) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		int result;
		try {
			reSql(sql,objects,1);
			conn.setAutoCommit(true);

			//psmt = conn.prepareStatement(reSql(sql,objects,1));
			psmt = conn.prepareStatement(sql);
			
			
			for (int i = 0; i < objects.length; i++) {
				psmt.setObject(i+1, objects[i]);
			}

			result = psmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(conn, psmt, null);
		}
		return result;
	}
	
	// 查询多行，结果集为List<Map<String, Object>>
		public List<Map<String, Object>> executeQuery(String sql, Object... objects) {
			Connection conn = getConnection();
			PreparedStatement pstm = null;
			ResultSet rs = null;
			ResultSetMetaData metaData = null;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			try {

				pstm = conn.prepareStatement(sql);
				
				for (int i = 0; i < objects.length; i++) {
					pstm.setObject(i+1, objects[i]);
				}

				rs = pstm.executeQuery();
				metaData = rs.getMetaData();
				int count = metaData.getColumnCount();
				while (rs.next()) {
					Map<String, Object> resultMap = new HashMap<String, Object>(
							count);
					for (int i = 1; i <= count; i++) {
						String columnName = metaData.getColumnLabel(i);

						String columnType = metaData.getColumnTypeName(i);
						String fieldType = StringUtil.replace(columnType,
								"UNSIGNED", "");
						fieldType = fieldType.trim();
						if ("TINYINT".equalsIgnoreCase(fieldType)
								|| "INTEGER".equalsIgnoreCase(fieldType)) {
							fieldType = "INTEGER";
							resultMap.put(columnName, new Integer(rs
									.getInt(columnName)));
						} else if ("BIGINT".equalsIgnoreCase(fieldType) || "DECIMAL".equalsIgnoreCase(fieldType)) {
							fieldType = "Long";
							resultMap.put(columnName, new Long(rs
									.getLong(columnName)));
						} else if ("TIMESTAMP".equalsIgnoreCase(fieldType)) {
							fieldType = "String";
							String value = rs.getString(columnName);
							if (value != null && value.endsWith(".0")) value = value.substring(0, value.length()-2);
							resultMap.put(columnName, value);
						} else {
							fieldType = "String";
							resultMap.put(columnName, rs.getString(columnName));
						}

					}
					mapList.add(resultMap);
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				close(conn, pstm, rs);
			}
			return mapList;
		}
	
	public void close(Connection conn, Statement pstm, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (pstm != null) {
			try {
				pstm.close();
				pstm = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 拼装sql语句
	 * 
	 * @param sql
	 * @param objects
	 * @param flag
	 *            0 查询 1 增、删、改
	 * @return
	 */
	public static String reSql(String sql, Object[] objects, int flag) {

		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] == null) {
					sql = sql.replaceFirst("\\?", "''");
				} else if (objects[i] instanceof String) {
					String tmp = (String) objects[i];
					tmp = tmp.replace("$", "\\$");
					tmp = setWen(tmp);
					sql = sql.replaceFirst("\\?", "'" + tmp + "'");
				} else if (objects[i] instanceof Integer) {
					int tmp = ((Integer) objects[i]).intValue();
					sql = sql.replaceFirst("\\?", tmp + "");
				} else if (objects[i] instanceof Long) {
					long tmp = ((Long) objects[i]).longValue();
					sql = sql.replaceFirst("\\?", tmp + "");
				} else if (objects[i] instanceof Double) {
					double tmp = ((Double) objects[i]).doubleValue();
					sql = sql.replaceFirst("\\?", tmp + "");
				} else if (objects[i] instanceof Float) {
					float tmp = ((Float) objects[i]).floatValue();
					sql = sql.replaceFirst("\\?", tmp + "");
				} else if (objects[i] instanceof Date) {
					Date tmp = (Date) objects[i];
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					sql = sql.replaceFirst("\\?", "'" + sdf.format(tmp) + "'");
				} else {
					String tmp = objects[i].toString();
					tmp = setWen(tmp);
					sql = sql.replaceFirst("\\?", "'" + tmp + "'");
				}
			}
		}

		if (flag == 1) {
			log.info("EXECUTE_SQL_BINLOG:" + sql);
		} else {
			log.debug("QUERY_SQL_BINLOG:" + sql);
		}
		sql = getWen(sql);
		return sql;
	}
	
	/**
	 * 替换value里面的?
	 * @param objects
	 */
	private static String setWen(String str){
		return str.replaceAll("\\?", "？");
	}
	
	/**
	 * 把替换的?替换回来
	 * @param sql
	 */
	private static String getWen(String sql){
		return sql.replaceAll("？", "?");
	}
	

}
