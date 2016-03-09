package com.youxigu.dynasty2.armyout.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.armyout.dao.IMilitarySituationDao;
import com.youxigu.dynasty2.armyout.domain.qingbao.BeAssembled;
import com.youxigu.dynasty2.armyout.domain.qingbao.BeAttacked;
import com.youxigu.dynasty2.armyout.domain.qingbao.BeDetected;
import com.youxigu.dynasty2.armyout.domain.qingbao.DetectAimResources;
import com.youxigu.dynasty2.armyout.domain.qingbao.General;
import com.youxigu.dynasty2.armyout.domain.qingbao.MilitarySituation;
import com.youxigu.dynasty2.armyout.domain.qingbao.MyDetect;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.MiSiType;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.PBGeneral;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.PBMiSi;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.PBResources;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91003Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91005Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91007Define;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.Response91009Define;
import com.youxigu.dynasty2.armyout.proto.SendMiSiMsg;
import com.youxigu.dynasty2.armyout.service.IMilitarySituationService;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.util.BaseException;

public class MilitarySituationService implements IMilitarySituationService {
	private static final int misiPageSize = 10; // 军情分页的每页大小
	
	private static final int maxCount = 500;// 最多保留500个情报

	private static final String beDetectedName = "您的%s被侦查";
	private static final String beDetectedContent = "%s侦查了您的%s";

	private static final String beAssembledName = "您的%s被集结中";
	private static final String beAssembledContent = "%s正在对您的%s发起集结";

	private static final String beAttackedName = "您的%s被进攻";
	private static final String beAttackedContent = "%s正在进攻您的%s";

	private static final String myDetectName = "侦查";
	private static final String myDetectContent = "%s 难民：%d 守军：%d";

	private IMilitarySituationDao misiDao;
	private IChatClientService messageService;

