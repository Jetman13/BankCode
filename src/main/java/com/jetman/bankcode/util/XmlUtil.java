package com.jetman.bankcode.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XmlUtil {
	/**
	 * 取得xml文件的根节点名称，即消息名称。
	 * 
	 * @param xmlStr
	 *            xml内容
	 * @return String 返回名称
	 */
	public static String getRootName(String xmlStr) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xmlStr));
		Element root = doc.getRootElement();
		return root.getName();
	}

	/**
	 * 把xml文件转换为map形式，其中key为有值的节点名称，并以其所有的祖先节点为前缀，用
	 * "."相连接。如：SubscribeServiceReq.Send_Address.Address_Info.DeviceType
	 * 
	 * @param xmlStr
	 *            xml内容
	 * @return Map 转换为map返回
	 */
	public static Map<String, String> xml2Map(String xmlStr)
			throws JDOMException, IOException {
		Map<String, String> rtnMap = new HashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xmlStr));
		// 得到根节点
		Element root = doc.getRootElement();
		String rootName = root.getName();
		//rtnMap.put("root.name", rootName);
		convert(root, rtnMap, rootName);
		return rtnMap;
	}


	public static void convert(Element e, Map<String, String> map,
			String lastname) {
		/*
		if (e.getAttributes().size() > 0) {
			Iterator it_attr = e.getAttributes().iterator();
			while (it_attr.hasNext()) {
				Attribute attribute = (Attribute) it_attr.next();
				String attrname = attribute.getName();
				String attrvalue = e.getAttributeValue(attrname);
				map.put(lastname + "." + attrname, attrvalue);
			}
		}
		*/
		List children = e.getChildren();
		Iterator it = children.iterator();
		while (it.hasNext()) {
			Element child = (Element) it.next();
			String name = lastname + "." + child.getName();
			// 如果有子节点，则递归调用
			if (child.getChildren().size() > 0) {
				convert(child, map, name);
			} else {
				// 如果没有子节点，则把值加入map
				map.put(name, child.getText());
				// 如果该节点有属性，则把所有的属性值也加入map
				if (child.getAttributes().size() > 0) {
					Iterator attr = child.getAttributes().iterator();
					while (attr.hasNext()) {
						Attribute attribute = (Attribute) attr.next();
						String attrname = attribute.getName();
						String attrvalue = child.getAttributeValue(attrname);
						map.put(name + "." + attrname, attrvalue);
					}
				}
			}
		}
	}

	/**
	 * 把xml文件转换为list形式，其中每个元素是一个map，map中的key为有值的节点名称，并以其所有的祖先节点为前缀，用
	 * "."相连接。如：SubscribeServiceReq.Send_Address.Address_Info.DeviceType
	 * 
	 * @param xmlStr
	 *            xml内容
	 * @return Map 转换为map返回
	 */
	public static List<Map<String, String>> xml2List(String xmlStr,String idName)
			throws JDOMException, IOException {
		System.out.println("xml = "+xmlStr);
		List<Map<String, String>> rtnList = new ArrayList<Map<String, String>>();
		Map<String, String> rtnMap = new HashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new StringReader(xmlStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<Map<String,String>>();
		}
		// 得到根节点
		Element root = doc.getRootElement();
		String rootName = root.getName();
		// 调用递归函数，得到所有最底层元素的名称和值，加入map中
		convert2List(root, rtnMap, rootName, rtnList,idName);
		if (rtnList.size() == 0)
			rtnList.add(rtnMap);
		return rtnList;
	}

	public static void convert2List(Element e, Map<String, String> map,
			String lastname, List<Map<String, String>> list,String idName) {
		/*
		if (e.getAttributes().size() > 0) {
			Iterator it_attr = e.getAttributes().iterator();
			while (it_attr.hasNext()) {
				Attribute attribute = (Attribute) it_attr.next();
				String attrname = attribute.getName();
				String attrvalue = e.getAttributeValue(attrname);
				map.put(lastname + "." + attrname, attrvalue);
			}
		}
		*/
		List children = e.getChildren();
		Iterator it = children.iterator();
		while (it.hasNext()) {
			Element child = (Element) it.next();
			String idValue = child.getAttributeValue("id");
			if(!idName.equals(idValue)) {
				continue;
			}
			
			
			List<Element> ch = child.getChildren();
			if (ch.size() > 0) {
				
				for (Element element : ch) {
					List<Element> kColl = element.getChildren();
					Map<String, String> aMap = new HashMap<String, String>();
					aMap.put(kColl.get(0).getAttributeValue("id"), kColl.get(0).getAttributeValue("value"));
					aMap.put(kColl.get(1).getAttributeValue("id"), kColl.get(1).getAttributeValue("value"));
					list.add(aMap);
				}

				
			}
			
		}
	}

	/**
	 * 打印map 的所有key和value
	 * 
	 * @param map
	 */
	public static void printMap(Map<String, String> map) {
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			System.out.println(key + ":" + map.get(key));
		}
	}
	
	public static void main(String[] args) throws JDOMException, IOException {
		String xml = 
			"<kColl id=\"noSessionSrvData\" append=\"false\">                                                                              "+
			"    <field id=\"EMPException\"/>                                                                                              "+
			"    <field id=\"errorMessage\"/>                                                                                              "+
			"    <field id=\"customerId\"/>                                                                                                "+
			"    <field id=\"provinceCode\"/>                                                                                              "+
			"    <field id=\"provinceName\"/>                                                                                              "+
			"    <field id=\"cityCode\" value=\"2640\"/>                                                                                   "+
			"    <field id=\"bankName\"/>                                                                                                  "+
			"    <field id=\"bankType\" value=\"104\"/>                                                                                    "+
			"    <field id=\"configName\"/>                                                                                                "+
			"    <field id=\"electricNo\"/>                                                                                                "+
			"    <field id=\"orderFlowNo\"/>                                                                                               "+
			"    <field id=\"dateTime\"/>                                                                                                  "+
			"    <iColl id=\"iCityInfo\" append=\"false\"></iColl>                                                                         "+
			"    <iColl id=\"iBankInfo\" append=\"false\">                                                                                 "+
			"        <kColl id=\"null\" append=\"false\">                                                                                  "+
			"            <field id=\"unionBankNo\" value=\"104264000019\"/>                                                                "+
			"            <field id=\"bankName\" value=\"中国银行股份有限公司齐齐哈尔分行\"/>                                                 "+
			"        </kColl>                                                                                                              "+
			"        <kColl id=\"null\" append=\"false\">                                                                                  "+
			"            <field id=\"unionBankNo\" value=\"104264000051\"/>                                                                "+
			"            <field id=\"bankName\" value=\"中国银行股份有限公司齐齐哈尔富拉尔基支行\"/>                                          "+
			"        </kColl>                                                                                                              "+
			"        <kColl id=\"null\" append=\"false\">                                                                                  "+
			"            <field id=\"unionBankNo\" value=\"104264100011\"/>                                                                "+
			"            <field id=\"bankName\" value=\"中国银行股份有限公司龙江支行\"/>                                                     "+
			"        </kColl>                                                                                                              "+
			"        <kColl id=\"null\" append=\"false\">                                                                                  "+
			"            <field id=\"unionBankNo\" value=\"104264200012\"/>                                                                "+
			"            <field id=\"bankName\" value=\"中国银行股份有限公司讷河支行\"/>                                                     "+
			"        </kColl>                                                                                                              "+
			"        <kColl id=\"null\" append=\"false\">                                                                                  "+
			"            <field id=\"unionBankNo\" value=\"104264300013\"/>                                                                "+
			"            <field id=\"bankName\" value=\"中国银行股份有限公司依安支行\"/>                                                     "+
			"        </kColl>                                                                                                              "+
			"        <kColl id=\"null\" append=\"false\">                                                                                  "+
			"            <field id=\"unionBankNo\" value=\"104264900010\"/>                                                                "+
			"            <field id=\"bankName\" value=\"中国银行股份有限公司克山支行\"/>                                                     "+
			"        </kColl>                                                                                                              "+
			"    </iColl>                                                                                                                  "+
			"    <iColl id=\"iPayUseList\" append=\"false\"></iColl>                                                                       "+
			"    <iColl id=\"iBankNotice\" append=\"false\"></iColl>                                                                       "+
			"    <iColl id=\"iOrderList\" append=\"false\"></iColl>                                                                        "+
			"    <field id=\"activateCode\"/>                                                                                              "+
			"    <field id=\"payAccountOpenNode\"/>                                                                                        "+
			"    <iColl id=\"iCityBank\" append=\"false\"></iColl>                                                                         "+
			"    <field id=\"transferTowardType\"/>                                                                                        "+
			"    <field id=\"errorCode\"/>                                                                                                 "+
			"    <field id=\"chargeFee\"/>                                                                                                 "+
			"    <field id=\"userAlias\"/>                                                                                                 "+
			"    <field id=\"password\"/>                                                                                                  "+
			"    <field id=\"passwordEncrypted\"/>                                                                                         "+
			"    <field id=\"oldPassword\"/>                                                                                               "+
			"    <field id=\"passwordOld\"/>                                                                                               "+
			"    <field id=\"passwordNew\"/>                                                                                               "+
			"    <field id=\"userid\"/>                                                                                                    "+
			"    <field id=\"flowId\"/>                                                                                                    "+
			"    <field id=\"uifAlias\"/>                                                                                                  "+
			"    <field id=\"stt\"/>                                                                                                       "+
			"    <field id=\"queryNumber\"/>                                                                                               "+
			"    <field id=\"flagNo\"/>                                                                                                    "+
			"    <field id=\"userId\"/>                                                                                                    "+
			"    <field id=\"openDate\"/>                                                                                                  "+
			"    <field id=\"turnPageBeginPos\" value=\"1\"/>                                                                              "+
			"    <field id=\"turnPageShowNum\" value=\"10\"/>                                                                              "+
			"    <field id=\"turnPageTotalNum\"/>                                                                                          "+
			"    <field id=\"queryType\"/>                                                                                                 "+
			"    <field id=\"PBA_ID\"/>                                                                                                    "+
			"    <field id=\"PBA_TYPE  \"/>                                                                                                "+
			"    <field id=\"PBA_TITLE \"/>                                                                                                "+
			"    <field id=\"PBA_CONTENT \"/>                                                                                              "+
			"    <field id=\"PBA_TELLERNO \"/>                                                                                             "+
			"    <field id=\"PBA_DATETIME  \"/>                                                                                            "+
			"    <field id=\"PBA_ACTIVEDATE  \"/>                                                                                          "+
			"    <field id=\"PBA_EXPIREDATE \"/>                                                                                           "+
			"    <field id=\"PBA_STT \"/>                                                                                                  "+
			"    <iColl id=\"iANNOUNCE\" append=\"false\"></iColl>                                                                         "+
			"    <iColl id=\"iUnionBankList\" append=\"false\"></iColl>                                                                    "+
			"    <field id=\"abateDate\"/>                                                                                                 "+
			"    <field id=\"Dueday\"/>                                                                                                    "+
			"    <field id=\"_ServletRequest\" value=\"weblogic.servlet.internal.ServletRequestImpl@23c923c9[                              "+
			"POST /corporbank/WEB-INF/mvcs/corporbank/ajax/ajax_nosession.jsp?bankType=104&amp;cityCode=2640 HTTP/1.1                      "+
			"Content-Length: 0                                                                                                             "+
			"Cache-Control: no-cache                                                                                                       "+
			"Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop                                                                   "+
			"apikey: 92991dfed0813889c28eb7d77f2871b8                                                                                      "+
			"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36     "+
			"Postman-Token: 998af17c-d585-5f95-0ac4-52848fae6777                                                                           "+
			"Accept: */*                                                                                                                   "+
			"Accept-Encoding: gzip, deflate                                                                                                "+
			"Accept-Language: zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4                                                                          "+
			"Cookie: JSESSIONIDCB=f0L2WnVLl6dK8nrWJvyjh05VyXyvprJ2ZtCYlWr8kpmNKWtXRtJx!294594868                                           "+
			"Via: 1.1 ID-0002262033114260 uproxy-2                                                                                         "+
			"X-Forwarded-For: 218.17.162.165                                                                                               "+
			"Connection: Keep-Alive                                                                                                        "+
			"Proxy-Client-IP: 218.17.162.165                                                                                               "+
			"X-WebLogic-KeepAliveSecs: 30                                                                                                  "+
			"X-WebLogic-Force-JVMID: -1106424160                                                                                           "+
			"                                                                                                                              "+
			"]\"/>                                                                                                                         "+
			"    <field id=\"retValue\" value=\"0\"/>                                                                                      "+
			"</kColl>                                                                                                                      ";
		
		 List<Map<String, String>> list = xml2List(xml,"iBankInfo");
		 int i = 1;
		 for (Map<String, String> map : list) {
			 System.out.println(i+"  ="+map);
			 i++;
		}
		
	}

}