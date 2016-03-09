package com.youxigu.dynasty2.map.dao;

import com.youxigu.dynasty2.map.domain.*;

import java.util.List;

public interface IResCasDao {
    List<ResCas> getAllResCas();

    @Deprecated
    List<ResCas> getResCasListByCasType(int casType);

    @Deprecated
    ResCas getResCasById(int resCasId);

    List<DynResCas> getDynResCasOfState(int stateId);

    DynResCas getDynResCas(int mapCellId);

    void createDynResCas(DynResCas dynResCas);

    void updateDynResCas(DynResCas dynResCas);

    void deleteDynResCas(int mapCellId);

    UserResCas getUserResCas(int mapCellId);

    void createUserResCas(UserResCas userResCas);

    void updateUserResCas(UserResCas userResCas);

    void deleteUserResCas(int mapCellId);

    GuildResCas getGuildResCasByMapCellId(int mapCellId);

    GuildResCas getGuildResCasByResCasId(int resCasId);

    void createGuildResCas(GuildResCas guildResCas);

    void updateGuildResCas(GuildResCas guildResCas);

    CountryResCas getCountryResCasByMapCellId(int mapCellId);

    CountryResCas getCountryResCasByResCasId(int resCasId);

    void createCountryResCas(CountryResCas countryResCas);

    void updateCountryResCas(CountryResCas countryResCas);

    List<DynResCasRule> getDynResCasRules();
}
