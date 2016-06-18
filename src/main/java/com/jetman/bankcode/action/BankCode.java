package com.jetman.bankcode.action;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.jdom.JDOMException;

import com.jetman.bankcode.util.CSVUtil;
import com.jetman.bankcode.util.HttpUtil;
import com.jetman.bankcode.util.StringUtil;
import com.jetman.bankcode.util.XmlUtil;



public class BankCode {

	private static List<String[]> province = new ArrayList<String[]>();
	private static List<String[]> bankNo = new ArrayList<String[]>();
	private static List<String[]> cityNo = new ArrayList<String[]>();
	private static Map<String , String > bankMap = new HashMap<String, String>();
//	private static Map<String,String> cityno = new HashMap<String, String>();
	static HostnameVerifier hv = new HostnameVerifier() {  
        public boolean verify(String urlHostName, SSLSession session) {  
            System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
                               + session.getPeerHost());  
            return true;  
        }  
    };  
      
    private static void trustAllHttpsCertificates() throws Exception {  
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
        javax.net.ssl.TrustManager tm = new miTM();  
        trustAllCerts[0] = tm;  
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext  
                .getInstance("SSL");  
        sc.init(null, trustAllCerts, null);  
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc  
                .getSocketFactory());  
    }  
  
    static class miTM implements javax.net.ssl.TrustManager,  
            javax.net.ssl.X509TrustManager {  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
  
        public boolean isServerTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public boolean isClientTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  
  
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  
    }  
	static {
		province.add(new String[]{"1000","������"});
		province.add(new String[]{"1100","�����"});
		province.add(new String[]{"1210","�ӱ�"});
		province.add(new String[]{"1610","ɽ��"});
		province.add(new String[]{"1910","���ɹ�"});
		province.add(new String[]{"2210","����"});
		province.add(new String[]{"2410","����"});
		province.add(new String[]{"2610","������"});
		province.add(new String[]{"2900","�Ϻ���"});
		province.add(new String[]{"3010","����"});
		province.add(new String[]{"3310","�㽭"});
		province.add(new String[]{"3610","����"});
		province.add(new String[]{"3910","����"});
		province.add(new String[]{"4210","����"});
		province.add(new String[]{"4510","ɽ��"});
		province.add(new String[]{"4910","����"});
		province.add(new String[]{"5210","����"});
		province.add(new String[]{"5510","����"});
		province.add(new String[]{"5810","�㶫"});
		province.add(new String[]{"6110","����"});
		province.add(new String[]{"6410","����"});
		province.add(new String[]{"6510","�Ĵ�"});
		province.add(new String[]{"6530","������"});
		province.add(new String[]{"7010","����"});
		province.add(new String[]{"7310","����"});
		province.add(new String[]{"7700","����"});
		province.add(new String[]{"7910","����"});
		province.add(new String[]{"8210","����"});
		province.add(new String[]{"8510","�ຣ"});
		province.add(new String[]{"8710","����"});
		province.add(new String[]{"8810","�½�"});
		
		bankNo.add(new String[]{"104","�й�����"});
		bankNo.add(new String[]{"102","�й���������"});
		bankNo.add(new String[]{"103","�й�ũҵ����"});
		bankNo.add(new String[]{"105","�й���������"});
		bankNo.add(new String[]{"301","��ͨ����"});
		bankNo.add(new String[]{"308","��������"});
		bankNo.add(new String[]{"304","��������"});
		bankNo.add(new String[]{"318","��������"});
		bankNo.add(new String[]{"306","�㷢����"});
		bankNo.add(new String[]{"307","ƽ������"});
		bankNo.add(new String[]{"302","����ʵҵ����"});
		bankNo.add(new String[]{"303","�й��������"});
		bankNo.add(new String[]{"305","�й���������"});
		bankNo.add(new String[]{"309","������ҵ����"});
		bankNo.add(new String[]{"310","�Ϻ��ֶ���չ����"});
		bankNo.add(new String[]{"403","�й�������������"});
		bankNo.add(new String[]{"-1" ,"��������"});
		
		bankMap.clear();
		bankMap.put("�й�����"        ,"104");
		bankMap.put("�й���������"    ,"102");
		bankMap.put("�й�ũҵ����"    ,"103");
		bankMap.put("�й���������"    ,"105");
		bankMap.put("��ͨ����"        ,"301");
		bankMap.put("��������"        ,"308");
		bankMap.put("��������"        ,"304");
		bankMap.put("��������"        ,"318");
		bankMap.put("�㷢����"        ,"306");
		bankMap.put("ƽ������"        ,"307");
		bankMap.put("����ʵҵ����"    ,"302");
		bankMap.put("�й��������"    ,"303");
		bankMap.put("�й���������"    ,"305");
		bankMap.put("������ҵ����"    ,"309");
		bankMap.put("�Ϻ��ֶ���չ����","310");
		bankMap.put("�й�������������","403");
		bankMap.put("��������"        ,"-1" );

		try {
//			cityno.clear();
			getCity();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	

	
	

	private static void getCity() throws JDOMException, IOException {
		
		
		for (String[] str : province) {
			String url = "https://www.hebbank.com/corporbank/cityQueryAjax.do";
			url+="?provinceCode="+str[0];
			String xml = HttpUtil.getHtml(url);
			xml = xml.substring(xml.indexOf("<kColl"), xml.length());  //��ȡ������Ϣ����kColl��ʼ����
			List<Map<String, String>> list = XmlUtil.xml2List(xml,"iCityInfo");
			for (Map<String, String> map : list) {
				//getUnicode(dataList,sb.toString(),map,str,no);
				cityNo.add(new String[] { map.get("cityCode"),map.get("cityName"),str[1]});
			}
		}
		
		//CSVUtils.exportCsv(new File("D:\\unicodeNew.csv"), dataList);
		
	}





	public static List<Map<String, String>> getUnicode(String bankNameNo,String cityNameNo,String key) throws JDOMException, IOException {
		String bankNo = bankNameNo;
		String cityNo = cityNameNo;
		cityNo = cityNo.substring(0, cityNo.length()-2);
		
		String url = "https://www.hebbank.com/corporbank/bankQueryAjax.do";
		url+="?bankType="+bankNo;
		url+="&cityCode="+cityNo;
		url+="&bankName="+"%"+key+"%";
		
		String xml = HttpUtil.getHtml(url);
		xml = xml.substring(xml.indexOf("<kColl"), xml.length());
		 List<Map<String, String>> list = XmlUtil.xml2List(xml,"iBankInfo");
//		return list.get(0).get("unionBankNo");
		return list;
		
	}
	
	public static void main(String[] args) throws Exception {
		
		StringBuffer tmpSb = new StringBuffer();
		List<String> dataList = new ArrayList<String>();
		tmpSb.append("ʡ��,����,���б��,����,���б��,֧������,���к�");
		dataList.add(tmpSb.toString());
		int len = 200;
		int i = 0;
		//ͨ�����кͳ��л�ȡ���к�
		for (String[] bank : bankNo) {
			i++;
			if(len <= i) {
				break;
			}
			for (String[] city : cityNo) {
				List<Map<String, String>> list = getUnicode(bank[0], city[0], "");
				for (Map<String, String> map : list) {
					if(StringUtil.isEmpty(map.get("bankName")) || StringUtil.isEmpty(map.get("unionBankNo"))) {
						continue;
					}
					StringBuffer sb = new StringBuffer();
					sb.append(city[2]).append(",").append(city[1]).append(",").append(city[0]).append(",").append(bank[1]).append(",")
					.append(bank[0]).append(",").append(map.get("bankName")).append(",\t").append(map.get("unionBankNo"));
					dataList.add(sb.toString());
				}
			}
		}
		CSVUtil.exportCsv(new File("D:\\unicodeNew.csv"), dataList);
		System.out.println("done");
		
	}
}
