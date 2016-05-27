package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileUtil {

	public static void write(String path,String content){
		File file = new File(path);  
		if(!file.exists())   
		{   
			try {   
				file.createNewFile();
				// 向文件写入内容(输出流)  
				byte bt[] = new byte[1024];  
				bt = content.getBytes();  
				try {  
					FileOutputStream in = new FileOutputStream(file);  
					try {  
						in.write(bt, 0, bt.length);  
						in.close();  
						// boolean success=true;  
						// System.out.println("写入文件成功");  
					} catch (IOException e) {  
						// TODO Auto-generated catch block  
						e.printStackTrace();  
					}  
				} catch (FileNotFoundException e) {  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
				}  
			} catch (IOException e) {   
				// TODO Auto-generated catch block   
				e.printStackTrace();   
			}   
		}  
	}

	public static String read(String path){
        File file = new File(path);
        BufferedReader reader = null;  
        String restr = "";  
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            while ((tempString = reader.readLine()) != null) {  
            	restr = restr + tempString;  
            }  
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        } 
        return restr;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
