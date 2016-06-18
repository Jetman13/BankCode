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
		province.add(new String[]{"1000","北京市"});
		province.add(new String[]{"1100","天津市"});
		province.add(new String[]{"1210","河北"});
		province.add(new String[]{"1610","山西"});
		province.add(new String[]{"1910","内蒙古"});
		province.add(new String[]{"2210","辽宁"});
		province.add(new String[]{"2410","吉林"});
		province.add(new String[]{"2610","黑龙江"});
		province.add(new String[]{"2900","上海市"});
		province.add(new String[]{"3010","江苏"});
		province.add(new String[]{"3310","浙江"});
		province.add(new String[]{"3610","安徽"});
		province.add(new String[]{"3910","福建"});
		province.add(new String[]{"4210","江西"});
		province.add(new String[]{"4510","山东"});
		province.add(new String[]{"4910","河南"});
		province.add(new String[]{"5210","湖北"});
		province.add(new String[]{"5510","湖南"});
		province.add(new String[]{"5810","广东"});
		province.add(new String[]{"6110","广西"});
		province.add(new String[]{"6410","海南"});
		province.add(new String[]{"6510","四川"});
		province.add(new String[]{"6530","重庆市"});
		province.add(new String[]{"7010","贵州"});
		province.add(new String[]{"7310","云南"});
		province.add(new String[]{"7700","西藏"});
		province.add(new String[]{"7910","陕西"});
		province.add(new String[]{"8210","甘肃"});
		province.add(new String[]{"8510","青海"});
		province.add(new String[]{"8710","宁夏"});
		province.add(new String[]{"8810","新疆"});
		
		bankNo.add(new String[]{"104","中国银行"});
		bankNo.add(new String[]{"102","中国工商银行"});
		bankNo.add(new String[]{"103","中国农业银行"});
		bankNo.add(new String[]{"105","中国建设银行"});
		bankNo.add(new String[]{"301","交通银行"});
		bankNo.add(new String[]{"308","招商银行"});
		bankNo.add(new String[]{"304","华夏银行"});
		bankNo.add(new String[]{"318","渤海银行"});
		bankNo.add(new String[]{"306","广发银行"});
		bankNo.add(new String[]{"307","平安银行"});
		bankNo.add(new String[]{"302","中信实业银行"});
		bankNo.add(new String[]{"303","中国光大银行"});
		bankNo.add(new String[]{"305","中国民生银行"});
		bankNo.add(new String[]{"309","福建兴业银行"});
		bankNo.add(new String[]{"310","上海浦东发展银行"});
		bankNo.add(new String[]{"403","中国邮政储蓄银行"});
		bankNo.add(new String[]{"-1" ,"其他银行"});
		
		bankMap.clear();
		bankMap.put("中国银行"        ,"104");
		bankMap.put("中国工商银行"    ,"102");
		bankMap.put("中国农业银行"    ,"103");
		bankMap.put("中国建设银行"    ,"105");
		bankMap.put("交通银行"        ,"301");
		bankMap.put("招商银行"        ,"308");
		bankMap.put("华夏银行"        ,"304");
		bankMap.put("渤海银行"        ,"318");
		bankMap.put("广发银行"        ,"306");
		bankMap.put("平安银行"        ,"307");
		bankMap.put("中信实业银行"    ,"302");
		bankMap.put("中国光大银行"    ,"303");
		bankMap.put("中国民生银行"    ,"305");
		bankMap.put("福建兴业银行"    ,"309");
		bankMap.put("上海浦东发展银行","310");
		bankMap.put("中国邮政储蓄银行","403");
		bankMap.put("其他银行"        ,"-1" );

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
			xml = xml.substring(xml.indexOf("<kColl"), xml.length());  //截取返回信息，从kColl开始解析
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
		tmpSb.append("省份,城市,城市编号,银行,银行编号,支行名称,联行号");
		dataList.add(tmpSb.toString());
		int len = 200;
		int i = 0;
		//通过银行和城市获取联行号
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
