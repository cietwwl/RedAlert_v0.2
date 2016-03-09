package com.youxigu.dynasty2.map.service.impl;

import java.util.List;

import org.shardbatis.spring.jdbc.transaction.DefaultTransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.map.domain.MapCell;
import com.youxigu.dynasty2.map.service.IAsyUpdateMapService;

/**
 * 坐标点更新事务回滚机制
 * 
 * @author LK
 * @date 2016年2月18日
 */
public class MapCellTransactionListener extends DefaultTransactionListener {
	private static final Logger log = LoggerFactory
			.getLogger(MapCellTransactionListener.class);

	@Override
	public void doBeginAfter(Object transaction,
			TransactionDefinition definition) {
		if (!ThreadLocalMapCellCache.isInTrans()) {
			ThreadLocalMapCellCache.clean();
		}
		ThreadLocalMapCellCache.setInTrans(true);
	}

	@Override
	public void doCommitAfter(DefaultTransactionStatus status) {
		try {
			// 先更新缓存
			List<Object> asyncDBDatas = ThreadLocalMapCellCache.commit();

			// 异步更新
			IAsyUpdateMapService asyUpdateMapService = (IAsyUpdateMapService) ServiceLocator
					.getSpringBean("asyUpdateMapService");
			if (asyncDBDatas != null && asyUpdateMapService != null) {
				asyUpdateMapService.update(asyncDBDatas);
			}
		} catch (Exception e) {
			// 吃掉所有异常
			e.printStackTrace();
		} finally {
			unlockMapCellsInCache();
			ThreadLocalMapCellCache.clean();
		}
	}

	@Override
	public void doRollbackAfter(DefaultTransactionStatus status) {
		unlockMapCellsInCache();
		ThreadLocalMapCellCache.clean();
	}

	/**
	 * mapcell释放锁
	 */
	private void unlockMapCellsInCache() {
		List<MapCell> lockedCells = ThreadLocalMapCellCache.getLockedMapCells();
		if (lockedCells != null && lockedCells.size() > 0) {
			for (MapCell mapCell : lockedCells) {
				// 吃掉异常，避免一个MapCell释放异常影响到其它MapCell
				try {
					mapCell.getLock().unlock();
					if (log.isDebugEnabled()) {
						log.debug("MapCell({},{}) unlocked", mapCell.getPosX(),
								mapCell.getPosY());
					}
				} catch (Exception e) {
					log.error("unlock MapCell({" + mapCell.getPosX() + "},{"
							+ mapCell.getPosY() + "}) failed", e);
				}
			}
		}
	}

}
