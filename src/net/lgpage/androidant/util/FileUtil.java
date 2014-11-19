package net.lgpage.androidant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileUtil {

	public static File genFile(String content,String path){
		String div = "\\";
		if(path.lastIndexOf("\\")>0){
			div ="\\";
		}else if(path.lastIndexOf("/")>0){
			div = "/";
		}
		File f = new File(path.substring(0,path.lastIndexOf(div)));
		if(!f.exists()){
			f.mkdirs();
		}
		f = new File(f, path.substring(path.lastIndexOf(div)+1));
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static void replaceId(File file){
		try {  
            BufferedReader bufReader =   
                new BufferedReader(  
                    new InputStreamReader(  
                        new FileInputStream(  
                            file),"UTF-8"));  
  
            StringBuffer strBuf = new StringBuffer();  
            for (String tmp = null; (tmp = bufReader.readLine()) != null; tmp = null) {  
                // 在这里做替换操作  
                tmp = tmp.replaceAll("android:id=\"@id", "android:id=\"@+id");  
                strBuf.append(tmp+"\n");  
            }  
            bufReader.close();  
            PrintWriter printWriter = new PrintWriter(file,"UTF-8");  
            printWriter.write(strBuf.toString().toCharArray());  
            printWriter.flush();  
            printWriter.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
}
