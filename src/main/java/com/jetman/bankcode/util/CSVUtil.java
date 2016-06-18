package com.jetman.bankcode.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**   
 * CSV����(�����͵���)
 *
 * @author �ּ���
 * @version 1.0 Jan 27, 2014 4:30:58 PM   
 */
public class CSVUtil {
    
    /**
     * ����
     * 
     * @param file csv�ļ�(·��+�ļ���)��csv�ļ������ڻ��Զ�����
     * @param dataList ����
     * @return
     */
    public static boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;
        
        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out, "gbk");
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }
        
        return isSucess;
    }
    
    /**
     * ����
     * 
     * @param file csv�ļ�(·��+�ļ�)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return dataList;
    }
    
    /**
     * ���� �̶�����
     * 
     * @param file csv�ļ�(·��+�ļ�)
     * @return
     * @throws IOException 
     */
    public static List<String> importCsvChar(String file,String ch) throws IOException{
    	List<String> lines=new ArrayList<String>();  
    	BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),ch));  
    	String line = null;  
    	while ((line = br.readLine()) != null) {  
    	      lines.add(line);  
    	}  
    	br.close();  
 
        return lines;
    }
}