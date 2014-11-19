package net.lgpage.androidant.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class View{

	
	
	
	public View() {
		super();
		setLayout(-2, -2);
	}
	String background;
	public  void setBackground(String background){
		this.background = background;
	}
	public  void setBackgroundDrawable(String drawable){
		this.background = "@drawable/"+drawable;
	}
	String width;
	String height;
	String id;
	String style;
	String gravity;
	String layoutGravity;
	Map<String, String>attmap;
	
	public String getStartTag(){
		StringBuilder sb = new StringBuilder();
		sb.append("<"+getClass().getSimpleName()+"\n");
		buildMap();
		for(Entry<String, String>e:attmap.entrySet()){
			sb.append(e.getKey()+"=\""+e.getValue()+"\"\n");
		}
		if(this instanceof ViewGroup){
			sb.append(">\n");
		}else{
			sb.append("/>\n");
		}
		return sb.toString();
	}
	
	public Map<String, String> getAttmap() {
		return attmap;
	}

	public void setAttmap(Map<String, String> attmap) {
		this.attmap = attmap;
	}

	public void buildMap(){
		attmap = new HashMap<>();
		attmap.put("android:layout_width", width);
		attmap.put("android:layout_height", height);
		if(id!=null){
			attmap.put("android:id", "@+id/"+id);
		}
		if(style!=null){
			attmap.put("style", "@style/"+style);
		}
		if(background!=null){
			attmap.put("android:background",background);
		}
		if(gravity!=null){
			attmap.put("android:gravity", gravity);
		}
		if(layoutGravity!=null){
			attmap.put("android:layout_gravity", layoutGravity);
		}
	}
	
	public void setLayout(int w,int h){
		if(w == -1){
			width = "match_parent";
		}else if(w == -2){
			width = "wrap_content";
		}else{
			width = w+"dp";
		}
		if(h == -1){
			height = "match_parent";
		}else if(h == -2){
			height = "wrap_content";
		}else{
			height = w+"dp";
		}
	}
	
	public String getLayoutGravity() {
		return layoutGravity;
	}

	public void setLayoutGravity(String layoutGravity) {
		this.layoutGravity = layoutGravity;
	}

	public String getGravity() {
		return gravity;
	}

	public void setGravity(String gravity) {
		this.gravity = gravity;
	}


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;
	}



	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBackground() {
		return background;
	}
	
	
}
