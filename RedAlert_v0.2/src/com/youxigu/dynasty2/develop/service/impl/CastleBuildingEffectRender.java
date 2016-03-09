package com.youxigu.dynasty2.develop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEntityEffectRender;
import com.youxigu.dynasty2.user.domain.UserEffect;
import com.youxigu.dynasty2.user.service.IUserEffectService;
import com.youxigu.dynasty2.util.BaseException;
/**
 * 建筑实体渲染器
 * @author Dagangzi
 *
 */
public class CastleBuildingEffectRender implements IEntityEffectRender {
    ICastleEffectService castleEffectService;
	IUserEffectService userEffectService;

    public void setCastleEffectService(ICastleEffectService castleEffectService) {
        this.castleEffectService = castleEffectService;
    }

	public void setUserEffectService(IUserEffectService userEffectService) {
		this.userEffectService = userEffectService;
	}

    /**
     * casId:城池ID <br>
     * level level :当前建筑等级（升降级后的） <br>
     * action:升级/降级
     */
    @Override
    public Map<String, Object> render(Entity entity, Map<String, Object> context) {
        int action = (Integer) context.get("action");
        if (action == Entity.ACTION_LEVEL_MUTLI) {
            return _render(entity, context);
        }

        Castle mainCastle = (Castle) context.get("mainCastle");
        int level = (Integer) context.get("level");


        int flag = 1;
        if (action == Entity.ACTION_LEVEL_DOWN) {
            level = level + 1;// 降级取原来的等级，减去原来的效果
            flag = -1;
        }
        long casId = mainCastle.getCasId();
        long userId = mainCastle.getUserId();

        int entId = 0;// entity.getEntId(); //建筑的效果不按entID区分，全部累加
        List<EffectDefine> effects = entity.getEffects(level);
        if (effects != null) {
            for (EffectDefine effect : effects) {
                int target = effect.getTarget();
                String effTypeId = effect.getEffTypeId();
                if (target == EffectDefine.TARGET_NONE) {
                    continue;
                }
                // 建筑的效果默认对城池
                if (target == EffectDefine.TARGET_CASTLE || target == EffectDefine.TARGET_DEFAULT) {

                    castleEffectService.lockCastleEffect(casId);

                    CastleEffect ce = castleEffectService.getCastleEffectByEffTypeId(casId, entId, effTypeId);
                    if (ce == null) {
                        // 新建
                        ce = new CastleEffect();
                        ce.setCasId(casId);
                        ce.setEntId(entId);
                        ce.setEffTypeId(effTypeId);
                        ce.setExpireDttm(null);
                        ce.setPerValue(effect.getPara1());
                        ce.setAbsValue(effect.getPara2());
                        castleEffectService.createCastleEffect(ce);
                    } else {
                        // 存在:升级则加上当前级别的值，降级减去原级别的值
                        // EffectDefine中配置的的第一级效果值是初始值，以后的值是在上一级基础上增加的值
                        if (flag == 1) {
                            ce.setPerValue(ce.getPerValue() + effect.getPara1());
                            ce.setAbsValue(ce.getAbsValue() + effect.getPara2());
                        } else {
                            ce.setPerValue(ce.getPerValue() - effect.getPara1());
                            ce.setAbsValue(ce.getAbsValue() - effect.getPara2());

                        }
                        ce.setExpireDttm(null);
                        castleEffectService.updateCastleEffect(ce);
                    }
                } else if (target == EffectDefine.TARGET_USER) {
					userEffectService.lockUserEffect(userId);

					UserEffect ue = userEffectService.getUserEffectByEffTypeId(
							userId, entId, effTypeId);
					if (ue == null) {
						ue = new UserEffect();
						ue.setUserId(userId);
						ue.setEntId(entId);
						ue.setEffTypeId(effTypeId);
						ue.setExpireDttm(UtilDate.NOTAVAIABLE);
						ue.setPerValue(effect.getPara1());
						ue.setAbsValue(effect.getPara2());
						userEffectService.createUserEffect(ue);
					} else {
						ue.setPerValue(ue.getPerValue() + effect.getPara1()
								* flag);
						ue.setAbsValue(ue.getAbsValue() + effect.getPara2()
								* flag);
						ue.setExpireDttm(UtilDate.NOTAVAIABLE);
						userEffectService.updateUserEffect(ue);
					}
                } else {
                    throw new BaseException("建筑效果目标错误");
                }
            }
        }
        return null;
    }

