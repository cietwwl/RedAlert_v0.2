package com.youxigu.dynasty2.develop.service;

import java.util.List;

import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.util.EffectValue;

/**
 * 城池效果-主要针对建筑效果，影响经济产出的效果
 *
 * @author Dagangzi
 */
public interface ICastleEffectService {

    /**
     * 取得城池的所有效果
     *
     * @param casId
     * @return
     */
    List<CastleEffect> getCastleEffectByCasId(long casId);

    /**
     * @param casId
     * @param effTypeId
     * @return
     */
    List<CastleEffect> getCastleEffectByEffTypeIdWithTimeout(long casId, String effTypeId);

    List<CastleEffect> getCastleEffectByEffTypeId(long casId, String effTypeId);

    /**
     * 按实体ID查询该实体产生的效果
     *
     * @param casId
     * @param entId
     * @return
     */
    List<CastleEffect> getCastleEffectByEntId(long casId, int entId);

    List<CastleEffect> getCastleEffectByEffTypeIdWithTimeout(long casId,
                                                             String effTypeId, int entId);

    /**
     * 按实体ID+效果类型
     *
     * @param casId
     * @param entId
     * @param effTypeId
     * @return
     */
    CastleEffect getCastleEffectByEffTypeId(long casId, int entId,
                                            String effTypeId);

    /**
     * 取得城池的某一类型效果的和
     *
     * @param casId
     * @param effTypeId
     * @return
     */
    EffectValue getSumCastleEffectValueByEffectType(long casId, String effTypeId);

    EffectValue getSumCastleEffectValueByEffectType(String effTypeId, List<CastleEffect> effects);

    int getSumCastleEffectAbsValueByEffectType(long casId, String effTypeId);

    int getSumCastleEffectPercentValueByEffectType(long casId, String effTypeId);

    /**
     * 创建/更新城池效果
     * <p/>
     * 更新：找到原有的具有相同entId的效果，直接更新成新的效果，不累加
     *
     * @param casId
     * @param effect
     */
    @Deprecated
    void addCastleEffect(long casId, Entity entity, EffectDefine effect);

    void createCastleEffect(CastleEffect ce);

    void createCastleEffect(CastleEffect ce, boolean notify);

    void updateCastleEffect(CastleEffect ce);

    void updateCastleEffect(CastleEffect ce, boolean notify);

    void deleteCastleEffect(CastleEffect ce);

    void deleteCastleEffect(CastleEffect ce, boolean notify);

    /**
     * 删除过期效果
     *
     * @param casId
     */
    List<CastleEffect> removeTimeoutCastleEffect(long casId);

    /**
     * 删除某一效果类型的效果
     *
     * @param casId
     * @param effTypeId
     */
    void removeCastleEffectByType(long casId, String effTypeId);


    void lockCastleEffect(long casId);


    /**
     * 得到某类效果加成后的时间
     *
     * @param casId
     * @param time
     * @param effectType
     * @return
     */
    int getActualTime(long casId, int time, String effectType);
}
