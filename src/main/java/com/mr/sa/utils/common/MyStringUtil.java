package com.mr.sa.utils.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyStringUtil extends StringUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(MyStringUtil.class);

	public static String inputStream2String(InputStream is) {
		InputStreamReader inputStreamReader = new InputStreamReader(is);
		BufferedReader in = new BufferedReader(inputStreamReader);
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			logger.info("Exception", e);
		} finally {
			try {
				inputStreamReader.close();
			} catch (IOException e) {
				logger.info("Exception", e);
			}
		}
		return buffer.toString();
	}

	/**
	 * "a,1,2," ,是否存在a
	 */
	public static boolean stringContainsString(String checker, String checkee) {
		String[] checkerArray = StringUtils.split(checker, ",");
		for (int i = 0; i < checkerArray.length; i++) {
			if (StringUtils.equals(checkee, checkerArray[i])) {
				return true;
			}
		}
		return false;
	}

	public static long getFirstNumberFromListStr(String string, String remove) {
		String newString = fixNumberListStr(string, remove);
		return Long.valueOf(StringUtils.split(newString, remove)[0]);
	}

	/**
	 * ",1,2," ,得到"2"
	 */
	public static long getLastNumberFromListStr(String string, String remove) {
		String newString = fixNumberListStr(string, remove);
		return Long.valueOf(StringUtils.split(newString, remove)[1]);
	}

	/**
	 * ",1,2,3," ,得到"1,2,3"
	 */
	public static String fixNumberListStr(String string, String remove) {
		if (string.startsWith(remove)) {
			string = string.substring(1);
		}
		if (string.endsWith(remove)) {
			string = StringUtils.substringBeforeLast(string, remove);
		}
		return string;
	}

	/**
	 * [ "10.10.70.121" ] 转10.10.70.121 { "Date": 1345605009000 }转1345605009000
	 */
	public static String fixJsonString(String string) {
		if (StringUtils.contains(string, "[")) {
			return StringUtils.substringBetween(string.trim(), "\"", "\"");
		}
		if (StringUtils.contains(string, "{")) {
			return StringUtils.substringBetween(string.trim(), ":", "}").trim();
		}
		return string;
	}

	/**
	 * ",1,2,3"去除2,得到"1,3"
	 */
	public static String removeFromNumListString(String string, long remove) {
		List aList = stringToLongList(string);
		int index = aList.indexOf(remove);
		aList.remove(index);
		Iterator it = aList.iterator();
		string = StringUtils.join(it, ",");
		return string;
	}

	/**
	 * "aaa,bbb,ccc"转{aaa,bbb,ccc}
	 */
	public static List<String> stringToStringList(String string) {
		List<String> strList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(string.trim(), ",");
		while (st.hasMoreTokens()) {
			String s = st.nextToken().trim();
			strList.add(s);
		}
		return strList;
	}
 

	/**
	 * String[] {1,2,3}转Long[] {1,2,3}
	 */
	public static Long[] stringArrayToLongArray(String[] stringArray) {
		if (stringArray.length > 0) {
			Long[] integerArray = new Long[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				integerArray[i] = Long.valueOf(stringArray[i]);
			}
			return integerArray;
		}
		return null;
	}

	/**
	 * String[] {1,2,3}转Integer[] {1,2,3}
	 */
	public static Integer[] stringArrayToIntegerArray(String[] stringArray) {
		if (stringArray.length > 0) {
			Integer[] integerArray = new Integer[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				integerArray[i] = Integer.valueOf(stringArray[i]);
			}
			return integerArray;
		}
		return null;
	}

	/**
	 * "1,2,3"转{1,2,3}
	 */
	public static List<Integer> stringToIntegerList(String string) {
		List<Integer> strList = new ArrayList<Integer>();
		StringTokenizer st = new StringTokenizer(string.trim(), ",");
		while (st.hasMoreTokens()) {
			try {
				Integer s = Integer.valueOf(st.nextToken().trim());
				strList.add(s);
			} catch (Exception e) {
				logger.info("Exception", e);
				continue;
			}

		}
		return strList;
	}

	/**
	 * ",1,2,3"转{1,2,3}
	 */
	public static List<Long> stringToLongList(String string) {
		List<Long> strList = new ArrayList<Long>();
		StringTokenizer st = new StringTokenizer(string.trim(), ",");
		while (st.hasMoreTokens()) {
			try {
				long s = Long.valueOf(st.nextToken().trim());
				strList.add(s);
			} catch (Exception e) {
				logger.info("Exception", e);
				continue;
			}

		}
		return strList;
	}

	/**
	 * "aaaBbbCcc"转"AAA_BBB_CCC"
	 */
	public static String fieldToColumn(String fieldStr) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < fieldStr.length(); i++) {
			char c = fieldStr.charAt(i);
			if (!Character.isUpperCase(c)) {
				sb.append(Character.toUpperCase(c));
			}
			if (0 == i && Character.isUpperCase(c)) {
				sb.append(c);
			}
			if (0 != i && Character.isUpperCase(c)) {
				sb.append("_" + c);
			}
		}
		return sb.toString();
	}

	/**
	 * "AAA_BBB_CCC"转"aaaBbbCcc"
	 */
	public static String columnTofield(String columnStr) {
		columnStr = StringUtils.upperCase(columnStr);
		StringBuffer sb = new StringBuffer();

		for (int i = 1; i < columnStr.length(); i++) {
			char c = columnStr.charAt(i);
			char b = columnStr.charAt(i - 1);
			if (b != 95) {
				sb.append(Character.toLowerCase(c));
			}
			if (b == 95) {
				sb.append(Character.toUpperCase(c));
			}
		}
		String s = columnStr.substring(0, 1).toLowerCase() + sb.toString();
		return StringUtils.remove(s, "_");
	}

	/**
	 * "aaaBbbCcc"转"AaaBbbCcc"
	 */
	public static String firstUpper(String str) {
		String s = str.substring(0, 1).toUpperCase() + str.substring(1);
		return s;
	}

	/**
	 * "AaaBbbCcc"转"aaaBbbCcc"
	 */
	public static String firstLower(String str) {
		String s = str.substring(0, 1).toLowerCase() + str.substring(1);
		return s;
	}

	/**
	 * " 0. 2 s"转"0.2s"
	 */
	public static String moveBlank(String str) {
		String s = StringUtils.replace(str, " ", "");
		return s;
	}

	/**
	 * "1024k"转"1(m)"
	 */
	public static String k2m(String str) {
		String s = StringUtils.substringBefore(str, "k");
		Long l = Long.valueOf(s) / 1024;
		return l.toString();
	}

	/**
	 * "1024m"转"1(g)"
	 */
	public static String k2g(String str) {
		String s = StringUtils.substringBefore(str, "k");
		Long l = Long.valueOf(s) / 1024 / 1024;
		return l.toString();
	}

	/**
	 * 统计字符串出现的次数
	 */
	public static long countNumber(String string, String find) {
		int chCnt = 0;
		Pattern p = Pattern.compile(find);
		Matcher m = p.matcher(string);
		while (m.find()) {
			chCnt++;
		}
		return chCnt;
	}

	/**
	 * 批量首字母小写
	 */
	public static void batchFirstLower() {
		File f = new File("b.txt");
		try {
			List<String> sl = FileUtils.readLines(f);
			for (int i = 0; i < sl.size(); i++) {
				String s = firstLower(sl.get(i));
				System.out.println(s);
			}
		} catch (IOException e) {
			logger.info("Exception", e);
		}
	}

	/**
	 * aaaa 得到 空格X Number - aaaa长度的空格
	 */
	public static void getTableFormat() {
		System.out.print(fixBlank("设备编码121212", 30));
		System.out.print(fixBlank("设备点位", 30));
		System.out.print(fixBlank("移动", 30));
		System.out.println(fixBlank("联通", 30));

		System.out.print(fixBlank("RF-1001-00057", 30));
		System.out.print(fixBlank("南门 ", 30));
		System.out.print(fixBlank("0", 30));
		System.out.println(fixBlank("0", 30));

		System.out.print(fixBlank("RF-1001-00057212121", 30));
		System.out.print(fixBlank("测试 00035", 30));
		System.out.print(fixBlank("0", 30));
		System.out.print(fixBlank("0", 30));
	}

	public static String fixBlank(String field, int blankNum) {
		int str = field.length();
		int blank = blankNum - str;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < blank; i++) {
			sb.append(" ");
		}
		return field + sb.toString();
	}

	/**
	 * 格式化XML
	 */
	public static String formatXml(String src) {
		String requestXML = src;
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(src));
			requestXML = null;
			XMLWriter writer = null;
			if (document != null) {
				try {
					StringWriter stringWriter = new StringWriter();
					OutputFormat format = new OutputFormat(" ", true);
					writer = new XMLWriter(stringWriter, format);
					writer.write(document);
					writer.flush();
					requestXML = stringWriter.getBuffer().toString();
					// requestXML = requestXML.replaceFirst("<.*?>",
					// "").substring(2);
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							logger.info("Exception", e);
						}
					}
				}
			}
		} catch (DocumentException e) {
			logger.info("Exception", e);
			requestXML = src;
		} catch (IOException e) {
			logger.info("Exception", e);
			requestXML = src;
		}
		// 去掉空行
		requestXML = deleteCRLFOnce(requestXML);
		return requestXML;
	}

	public void deleteBlankLines4() {
		String input = "aa\r\n\t\r\nbb\r\n\r\n\r\ncc\r\naa\n\nbb\n\n\ncc\n";
		System.out.println(input);
		System.out.println("--------------------------------------");
		System.out.println(deleteCRLF(input));
	}

	/***
	 * delete CRLF; delete empty line ;delete blank lines
	 * 
	 * @param input
	 * @return
	 */
	private static String deleteCRLFOnce(String input) {

		return input.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1");

	}

	/**
	 * delete CRLF; delete empty line ;delete blank lines
	 * 
	 * @param input
	 * @return
	 */
	public static String deleteCRLF(String input) {
		input = deleteCRLFOnce(input);
		return deleteCRLFOnce(input);
	}

	/**
	 * 存储用XML
	 */
	public static String cleanXml(String src) {
		return StringUtils.remove(src, "\r\n");
	}

	/**
	 * json字符串的格式化
	 * 
	 * @author peiyuxin
	 * @param json                         需要格式的json串
	 * @param fillStringUnit每一层之前的占位符号比如空格 制表符
	 * @return
	 */
	public static String formatJson(String json, String fillStringUnit) {
		if (json == null || json.trim().length() == 0) {
			return null;
		}

		int fixedLenth = 0;
		ArrayList<String> tokenList = new ArrayList<String>();
		{
			String jsonTemp = json;
			// 预读取
			while (jsonTemp.length() > 0) {
				String token = getToken(jsonTemp);
				jsonTemp = jsonTemp.substring(token.length());
				token = token.trim();
				tokenList.add(token);
			}
		}

		for (int i = 0; i < tokenList.size(); i++) {
			String token = tokenList.get(i);
			int length = token.getBytes().length;
			if (length > fixedLenth && i < tokenList.size() - 1
					&& tokenList.get(i + 1).equals(":")) {
				fixedLenth = length;
			}
		}

		StringBuilder buf = new StringBuilder();
		int count = 0;
		for (int i = 0; i < tokenList.size(); i++) {

			String token = tokenList.get(i);

			if (token.equals(",")) {
				buf.append(token);
				doFill(buf, count, fillStringUnit);
				continue;
			}
			if (token.equals(":")) {
				buf.append(" ").append(token).append(" ");
				continue;
			}
			if (token.equals("{")) {
				String nextToken = tokenList.get(i + 1);
				if (nextToken.equals("}")) {
					i++;
					buf.append("{ }");
				} else {
					count++;
					buf.append(token);
					doFill(buf, count, fillStringUnit);
				}
				continue;
			}
			if (token.equals("}")) {
				count--;
				doFill(buf, count, fillStringUnit);
				buf.append(token);
				continue;
			}
			if (token.equals("[")) {
				String nextToken = tokenList.get(i + 1);
				if (nextToken.equals("]")) {
					i++;
					buf.append("[ ]");
				} else {
					count++;
					buf.append(token);
					doFill(buf, count, fillStringUnit);
				}
				continue;
			}
			if (token.equals("]")) {
				count--;
				doFill(buf, count, fillStringUnit);
				buf.append(token);
				continue;
			}

			buf.append(token);
			// 左对齐
			if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
				int fillLength = fixedLenth - token.getBytes().length;
				if (fillLength > 0) {
					for (int j = 0; j < fillLength; j++) {
						buf.append(" ");
					}
				}
			}
		}
		return buf.toString();
	}

	private static String getToken(String json) {
		StringBuilder buf = new StringBuilder();
		boolean isInYinHao = false;
		while (json.length() > 0) {
			String token = json.substring(0, 1);
			json = json.substring(1);

			if (!isInYinHao && (token.equals(":") || token.equals("{")
					|| token.equals("}") || token.equals("[")
					|| token.equals("]") || token.equals(","))) {
				if (buf.toString().trim().length() == 0) {
					buf.append(token);
				}

				break;
			}

			if (token.equals("\\")) {
				buf.append(token);
				buf.append(json.substring(0, 1));
				json = json.substring(1);
				continue;
			}
			if (token.equals("\"")) {
				buf.append(token);
				if (isInYinHao) {
					break;
				} else {
					isInYinHao = true;
					continue;
				}
			}
			buf.append(token);
		}
		return buf.toString();
	}

	private static void doFill(StringBuilder buf, int count,
			String fillStringUnit) {
		buf.append("\n");
		for (int i = 0; i < count; i++) {
			buf.append(fillStringUnit);
		}
	}

	/**
	 * 批量处理WEB.XML
	 */
	public static void batchHandleWebXml() {
		File f = new File("c.txt");
		try {
			List<String> sl = FileUtils.readLines(f);
			for (int i = 0; i < sl.size(); i++) {
				String s = firstLower(sl.get(i));
				System.out.println("<filter-mapping>");
				System.out.println(
						"<filter-name>operatorCheckerFilter</filter-name>");
				System.out.print("<url-pattern>/");
				System.out.print(s);
				System.out.println("</url-pattern>");
				System.out.println("</filter-mapping>");
			}
		} catch (IOException e) {
			logger.info("Exception", e);
		}
	}

	/**
	 * xxx_xxx变xxxXxx
	 */
	public static void batchHandleColumn() {
		File f = new File("c.txt");
		try {
			List<String> sl = FileUtils.readLines(f);
			for (int i = 0; i < sl.size(); i++) {
				String s = sl.get(i);
				System.out.println(StringUtils.trim(columnTofield(s)));

			}
		} catch (IOException e) {
			logger.info("Exception", e);
		}
	}

	/**
	 * xxx_xxx变xxxXxx
	 */
	public static void batchHandleColumn2() {
		File f1 = new File("f:\\1.txt");
		File f2 = new File("f:\\2.txt");
		try {
			List<String> sl = FileUtils.readLines(f1, "GBK");
			List<String> s2 = FileUtils.readLines(f2, "GBK");
			String s3 = FileUtils.readFileToString(f2, "GBK");
			for (int i = 0; i < sl.size(); i++) {

				String ss1 = sl.get(i);
				System.out.print(i + "*" + ss1);
				for (int j = 0; j < s2.size(); j++) {
					if (!s3.contains(ss1)) {
						System.out.print("\r\n");
						continue;
					}
					String ss2 = s2.get(j);
					if (ss2.contains(ss1)) {
						System.out.println("--" + ss1 + "|" + ss2);
					}

				}

			}
		} catch (IOException e) {
			logger.info("Exception", e);
		}
	}

	public static void batchHandleColumn3() {
		File f3 = new File("f:\\3.txt");
		try {
			List<String> s3 = FileUtils.readLines(f3, "GBK");
			for (int i = 0; i < s3.size(); i++) {
				System.out.println(StringUtils.substringAfter(s3.get(i), "|"));
			}
		} catch (IOException e) {
			logger.info("Exception", e);
		}
	}

	public static void batchCode1() {
		File f3 = new File("f:\\4.txt");
		try {
			List<String> s3 = FileUtils.readLines(f3, "GBK");
			for (int i = 0; i < s3.size(); i++) {
				String s = s3.get(i);
				// params.addElement("Province").addText(orderId);
				String s1 = StringUtils.substringBetween(s, "addElement(\"",
						"\").addText");
				String s2 = StringUtils.substringBetween(s, "addText(", ");");

				// if (StringUtils.isNotBlank(province))
				// {params.addElement("OrderId").addText(province);}
				StringBuffer sb = new StringBuffer();
				sb.append("if (StringUtils.isNotBlank(").append(s2)
						.append(")) {params.addElement(\"").append(s1)
						.append("\").addText(").append(s2).append(");}");
				System.out.println(sb.toString());

			}
		} catch (IOException e) {
			logger.info("Exception", e);
		}
	}

	/**
	 * 判断一个字符串是否是UUID
	 * 
	 */
	public static boolean isUuid(String string) {
		// 8a998be95161e494015161f9ac7b0008转为57f27802-b2c3-4cf5-b169-8ea1e8fafc79
		if (string.length() != 32 || string.contains("|")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return StringUtils.remove(s, "-");
	}

	/**
	 * 格式化属性命名规范
	 */
	public static String formatFiled(String str) {
		// 空格区分大小写,首字母小写
		str = StringUtils.replace(str, " ", "_");
		str = columnTofield(str);
		str = firstLower(str);
		return str;
	}

	/**
	 * 文件内容 a b -> b a
	 */
	public static void revertFileText() {
		File f = new File("f.txt");
		try {
			List<String> strList = FileUtils.readLines(f);
			Collections.reverse(strList);
			FileUtils.writeLines(f, strList);
		} catch (IOException e) {
			logger.info("Exception", e);
		}
	}

	/**
	 * 微信过滤表情
	 */
	public static void cleanWxEmoji(String nick) {
		StringBuilder nicksb = new StringBuilder();
		int l = nick.length();
		for (int i = 0; i < l; i++) {
			char _s = nick.charAt(i);
			if (isEmojiCharacter(_s)) {
				nicksb.append(_s);
			}
		}
		nicksb.toString();
	}

	public static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 检查代码生成
	 */
	public static void checkCodeGen() {
		try {
			List<String> sL = FileUtils.readLines(new File("a.txt"));
			for (String string : sL) {
				// 名称&name;
				if (StringUtils.isNotBlank(string)) {
					String c = StringUtils.substringBetween(string, "// ", "&");
					String e = StringUtils.substringBetween(string, "&", ";");
					String s = "if (StringUtils.isBlank(orderItem.get"
							+ MyStringUtil.firstUpper(e)
							+ "())) { return \"必填项为空: " + c + "   \"; }";
					System.out.println(s);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * D:\workspace\workspace-sa\rcsmms-web\src\main\java\com\mr\rcsmms\
	 * service\tag\CommodityTagService.java ->
	 * com.mr.rcsmms.service.tag.CommodityTagService
	 */
	public static String fixClassName(String path) {
		path = StringUtils.replace(path, "\\", ".");
		path = StringUtils.substringBetween(path, "main.java.", ".java");
		return path;
	}

	/**
	 * 判断字符串存在大写
	 */
	public static boolean isExistUpperCase(String word) {
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (Character.isUpperCase(c)) {
				return true;
			}
		}
		return false;
	}
}
