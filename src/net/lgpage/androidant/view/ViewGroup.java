package net.lgpage.androidant.view;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewGroup extends View {

	List<View> childs = new ArrayList<View>();
	public static String xmlns = "http://schemas.android.com/apk/res/android" ;
	boolean isroot;
	
	public boolean isIsroot() {
		return isroot;
	}

	public void setIsroot(boolean isroot) {
		this.isroot = isroot;
	}

	public void addView(View view) {
		childs.add(view);
	}

	public void removeView(View view) {
		childs.remove(view);
	}
	
	@Override
	public void buildMap() {
		super.buildMap();
		if(isroot){
			attmap.put("xmlns:android", xmlns);
		}
	}

	public  String getEndTag(){
		return "</"+getClass().getSimpleName()+">\n";
	}

	public List<View> getChilds() {
		return childs;
	}

	public void setChilds(List<View> childs) {
		this.childs = childs;
	}
	
}
