import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 按openId生成白名单sql
 * @author Administrator
 *
 */
public class WhiteList {
public static void main(String[] args){
	BufferedReader reader = null;
	BufferedWriter wr = null;
	try {

		reader = new BufferedReader(new FileReader("E:/sftp/100656690_1_20130123.txt_openid"));
		wr = new BufferedWriter(new FileWriter("e:/sftp/whiteList.sql"));
		String opennId = reader.readLine();
		int count=0;
		boolean insert = false;
		while (opennId != null) {
			count++;
			opennId = opennId.trim();
			if (opennId.equals("F39D34731C2768383CCD860F77C8092A")){
				insert=true;
			}
			if (insert){
					wr.write("insert IGNORE into accountwhitelist values('");
					wr.write(opennId);
					wr.write("',now(),1);\n");
					try {
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			opennId = reader.readLine();
		}
		System.out.println("count="+count);
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
