package com.jetman.bankcode.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringUtil {
	public static String getStackTraceAsString(Exception e) {
		// StringWriter��������ջ��Ϣ
		StringWriter stringWriter = new StringWriter();
		// ���뽫StringWriter��װ��PrintWriter����
		// ������printStackTrace��Ҫ��
		PrintWriter printWriter = new PrintWriter(stringWriter);
		// ��ȡ��ջ��Ϣ
		e.printStackTrace(printWriter);
		// ת����String�������ظ�String
		StringBuffer error = stringWriter.getBuffer();
		return error.toString();
	}

	public static int count(String content, String str) {
		int value = 0, pos = -1;
		while (true) {
			pos = content.indexOf(str, pos + 1);
			if (pos == -1)
				break;
			value++;
		}
		return value;
	}

	public static String toUtf8String(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;
			for (int i = 0; i < s.length(); i++) {
				c = s.charAt(i);
				if (c >= 0 && c <= 255) {
					sb.append(c);
				} else {
					byte[] b;
					b = Character.toString(c).getBytes("utf-8");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String replace(String content, String oldStr, String newStr) {
		StringBuffer newContent = new StringBuffer();
		int len = oldStr.length();
		int i = 0, j = 0;
		while (true) {
			j = content.indexOf(oldStr, i);
			if (j != -1) {
				newContent.append(content.substring(i, j) + newStr);
				i = j + len;
			} else {
				newContent.append(content.substring(i));
				break;
			}
		}
		return new String(newContent);
	}

	public static long ip_string_long(String ip) {
		java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
		long a = Long.parseLong(st.nextToken()) << 24;
		long b = Long.parseLong(st.nextToken()) << 16;
		long c = Long.parseLong(st.nextToken()) << 8;
		long d = Long.parseLong(st.nextToken());
		return a + b + c + d;
	}

	public static String ip_long_string(long ip) {
		return ((ip >> 24) & 0xff) + "." + ((ip >> 16) & 0xff) + "."
				+ ((ip >> 8) & 0xff) + "." + ((ip) & 0xff);
	}

	public static boolean checkIpAddress(String ip) {
		try {
			java.util.StringTokenizer st = new java.util.StringTokenizer(ip,
					".");
			String value = "";
			if (st.hasMoreTokens()) {
				value = st.nextToken();
				if (Integer.parseInt(value) >= 0
						&& Integer.parseInt(value) <= 255) {
					if (st.hasMoreTokens()) {
						value = st.nextToken();
						if (Integer.parseInt(value) >= 0
								&& Integer.parseInt(value) <= 255) {
							if (st.hasMoreTokens()) {
								value = st.nextToken();
								if (Integer.parseInt(value) >= 0
										&& Integer.parseInt(value) <= 255) {
									if (st.hasMoreTokens()) {
										value = st.nextToken();
										if (Integer.parseInt(value) >= 0
												&& Integer.parseInt(value) <= 255) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isMobileNumber(String s) {// �ж��ǲ����ֻ���
		if (s.length() == 11) {
			if (s.startsWith("13")) {
				if (isNumberString(s)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isLetterString(String s) {// ����ַ������ÿ���ַ��Ƿ�����ĸ
		return checkString(s,
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	public static boolean isNumberString(String s) {// ����ַ������ÿ���ַ��Ƿ�������
		return checkString(s, "1234567890");
	}

	public static boolean isLetterAndNumberString(String s) {// ����ַ������ÿ���ַ��Ƿ�����ĸ������
		return checkString(s,
				"1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	public static boolean checkString(String source, String check) {// ����ַ���
		for (int i = 0; i < source.length(); i++) {
			if (check.indexOf(source.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(String s) {// �ж��ַ����Ƿ���null��""
		if (s == null || "".equals(s)) {
			return true;
		}
		s = s.trim();
		if ("".equals(s))
			return true;
		return false;
	}

	public static boolean isNotEmpty(String s) {// �ж��ַ����Ƿ���null��""
		return (!isEmpty(s));
	}

	public static String substring(String s, int p1, int p2) {
		if (isEmpty(s))
			return "";
		int len = s.length();
		if (p1 >= len)
			return "";
		if (p2 > len)
			p2 = len;
		s = s.substring(p1, p2);
		if (isNotEmpty(s))
			s += "����";
		return s;
	}

	public static boolean ifParamInArray(String[] value, String param) {
		if (param == null)
			return false;
		if (value == null)
			return false;
		for (int i = 0; i < value.length; i++) {
			if (param.equals(value[i])) {
				return true;
			}
		}
		return false;
	}

	public static String firstLetterToUpper(String value) {// �ַ����е�һ���ַ�ת��д
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	public static String firstLetterToLower(String value) {// �ַ����е�һ���ַ�ת��д
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	public static String[] split(String str, String regex) {
		if (str == null)
			return new String[0];
		String[] value = new String[count(str, regex) + 1];
		java.util.StringTokenizer st = new java.util.StringTokenizer(str, regex);
		int i = 0;
		while (st.hasMoreTokens()) {
			value[i++] = st.nextToken();
		}
		return value;
	}

	public static String[] objectArrayToStringArray(Object[] objects) {
		String[] value = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			value[i] = (String) objects[i];
		}
		return value;
	}

	/**
	 * ��yyyy-mm-dd��ʽ���ַ���ת����Long
	 * 
	 * @param str
	 * @return
	 */
	public static Long strToLongStart(String str) {
		if (str != null && str.matches("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}")) {
			StringBuffer value = new StringBuffer();
			value.append(str.substring(0, 4));
			value.append(str.substring(5, 7));
			value.append(str.substring(8, 10));
			value.append("00");
			return new Long(value.toString());
		} else {
			return null;
		}

	}
	
	/**
	 * yyyy-MM-dd��ʽ��� yyyy-MM-dd HH:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static String strToDate(String str, String suffix) {
		if (str != null && str.matches("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}")) {
			return str + suffix;
		} else {
			return null;
		}
	}

	/**
	 * ��yyyy-mm-dd��ʽ���ַ���ת����Long
	 * 
	 * @param str
	 * @return
	 */
	public static Long strToLongEnd(String str) {
		if (str != null && str.matches("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}")) {
			StringBuffer value = new StringBuffer();
			value.append(str.substring(0, 4));
			value.append(str.substring(5, 7));
			value.append(str.substring(8, 10));
			value.append("23");
			return new Long(value.toString());
		} else {
			return null;
		}
	}

	/**
	 * ������yyyyMMddHH��ʽ����ֵ ת����yyyy-MM-dd HH
	 * 
	 * @param date
	 * @return
	 */
	public static String longToStr(Long date) {
		String time = "";
		// ���ڱ���ʱ��ʱ��
		if (date != null && date > 2013061900) {
			StringBuffer str = new StringBuffer();
			str.append(date/1000000);
			str.append("��");
			date = date%1000000;
			
			str.append(date/10000);
			str.append("��");
			date = date%10000;
			
			str.append(date/100);
			str.append("��");
			date = date%100;
			str.append(date);
			str.append("ʱ");
			
			time = str.toString();
		}
		return time;
	}
	
	
	/**
	 * �̻�״̬�ж� �ȷ�˵�ڶ�λ��ע�� chkState(0,2) ��û��ע�� chkState(2,2)ע����
	 * 
	 * @param state
	 *            ���ݿ�洢��״̬(ʮ����)
	 * @param bit
	 *            ��Ҫ�жϵ�λ��
	 * @return true ��ʾ������״̬
	 */
	public static boolean chkState(int state, int bit) {
		String state_bit = Integer.toBinaryString(state);

		// �ж�λ��
		if (state_bit.length() >= bit) {
			// �Ѷ����ƴ����� ��Ϊjava�����λ�����
			state_bit = new StringBuffer(state_bit).reverse().toString();
			if (state_bit.length() > bit && state_bit.charAt(bit) == '1') {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	

	/**
	 * �̻�״̬���� ָ��λ������
	 * 
	 * @param stateԭ״̬
	 *            (ʮ����)
	 * @param bitλ��
	 * @param flag
	 *            ֻ��0 1���� 0ȡ��ָ��λ��ֵ 1 ����ָ��λ�õ�ֵ
	 * @return ���ؼ�����״̬(ʮ������)
	 */
	public static int setState(int state, int bit, int flag) {
		if (flag != 0 && flag != 1) {
			return state;
		}
		String state_bit = Integer.toBinaryString(state);

		// �Ѷ����ƴ����� ��Ϊjava�����λ�����
		state_bit = new StringBuffer(state_bit).reverse().toString();

		if (state_bit.length() < bit) {
			int tmp = bit - state_bit.length();
			StringBuffer str = new StringBuffer();
			str.append(state_bit);
			for (int i = 0; i < tmp; i++) {
				str.append("0");
			}
			state_bit = str.toString();
		}

		// �滻��bitΪ1
		StringBuffer last = new StringBuffer();
		last.append(state_bit.substring(0, bit));
		last.append(flag);

		bit++;
		if (bit < state_bit.length()) {
			last.append(state_bit.substring(bit, state_bit.length()));
		}

		String lastState = last.reverse().toString();
		BigInteger last_state = new BigInteger(lastState, 2);
		return last_state.intValue();
	}
	
	/**
	 * ȡ�������λ���
	 * 
	 * @return
	 */
	public static String getAgentId() {

		int n_1 = new Random().nextInt(10);
		if (n_1 == 0 || n_1 == 4) {
			n_1 = n_1 + 1;
		}
		int n_2 = new Random().nextInt(10);
		if (n_2 == 4) {
			n_2 = n_2 - 1;
		}
		int n_3 = new Random().nextInt(10);
		if (n_3 == 4) {
			n_3 = n_3 + 1;
		}
		int n_4 = new Random().nextInt(10);
		if (n_4 == 4) {
			n_4 = n_4 - 1;
		}
		int n_5 = new Random().nextInt(10);
		if (n_5 == 4) {
			n_5 = n_5 + 1;
		}

		StringBuffer id = new StringBuffer();
		id.append(n_1).append(n_2).append(n_3).append(n_4).append(n_5);
		return id.substring(0, 5);
	}
	
	//��ȡtxt�ļ���list  ����Ϊ��λ
	public static List<String> readTxtFileToList(String filePath){
		List<String> list = new ArrayList<String>();
		try {
           // String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file)
                //,encoding
                );//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	list.add(lineTxt);
                }
                read.close();
		    }else{
		        System.out.println("�Ҳ���ָ�����ļ�");
		    }
	    } catch (Exception e) {
	        System.out.println("��ȡ�ļ����ݳ���");
	        e.printStackTrace();
	    }
	    return list;
	}

	public static String listToString(List<String> list) {
		String ins = "";
		if(list==null) {
			return ins;
		}
		for (String s : list) {
			ins +="'" + s + "',";
		}
		if(ins.length()>0) {
			ins = ins.substring(0, ins.length()-1);
		}else {
			ins = "''";
		}
		return ins;
	}
	
	public static String listToStringNoDH(List<String> list) {
		String ins = "";
		if(list==null) {
			return ins;
		}
		for (String s : list) {
			ins +="" + s + ",";
		}
		if(ins.length()>0) {
			ins = ins.substring(0, ins.length()-1);
		}else {
			ins = "";
		}
		return ins;
	}
}
