package net.lgpage.androidant.generate;

import net.lgpage.androidant.util.FileUtil;

public class ButtonDrawable {

	int paddingTop = 0;
	int paddingBottom = 0;
	int paddingLeft = 0;
	int paddingRight = 0;
	int radius = 0;

	String bgcolor;
	String bgcolor_p;
	float line_size = 0;
	String line_color;
	String drawableName;
	
	public String getDrawableName() {
		if(drawableName == null){
			drawableName = genDrawableName();
		}
		return drawableName;
	}

	public void setDrawableName(String drawableName) {
		this.drawableName = drawableName;
	}

	public void toFile(String dir) {
		String filename = getFileName();
		FileUtil.genFile(generate(), dir + filename);
	}
	public String genDrawableName() {
		String ap = "";
		char r = 0;
		char g = 0;
		char b = 0;
		if (bgcolor.matches("#.{8}")) {
			ap = bgcolor.substring(1, 2);
			r = bgcolor.charAt(3);
			g = bgcolor.charAt(5);
			b = bgcolor.charAt(7);
		} else if (bgcolor.matches("#.{6}")) {
			r = bgcolor.charAt(1);
			g = bgcolor.charAt(3);
			b = bgcolor.charAt(5);
		} else if (bgcolor.matches("#.{3}")) {
			r = bgcolor.charAt(1);
			g = bgcolor.charAt(2);
			b = bgcolor.charAt(3);
		} else if (bgcolor.matches("#.{4}")) {
			ap = bgcolor.substring(1, 1);
			r = bgcolor.charAt(2);
			g = bgcolor.charAt(3);
			b = bgcolor.charAt(4);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("c" + r + g + b + ap);
		if (radius > 0) {
			sb.append("_r" + radius);
		}
		if (line_size > 0) {
			int size = (int) line_size;
			sb.append("_l" + size);
		}
		if (bgcolor_p != null) {
			sb.append("_bt");
		} else {
			sb.append("_bg");
		}
		return sb.toString().toLowerCase();
	}
	public String getFileName() {
		return getDrawableName()+".xml";
	}

	public String generate() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append("<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n");
		sb.append("\n");
		if (bgcolor_p != null) {
			sb.append("    <item android:state_pressed=\"true\"><shape>\n");
			sb.append("            <gradient android:angle=\"0\" android:endColor=\""
					+ bgcolor_p
					+ "\" android:startColor=\""
					+ bgcolor_p
					+ "\" />\n");
			sb.append("\n");
			sb.append("            <corners android:bottomLeftRadius=\""
					+ radius + "dp\" android:bottomRightRadius=\"" + radius
					+ "dp\" android:topLeftRadius=\"" + radius
					+ "dp\" android:topRightRadius=\"" + radius + "dp\" />\n");
			sb.append("\n");
			sb.append("            <padding android:bottom=\"" + paddingBottom
					+ "dp\" android:left=\"" + paddingLeft
					+ "dp\" android:right=\"" + paddingRight
					+ "dp\" android:top=\"" + paddingTop + "dp\" />\n");
			sb.append("\n");
			if (line_size > 0)
				sb.append("            <stroke android:width=\"" + line_size
						+ "dp\" android:color=\"" + line_color
						+ "\"></stroke>\n");
			sb.append("        </shape></item>\n");
		}
		sb.append("    <item><shape>\n");
		sb.append("            <gradient android:angle=\"0\" android:endColor=\""
				+ bgcolor + "\" android:startColor=\"" + bgcolor + "\" />\n");
		sb.append("\n");
		sb.append("            <corners android:bottomLeftRadius=\"" + radius
				+ "dp\" android:bottomRightRadius=\"" + radius
				+ "dp\" android:topLeftRadius=\"" + radius
				+ "dp\" android:topRightRadius=\"" + radius + "dp\" />\n");
		sb.append("\n");
		sb.append("            <padding android:bottom=\"" + paddingBottom
				+ "dp\" android:left=\"" + paddingLeft
				+ "dp\" android:right=\"" + paddingRight
				+ "dp\" android:top=\"" + paddingTop + "dp\" />\n");
		sb.append("\n");
		if (line_size > 0)
			sb.append("            <stroke android:width=\"" + line_size
					+ "dp\" android:color=\"" + line_color + "\"></stroke>\n");

		sb.append("        </shape></item>\n");
		sb.append("\n");
		sb.append("</selector>\n");
		return sb.toString();
	}

	int rl;
	int rt;
	int rr;
	int rb;

	// public void set

	public ButtonDrawable(int radius, String bgcolor) {
		super();
		this.radius = radius;
		this.bgcolor = bgcolor;
	}

	public ButtonDrawable(int radius, String bgcolor, String bgcolor_p) {
		super();
		this.radius = radius;
		this.bgcolor = bgcolor;
		this.bgcolor_p = bgcolor_p;
	}

	public ButtonDrawable(int radius, String bgcolor, String bgcolor_p,
			float line_size, String line_color) {
		super();
		this.radius = radius;
		this.bgcolor = bgcolor;
		this.bgcolor_p = bgcolor_p;
		this.line_size = line_size;
		this.line_color = line_color;
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getBgcolor_p() {
		return bgcolor_p;
	}

	public void setBgcolor_p(String bgcolor_p) {
		this.bgcolor_p = bgcolor_p;
	}

	public float getLine_size() {
		return line_size;
	}

	public void setLine_size(float line_size) {
		this.line_size = line_size;
	}

	public String getLine_color() {
		return line_color;
	}

	public void setLine_color(String line_color) {
		this.line_color = line_color;
	}

}
