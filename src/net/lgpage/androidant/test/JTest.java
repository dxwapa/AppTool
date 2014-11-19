package net.lgpage.androidant.test;

import net.lgpage.androidant.generate.ButtonDrawable;

import org.junit.Test;

public class JTest {
	
	/*@Test
	public void test() {
		TextViewStyle style = new TextViewStyle(16, "#ff1234");
		System.out.println(style.getTag());
		ViewXmlBuilder b = new ViewXmlBuilder();
		LinearLayout l = new LinearLayout();
		l.setId("ac_ll0");
		l.setIsroot(true);
		TextView tv = new TextView();
		tv.setId("ac_tv");
		tv.setStyle(style.getStyleName());
		ButtonDrawable bd = new ButtonDrawable(2, "#abcdef");
		String name = bd.getDrawableName();
		tv.setBackgroundDrawable(name);
		l.addView(tv);
		LinearLayout l2 = new LinearLayout();
		l2.setId("ac_ll1");
		l2.addView(tv);
		l.addView(l2);
		String content = b.builderView(l);
		File file = FileUtil.genFile(content, "E:\\ac_test1.xml");
		GenerateActivity ga = new GenerateActivity(file);
		try {
			ga.toFile("E:\\");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	/*@Test
	public void test2() throws Exception{
		GenerateActivity ga = new GenerateActivity(new File("E://fm_shangpintongji.xml"));
		ga.toFile("E://");
	}*/
	@Test
	public void test3(){
		ButtonDrawable	bd = new ButtonDrawable(5, "#A69E8A", "#F9F9F8", 1f, "#64D2BB");
		bd.toFile("D://");
		bd = new ButtonDrawable(3, "#ffffff", null, 1f, "#adadad");
		bd.toFile("D://");
		
	}
	
	@Test
	public void test4()  {
		//System.out.println(file.getParentFile().toString()+file.getName());
		/*String str = ColorUtil.toHexEncoding(Color.red);
		System.out.println(str);*/
		/*try {
			Process p = Runtime.getRuntime().exec("cmd /c mysqldump -hlocalhost -uroot -proot daydayhealth stepnote > e:/haha.sql");
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	
	
}
