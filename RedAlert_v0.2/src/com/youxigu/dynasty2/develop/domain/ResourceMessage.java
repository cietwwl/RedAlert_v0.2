package com.youxigu.dynasty2.develop.domain;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;

/**
 * 推城池资源变化事件
 * @author Dagangzi
 *
 */
public class ResourceMessage implements ISendMessage, Serializable {
    private static final long serialVersionUID = 7629443012007824416L;
	private long goldNum;//金矿数(资源当前数量)
	private long ironNum;//铁矿数
	private long oilNum;//油矿数量
	private long uraniumNum;//铀矿数量
	private long casGold;//建筑内金矿数(资源当前数量)
	private long casIron;//建筑内铁矿数
	private long casOil;//建筑内油矿数量
	private long casUranium;//建筑内铀矿数量

	public ResourceMessage() {
	}

	public ResourceMessage(CastleResource castleResource) {
		this.goldNum = castleResource.getGoldNum();
		this.ironNum = castleResource.getIronNum();
		this.oilNum = castleResource.getOilNum();
		this.uraniumNum = castleResource.getUranium();
		this.casGold = castleResource.getCasGoldNum();
		this.casIron = castleResource.getCasIronNum();
		this.casOil = castleResource.getCasOilNum();
		this.casUranium = castleResource.getCasUranium();
	}

	public long getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(long goldNum) {
		this.goldNum = goldNum;
	}

	public long getIronNum() {
		return ironNum;
	}

	public void setIronNum(long ironNum) {
		this.ironNum = ironNum;
	}

	public long getOilNum() {
		return oilNum;
	}

	public void setOilNum(long oilNum) {
		this.oilNum = oilNum;
	}

	public long getUraniumNum() {
		return uraniumNum;
	}

	public void setUraniumNum(long uraniumNum) {
		this.uraniumNum = uraniumNum;
	}

	public long getCasGold() {
		return casGold;
	}

	public void setCasGold(long casGold) {
		this.casGold = casGold;
	}

	public long getCasIron() {
		return casIron;
	}

	public void setCasIron(long casIron) {
		this.casIron = casIron;
	}

	public long getCasOil() {
		return casOil;
	}

	public void setCasOil(long casOil) {
		this.casOil = casOil;
	}

	public long getCasUranium() {
		return casUranium;
	}

	public void setCasUranium(long casUranium) {
		this.casUranium = casUranium;
	}

	@Override
	public Message build() {
		// 必须包含responseHead
		DevelopMsg.ResourceSendEvent.Builder sEvent = DevelopMsg.ResourceSendEvent.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(EventMessage.TYPE_RESOURCE_CHANGED);
		headBr.setRequestCmd(EventMessage.TYPE_RESOURCE_CHANGED);
		sEvent.setResponseHead(headBr.build());

		sEvent.setGoldNum(this.goldNum);// 金矿数
		sEvent.setIronNum(this.ironNum);// 铁矿数
		sEvent.setOilNum(this.oilNum);// 油矿数量
		sEvent.setUraniumNum(this.uraniumNum);// 铀矿数量

		sEvent.setCasGold(this.casGold);// 建筑内金矿数
		sEvent.setCasIron(this.casIron);// 建筑内铁矿数
		sEvent.setCasOil(this.casOil);// 建筑内油矿数量
		sEvent.setCasUranium(this.casUranium);// 建筑内铀矿数量
		return sEvent.build();
	}
}
