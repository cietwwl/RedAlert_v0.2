package com.youxigu.dynasty2.map.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;

/**
 * 地图区域。把大地图拆分成一个一个的小区域，每个小区域里面包含一定数量的MapCell<br>
 * 如果玩家进入区域对象，保存玩家id，主要对停留在区域对象上的玩家进行一些区域状态变化的消息广播<br>
 * 设计世界地图服务器的坐标是1200*1200,每一个服务器坐标对应的像素单位是
 * 20*20，手机屏幕按照1280*720，每个区域大小设置的服务器坐标是22*12<br>
 * 
 * @author fengfeng
 *
 */
public class Area implements Serializable {
	private static final long serialVersionUID = -8370703620319916541L;
	public static final int width = 22;// 每个区块 宽是 22 单位个 MapCell
	public static final int high = 12;// 每个区块 高是 12 单位个 MapCell

	/** 区域id */
	private int id;//
	/** 区域所在x坐标 */
	private int areaPosx;//
	/** 区域所在y坐标 */
	private int areaPosy;
	/** 区块下面包含的所有在这个区块里面的格子,主要做数据加载用。。不能通过这个对象来修改里面的数据 */
	private Map<Integer, MapCell> cells = new HashMap<Integer, MapCell>();
	/** 进入这个区域的玩家 key=userId val=userId,集合数据会增加删除 */
	private Map<Long, Long> users = new ConcurrentHashMap<Long, Long>();

	public Area(int id, int areaPosx, int areaPosy) {
		super();
		this.id = id;
		this.areaPosx = areaPosx;
		this.areaPosy = areaPosy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAreaPosx() {
		return areaPosx;
	}

	public void setAreaPosx(int areaPosx) {
		this.areaPosx = areaPosx;
	}

	public int getAreaPosy() {
		return areaPosy;
	}

	public void setAreaPosy(int areaPosy) {
		this.areaPosy = areaPosy;
	}

	/**
	 * 用户进入区域
	 * 
	 * @param userId
	 */
	public void addUser(long userId) {
		this.users.put(userId, userId);
	}

	/**
	 * 用户离开区域
	 * 
	 * @param userId
	 */
	public void removeUser(long userId) {
		this.users.remove(userId);
	}

	/**
	 * 此方法创建区域的时候调用一次。。 主要初始化的时候调用
	 * 
	 * @param cell
	 */
	public void addMapCell(MapCell cell) {
		this.cells.put(cell.getId(), cell);
	}

	/**
	 * 广播消息给区块里面的所有玩家
	 * 
	 * @param messageService
	 * @param eventType
	 * @param msg
	 */
	public void broadcastAreaMsg(IChatClientService messageService,
			int eventType, ISendMessage msg) {
		for (long u : users.keySet()) {
			messageService.sendEventMessage(u, eventType, msg);
		}
	}

}
