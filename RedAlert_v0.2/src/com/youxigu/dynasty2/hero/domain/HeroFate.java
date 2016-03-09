package com.youxigu.dynasty2.hero.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;
import com.youxigu.dynasty2.util.StringUtils;

/**
 * 情缘 装备或是武将
 * 
 * @author Dagangzi
 *
 */
public class HeroFate implements java.io.Serializable {
	public static final int HERO_FATE = 1;
	public static final int EQUIP_FATE = 2;
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_EQUIP = 2;// 默认两件装备开启套装
	public static final int MAX_FATE = 4;// 标记最多可以表示16种组合的套装
	private int fateId;// 情缘id
	private String fateName;// 名称
	private String fateDesc;// 描述
	private int type;// 1武将情缘，2装备情缘
	private String reqIds;// 如果是装备情缘则配置需要的装备id，如果是武将情缘则需要配置武将的id di之间逗号隔开
	private String value;// 加成属性值

	/**
	 * reqIds对应的系统武将或者装备Id
	 */
	private transient int[] reqEntIds;
	/**
	 * value对应的效果值
	 */
	private transient List<Map<String, EffectValue>> properties;

	public int getFateId() {
		return fateId;
	}

	public void setFateId(int fateId) {
		this.fateId = fateId;
	}

	public String getFateName() {
		return fateName;
	}

	public void setFateName(String fateName) {
		this.fateName = fateName;
	}

	public String getFateDesc() {
		return fateDesc;
	}

	public void setFateDesc(String fateDesc) {
		this.fateDesc = fateDesc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReqIds() {
		return reqIds;
	}

	public void setReqIds(String reqIds) {
		this.reqIds = reqIds;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int[] getReqEntIds() {
		return reqEntIds;
	}

	/**
	 * 判断满足的最高的套装 套装默认2件装备开始
	 * 
	 * @param equips
	 *            已经拥有的套装
	 * @return i=2表示符合两件套装 i=3就是3件套装 i=4就是4件套装
	 */
	public int satisfyHeroFate(Set<Integer> equips) {
		int[] reqEntIds = getReqEntIds();
		int i = 0;
		for (Integer reqEntId : reqEntIds) {
			if (!equips.contains(reqEntId)) {
				continue;
			}
			++i;
		}
		return i;
	}

	/**
	 * 获取装备的套装属性...
	 * 
	 * @param i
	 *            i=2表示符合两件套装 i=3就是3件套装 i=4就是4件套装
	 * @return
	 */
	public Map<String, EffectValue> getEquipFate(int i) {
		if (i < DEFAULT_EQUIP) {
			throw new BaseException("计算套装错误 i=" + i);
		}
		return properties.get(i - DEFAULT_EQUIP);
	}

	/**
	 * 此方法目前只在计算装备套装的时候使用。方法会变换下套装id。。<br>
	 * 
	 * @param i
	 *            i=2表示符合两件套装 i=3就是3件套装 i=4就是4件套装
	 * @return
	 */
	public int calculateHeroFateId(int i) {
		if (i < DEFAULT_EQUIP) {
			throw new BaseException("计算套装id错误 i=" + i);
		}
		int r = (getFateId() << MAX_FATE) | i;
		return r;
	}

	/**
	 * 获取id标记的套装id
	 * 
	 * @param id
	 * @return
	 */
	public static int resoveHeroFateId(int id) {
		int n = (id >> MAX_FATE);
		return n;
	}

	/**
	 * 获取id标记的是第几条套装属性 目前只在装备套装上面使用
	 * 
	 * @return
	 */
	public static int resoveHeroFateAttrIndex(int id) {
		int n = (id >> MAX_FATE);
		return (id ^ (n << MAX_FATE));
	}

	public void setReqEntIds(int[] reqEntIds) {
		this.reqEntIds = reqEntIds;
	}

	public List<Map<String, EffectValue>> getProperties() {
		return properties;
	}

	public void setProperties(List<Map<String, EffectValue>> properties) {
		this.properties = properties;
	}

	/**
	 * 解析情缘
	 */
	public void parse() {
		if (reqIds != null && reqIds.length() > 0) {
			String[] arr = StringUtils.split(reqIds, ",");
			reqEntIds = new int[arr.length];
			for (int i = 0; i < reqEntIds.length; i++) {
				reqEntIds[i] = Integer.parseInt(arr[i]);
			}
		}

		if (!StringUtils.isEmpty(value)) {
			String[] props = StringUtils.split(value, ";");// 获取每一组的数据
			properties = new java.util.ArrayList<Map<String, EffectValue>>(
					props.length);
			for (String group : props) {
				// 获取每一组里面的加成属性
				String ats[] = group.split(":");
				Map<String, EffectValue> p = new HashMap<String, EffectValue>();
				properties.add(p);
				for (String one : ats) {
					String[] tmp = StringUtils.split(one, ",");
					EffectValue itemProp = new EffectValue();
					int value = Integer.parseInt(tmp[1]);
					if (tmp.length == 3) {
						if ("A".equals(tmp[2])) {
							itemProp.setAbsValue(value);
						} else {
							itemProp.setPerValue(value);
						}
					} else {
						itemProp.setAbsValue(value);
					}
					p.put(tmp[0], itemProp);
				}
			}
		}
		if (type == 2) {
			if (reqEntIds.length - DEFAULT_EQUIP + 1 != properties.size()) {
				throw new BaseException("装备套装属性配置错误.套装数量和套装属性不对应" + getFateId());
			}
		}
	}

}
