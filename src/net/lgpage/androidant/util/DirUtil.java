package net.lgpage.androidant.util;

import java.io.File;

import net.lgpage.androidant.generate.GenerateActivity;
import net.lgpage.androidant.generate.GenerateDialog;
import net.lgpage.androidant.generate.GenerateFragment;
import net.lgpage.androidant.generate.GeneratePopupWindow;

public class DirUtil {

	public static void main(String[] args) throws Exception{
		DirUtil util = new DirUtil();
		final String dir = "E:\\";
		util.excuteDir(dir, ".xml",new RunFile() {
			@Override
			public void doFile(File file) throws Exception {
				String packagestr =  "org.moder.wordermng";
				if(file.getName().startsWith("ac_")){
					GenerateActivity gf = new GenerateActivity(file, "org.moder.wordermng");
					gf.toFile(dir);
				}else if(file.getName().startsWith("fm_")){
					GenerateFragment ga= new GenerateFragment(file,packagestr+".fragment");
					ga.toFile(dir+"fragment\\");
				}else if(file.getName().startsWith("dg_")){
					GenerateDialog gd = new GenerateDialog(file,packagestr+".dialog");
					gd.toFile(dir+"dialog\\");
				}else if(file.getName().startsWith("pop_")){
					GeneratePopupWindow gd = new GeneratePopupWindow(file,packagestr+".pop");
					gd.toFile(dir+"pop\\");
				}
			}
		});
		
		/*util.excuteDir("E:\\AndroidƒÊœÚ÷˙ ÷_v2.2\\POSStage\\res\\layout", ".xml", new RunFile() {
			
			@Override
			public void doFile(File file) throws Exception {
				FileUtil.replaceId(file);
			}
		});*/
		
	}
	
	public void excuteDir(String dirpath,String endwith,RunFile runfile)throws Exception{
		File dir = new File(dirpath);
		if(dir.isDirectory()){
			File  fs[] = dir.listFiles();
			for(File f :fs){
				if(f.isFile()){
					if(endwith==null){
						if(runfile!=null){
							runfile.doFile(f);
						}
					}else{
						if(f.getName().endsWith(endwith)){
							if(runfile!=null){
								runfile.doFile(f);
							}
						}
					}
				}
			}
		}
	}
	
	public interface RunFile{
		public void doFile(File file)throws Exception;
	}
	
}
