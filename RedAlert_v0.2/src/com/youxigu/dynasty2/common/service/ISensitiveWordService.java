package com.youxigu.dynasty2.common.service;

/**
 * 敏感词汇管理
 * 
 * @author Administrator
 * 
 */
public interface ISensitiveWordService {

	/**
	 * 敏感词汇匹配验证
	 * @param word
	 * @return
	 */
	boolean match(String word);

	/**
	 * 敏感词替代：
	 * @param word
	 * @param replace
	 * @return
	 */
	String replace(String word, String replace);
	
	String replace(String word, String replace,boolean removeSpace);	
}
