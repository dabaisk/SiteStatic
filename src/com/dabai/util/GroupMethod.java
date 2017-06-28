package com.dabai.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * GroupMethod�� ����ƥ�䲢ץȥ Html��������Ҫ������
 * @author SoFlash - ����԰  http://www.cnblogs.com/longwu
 */
public class GroupMethod {
	// ����2���ַ������� һ����pattern(����ʹ�õ�����) ��һ��matcher��htmlԴ����
	public String regularGroup(String pattern, String matcher) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(matcher);
		if (m.find()) { // �������
			return m.group();// ���ز��������
		} else {
			return ""; // ���򷵻�һ����ֵ
		}
	}
}