    // 跨越等级的升级/降级
    private Map<String, Object> _render(Entity entity, Map<String, Object> context) {

        Castle mainCastle = (Castle) context.get("mainCastle");
        int oldLevel = (Integer) context.get("oldLevel");
        int level = (Integer) context.get("level");
        //int action = (Integer) context.get("action");

        int flag = 1;
        int maxLv = 0;
        int minLv = 0;
        if (oldLevel > level) {
            maxLv = oldLevel;
            minLv = level;
            flag = -1;
        } else {
            maxLv = level;
            minLv = oldLevel + 1;
        }

        int entId = 0;// entity.getEntId(); //建筑的效果不按entID区分，全部累加
        long casId = mainCastle.getCasId();
        long userId = mainCastle.getUserId();

        Map<String, CastleEffect> casEffectMaps = new HashMap<String, CastleEffect>();
//		Map<String, UserEffect> userEffectMaps = new HashMap<String, UserEffect>();

        for (int i = minLv; i <= maxLv; i++) {
            List<EffectDefine> effects = entity.getEffects(i);
            if (effects != null) {
                for (EffectDefine effect : effects) {
                    if (effect.getTarget() == EffectDefine.TARGET_NONE) {
                        continue;
                    }
                    int target = effect.getTarget();
                    String effTypeId = effect.getEffTypeId();
                    // 建筑的效果默认对城池
                    if (target == EffectDefine.TARGET_CASTLE || target == EffectDefine.TARGET_DEFAULT) {
                        CastleEffect ce = casEffectMaps.get(effTypeId);
                        if (ce == null) {
                            castleEffectService.lockCastleEffect(casId);
                            ce = castleEffectService.getCastleEffectByEffTypeId(casId, entId, effTypeId);
                            if (ce != null) {
                                casEffectMaps.put(effTypeId, ce);
                            }
                        }
                        if (ce == null) {
                            // 新建
                            ce = new CastleEffect();
                            ce.setCasId(casId);
                            ce.setEntId(entId);
                            ce.setEffTypeId(effTypeId);
                            ce.setExpireDttm(null);
                            ce.setPerValue(effect.getPara1());
                            ce.setAbsValue(effect.getPara2());
                            casEffectMaps.put(effTypeId, ce);
                            // castleEffectService.createCastleEffect(ce);
                        } else {
                            // 存在:升级则加上当前级别的值，降级减去原级别的值
                            // EffectDefine中配置的的第一级效果值是初始值，以后的值是在上一级基础上增加的值
                            if (flag == 1) {
                                ce.setPerValue(ce.getPerValue() + effect.getPara1());
                                ce.setAbsValue(ce.getAbsValue() + effect.getPara2());
                            } else {
                                ce.setPerValue(ce.getPerValue() - effect.getPara1());
                                ce.setAbsValue(ce.getAbsValue() - effect.getPara2());
                            }
                            ce.setExpireDttm(null);
                        }
                    } else if (target == EffectDefine.TARGET_USER) {
//						UserEffect ue = userEffectMaps.get(effTypeId);
//						if (ue == null) {
//							userEffectService.lockUserEffect(userId);
//
//							ue = userEffectService.getUserEffectByEffTypeId(
//									userId, entId, effTypeId);
//							if (ue != null) {
//								userEffectMaps.put(effTypeId, ue);
//							}
//						}
//						if (ue == null) {
//							ue = new UserEffect();
//							ue.setUserId(userId);
//							ue.setEntId(entId);
//							ue.setEffTypeId(effect.getEffTypeId());
//							ue.setExpireDttm(UtilDate.NOTAVAIABLE);
//							ue.setPerValue(effect.getPara1());
//							ue.setAbsValue(effect.getPara2());
//							userEffectMaps.put(effTypeId, ue);
//						} else {
//							ue.setPerValue(ue.getPerValue() + effect.getPara1()
//									* flag);
//							ue.setAbsValue(ue.getAbsValue() + effect.getPara2()
//									* flag);
//							ue.setExpireDttm(UtilDate.NOTAVAIABLE);
//						}
                    } else {
                        throw new BaseException("建筑效果目标错误");
                    }
                }
            }

        }

        for (CastleEffect e : casEffectMaps.values()) {
            if (e.getId() == 0) {
                castleEffectService.createCastleEffect(e);
            } else {
                castleEffectService.updateCastleEffect(e);
            }
        }

//		for (UserEffect e : userEffectMaps.values()) {
//			if (e.getId() == 0) {
//				userEffectService.createUserEffect(e);
//			} else {
//				userEffectService.updateUserEffect(e);
//			}
//		}
        return null;
    }
}
