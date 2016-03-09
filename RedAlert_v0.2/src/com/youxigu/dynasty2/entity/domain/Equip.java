package com.youxigu.dynasty2.entity.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.hero.domain.HeroFate;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.dynasty2.util.StringUtils;

/**
 * 装备
 *
 */
public class Equip extends Item {
	private static final Logger log = LoggerFactory.getLogger(Equip.class);
	private static final long serialVersionUID = -1414584429073429722L;
	private int suitId;// 套装id

	private int etype;// 合成装备1 打造装备2
	/**
	 * 装备属性： HERO_AGILE,26,3,A;HERO_HP,104,10,A; {属性ID,属性值,强化一次增加值,绝对值/百分比}
	 * 目前百分比不再支持
	 */
	private String value;// property,value,A/P;

	private String fragmentItem;// 合成所需道具
	private int gold;// 打造消耗的金币数量
	private String buildItem;// 装备打造所需道具

	private String buildAttr1;// 装备打造属性1
	private String buildAttr2;// 装备打造属性2
	private String buildAttr3;// 装备打造属性3
	private String buildSpecialAttr;// 装备打造的特殊属性
	private int buildFactor;// 装备打造强化属性计算系数,系数按照1000分比来做0.1就应该是100

	/** 如果这件装备是制定的武将穿戴则会有特定的属性加成 */
	private String specSysHeroId;// 专属的系统武将ID集合
	private String specValue;// property,value,A/P;

	private int equipDebris;// 分解的装备碎片id
	private int equipDebrisCount;// 分解的数量
	private int goldNum;// 装备自身分解获得的金矿

	/**
	 * 装备的属性,根据value属性解析得到
	 */
	private transient List<ItemProperty> properties;
	private transient List<ItemProperty> specProperties;
	private transient Map<Integer, Integer> specSysHeroIds = new HashMap<Integer, Integer>();
	/**
	 * 装备相关联的情缘
	 */
	private transient List<HeroFate> equipFates;

	private transient RandomItemProperty attr1;// 装备打造属性1
	private transient RandomItemProperty attr2;// 装备打造属性2
	private transient RandomItemProperty attr3;// 装备打造属性3
	private transient List<RandomItemProperty> specialAttr;// 装备打造的特殊属性

	private transient Map<Integer, Integer> fragmentItems = new HashMap<Integer, Integer>();
	private transient Map<Integer, Integer> buildItems = new HashMap<Integer, Integer>();

	public Equip() {
		super();
	}

	public void init() {
		this.setProperties(ItemProperty.parseProperty(this.getValue()));
		if (!StringUtils.isEmpty(this.getSpecSysHeroId())) {// 专属武将属性
			String syss[] = getSpecSysHeroId().split(";");
			for (String s : syss) {
				int id = Integer.valueOf(s);
				specSysHeroIds.put(id, id);
			}
			this.setSpecProperties(ItemProperty.parseProperty(this
					.getSpecValue()));
		}
		// 解析打造属性
		this.setAttr1(parseRandomProperty(this.getBuildAttr1()));
		this.setAttr2(parseRandomProperty(this.getBuildAttr2()));
		this.setAttr3(parseRandomProperty(this.getBuildAttr3()));

		// 打造的特殊属性
		this.setSpecialAttr(parseRandomPropertyList(this.getBuildSpecialAttr()));
		parseItem();
	}

	private void parseItem() {
		// 合成
		if (!StringUtils.isEmpty(this.getFragmentItem())) {
			String fi[] = this.getFragmentItem().split(";");
			for (String i : fi) {
				String is[] = i.split(":");
				if (is.length != 2) {
					continue;
				}
				this.getFragmentItems().put(Integer.valueOf(is[0]),
						Integer.valueOf(is[1]));
			}
		}
		// 打造装备
		if (!StringUtils.isEmpty(this.getBuildItem())) {
			String fi[] = this.getBuildItem().split(";");
			for (String i : fi) {
				String is[] = i.split(":");
				if (is.length != 2) {
					continue;
				}
				this.getBuildItems().put(Integer.valueOf(is[0]),
						Integer.valueOf(is[1]));
			}
		}
	}

	private RandomItemProperty parseRandomProperty(String value) {
		List<RandomItemProperty> properties = parseRandomPropertyList(value);
		if (properties == null || properties.isEmpty()) {
			return null;
		}
		return properties.get(0);
	}

	private List<RandomItemProperty> parseRandomPropertyList(String value) {
		List<RandomItemProperty> properties = new ArrayList<RandomItemProperty>();
		if (StringUtils.isEmpty(value)) {
			return properties;
		}

		String[] props = StringUtils.split(value, ";");
		for (String one : props) {
			String[] tmp = StringUtils.split(one, ",");
			if (tmp.length <= 1 || tmp.length < 4) {
				continue;
			}
			RandomItemProperty prop = new RandomItemProperty();
			prop.setPropName(tmp[0]);
			prop.setMinValue(Integer.parseInt(tmp[1]));
			prop.setMaxValue(Integer.parseInt(tmp[2]));
			prop.setAbs("A".equals(tmp[3]));
			if (tmp.length >= 5) {
				prop.setWeightVal(Integer.parseInt(tmp[4]));
			}
			if (tmp.length >= 6) {
				prop.setSpecialWeightVal(Integer.parseInt(tmp[5]));
			}
			if (prop.getMinValue() >= prop.getMaxValue()) {
				throw new BaseException("装备打造属性配置错误,属性最大值小于等于最小值" + value);
			}
			properties.add(prop);
		}
		return properties;
	}

