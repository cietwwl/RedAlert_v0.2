package com.youxigu.dynasty2.common.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.youxigu.dynasty2.common.service.ISensitiveWordService;

/**
 * 敏感词汇检查service
 * 
 * 
 * @author Administrator
 * 
 */
// TODO:是否改成使用lucence
public class SensitiveWordService implements ISensitiveWordService {

	private String[] sensitiveWords = null;
	private String replaceWord = "**";

	private String sensitiveWordsConf = "/conf/sensitive.properties";

	public void setReplaceWord(String replaceWord) {
		this.replaceWord = replaceWord;
	}

	public void setSensitiveWords(String sensitiveWords) {
		if (sensitiveWords != null)
			this.sensitiveWords = StringUtils.split(sensitiveWords,",");
	}

	public void setSensitiveWordsConf(String sensitiveWordsConf) {
		this.sensitiveWordsConf = sensitiveWordsConf;

	}

	public void init() {
		if (this.sensitiveWordsConf != null) {
			Properties p = new Properties();
			InputStream in = null;
			try {
				in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(sensitiveWordsConf);
				if (in != null) {
					p.load(new InputStreamReader(in, "utf-8"));
					this.replaceWord = p.getProperty("replaceWord", "*");
					this.setSensitiveWords(p.getProperty("sensitive"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e) {
				}
			}
			;
		}
	}

	@Override
	public boolean match(String word) {
		if (sensitiveWords == null)
			return false;
		String ret = word.replaceAll("\\s+", ""); // 去掉空白
		if (word.length() != ret.length())
			return false;

		boolean match = _match(ret);
		if (!match) {
			ret = ret.toLowerCase();
			match = _match(ret);
		}

		if (!match) {
			ret = ret.toUpperCase();
			match = _match(ret);
		}

		return match;

	}

	private boolean _match(String word) {
		int num = sensitiveWords.length;
		for (int i = 0; i < num; ++i) {
			try {
				if (word.indexOf(sensitiveWords[i]) >= 0) {
					return true;
				}

			} catch (Exception e) {
			}
		}
		return false;
	}

	@Override
	public String replace(String word, String replace, boolean removeSpace) {
		if (sensitiveWords == null)
			return word;

		if (removeSpace) {
			word = word.replaceAll("\\s+", ""); // 去掉空白
		}

		if (replace == null)
			replace = replaceWord;
		int num = sensitiveWords.length;
		for (int i = 0; i < num; ++i) {
			try {
				if (word.indexOf(sensitiveWords[i]) >= 0) {
					word = StringUtils.replace(word, sensitiveWords[i],
							replaceWord);
				}
			} catch (Exception e) {
			}
		}

		// TODO:大小写判断

		return word;
	}

	@Override
	public String replace(String word, String replace) {
		return replace(word,replace,true);
	}

}
