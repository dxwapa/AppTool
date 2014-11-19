package net.lgpage.androidant.util;

import java.awt.Color;

public class ColorUtil {

	//Colorת��Ϊ16������ʾ
    public static String toHexEncoding(Color color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
 
        R = Integer.toHexString(color.getRed());
        G = Integer.toHexString(color.getGreen());
        B = Integer.toHexString(color.getBlue());
 
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
 
        sb.append("0x");
        sb.append(R);
        sb.append(G);
        sb.append(B);
 
        return sb.toString();
    }
    public static String toJinEncoding(Color color){
    	String x0 = toHexEncoding(color);
    	return "#"+x0.substring(2);
    }
    
    //���ַ���������ɫֵת����java.awt.Color
    public static Color parseToColor(final String c) {
        Color convertedColor = Color.ORANGE;
        try {
            convertedColor = new Color(Integer.parseInt(c, 16));
        } catch(NumberFormatException e) {
            // codes to deal with this exception
        }
        return convertedColor;
    }

}