	public void setMisiDao(IMilitarySituationDao misiDao) {
		this.misiDao = misiDao;
	}
	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	/**
	 * 锁
	 * 
	 * @param userId
	 */
	private void lockMilitarySituation(long userId) {
		try {
			MemcachedManager.lock("Military_Situation_" + userId);
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	@Override
	public void doAddBeDetected(long userId, BeDetected beDetected) {
		// 锁
		this.lockMilitarySituation(userId);
		// 构建情报对象
		MilitarySituation misi = new MilitarySituation();
		misi.setMiSiType(MiSiType.BeDetected_VALUE);
		misi.setName(String.format(beDetectedName, beDetected.getAimName()));
		String content = String.format(beDetectedContent,
				beDetected.getOriginCommanderName(), beDetected.getAimName());
		misi.setContent(content);
		// 相信情报存储的是pb中定义的相信情报对象，这样取出返回即可
		Response91003Define.Builder builder = Response91003Define
				.newBuilder();
		Date time = new Date();
		builder.setTime(time.getTime());
		builder.setAimIcon(beDetected.getAimIcon());
		builder.setAimName(beDetected.getAimName());
		builder.setAimPointX(beDetected.getAimPointX());
		builder.setAimPointY(beDetected.getAimPointY());
		builder.setOriginIcon(beDetected.getOriginIcon());
		builder.setOriginCommanderName(beDetected.getOriginCommanderName());
		builder.setOriginBasePointX(beDetected.getOriginBasePointX());
		builder.setOriginBasePointY(beDetected.getOriginBasePointY());
		builder.setContent(content);

		misi.setMisiDetail(builder.build().toByteArray());
		misi.setUserId(userId);
		misi.setHasView((byte) 0);
		misi.setTime(time);
		// 插入数据库
		misiDao.insertMilitarySituation(misi);
		// 推送新情报
		messageService.sendEventMessage(userId, EventMessage.TYPE_NEW_MISI,
				new SendMiSiMsg(miSiToPbMiSi(misi)));
	}

	@Override
	public void doAddBeAssembled(long userId, BeAssembled beAssembled) {
		// 锁
		this.lockMilitarySituation(userId);
		// 构建情报对象
		MilitarySituation misi = new MilitarySituation();
		misi.setMiSiType(MiSiType.BeAssembled_VALUE);
		misi.setName(String.format(beAssembledName, beAssembled.getAimName()));
		String content = String.format(beAssembledContent,
				beAssembled.getOriginCommanderName(), beAssembled.getAimName());
		misi.setContent(content);

		Response91005Define.Builder builder = Response91005Define
				.newBuilder();
		Date time = new Date();
		builder.setTime(time.getTime());
		builder.setAimIcon(beAssembled.getAimIcon());
		builder.setAimName(beAssembled.getAimName());
		builder.setAimPointX(beAssembled.getAimPointX());
		builder.setAimPointY(beAssembled.getAimPointY());
		builder.setOriginUnionName(beAssembled.getOriginUnionName());
		builder.setOriginCommanderName(beAssembled.getOriginCommanderName());
		builder.setEndTime(beAssembled.getEndTimeDate().getTime());
		List<String> icons = beAssembled.getOriginCommanderIcons();
		for (String icon : icons) {
			builder.addOriginCommanderIcons(icon);
		}
		builder.setOriginBasePointX(beAssembled.getOriginBasePointX());
		builder.setOriginBasePointY(beAssembled.getOriginBasePointY());

		misi.setMisiDetail(builder.build().toByteArray());
		misi.setUserId(userId);
		misi.setHasView((byte) 0);
		misi.setTime(time);
		// 插入数据库
		misiDao.insertMilitarySituation(misi);
		// 推送新情报
		messageService.sendEventMessage(userId, EventMessage.TYPE_NEW_MISI,
				new SendMiSiMsg(miSiToPbMiSi(misi)));
	}

	@Override
	public void doAddBeAttacked(long userId, BeAttacked beAttacked) {
		// 锁
		this.lockMilitarySituation(userId);
		// 构建情报对象
		MilitarySituation misi = new MilitarySituation();
		misi.setMiSiType(MiSiType.BeAttacked_VALUE);
		misi.setName(String.format(beAttackedName, beAttacked.getAimName()));
		String content = String.format(beAttackedContent,
				beAttacked.getOriginCommanderName(), beAttacked.getAimName());
		misi.setContent(content);

		Response91007Define.Builder builder = Response91007Define
				.newBuilder();
		Date time = new Date();
		builder.setTime(time.getTime());
		builder.setAimIcon(beAttacked.getAimIcon());
		builder.setAimName(beAttacked.getAimName());
		builder.setAimPointX(beAttacked.getAimPointX());
		builder.setAimPointY(beAttacked.getAimPointY());
		builder.setOriginUnionName(beAttacked.getOriginUnionName());
		builder.setOriginCommanderName(beAttacked.getOriginCommanderName());
		builder.setUnionAttackPower(beAttacked.getUnionAttackPower());
		List<General> generals = beAttacked.getOriginGenerals();
		for (General general : generals) {
			PBGeneral.Builder pBuilder = PBGeneral.newBuilder();
			pBuilder.setIcon(general.getIcon());
			pBuilder.setLevel(general.getLevel());
			pBuilder.setDurableValue(general.getDurableValue());
			builder.addOriginGenerals(pBuilder.build());
		}
		builder.setArriveTime(beAttacked.getArriveTime().getTime());
		builder.setCommanderBasePointX(beAttacked.getCommanderBasePointX());
		builder.setCommanderBasePointY(beAttacked.getCommanderBasePointY());

		misi.setMisiDetail(builder.build().toByteArray());
		misi.setUserId(userId);
		misi.setHasView((byte) 0);
		misi.setTime(time);
		// 插入数据库
		misiDao.insertMilitarySituation(misi);
		// 推送新情报
		messageService.sendEventMessage(userId, EventMessage.TYPE_NEW_MISI,
				new SendMiSiMsg(miSiToPbMiSi(misi)));
	}

	@Override
	public void doAddMyDetect(long userId, MyDetect myDetect) {
		// 锁
		this.lockMilitarySituation(userId);
		// 构建情报对象
		MilitarySituation misi = new MilitarySituation();
		misi.setMiSiType(MiSiType.MyDetect_VALUE);
		misi.setName(String.format(myDetectName));
		String content = String.format(myDetectContent, myDetect
				.getDefenderCommanderName(), myDetect.getAimResource()
				.getRefugee(), myDetect.getDefenderCount());
		misi.setContent(content);

		Response91009Define.Builder builder = Response91009Define
				.newBuilder();
		Date time = new Date();
		builder.setTime(time.getTime());
		builder.setAimIcon(myDetect.getAimIcon());
		builder.setAimName(myDetect.getAimName());
		builder.setAimPointX(myDetect.getAimPointX());
		builder.setAimPointY(myDetect.getAimPointY());
		builder.setDefenderUnionName(myDetect.getDefenderUnionName());
		builder.setDefenderCommanderName(myDetect.getDefenderCommanderName());
		builder.setUnionAttackPower(myDetect.getDefenderAttackPower());
		List<General> generals = myDetect.getDefenderGenerals();
		for (General general : generals) {
			PBGeneral.Builder pBuilder = PBGeneral.newBuilder();
			pBuilder.setIcon(general.getIcon());
			pBuilder.setLevel(general.getLevel());
			pBuilder.setDurableValue(general.getDurableValue());
			builder.addDefenderGenerals(pBuilder.build());
		}
		DetectAimResources resources = myDetect.getAimResource();
		PBResources.Builder pBuilder = PBResources.newBuilder();
		pBuilder.setMoney(resources.getMoney());
		pBuilder.setOil(resources.getOil());
		pBuilder.setAxis(resources.getAxis());
		pBuilder.setSpareParts(resources.getSpareParts());
		pBuilder.setIron(resources.getIron());
		pBuilder.setRefugee(resources.getRefugee());
		builder.setAimResources(pBuilder.build());

		misi.setMisiDetail(builder.build().toByteArray());
		misi.setUserId(userId);
		misi.setHasView((byte) 0);
		misi.setTime(time);
		// 插入数据库
		misiDao.insertMilitarySituation(misi);
		// 推送新情报
		messageService.sendEventMessage(userId, EventMessage.TYPE_NEW_MISI,
				new SendMiSiMsg(miSiToPbMiSi(misi)));
	}
	
	@Override
	public List<MilitarySituation> getMilitarySituationList(long userId,
			int pageNum) {
		List<MilitarySituation> misiList = misiDao
				.getMilitarySituationListByUserId(userId);
		int size = misiList.size();
		if (size > maxCount) {
			// 删除多余的情报
			int deId = misiList.get(maxCount - 1).getId();
			misiDao.deleteMilitarySituations(deId, userId);
		}
		if(pageNum < 0){
			throw new BaseException("访问参数错误");
		}
		
		int beginIndex = pageNum * misiPageSize;
		if (beginIndex >= misiList.size() || beginIndex >= maxCount) {
			throw new BaseException("没有更多的情报");
		}
		int endIndex = (pageNum+1) * misiPageSize;
		if (endIndex > maxCount) {
			endIndex = maxCount;
		}
		if (endIndex > misiList.size()) {
			endIndex = misiList.size();
		}
		return misiList.subList(beginIndex, endIndex);
	}

	@Override
	public MilitarySituation getMilitarySituation(int id) {
		return misiDao.getMilitarySituationById(id);
	}

	@Override
	public void markHasView(int id) {
		misiDao.setHasView(id, true);
	}

	private PBMiSi miSiToPbMiSi(MilitarySituation misi) {
		PBMiSi.Builder miSi = PBMiSi.newBuilder();
		miSi.setId(misi.getId());
		miSi.setHasView(misi.hasView() ? 1 : 0);
		miSi.setType(MiSiType.valueOf(misi.getMiSiType()));
		miSi.setName(misi.getName());
		miSi.setContent(misi.getContent());
		return miSi.build();
	}
}
