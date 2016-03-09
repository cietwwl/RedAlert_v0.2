package com.youxigu.dynasty2.entity.dao;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.*;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.entity.domain.effect.EntityEffect;
import com.youxigu.dynasty2.hero.domain.HeroFate;
import com.youxigu.dynasty2.hero.domain.HeroStrength;
import com.youxigu.dynasty2.hero.domain.RelifeLimit;

/*
 * 所有实体的DAO接口
 * 
 * TODO：最好所有的entity子类由各自的DAO实现，不要放在EntityDao中
 */
public interface IEntityDao {

	/**
	 * 得到所有实体定义:对应Entity表
	 * 
	 * @return
	 */
	List<Map<String, Object>> getEntitys();

	/**
	 * 得到抽象实体定义
	 * 
	 * @param entId
	 * @return
	 */
	Map<String, Object> getEntityById(int entId);

	/**
	 * 获得实体建造消耗
	 * 
	 * @param entityId
	 * @return
	 */
	List<EntityConsumeEx> getAllEntityConsumes();

	/**
	 * 获得实体建造约束
	 * 
	 * @param entityId
	 * @return
	 */
	List<EntityLimit> getAllEntityLimits();

	/**
	 * 取得所有的EffectType
	 */
	List<EffectTypeDefine> getAllEffectTypes();

	/**
	 * 取得entity的效果
	 * 
	 * @param entId
	 * @return
	 */
	List<EffectDefine> getAllEffectDefines();

	List<EntityEffect> getAllEntityEffects();

	// ///////////////////////////创建实体定义数据使用

	void deleteEntity(Entity entity);

	void deleteEntityCapacity(int entId);

	void deleteEntityConsume(int entId);

	void deleteEntityLimit(int entId);

	void deleteEntityEffect(int entId);

	void deleteEffectDefine(int effId);

	void deleteEffectTypeDefine(String effTypeId);

	void createEntity(Entity entity);

	void createEntityConsume(EntityConsumeEx consume);

	void createEntityLimit(EntityLimit limit);

	void createEffectDefine(EffectDefine ed);

	void createEntityEffect(int entId, int effId);

	void createEffectTypeDefine(EffectTypeDefine etd);

	/**
	 * 取得特定道具类型
	 * 
	 * @param entityId
	 *            道具类型id
	 * @return
	 */
	List<Item> listItems();

	Item getItemByEntId(int entityId);

	void deleteItem(int entId);

	void createItem(Item entity);

	List<ItemExchange> listItemExchanges();

	/**
	 * 取得装备定义
	 * 
	 * @param entityId
	 * @return
	 */
	List<Equip> listEquips();

	Equip getEquipByEntId(int entityId);

	void deleteEquip(int entId);

	void createEquip(Equip entity);

	/**
	 * 根据掉落包编号获取掉落包
	 * 
	 * @param entityId
	 * @return
	 */
	List<DropPackEntity> listDropPackEntitys();

	DropPackEntity getDropPackByEntId(int entityId);

	void deleteDropPack(int entId);

	void createDropPack(DropPackEntity entity);

	/**
	 * 根据掉落包编号获取宝箱掉落包
	 * 
	 * @param entityId
	 * @return
	 */
	List<BoxEntity> listBoxEntitys();

	BoxEntity getBoxByEntId(int entityId);

	void deleteBox(int entId);

	void createBox(BoxEntity entity);

	/**
	 * 根据掉落包编号获取掉落包掉落配置
	 * 
	 * @param entId
	 * @return
	 */
	List<DropPackItem> getDropPackItemsByEntId(int entId);

	List<DropPackItem> getDropPackItems();

	void deleteDropPackItem(int entId);

	void createDropPackItem(DropPackItem item);

	/**
	 * 取得实体其他约束条件-时间-人口-面积-主城-贡献
	 * 
	 * @param entityId
	 * @return
	 */
	List<Party> listPartys();

	Party getPartyByEntId(int entityId);

	void deleteParty(int entId);

	void createParty(Party entity);

	/**
	 * 取得特定建筑类型
	 * 
	 * @param entityId
	 *            建筑类型定义
	 * @return
	 */
	List<Building> listBuildings();

	Building getBuildingByEntId(int entityId);

	void deleteBuilding(int entId);

	void createBuilding(Building entity);

	/**
	 * 取得特定科技类型
	 * 
	 * @param entityId
	 *            科技类型id
	 * @return
	 */
	List<Technology> listTechnologys();

	Technology getTechnologyByEntId(int entityId);

	void deleteTechnology(int entId);

	void createTechnology(Technology entity);

	/**
	 * 取得特定部队类型
	 * 
	 * @return
	 */
	List<Army> listArmys();

	Army getArmyByEntId(int entityId);

	void deleteArmy(int entId);

	void createArmy(Army entity);

	/**
	 * 取得特定资源类型
	 * 
	 * @param entityId
	 *            资源类型id
	 * @return
	 */
	List<Resource> listResources();

	Resource getResourceByEntId(int entityId);

	void deleteResource(int entId);

	void createResource(Resource entity);

	/**
	 * 武将技能
	 * 
	 * @param entityId
	 * @return
	 */
	HeroSkill getHeroSkillByEntId(int entityId);

	List<HeroSkill> listHeroSkills();

	List<HeroSkillEffect> listHeroSkillEffects();

	List<HeroSkillEffect> getHeroSkillEffectsBySkillId(int entityId);

	List<HeroSkillLimit> listHeroSkillLimits();

	List<HeroSkillLimit> getHeroSkillLimitBySkillId(int entityId);

	List<HeroSkillEffectLimit> listHeroSkillEffectLimits();

	List<HeroSkillEffectLimit> getHeroSkillEffectLimitByEffId(int effId);

	/**
	 * 取得系统武将实体
	 * 
	 * @param entityId
	 * @return
	 */
	SysHero getSysHeroByEntId(int entityId);

	List<SysHero> listSysHeros();

	/**
	 * 加载所有武将情缘
	 * 
	 * @return
	 */
	List<HeroFate> listHeroFates();

	List<HeroFate> listHeroFatesByType(int type);

	/**
	 * 装备升级规则
	 * 
	 * @return
	 */
	List<EquipLevelUpRule> getEquipLevelUpRules();

	/**
	 * 加载武将进阶的数据 取出来的数据是有序的
	 * 
	 * @return
	 */
	public List<RelifeLimit> getRelifeLimitList();

	/**
	 * 加载武将强化的数据
	 * 
	 * @return
	 */
	public List<HeroStrength> getHeroStrengthList();

	/**
	 * 获取所有的系统武将所属的国家信息
	 * 
	 * @return
	 */
	List<SysHeroCountry> getSysHeroCountrys();
}
