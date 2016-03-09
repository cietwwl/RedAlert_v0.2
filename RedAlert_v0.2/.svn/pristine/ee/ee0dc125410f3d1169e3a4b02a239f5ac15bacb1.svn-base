package com.youxigu.dynasty2.entity.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.entity.dao.IEntityDao;
import com.youxigu.dynasty2.entity.domain.*;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.entity.domain.effect.EntityEffect;
import com.youxigu.dynasty2.hero.domain.HeroFate;
import com.youxigu.dynasty2.hero.domain.HeroStrength;
import com.youxigu.dynasty2.hero.domain.RelifeLimit;

@SuppressWarnings("unchecked")
public class EntityDao extends BaseDao implements IEntityDao {

	@Override
	public List<Map<String, Object>> getEntitys() {
		return this.getSqlMapClientTemplate().queryForList("listAllEntity");
	}

	@Override
	public Map<String, Object> getEntityById(int entId) {
		return (Map<String, Object>) this.getSqlMapClientTemplate()
				.queryForObject("getEntityByEntId", entId);
	}

	@Override
	public List<Item> listItems() {
		return this.getSqlMapClientTemplate().queryForList("listItems");
	}

	@Override
	public Item getItemByEntId(int entityId) {
		return (Item) this.getSqlMapClientTemplate().queryForObject(
				"getItemByEntId", entityId);
	}

	@Override
	public List<ItemExchange> listItemExchanges() {
		return this.getSqlMapClientTemplate().queryForList("itemExchange");
	}

	@Override
	public List<Equip> listEquips() {
		return this.getSqlMapClientTemplate().queryForList("listEquips");
	}

	@Override
	public Equip getEquipByEntId(int entityId) {
		return (Equip) this.getSqlMapClientTemplate().queryForObject(
				"getEquipByEntId", entityId);
	}

	@Override
	public List<EffectTypeDefine> getAllEffectTypes() {
		return this.getSqlMapClientTemplate().queryForList("getAllEffectTypes");
	}

	@Override
	public List<DropPackEntity> listDropPackEntitys() {
		return this.getSqlMapClientTemplate().queryForList(
				"listDropPackEntitys");
	}

	@Override
	public DropPackEntity getDropPackByEntId(int entityId) {
		return (DropPackEntity) this.getSqlMapClientTemplate().queryForObject(
				"getDropPackByEntId", entityId);
	}

	@Override
	public List<BoxEntity> listBoxEntitys() {
		return this.getSqlMapClientTemplate().queryForList("listBoxEntitys");
	}

	@Override
	public BoxEntity getBoxByEntId(int entityId) {
		return (BoxEntity) this.getSqlMapClientTemplate().queryForObject(
				"getBoxByEntId", entityId);
	}

	// ///////////////////////////创建实体定义数据使用
	@Override
	public void createEffectDefine(EffectDefine ed) {

		this.getSqlMapClientTemplate().insert("insertEffectDefine", ed);
	}

	@Override
	public void createEffectTypeDefine(EffectTypeDefine etd) {
		this.getSqlMapClientTemplate().insert("insertEffectTypeDefine", etd);
	}

	@Override
	public void createEntity(Entity entity) {
		this.getSqlMapClientTemplate().insert("insertEntity", entity);

	}

	@Override
	public void createEntityConsume(EntityConsumeEx consume) {
		this.getSqlMapClientTemplate().insert("insertEntityConsume", consume);

	}

