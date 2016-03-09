package com.youxigu.dynasty2.map.service.impl.command;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.map.domain.action.RefreshAction;
import com.youxigu.dynasty2.map.domain.action.TimeAction;
import com.youxigu.dynasty2.map.service.ICommandDistatcher;
import com.youxigu.dynasty2.map.service.IMapService;
import com.youxigu.dynasty2.map.service.IResCasService;
import com.youxigu.dynasty2.util.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class RefreshDynResCasCommand extends NoTransCommand {
    protected Logger log = LoggerFactory.getLogger(RefreshDynResCasCommand.class);
    public static final int ACTION_START_DYN_RES_CAS = 1;
    public static final int ACTION_CREATE_DYN_RES_CAS = 2;

    protected ICommandDistatcher commandDistatcher;
    protected IResCasService resCasService;
    protected ICommonService commonService;
    protected IMapService mapService;

    @Override
    public Map<String, Object> excute(TimeAction action) {
        if(!(action instanceof RefreshAction)){
            throw new BaseException("错误的配置");
        }
        RefreshAction refreshAction = (RefreshAction)action;
        if(refreshAction.getAct() == ACTION_START_DYN_RES_CAS) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("开始执行刷新动态资源点任务");
                }

                List<Integer> stateIdList = mapService.getAllStateIds();
                for (int i = 0; i < stateIdList.size(); i++) {
                    int state = stateIdList.get(i);
                    try {
                        RefreshAction cmd = new RefreshAction();
                        cmd.setCmd(COMMAND_2);
                        int interval = i * (5 * 60);
//                        int interval = i * (1 * 20);
                        cmd.setTime(UtilDate.moveSecond(interval).getTime());
                        cmd.setStateId(state);
                        cmd.setAct(ACTION_CREATE_DYN_RES_CAS);
                        commandDistatcher.putCommander(0, cmd);

                        if (log.isDebugEnabled()) {
                            log.debug("新增动态刷{}区资源点任务，将在{}秒后执行", state,
                                    interval);
                        }


                    } catch (Exception e) {
                        log.error("启动刷新资源点异常", e);
                    }
                }
            } finally {
                RefreshAction cmd = new RefreshAction();
                cmd.setCmd(COMMAND_2);
                int interval = commonService.getSysParaIntValue(
                        AppConstants.DYN_RES_CAS_REFRESH_INTERVAL_SECONDS,
                        AppConstants.DYN_RES_CAS_REFRESH_INTERVAL_SECONDS_DEFAULT_VALUE);
                cmd.setTime(UtilDate.moveSecond(interval).getTime());
                cmd.setAct(ACTION_START_DYN_RES_CAS);
                commandDistatcher.putCommander(0, cmd);

                if (log.isDebugEnabled()) {
                    log.debug("新增动态刷资源点任务，将在{}秒后执行",
                            AppConstants.DYN_RES_CAS_REFRESH_INTERVAL_SECONDS_DEFAULT_VALUE);
                }
            }
        }
        else if(refreshAction.getAct() == ACTION_CREATE_DYN_RES_CAS){
            try {
                resCasService.refreshDynResCas(refreshAction.getStateId());
            }
            catch (Exception e){
                log.error("启动刷新资源点异常", e);
            }
        }
        return null;
    }

    public ICommandDistatcher getCommandDistatcher() {
        return commandDistatcher;
    }

    public void setCommandDistatcher(ICommandDistatcher commandDistatcher) {
        this.commandDistatcher = commandDistatcher;
    }

    public IResCasService getResCasService() {
        return resCasService;
    }

    public void setResCasService(IResCasService resCasService) {
        this.resCasService = resCasService;
    }

    public ICommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }

    public IMapService getMapService() {
        return mapService;
    }

    public void setMapService(IMapService mapService) {
        this.mapService = mapService;
    }
}
