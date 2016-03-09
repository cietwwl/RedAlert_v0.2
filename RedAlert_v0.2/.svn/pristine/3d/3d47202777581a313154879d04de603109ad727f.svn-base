import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import flex.messaging.util.URLEncoder;


public class UrlEncoderTest {

	public static void main(String[] args) {
		BufferedReader reader = null;
		BufferedWriter wr = null;
		try {

			reader = new BufferedReader(new FileReader(
					"e:/sftp/UTF-8转换1.csv"));
			wr = new BufferedWriter(new FileWriter("e:/sftp/UTF-8转换2.csv"));
			
			String key = reader.readLine();
			int count = 0;
		
			while (key != null) {
				String value=URLEncoder.encode(key,"utf-8");
				System.out.println(key+"="+value);
				wr.write(key);				
				wr.write(",");
				wr.write(value);
				wr.write("\n");
				key = reader.readLine();
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
