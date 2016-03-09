package com.youxigu.dynasty2.armyout.web;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.youxigu.dynasty2.armyout.domain.qingbao.MilitarySituation;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.MiSiType;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.PBMiSi;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Request91001Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Request91003Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91001Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91003Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91005Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91007Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91009Define;
import com.youxigu.dynasty2.armyout.service.IMilitarySituationService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class MilitarySituationAction extends BaseAction {
	private IMilitarySituationService misiService;
	

	public void setMisiService(IMilitarySituationService misiService) {
		this.misiService = misiService;
	}

	/**
	 * 获取军情列表-91001
	 * */
	public Object getMisiList(Object obj , Response context){
		Request91001Define getMiSi = (Request91001Define)obj;
		UserSession us = super.getUserSession(context);
		long userId = us.getUserId();
		// 获取军情列表
		List<MilitarySituation> misiList = misiService.getMilitarySituationList(userId, 
				getMiSi.getPageNum());
		for (MilitarySituation militarySituation : misiList) {
			System.err.println(militarySituation);
		}
		// 构建返回的军情列表
		Response91001Define.Builder ret = Response91001Define.newBuilder();
		ret.setResponseHead(super.getResponseHead(getMiSi.getCmd()));
		for (MilitarySituation militarySituation : misiList) {
			PBMiSi.Builder miSi = PBMiSi.newBuilder();
			miSi.setId(militarySituation.getId());
			miSi.setHasView(militarySituation.hasView()?1:0);
			miSi.setType(MiSiType.valueOf(militarySituation.getMiSiType()));
			miSi.setName(militarySituation.getName());
			miSi.setContent(militarySituation.getContent());
			ret.addMiSis(miSi.build());
		}
		return ret.build();
	}
	/**
	 * 获取被侦查军情详情-91003
	 * */
	public Object getMisiDetailBeDetected(Object obj , Response context){
		Request91003Define getMiSiDetails = (Request91003Define)obj;
		return getMiSiDetail(getMiSiDetails.getId(),getMiSiDetails.getCmd(),MiSiType.BeDetected);
	}
	/**
	 * 获取被集结军情详情-91005
	 * */
	public Object getMisiDetailBeAssembled(Object obj , Response context){
		Request91003Define getMiSiDetails = (Request91003Define)obj;
		return getMiSiDetail(getMiSiDetails.getId(),getMiSiDetails.getCmd(),MiSiType.BeAssembled);
	}
	/**
	 * 获取被进攻军情详情-91007
	 * */
	public Object getMisiDetailBeAttacked(Object obj , Response context){
		Request91003Define getMiSiDetails = (Request91003Define)obj;
		return getMiSiDetail(getMiSiDetails.getId(),getMiSiDetails.getCmd(),MiSiType.BeAttacked);
	}
	/**
	 * 获取侦查军情详情-91009
	 * */
	public Object getMisiDetailMyDetect(Object obj , Response context){
		Request91003Define getMiSiDetails = (Request91003Define)obj;
		return getMiSiDetail(getMiSiDetails.getId(),getMiSiDetails.getCmd(),MiSiType.MyDetect);
	}
	/**
	 * 根据id获取军情详情
	 * @param id
	 * @return
	 */
	private Object getMiSiDetail(int id,int cmd,MiSiType miSiType){
		MilitarySituation misi = misiService.getMilitarySituation(id);
		if(misi == null){
			throw new BaseException("情报不存在,id = "+id);
		}
		misiService.markHasView(misi.getId());// 标记为已查看.
		
		MiSiType type = MiSiType.valueOf(misi.getMiSiType());
		if(type != miSiType){
			throw new BaseException("前端传递参数出错，类型错误！");
		}
		Object ret = null;
		try {
			switch (type) {
			case BeDetected: // 被侦查
				ret = Response91003Define.parseFrom(misi.getMisiDetail())
						.toBuilder().setResponseHead(super.getResponseHead(cmd)).build();
				break;
			case BeAssembled: // 被集结
				ret = Response91005Define.parseFrom(misi.getMisiDetail())
						.toBuilder().setResponseHead(super.getResponseHead(cmd)).build();
				break;
			case BeAttacked: // 被进攻
				ret = Response91007Define.parseFrom(misi.getMisiDetail())
						.toBuilder().setResponseHead(super.getResponseHead(cmd)).build();
				break;
			case MyDetect: // 我的侦查
				ret = Response91009Define.parseFrom(misi.getMisiDetail())
						.toBuilder().setResponseHead(super.getResponseHead(cmd)).build();
				break;

			default:
				break;
			}
		} catch (InvalidProtocolBufferException e) {
			// 程序正确，数据库不更改，这个不可能发生
			throw new BaseException("详细军情不存在");
		}
		if(ret == null){
			// 程序正确，数据库不更改，这个不可能发生
			throw new BaseException("详细军情不存在，军情类型不存在");
		}
		
		return ret;
	}
}
