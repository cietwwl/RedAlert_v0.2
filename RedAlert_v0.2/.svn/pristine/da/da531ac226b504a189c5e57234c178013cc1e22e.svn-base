package com.youxigu.dynasty2.core.flex.amf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.core.flex.ActionDefine;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.IWolfService;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.Response;

/**
 * 开放平台交易处理
 * 
 * @author Administrator
 * 
 */
public class OpenPlatformTransFilter {
	public static Logger log = LoggerFactory
			.getLogger(OpenPlatformTransFilter.class);
	/**
	 * 返回给前台的token的key
	 */
	public static final String TOKEN = "_token";
	/**
	 * 前台获取token时代的业务逻辑参数map的key
	 */
	public static final String TOKEN_DATA = "_token_data";

	public static final String TOKEN_DATA_MAP = "_token_data_map";

	public static final String T_ITEMID = "_itemId";
	public static final String T_PRICE = "_price";// 单价
	public static final String T_NUM = "_num";// 数量
	public static final String T_AMT = "_amt";// 金额=单价*金额
	public static final String T_DESC = "_desc";
	public static final String T_PIC = "_pic";
	public static final String T_SELLER = "_seller";
	public static final String T_FEE = "_fee";

	public static final String BACK_AMT = "_B_AMT";// Q点
	public static final String BACK_QUAN = "_B_QUAN";// 券
	public static final String BACK_NUM = "_B_NUM"; // 数量

	public static final String T_DESC_DEFAULT = "升值服务*升值服务";

	public static final String T_BACK2FLASH = "qqError"; // 前台要求必须加上的一个标志字段

	public static final int ENT_ITEM_GUILDCAVE = -10000;// 联盟洞穴虚拟道具
	public static final int ENT_CREATEARMY = -10001;// 强征士兵虚拟道具
	public static final int ENT_CHG_COUNTRY = -10002;// 转国虚拟道具
	public static final int ENT_FAST_BUILDING = -10003;// 加速建筑
	public static final int ENT_FAST_TOWERARNY = -10004;// 加速城防
	public static final int ENT_FAST_TECH = -10005;// 加速科技
	public static final int ENT_FAST_USERTASK = -10006;// 加速君主任务
	public static final int ENT_FAST_RISK = -10007;// 加速冒险挂机
	public static final int ENT_FLOW_OPEN = -10008;// 花盆开启
	public static final int ENT_HERO_NAME = -10009;// 武将改名

	public static final int ENT_LEAGUE_REFRESH = -10010;// 百战刷新对手
	public static final int ENT_MISSION_HONOR_COMMIT = -10011;// 元宝提交声望任务
	public static final int ENT_MISSION_HONOR_CHANGE = -10012;// 元宝更换声望任务
	public static final int ENT_MISSION_HONOR_STAR = -10013;// 元宝刷新声望任务难度
	public static final int ENT_RISK_CARD = -10014;// 冒险翻牌
	public static final int ENT_GEM_COMPOSE = -10015;// 宝石合成
	public static final int ENT_GEM_TRIBUTE = -10016;// 元宝朝贡
	public static final int ENT_HERO_PRACTICE = -10017;// 元宝领取武将修炼经验
	public static final int ENT_LEAGUE_COOL = -10018;// 百战失败冷却
	public static final int ENT_LUCKYRANK_COOL = -10019;// 幸运榜中榜冷却
	public static final int ENT_BUY_ANINAL = -10020;// 购买生肖
	/**
	 * 用ThreadLocal保存开平交易回调确认的token及数据 在游戏逻辑扣元宝的时候校验是否有token
	 */
	private static ThreadLocal<TokenData> tokenDatas = new ThreadLocal<TokenData>();

	// private boolean useOpenPlatformTrans = false;
	private IWolfService amf3WolfService;

	public void setAmf3WolfService(IWolfService amf3WolfService) {
		this.amf3WolfService = amf3WolfService;
	}

	/**
	 * 执行前置方法,加入开放平台交易需要的参数
	 * 
	 * @param session
	 * @param in
	 * @param ad
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void doBeforeFilter(Response response, Map<String, Object> in,
			Map<Integer, ActionDefine> actions)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (!Constant.USE_OP_TRANS) {
			return;
		}
		Map<String, Object> params = (Map<String, Object>) in.get(TOKEN_DATA);
		if (params != null) {
			int cmd = (Integer) params.get(IAMF3Action.ACTION_CMD_KEY);
			ActionDefine ad = actions.get(cmd);
			if (ad != null) {
				Method method = ad.getPrevMethod();
				if (method != null) {
					method.invoke(ad.getBean(),
							new Object[] { params, response });
				} else {
					log.error("cmd:{}没有配置交易预处理方法", cmd);
				}
			} else {
				log.error("非法cmd:{}", cmd);
			}
		} else {
			log.error("没有_token_data参数");
		}

	}

	/**
	 * 交易的后置方法，执行具体的游戏逻辑
	 * 
	 * @param response
	 * @param in
	 * @param out
	 */
	public void doAfterFilter(Response response, Map<String, Object> in,
			Map<String, Object> out) {
		if (!Constant.USE_OP_TRANS) {
			return;
		}
		IoSession session = response.getSession();
		String token = (String) out.get(TOKEN);
		Map<String, Object> tokenData = (Map<String, Object>) in
				.get(TOKEN_DATA);
		if (token != null && tokenData != null) {

			Map<String, TokenData> cacheMap = (Map<String, TokenData>) session
					.getAttribute(TOKEN_DATA_MAP);
			// 先过滤掉所有超过15分钟的
			long now = System.currentTimeMillis();
			if (cacheMap != null) {
				Iterator<TokenData> lit = cacheMap.values().iterator();
				while (lit.hasNext()) {
					TokenData td = lit.next();
					if (now - td.dttm > AppConstants.MINUTE_15_MILLS) {
						lit.remove();
					}
				}
			} else {
				cacheMap = new ConcurrentHashMap<String, TokenData>(1);
				session.setAttribute(TOKEN_DATA_MAP, cacheMap);
			}

			TokenData td = new TokenData();
			td.dttm = now;
			td.token = token;
			td.params = tokenData;
			cacheMap.put(token, td);
			if (log.isDebugEnabled()) {
				log.debug("缓存开放平台交易数据,{}:{}", token, tokenData);
			}

		} else {
			log.error("开放平台交易过滤器数据异常,丢弃，{}，{}", token, tokenData);
		}
	}

