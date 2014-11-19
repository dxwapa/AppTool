package net.lgpage.androidant.util;

import java.util.List;

import net.lgpage.androidant.view.View;
import net.lgpage.androidant.view.ViewGroup;

public class ViewXmlBuilder {

	public String builderView(View v){
		StringBuilder sb = new StringBuilder();
		if(v instanceof ViewGroup){
			ViewGroup vg = (ViewGroup) v;
			sb.append(vg.getStartTag());
			List<View>list = vg.getChilds();
			for(int i = 0;i<list.size();i++){
				sb.append(builderView(list.get(i)));
			}
			sb.append(vg.getEndTag());
		}else{
			sb.append(v.getStartTag());
		}
		return sb.toString();
	}
	public void buildActivityXml(String dirpath,String xmlname,String content){
		String filename = "ac_"+xmlname+".xml";
		FileUtil.genFile(content, dirpath+filename);
	}
}
