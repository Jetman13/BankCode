package com.jetman.bankcode.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jetman.bankcode.database.DruidOperator;

public class TestDruid {
	 private BeanFactory factory = null;  
	 public List<String> datalist = new ArrayList<String>();
     Logger log = LogManager.getLogger(TestDruid.class.getName());
	    @Before  
	    public void before() {  
	        factory = new ClassPathXmlApplicationContext("applicationContext.xml");  
	        for(int i=0;i<3;i++) {
	        	datalist.add("中文"+i+","+"中文2"+i+","+"3"+i+","+"4"+i+","+"523"+i+","+"6"+i+","+"7"+i);
	        }
	        System.out.println(factory);
	    }  
	      
	    @Test
	    public void testSpring() {  
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
				if(i%1000 == 0 && sb.length() > 0) {
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
	    }
	    
	    @Test
	    public void testdb() {
	    	DruidOperator dop = (DruidOperator) factory.getBean("druidOperator");
	    	String sql = "insert into my_db.t_shop_order_sn (F_order_id,F_pinpad_uuid) value ('订单号1','隼鸟号')";
	    	dop.executeUpdate(sql);
	    }

}
