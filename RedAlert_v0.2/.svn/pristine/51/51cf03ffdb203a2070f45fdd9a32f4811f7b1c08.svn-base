import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBScript {

	public static void main(String[] args) {
		String dbName = "dynasty2_sx1";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String conUrl = "jdbc:mysql://172.16.3.54:3306/information_schema?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(conUrl, "root", "root");

			pstmt = conn
					.prepareStatement("select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA='"
							+ dbName + "'");
			rs = pstmt.executeQuery();

			StringBuilder sb_script = new StringBuilder(10240);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				PreparedStatement pstmt1 = conn
						.prepareStatement("select COLUMN_NAME from information_schema.COLUMNS where TABLE_SCHEMA=? and TABLE_NAME=?");

				pstmt1.setString(1, dbName);
				pstmt1.setString(2, tableName);
				ResultSet rs1 = pstmt1.executeQuery();
				StringBuilder sb_field = new StringBuilder(256);
				while (rs1.next()) {
					sb_field.append(rs1.getString("COLUMN_NAME")).append(",");

				}
				String fileds = sb_field.substring(0, sb_field.length() - 1);

				sb_script.append("INSERT INTO TARGETDB.").append(tableName)
						.append(" (").append(fileds).append(")");
				sb_script.append(" SELECT ").append(fileds).append(
						" FROM SOURCEDB.").append(tableName).append(";\n");

				rs1.close();
				pstmt1.close();

			}

			try {
				BufferedWriter wr = new BufferedWriter(new FileWriter(
						"e:/sftp/dbscript.sql"));

				wr.write(sb_script.toString());

				wr.flush();
				wr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
