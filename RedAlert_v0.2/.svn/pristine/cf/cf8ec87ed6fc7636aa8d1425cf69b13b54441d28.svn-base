import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.StringUtils;

public class TestString {

	public static void main(String[] args) {
		int newNum = 0;
		int num = -3;
		int entId = 11103;
		String cardIds = "11101,1;11102,2;11103,3";
		int len = (cardIds == null ? 0 : cardIds.length());
		if (len == 0) {
			if (num < 0) {
				throw new BaseException("武魂数量不足");
			}
			StringBuilder sb = new StringBuilder(20);
			sb.append(entId).append(",").append(num);
			newNum = num;
			System.out.println(sb.toString());
		} else {
			// 查找原有的相同的卡
			boolean has = false;
			String str = entId + ",";
			String[] idNumArr = StringUtils.split(cardIds, ";");
			int arrLen = idNumArr.length;
			for (int i = 0; i < arrLen; i++) {
				if (idNumArr[i].startsWith(str)) {
					has = true;

					int pos = idNumArr[i].indexOf(",");
					int oldNum = Integer.parseInt(idNumArr[i]
							.substring(pos + 1));
					newNum = oldNum + num;
					if (newNum < 0) {
						throw new BaseException("武魂数量不足");
					} else if (newNum == 0) {
						idNumArr[i] = null;
					} else {
						StringBuilder sb = new StringBuilder(20);
						sb.append(entId).append(",").append(newNum);
						idNumArr[i] = sb.toString();
					}
					break;
				}
			}

			if (has) {
				StringBuilder sb = new StringBuilder(len + 20);
				boolean hasPrefix = false;
				for (int i = 0; i < arrLen; i++) {
					if (idNumArr[i] != null) {
						if (hasPrefix) {
							sb.append(";");
						} else {
							hasPrefix = true;
						}
						sb.append(idNumArr[i]);
					}

				}
				if (sb.length() > 0) {
					System.out.println(sb.toString());
				} else {
					System.out.println("空");
				}

			} else {
				if (num < 0) {
					throw new BaseException("武魂数量不足");
				}
				newNum = num;
				StringBuilder sb = new StringBuilder(cardIds);
				sb.append(";").append(entId).append(",").append(num);
				System.out.println(sb.toString());
			}
		}
		System.out.println("newNum=" + newNum);
	}
}
