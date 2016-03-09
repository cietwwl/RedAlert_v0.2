package com.youxigu.dynasty2.util;

import java.util.ArrayList;
import java.util.List;

public interface IndexEnum {
	public int getIndex();

	public static class IndexEnumUtil<E> {
		public static <E extends IndexEnum> List<E> toIndexes(E[] enums) {
			int maxIndex = 0;
			for (E e : enums) {
				if (e.getIndex() < 0) {
					throw new IllegalArgumentException(
							"enum index < 0 index = " + e.getIndex());
				}
				if (maxIndex < e.getIndex()) {
					maxIndex = e.getIndex();
				}
			}
			if (maxIndex >= 500) {
				// 最多设置500个元素
				throw new IllegalArgumentException("enum index >500 index="
						+ maxIndex);
			}
			List<E> result = new ArrayList<E>(maxIndex);
			for (int i = 0; i <= maxIndex; i++) {
				result.add(null);
			}

			for (E e : enums) {
				if (result.get(e.getIndex()) != null) {
					throw new IllegalArgumentException(
							"enum index has same index Enum index="
									+ e.getIndex());
				}
				result.add(e.getIndex(), e);
			}
			return result;

		}
	}
}
