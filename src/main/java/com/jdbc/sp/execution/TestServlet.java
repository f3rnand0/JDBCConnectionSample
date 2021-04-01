package com.jdbc.sp.execution;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataSource dataSource = null;
		Connection connection = null;

		//resp.setContentType("text/html");

		// PrintWriter writer = resp.getWriter();
		// writer.println("<h1>Connection Pool Test</h1>");

		try {
			// JNDI
			Context ctx = new InitialContext();

			String jndiDS = req.getParameter("jndiDS");
			System.out.println("jndiDS: " + jndiDS);

			String timeout = req.getParameter("timeout");
			System.out.println("timeout: " + timeout);

			if (jndiDS == null) {
				jndiDS = "jdbc/CTS_BDD_MF";
			}

			dataSource = (DataSource) ctx.lookup(jndiDS);
			connection = (Connection) (dataSource.getConnection());
			System.out.println("connection: " + connection);
			// JNDI end

			SpExecutor spExecutor = new SpExecutor();
			spExecutor.executeSp();

		} catch (Exception e) {
			System.out.println("[TestServlet]");
			Writer result = new StringWriter();
		    final PrintWriter printWriter = new PrintWriter(result);
		    e.printStackTrace(printWriter);
		    System.out.println(result.toString());
		} finally {
			if (connection != null) {
				// Thread.sleep(10000);
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("connection closed");
			}

		}
	}
}
