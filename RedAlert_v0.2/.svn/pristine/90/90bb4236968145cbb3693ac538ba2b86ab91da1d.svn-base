
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


public class OpenIdTest {

	public static void main(String[] args) {
		BufferedReader reader = null;
		BufferedWriter wr = null;
		try {

			reader = new BufferedReader(new FileReader(
					"e:/sftp/tlog_cash(1).csv"));
			wr = new BufferedWriter(new FileWriter("e:/sftp/tlog_cash_1.sql"));
			
			String opennId = reader.readLine();
			int count = 0;
		
			while (opennId != null) {
				count++;
				String[] tmps = opennId.trim().split(",");
				wr.write("insert into openidcash(openId,userId,cash) values");				
				wr.write("('");
				wr.write(tmps[0]);
				wr.write("',");
				wr.write(tmps[1]);
				wr.write(",");
				wr.write(tmps[2]);
				wr.write(");\n");
				opennId = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				wr.close();
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