	public List<ItemProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<ItemProperty> properties) {
		this.properties = properties;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public int getEtype() {
		return etype;
	}

	public void setEtype(int etype) {
		this.etype = etype;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFragmentItem() {
		return fragmentItem;
	}

	public void setFragmentItem(String fragmentItem) {
		this.fragmentItem = fragmentItem;
	}

	public String getBuildItem() {
		return buildItem;
	}

	public void setBuildItem(String buildItem) {
		this.buildItem = buildItem;
	}

	@Override
	public void check() {
		super.check();
	}

	public String getSpecValue() {
		return specValue;
	}

	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}

	public String getSpecSysHeroId() {
		return specSysHeroId;
	}

	public void setSpecSysHeroId(String specSysHeroId) {
		this.specSysHeroId = specSysHeroId;
	}

	public List<ItemProperty> getSpecProperties() {
		return specProperties;
	}

	public void setSpecProperties(List<ItemProperty> specProperties) {
		this.specProperties = specProperties;
	}

	public List<HeroFate> getEquipFates() {
		return equipFates;
	}

	public void setEquipFates(List<HeroFate> equipFates) {
		if (equipFates != null && equipFates.size() > 2) {
			throw new BaseException("通一件装备绑定的两个套装id" + getEntId());
		}
		this.equipFates = equipFates;
	}

	public String getBuildAttr1() {
		return buildAttr1;
	}

	public void setBuildAttr1(String buildAttr1) {
		this.buildAttr1 = buildAttr1;
	}

	public String getBuildAttr2() {
		return buildAttr2;
	}

	public void setBuildAttr2(String buildAttr2) {
		this.buildAttr2 = buildAttr2;
	}

	public String getBuildAttr3() {
		return buildAttr3;
	}

	public void setBuildAttr3(String buildAttr3) {
		this.buildAttr3 = buildAttr3;
	}

	public String getBuildSpecialAttr() {
		return buildSpecialAttr;
	}

	public void setBuildSpecialAttr(String buildSpecialAttr) {
		this.buildSpecialAttr = buildSpecialAttr;
	}

	public RandomItemProperty getAttr1() {
		return attr1;
	}

	public void setAttr1(RandomItemProperty attr1) {
		this.attr1 = attr1;
	}

	public RandomItemProperty getAttr2() {
		return attr2;
	}

	public void setAttr2(RandomItemProperty attr2) {
		this.attr2 = attr2;
	}

	public RandomItemProperty getAttr3() {
		return attr3;
	}

	public void setAttr3(RandomItemProperty attr3) {
		this.attr3 = attr3;
	}

	public List<RandomItemProperty> getSpecialAttr() {
		return specialAttr;
	}

	public void setSpecialAttr(List<RandomItemProperty> specialAttr) {
		this.specialAttr = specialAttr;
	}

	public Map<Integer, Integer> getFragmentItems() {
		return fragmentItems;
	}

	public void setFragmentItems(Map<Integer, Integer> fragmentItems) {
		this.fragmentItems = fragmentItems;
	}

	public Map<Integer, Integer> getBuildItems() {
		return buildItems;
	}

	public void setBuildItems(Map<Integer, Integer> buildItems) {
		this.buildItems = buildItems;
	}

	/**
	 * true表示合成装备
	 * 
	 * @return
	 */
	public boolean isFragmentEquip() {
		return this.etype == 1;
	}

	/**
	 * true 表示打造装备
	 * 
	 * @return
	 */
	public boolean isBuildEquip() {
		return this.etype == 2;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getEquipDebris() {
		return equipDebris;
	}

	public void setEquipDebris(int equipDebris) {
		this.equipDebris = equipDebris;
	}

	public int getEquipDebrisCount() {
		return equipDebrisCount;
	}

	public void setEquipDebrisCount(int equipDebrisCount) {
		this.equipDebrisCount = equipDebrisCount;
	}

	public RandomItemProperty randomBuildAttr() {
		int sum = 0;
		int[] rd = new int[3];
		if (attr1 != null) {
			sum += attr1.getWeightVal();
			rd[0] = (attr1.getWeightVal());
		}
		if (attr2 != null) {
			sum += attr2.getWeightVal();
			rd[1] = (attr2.getWeightVal());
		}
		if (attr3 != null) {
			sum += attr3.getWeightVal();
			rd[2] = (attr3.getWeightVal());
		}
		int index = MathUtils.randomOneFromArray(rd, sum);
		switch (index) {
		case 0:
			return attr1;
		case 1:
			return attr2;
		case 2:
			return attr3;
		default:
			break;
		}
		log.error("equip randomBuildAttr error att is null index={},entId={}",
				index, getEntId());
		return null;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	public boolean hasSpecSysHeroId(int sysHeroId) {
		return specSysHeroIds.containsKey(sysHeroId);
	}

	public int getBuildFactor() {
		return buildFactor;
	}

	public void setBuildFactor(int buildFactor) {
		this.buildFactor = buildFactor;
	}

	public float getBuildFactorF() {
		return this.buildFactor / 1000.0f;
	}

}
