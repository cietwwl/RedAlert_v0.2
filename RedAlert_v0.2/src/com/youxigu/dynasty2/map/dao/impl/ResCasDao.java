package com.youxigu.dynasty2.map.dao.impl;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.map.dao.IResCasDao;
import com.youxigu.dynasty2.map.domain.*;

import java.util.List;

public class ResCasDao extends BaseDao implements IResCasDao{
    @Override
    public List<ResCas> getAllResCas() {
        return getSqlMapClientTemplate().queryForList("getAllResCas");
    }

    @Override
    public List<ResCas> getResCasListByCasType(int casType) {
        return getSqlMapClientTemplate().queryForList("getAllResCas", casType);
    }

    @Override
    public ResCas getResCasById(int resCasId) {
        return (ResCas)getSqlMapClientTemplate().queryForObject("getResCasById", resCasId);
    }

    @Override
    public List<DynResCas> getDynResCasOfState(int stateId) {
        return getSqlMapClientTemplate().queryForList("getDynResCasOfState", stateId);
    }

    @Override
    public DynResCas getDynResCas(int mapCellId) {
        return (DynResCas)getSqlMapClientTemplate().queryForObject("getDynResCas", mapCellId);
    }

    @Override
    public void createDynResCas(DynResCas dynResCas) {
        getSqlMapClientTemplate().insert("createDynResCas", dynResCas);
    }

    @Override
    public void updateDynResCas(DynResCas dynResCas) {
        getSqlMapClientTemplate().update("updateDynResCas", dynResCas);
    }

    @Override
    public void deleteDynResCas(int mapCellId) {
        getSqlMapClientTemplate().delete("deleteDynResCas", mapCellId);

    }

    @Override
    public UserResCas getUserResCas(int mapCellId) {
        return (UserResCas)getSqlMapClientTemplate().queryForObject("getUserResCas", mapCellId);
    }

    @Override
    public void createUserResCas(UserResCas userResCas) {
        getSqlMapClientTemplate().insert("createUserResCas", userResCas);
    }

    @Override
    public void updateUserResCas(UserResCas userResCas) {
        getSqlMapClientTemplate().update("updateUserResCas", userResCas);
    }

    @Override
    public void deleteUserResCas(int mapCellId) {
        getSqlMapClientTemplate().delete("deleteUserResCas", mapCellId);
    }

    @Override
    public GuildResCas getGuildResCasByMapCellId(int mapCellId) {
        return (GuildResCas)getSqlMapClientTemplate().queryForObject("getGuildResCasByMapCellId", mapCellId);
    }

    @Override
    public GuildResCas getGuildResCasByResCasId(int resCasId) {
        return (GuildResCas)getSqlMapClientTemplate().queryForObject("getGuildResCasByResCasId", resCasId);
    }

    @Override
    public void createGuildResCas(GuildResCas guildResCas) {
        getSqlMapClientTemplate().insert("createGuildResCas", guildResCas);
    }

    @Override
    public void updateGuildResCas(GuildResCas guildResCas) {
        getSqlMapClientTemplate().update("updateGuildResCas", guildResCas);
    }

    @Override
    public CountryResCas getCountryResCasByMapCellId(int mapCellId) {
        return (CountryResCas)getSqlMapClientTemplate().queryForObject("getCountryResCasByMapCellId", mapCellId);
    }

    @Override
    public CountryResCas getCountryResCasByResCasId(int resCasId) {
        return (CountryResCas)getSqlMapClientTemplate().queryForObject("getCountryResCasByResCasId", resCasId);
    }

    @Override
    public void createCountryResCas(CountryResCas countryResCas) {
        getSqlMapClientTemplate().insert("createCountryResCas", countryResCas);
    }

    @Override
    public void updateCountryResCas(CountryResCas countryResCas) {
        getSqlMapClientTemplate().update("updateCountryResCas", countryResCas);

    }

    @Override
    public List<DynResCasRule> getDynResCasRules() {
        return getSqlMapClientTemplate().queryForList("getDynResCasRules");
    }

}