	/**
	 * 执行交易
	 * 
	 * @param accId
	 *            帐号Id,account.accId (accountService.getAccountByName(openId))
	 * @param token
	 * @param num
	 *            只有在商城交易的时候可以传数量，否则传0;
	 */
	public void doTransaction(long accId, String token, int num, int qb,
			int quan) {
		IoSession session = OnlineUserSessionManager.getIoSessionByAccId(accId);
		if (session == null) {
			throw new BaseException("玩家不在线,交易失败！");
		}
		TokenData td = null;

		Map<String, TokenData> cacheMap = (Map<String, TokenData>) session
				.getAttribute(TOKEN_DATA_MAP);
		if (cacheMap != null) {
			td = cacheMap.remove(token);
		}
		if (td == null) {
			throw new BaseException("错误的token，交易失败！");
		}
		long now = System.currentTimeMillis();
		if (now - td.dttm > AppConstants.MINUTE_15_MILLS) {
			throw new BaseException(1001, "token超时");
		}
		tokenDatas.set(td);
		td.params.put(BACK_AMT, qb);
		td.params.put(BACK_QUAN, quan);

		// td.isTrust = true;
		// td.params.put(TOKEN, token);
		td.params.put(BACK_NUM, num);

		// td.params.put(BACK_AMT, money);
		td.params.put(T_BACK2FLASH, Byte.MAX_VALUE);

		Object tmp = amf3WolfService.handleMessage(new Response(session),
				td.params);
		if (tmp instanceof Map) {
			Map<String, Object> retu = (Map<String, Object>) tmp;
			Integer errorCode = (Integer) retu
					.get(IAMF3Action.ACTION_ERROR_CODE_KEY);
			if (errorCode != null && errorCode.intValue() != 0) {
				throw new BaseException(errorCode, (String) retu
						.get(IAMF3Action.ACTION_ERROR_KEY));
			}
		} else {
			throw new BaseException("系统异常:" + tmp.toString());
		}

	}

	/**
	 * 返回的是金额
	 * 
	 * @return
	 */
	public static TokenData closeToken() {
		if (!Constant.USE_OP_TRANS) {
			throw new BaseException("开放平台交易未开放!");
		}
		TokenData td = tokenDatas.get();
		if (td != null) {
			tokenDatas.remove();// 删除，只检查一次
			// return td.params.get(BACK_AMT);
			return td;
		}
		throw new BaseException("交易请求非法，拒绝");
	}

	// /**
	// * 返回的是金额
	// * @return
	// */
	// public static int getTokenCash() {
	// if (!Constant.USE_OP_TRANS) {
	// return 0;
	// }
	// TokenData td = tokenDatas.get();
	// if (td != null) {
	// Integer amt = (Integer)td.params.get(BACK_AMT);
	// if (amt==null){
	// return 0;
	// }else{
	// return amt;
	// }
	// }
	// return 0;
	// }

	public static boolean hasToken() {
		if (!Constant.USE_OP_TRANS) {
			return false;
		}
		TokenData td = tokenDatas.get();
		if (td != null) {
			return true;
			// return td.params.get(BACK_AMT);
		}

		return false;
	}

	public static class TokenData implements java.io.Serializable {
		public long dttm;// 超过15分钟的废弃
		public String token;
		public Map<String, Object> params;// 业务逻辑参数

		public int getNum() {
			Integer tmp = (Integer) params.get(BACK_NUM);
			if (tmp == null) {
				return 0;
			} else {
				return tmp;
			}
		}

		public int getQb() {
			Integer tmp = (Integer) params.get(BACK_AMT);
			if (tmp == null) {
				return 0;
			} else {
				return tmp;
			}

		}

		public int getQuan() {
			Integer tmp = (Integer) params.get(BACK_QUAN);
			if (tmp == null) {
				return 0;
			} else {
				return tmp;
			}

		}

		public int getAmt() {
			return getQb() + getQuan();
		}
	}
}
