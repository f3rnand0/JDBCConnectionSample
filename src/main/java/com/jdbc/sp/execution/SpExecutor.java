package com.jdbc.sp.execution;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class SpExecutor {

	public void executeSp() throws SQLException, ClassNotFoundException {
		Connection connection = getConnection("jdbc:sybase:Tds:172.19.2.28:7026/sampledb", "sa", "password");
		CallableStatement callableStatement = null;
		long t1 = System.currentTimeMillis();
		boolean execute = false;
		int returnValue = 0;

		try {
			String statement = "{?=call cob_soa..sp_sr_notdebcred (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			callableStatement = connection.prepareCall(statement);
			execute = callableStatement.execute();
			returnValue = callableStatement.getInt(1);
			ResultSet rs = callableStatement.getResultSet();
			while (callableStatement.getMoreResults()) {
				rs.getString("1");
				System.out.println("dd");
			}
		} catch (Exception e) {
			System.out.println("[SPExecutor]");
			Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			System.out.println(result.toString());
		} finally {
			long t2 = System.currentTimeMillis();
			System.out.println("result: " + execute);
			System.out.println("return: " + returnValue);
			System.out.println("total : " + (t2 - t1));
			if (callableStatement != null) {
				callableStatement.close();
			}

			if (connection != null) {
				connection.close();
			}
		}
	}

	private static Connection getConnection(String url, String usuario, String pwd) throws SQLException, ClassNotFoundException {
		Class.forName("com.sybase.jdbc3.jdbc.SybConnectionPoolDataSource");
		return DriverManager.getConnection(url, usuario, pwd);
	}
}
