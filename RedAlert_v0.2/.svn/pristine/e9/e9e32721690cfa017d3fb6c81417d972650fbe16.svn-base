import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class NativeTest {

	public static void main(String[] args) {
		
		try {
			DatagramSocket client = new DatagramSocket();
			byte[] recvBuf = new byte[1024];
			
			DatagramPacket sendPack = new DatagramPacket(recvBuf,
					recvBuf.length,InetAddress.getByName("172.16.0.59"),9990);
			client.send(sendPack);
			System.out.println("====");
		} catch (Exception e) {
			e.printStackTrace();
		}
		CLibrary libary = null;
		try {
			String LIBNAME = args[0];
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			String confPath = System.getenv("TX_CONF_PATH");
			NativeLibrary.addSearchPath(LIBNAME, confPath);

			String classPath = loader.getResource(LIBNAME).getPath();
			URL url = loader.getResource(LIBNAME);

			System.out.println("url=" + url);
			if (url != null) {
				System.out.println("url.getFile()=" + url.getFile());
				Native.loadLibrary(url.getFile(), CLibrary.class);
			}
			if (libary == null) {
				libary = (CLibrary) Native.loadLibrary(LIBNAME, CLibrary.class);
			}

			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface CLibrary extends Library {
		int tgamelog_init(String szFileName);

		int tgame_bin_info(String szCat, int iId, int iCls, int iType,
				int iVersion, String szBuff, int iBufflen);

		int tgame_fini();
	}
}
