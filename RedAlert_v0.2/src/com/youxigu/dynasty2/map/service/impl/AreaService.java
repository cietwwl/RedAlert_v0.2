package com.youxigu.dynasty2.map.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.map.domain.Area;
import com.youxigu.dynasty2.map.domain.MapCell;
import com.youxigu.dynasty2.map.service.IAreaService;
import com.youxigu.dynasty2.map.service.IMapService;

/**
 * 使用广播消息的时候一定要主要 必须先加入到 指定区域后 再 进行指定区域的消息广播
 * 
 * @author fengfeng
 *
 */
public class AreaService implements IAreaService {
	private static final Logger log = LoggerFactory
			.getLogger(AreaService.class);
	private static final int RANGE = 1;// 广播消息区块的范围
	/** 保存所有的区块id */
	private Map<Integer, Area> areas = new ConcurrentHashMap<Integer, Area>();
	/** 关联用户所在的区块 key用户id val 区块id。。主要是玩家下线好移除集合里面绑定的数据 **/
	private Map<Long, Integer> userAreas = new ConcurrentHashMap<Long, Integer>();

	private IChatClientService messageService;

	private IMapService mapService;

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}

	private int getAreaPosx(int posX) {
		return posX / Area.width;
	}

	private int getAreaPosy(int posY) {
		return posY / Area.high;
	}

	private int getAreaId(int posX, int posY) {
		return calculateAreaId(getAreaPosx(posX), getAreaPosy(posY));
	}

	private int calculateAreaId(int x, int y) {
		return x * 10000 + y;
	}

	@Override
	public void playerEnterArea(long userId, int posX, int posY) {
		int areaId = getAreaId(posX, posY);
		Area area = areas.get(areaId);
		if (area != null) {
			// 玩家进入的是同一个区域，不需要再次加入
			return;
		}// 不同区域

		// 如果存在,玩家退出上一个区域
		playerExitWorldMap(userId);

		// 表示没有玩家来过这个区域
		area = new Area(areaId, getAreaPosx(posX), getAreaPosy(posY));
		area.addUser(userId);
		// 加载这个区域下面的所有格子数据 MapCell,获取这个区域的开始和结束坐标
		int startX = posX - Area.width;
		int endX = posX + Area.width;
		int startY = posY - Area.high;
		int endY = posY + Area.high;

		startX = Math.max(startX, 0);
		endX = Math.min(endX, IMapService.MAP_MAX_WIDTH);

		startY = Math.max(startY, 0);
		endY = Math.min(endY, IMapService.MAP_MAX_WIDTH);

		for (int i = startX; i <= endX; i++) {
			for (int z = startY; z <= endY; z++) {
				int id = MapCell.calculateId(i, z);
				MapCell cell = mapService.getMapCellCache(id);
				if (cell == null) {
					continue;
				}
				area.addMapCell(cell);
			}
		}

		areas.put(areaId, area);

		userAreas.put(userId, areaId);
		// TODO 发送这个区域的所有MapCell数据给玩家
	}

	@Override
	public void broadcastAreaMsg(int eventType, ISendMessage msg, int posX,
			int posY) {
		// List<Area> as = getArea9(posX, posY);
		// for (Area a : as) {
		// a.broadcastAreaMsg(messageService, eventType, msg);
		// }
		int areaId = getAreaId(posX, posY);
		Area area = areas.get(areaId);
		if (area == null) {
			log.error("区域上面没有玩家,不需要广播消息posX={},posY={},eventType={}",
					new Object[] { posX, posY, eventType });
			return;
		}

		area.broadcastAreaMsg(messageService, eventType, msg);
	}

	/**
	 * 获取一个格子周围的8个区块 加自己所在的区块总共9个，返回的list 集合size不一定是9
	 * 
	 * @param posX
	 * @param posY
	 * @return
	 */
	private List<Area> getArea9(int posX, int posY) {
		List<Area> as = new ArrayList<Area>();
		int areaId = getAreaId(posX, posY);
		Area area = areas.get(areaId);
		if (area != null) {
			as.add(area);
			// log.error(
			// "区块消息广播错误,区块不存在,不能广播消息eventType={}, areaId={}, posX={}, posY={}",
			// new Object[] { eventType, areaId, posX, posY });
			// return;
		}
		// 获取格子周围的8个区块
		int x = getAreaPosx(posX);
		int y = getAreaPosy(posY);

		int startX = x - RANGE;
		int endX = x + RANGE;
		int startY = y - RANGE;
		int endY = y + RANGE;
		startX = Math.max(startX, 0);
		endX = Math.min(endX, IMapService.MAP_MAX_WIDTH);

		startY = Math.max(startY, 0);
		endY = Math.min(endY, IMapService.MAP_MAX_WIDTH);

		for (int i = startX; i <= endX; i++) {
			for (int z = startY; z <= endY; z++) {
				int id = calculateAreaId(endX, endY);
				Area a1 = areas.get(id);
				if (a1 == null) {// 表示这个区块没人进来过
					continue;
				}

				as.add(a1);
			}
		}
		return as;
	}

	@Override
	public void playerExitWorldMap(long userId) {
		Integer areaId = userAreas.remove(userId);
		if (areaId == null) {
			return;
		}

		Area area = areas.get(areaId);
		if (area != null) {
			area.removeUser(userId);
		}
	}

}
