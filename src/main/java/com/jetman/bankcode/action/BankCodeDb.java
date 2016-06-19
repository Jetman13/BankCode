package com.jetman.bankcode.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jetman.bankcode.database.DruidOperator;



public class BankCodeDb {

	private static Logger log = LogManager.getLogger(BankCodeDb.class.getName());
	 private static BeanFactory factory =  new ClassPathXmlApplicationContext("applicationContext.xml");    
	 private static final int COUNT = 1000;
	
	public static int insertBankCode(List<String> datalist) {
		 DruidOperator dop = (DruidOperator) factory.getBean("druidOperator");
		 String sqlhead = "insert into test.t_bank_code (city_name,city_no,bank_name,bank_no,"
		 		+ "branch_name,bank_code) value ";
		 StringBuffer sb = new StringBuffer();
		 int i = 0;
		 for (String s : datalist) {
			 if(i==0) {
				 i++;
				 continue;
			 }
			 String[] sp = s.split(",");
			if(i%COUNT == 0 && sb.length() > 0) {
				int l = dop.executeUpdate(sqlhead + sb.substring(0, sb.length()-1));
				log.info(l);
				sb = new StringBuffer();
			}
			i++;
			sb.append("('").append(sp[1].trim()).append("',").append(sp[2].trim()).append(",'").append(sp[3].trim())
			.append("',").append(sp[4].trim()).append(",'").append(sp[5].trim()).append("','").append(sp[6].trim()).append("'),");
		}
		 
		 if(sb.length() > 0) {
				int l = dop.executeUpdate(sqlhead + sb.substring(0, sb.length()-1));
				log.info(l);
			}
		return 1;
	}
	
}