	@Override
	public void createEntityEffect(int entId, int effId) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("entId", entId);
		params.put("effId", effId);
		this.getSqlMapClientTemplate().insert("insertEntityEffect", params);
	}

	@Override
	public void createEntityLimit(EntityLimit limit) {
		this.getSqlMapClientTemplate().insert("insertEntityLimit", limit);

	}

	@Override
	public void deleteEffectDefine(int effId) {
		this.getSqlMapClientTemplate().delete("deleteEffectDefine", effId);

	}

	@Override
	public void deleteEffectTypeDefine(String effTypeId) {
		this.getSqlMapClientTemplate().delete("deleteEffectTypeDefine",
				effTypeId);

	}

	@Override
	public void deleteEntity(Entity entity) {
		this.getSqlMapClientTemplate()
				.delete("deleteEntity", entity.getEntId());

	}

	@Override
	public void deleteEntityCapacity(int entId) {
		this.getSqlMapClientTemplate().delete("deleteEntityCapacity", entId);

	}

	@Override
	public void deleteEntityConsume(int entId) {
		this.getSqlMapClientTemplate().delete("deleteEntityConsume", entId);

	}

	@Override
	public void deleteEntityEffect(int entId) {
		this.getSqlMapClientTemplate().delete("deleteEntityEffect", entId);

	}

	@Override
	public void deleteEntityLimit(int entId) {
		this.getSqlMapClientTemplate().delete("deleteEntityLimit", entId);

	}

	@Override
	public void createDropPack(DropPackEntity entity) {
		createItem(entity);

	}

	@Override
	public void createBox(BoxEntity entity) {
		createItem(entity);

	}

	@Override
	public void createEquip(Equip entity) {
		this.getSqlMapClientTemplate().insert("insertEquip", entity);
	}

	@Override
	public void createItem(Item entity) {
		this.getSqlMapClientTemplate().insert("insertItem", entity);

	}

	@Override
	public void deleteDropPack(int entId) {
		this.deleteItem(entId);

	}

	@Override
	public void deleteBox(int entId) {
		this.deleteItem(entId);

	}

	@Override
	public void deleteEquip(int entId) {
		this.getSqlMapClientTemplate().delete("deleteEquip", entId);
	}

	@Override
	public void deleteItem(int entId) {
		this.getSqlMapClientTemplate().delete("deleteItem", entId);

	}

	@Override
	public List<EntityConsumeEx> getAllEntityConsumes() {
		return this.getSqlMapClientTemplate().queryForList(
				"getAllEntityConsumes");
	}

	@Override
	public List<EntityLimit> getAllEntityLimits() {
		return this.getSqlMapClientTemplate().queryForList(
				"getAllEntityEntityLimits");
	}

	@Override
	public List<EffectDefine> getAllEffectDefines() {
		return this.getSqlMapClientTemplate().queryForList(
				"getAllEffectDefines");
	}

	@Override
	public List<EntityEffect> getAllEntityEffects() {
		return this.getSqlMapClientTemplate().queryForList(
				"getAllEntityEffects");
	}

	@Override
	public List<DropPackItem> getDropPackItemsByEntId(int entId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getDropPackItemsByEntId", entId);
	}

	@Override
	public List<DropPackItem> getDropPackItems() {
		return this.getSqlMapClientTemplate().queryForList("getDropPackItems");
	}

	@Override
	public void deleteDropPackItem(int entId) {
		this.getSqlMapClientTemplate().delete("deleteDropPackItem", entId);

	}

	@Override
	public void createDropPackItem(DropPackItem item) {
		this.getSqlMapClientTemplate().insert("insertDropPackItem", item);

	}

	@Override
	public List<Party> listPartys() {
		return this.getSqlMapClientTemplate().queryForList("listPartys");
	}

	@Override
	public Party getPartyByEntId(int entityId) {
		return (Party) this.getSqlMapClientTemplate().queryForObject(
				"getPartyByEntId", entityId);
	}

	@Override
	public void deleteParty(int entId) {
		this.getSqlMapClientTemplate().delete("deleteParty", entId);

	}

	@Override
	public void createParty(Party entity) {
		this.getSqlMapClientTemplate().insert("insertParty", entity);

	}

	@Override
	public List<Building> listBuildings() {
		return this.getSqlMapClientTemplate().queryForList("listBuildings");
	}

	@Override
	public Building getBuildingByEntId(int entityId) {
		return (Building) this.getSqlMapClientTemplate().queryForObject(
				"getBuildingByEntId", entityId);
	}

	@Override
	public void deleteBuilding(int entId) {
		this.getSqlMapClientTemplate().delete("deleteBuilding", entId);

	}

	@Override
	public void createBuilding(Building entity) {
		this.getSqlMapClientTemplate().insert("insertBuilding", entity);
	}

	@Override
	public List<Technology> listTechnologys() {
		return this.getSqlMapClientTemplate().queryForList("listTechnologys");
	}

	@Override
	public Technology getTechnologyByEntId(int entityId) {
		return (Technology) this.getSqlMapClientTemplate().queryForObject(
				"getTechnologyByEntId", entityId);
	}

	@Override
	public void deleteTechnology(int entId) {
		this.getSqlMapClientTemplate().delete("deleteTechnology", entId);

	}

	@Override
	public void createTechnology(Technology entity) {
		this.getSqlMapClientTemplate().insert("insertTechnology", entity);

	}

	@Override
	public List<Army> listArmys() {
		return this.getSqlMapClientTemplate().queryForList("listArmys");
	}

	@Override
	public Army getArmyByEntId(int entityId) {
		return (Army) this.getSqlMapClientTemplate().queryForObject(
				"getArmyByEntId", entityId);
	}

	@Override
	public void deleteArmy(int entId) {
		this.getSqlMapClientTemplate().delete("deleteArmy", entId);

	}

	@Override
	public void createArmy(Army entity) {
		this.getSqlMapClientTemplate().insert("insertArmy", entity);
	}

	@Override
	public List<Resource> listResources() {
		return this.getSqlMapClientTemplate().queryForList("listResources");
	}

	@Override
	public Resource getResourceByEntId(int entityId) {
		return (Resource) this.getSqlMapClientTemplate().queryForObject(
				"getResourceByEntId", entityId);
	}

	@Override
	public void deleteResource(int entId) {
		this.getSqlMapClientTemplate().delete("deleteResource", entId);
	}

	@Override
	public void createResource(Resource entity) {
		this.getSqlMapClientTemplate().insert("insertResource", entity);
	}

	@Override
	public HeroSkill getHeroSkillByEntId(int entityId) {
		return (HeroSkill) this.getSqlMapClientTemplate().queryForObject(
				"getHeroSkillByEntId", entityId);
	}

	@Override
	public List<HeroSkill> listHeroSkills() {
		return this.getSqlMapClientTemplate().queryForList("listHeroSkills");
	}

	@Override
	public List<HeroSkillEffect> listHeroSkillEffects() {
		return this.getSqlMapClientTemplate().queryForList(
				"listHeroSkillEffects");
	}

	@Override
	public List<HeroSkillEffect> getHeroSkillEffectsBySkillId(int entityId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getHeroSkillEffects", entityId);
	}

	@Override
	public List<HeroSkillLimit> listHeroSkillLimits() {
		return this.getSqlMapClientTemplate().queryForList(
				"listHeroSkillLimits");
	}

	public List<HeroSkillLimit> getHeroSkillLimitBySkillId(int entityId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getHeroSkillLimits", entityId);
	}

	@Override
	public List<HeroSkillEffectLimit> listHeroSkillEffectLimits() {
		return this.getSqlMapClientTemplate().queryForList(
				"listHeroSkillEffectLimits");
	}

	@Override
	public List<HeroSkillEffectLimit> getHeroSkillEffectLimitByEffId(int effId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getHeroSkillEffectLimitByEffId", effId);
	}

	@Override
	public SysHero getSysHeroByEntId(int entityId) {
		return (SysHero) this.getSqlMapClientTemplate().queryForObject(
				"getSysHeroByEntId", entityId);
	}

	@Override
	public List<SysHero> listSysHeros() {
		return this.getSqlMapClientTemplate().queryForList("listSysHeros");
	}

	@Override
	public List<HeroFate> listHeroFates() {
		return this.getSqlMapClientTemplate().queryForList("listHeroFates");
	}

	@Override
	public List<HeroFate> listHeroFatesByType(int type) {
		return this.getSqlMapClientTemplate().queryForList(
				"listHeroFatesByType", type);
	}

	@Override
	public List<EquipLevelUpRule> getEquipLevelUpRules() {
		return this.getSqlMapClientTemplate().queryForList(
				"getEquipLevelUpRules");
	}

	@Override
	public List<RelifeLimit> getRelifeLimitList() {
		return this.getSqlMapClientTemplate()
				.queryForList("getRelifeLimitList");
	}

	@Override
	public List<HeroStrength> getHeroStrengthList() {
		return this.getSqlMapClientTemplate().queryForList("getHeroStrength");
	}

	@Override
	public List<SysHeroCountry> getSysHeroCountrys() {
		return this.getSqlMapClientTemplate()
				.queryForList("getSysHeroCountrys");
	}
}
