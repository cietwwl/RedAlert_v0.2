package com.youxigu.dynasty2.develop.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.asyncdb.service.impl.IDUtil;
import com.youxigu.dynasty2.develop.dao.ICastleDao;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleBuilder;
import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.domain.UserTechnology;

@SuppressWarnings("unchecked")
public class CastleDao extends BaseDao implements ICastleDao {

	private static final String ID_TYPE = "CASTLE";

	// ==========================Castle========================

	@Override
	public void createCastle(Castle castle) {
		castle.setCasId(IDUtil.getNextId(ID_TYPE));
		this.getSqlMapClientTemplate().insert("insertCastle", castle);
	}

	@Override
	public void updateCastle(Castle castle) {
		this.getSqlMapClientTemplate().update("updateCastle", castle);
	}

	@Override
	public Castle getCastleById(long casId) {
		return (Castle) this.getSqlMapClientTemplate().queryForObject(
				"getCastleById", casId);
	}

	@Override
	public Castle getCastleByUserId(long userId) {
		return (Castle) this.getSqlMapClientTemplate().queryForObject(
				"getCastleByUserId", userId);
	}

	// ==========================CastleBuilding========================

    @Override
    public CastleBuilding getCastBuildingById(long casId, long casBuildingId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("casId", casId);
        params.put("casBuiId", casBuildingId);
        return (CastleBuilding) this.getSqlMapClientTemplate().queryForObject(
                "getCastleBuildingById", params);
    }

	@Override
	public List<CastleBuilding> getCastBuildingsByCasId(long casId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getCastleBuildingsByCasId", casId);
	}

	@Override
	public void createCastleBuilding(CastleBuilding cb) {
		if (cb.getCasBuiId() > 0) {
			this.getSqlMapClientTemplate().update("updateCastleBuilding", cb);
		} else {
			this.getSqlMapClientTemplate().insert("insertCastleBuilding", cb);
		}
	}

	@Override
	public void deleteCastleBuilding(CastleBuilding cb) {
		this.getSqlMapClientTemplate().delete("deleteCastleBuilding", cb);
	}

	@Override
	public void updateCastleBuilding(CastleBuilding cb) {
		if (cb.getCasBuiId() > 0) {
			this.getSqlMapClientTemplate().update("updateCastleBuilding", cb);
		} else {
			this.getSqlMapClientTemplate().insert("insertCastleBuilding", cb);
		}
	}

	// ==========================CastleBuilder=========================

	@Override
	public void createCastleBuilder(CastleBuilder casBuilder) {
		this.getSqlMapClientTemplate()
				.insert("insertCastleBuilder", casBuilder);
	}

	@Override
	public void deleteCastleBuilder(CastleBuilder casBuilder) {
		this.getSqlMapClientTemplate()
				.delete("deleteCastleBuilder", casBuilder);
	}

	@Override
	public List<CastleBuilder> getCastleBuilders(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getCastleBuilders",
				userId);
	}

    @Override
    public void updateCastleBuilder(CastleBuilder casBuilder) {
        this.getSqlMapClientTemplate().update("updateCastleBuilder", casBuilder);
    }

    // ==========================Technology=========================

    @Override
    public void createUserTechnology(UserTechnology ut) {
        if (ut.getId() > 0) {
            this.getSqlMapClientTemplate().update("updateUserTech", ut);
        } else {
            this.getSqlMapClientTemplate().insert("insertUserTech", ut);
        }

    }

    @Override
    public List<UserTechnology> getUserTechnologysByUserId(long userId) {

        return this.getSqlMapClientTemplate().queryForList(
                "getUserTechsByUserId", userId);
    }

    @Override
    public void updateUserTechnology(UserTechnology ut) {
        if (ut.getId() > 0) {
            this.getSqlMapClientTemplate().update("updateUserTech", ut);
        } else {
            this.getSqlMapClientTemplate().insert("insertUserTech", ut);
        }

    }


    @Override
    public UserTechnology getUserTechnologyById(long userId, long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("id", id);
        return (UserTechnology) this.getSqlMapClientTemplate().queryForObject(
                "getUserTechById", params);
    }

    @Deprecated
    @Override
    public UserTechnology getUserTechnologyByEntId(long userId, int entId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("entId", entId);
        return (UserTechnology) this.getSqlMapClientTemplate().queryForObject(
                "getUserTechsByEntId", params);
    }

}
