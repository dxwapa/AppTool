package net.lgpage.androidant.view.attr;

public class TextViewStyle {

	int textsize;
	String color;
	String styleName;
	
	
	
	public String getStyleName() {
		if(styleName == null){
			styleName = genStyleName();
		}
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public TextViewStyle(int textsize, String color) {
		super();
		this.textsize = textsize;
		this.color = color;
	}

	public int getTextsize() {
		return textsize;
	}

	public void setTextsize(int textsize) {
		this.textsize = textsize;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTag(){
		StringBuilder sb = new StringBuilder();
		sb.append("<style name=\""+getStyleName()+"\">\n");
		sb.append("	<item name=\"android:textColor\">"+color+"</item>\n");
		sb.append("	<item name=\"android:textSize\">"+textsize+"sp</item>\n");
		sb.append("</style>\n");
		return sb.toString();
	}
	
	public String genStyleName() {
		String ap = "";
		char r = 0;
		char g = 0;
		char b = 0;
		if (color.matches("#.{8}")) {
			ap = color.substring(1, 2);
			r = color.charAt(3);
			g = color.charAt(5);
			b = color.charAt(7);
		} else if (color.matches("#.{6}")) {
			r = color.charAt(1);
			g = color.charAt(3);
			b = color.charAt(5);
		} else if (color.matches("#.{3}")) {
			r = color.charAt(1);
			g = color.charAt(2);
			b = color.charAt(3);
		} else if (color.matches("#.{4}")) {
			ap = color.substring(1, 1);
			r = color.charAt(2);
			g = color.charAt(3);
			b = color.charAt(4);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("c" + r + g + b + ap+"_"+textsize);
		return sb.toString().toLowerCase();
	}
}
