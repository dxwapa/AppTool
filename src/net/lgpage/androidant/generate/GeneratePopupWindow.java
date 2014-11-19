package net.lgpage.androidant.generate;

import java.io.File;

import net.lgpage.androidant.generate.AndroidXmlGenerate.SetType;
import net.lgpage.androidant.util.FileUtil;

public class GeneratePopupWindow {

	public void toFile(String dirpath) throws Exception {
		FileUtil.genFile(genPop(), dirpath + classname + ".java");
	}

	public GeneratePopupWindow(File file) {
		super();
		this.file = file;
		xmlgen = new AndroidXmlGenerate(file, SetType.Pop);
		initName();
		initPart();
	}

	public GeneratePopupWindow(File file, String pkg) {
		super();
		this.file = file;
		this.pkg = pkg;
		xmlgen = new AndroidXmlGenerate(file, SetType.Pop);
		initName();
		initPart();
	}

	File file;
	String pkg;
	String defines;
	String setViews;
	String setEvents;
	String xmlname;
	String classname;
	String prename = "pop_";
	String header;

	public String generateDialog() {
		StringBuilder sb = new StringBuilder();
		if(pkg!=null)
			sb.append("package " + pkg + ";\n");
			sb.append("\n");
		sb.append("public class "+classname+" {\n");
		sb.append("\n");
		sb.append("	DialogListener listener;\n");
		sb.append("\n");
		sb.append("	public DialogListener getListener() {\n");
		sb.append("		return listener;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	public void setListener(DialogListener listener) {\n");
		sb.append("		this.listener = listener;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	Context context;\n");
		sb.append("\n");
		sb.append("	public "+classname+"(Context context) {\n");
		sb.append("		super();\n");
		sb.append("		this.context = context;\n");
		sb.append("		onCreate();\n");
		sb.append("	}\n");
		sb.append("	\n");
		sb.append("	public void onCreate() {\n");
		sb.append("		dialog = new Dialog(context, R.style.dialog);\n");
		sb.append("		view = LayoutInflater.from(context).inflate(R.layout." + xmlname
				+ ", null);\n");
		if (setViews != null)
			sb.append("		setViews();\n");
		if(setEvents!=null)
			sb.append("		setEvents();\n");
		sb.append("		dialog.setContentView(view);\n");
		sb.append("	}\n");
		if (setViews != null)
			sb.append(setViews);
		if(setEvents!=null)
			sb.append(setEvents);
		sb.append("\n");
		sb.append("	public Dialog getDialog() {\n");
		sb.append("		return dialog;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	public void setDialog(Dialog dialog) {\n");
		sb.append("		this.dialog = dialog;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	public View getView() {\n");
		sb.append("		return view;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	public void setView(View view) {\n");
		sb.append("		this.view = view;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	Dialog dialog;\n");
		sb.append("	View view;\n");
		if(defines!=null)
			sb.append(defines);
		sb.append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	public String genPop(){
		StringBuilder sb = new StringBuilder();
		if(pkg!=null)
			sb.append("package " + pkg + ";\n");
			sb.append("\n");
			sb.append("public class "+classname+" {\n");
		sb.append("\n");
		sb.append("	PopupWindow window;\n");
		sb.append("	View view;\n");
		sb.append("	Context context;\n");
		sb.append("	DialogListener listener;\n");
		sb.append("	public DialogListener getListener() {\n");
		sb.append("		return listener;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	public void setListener(DialogListener listener) {\n");
		sb.append("		this.listener = listener;\n");
		sb.append("	}\n");
		sb.append("	public PopupWindow getWindow() {\n");
		sb.append("		return window;\n");
		sb.append("	}\n");
		sb.append("	\n");
		sb.append("	public void setWindow(PopupWindow window) {\n");
		sb.append("		this.window = window;\n");
		sb.append("	}\n");
		sb.append("	\n");
		sb.append("	public "+classname+"(Context context) {\n");
		sb.append("		super();\n");
		sb.append("		this.context = context;\n");
		sb.append("		onCreate();\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	private void onCreate(){\n");
		sb.append("		view = LayoutInflater.from(context).inflate(R.layout." + xmlname
				+ ", null);\n");
		sb.append("		window = new PopupWindow(view,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);\n");
		sb.append("		window.setBackgroundDrawable(new BitmapDrawable()); // 响应返回键必须的语句\n");
		
		if (setViews != null)
			sb.append("		setViews();\n");
		if(setEvents!=null)
			sb.append("		setEvents();\n");
		sb.append("		window.setContentView(view);\n");
		sb.append("		window.setFocusable(true);\n");
		sb.append("		window.setOutsideTouchable(true);\n");
		sb.append("	}\n");
		sb.append("	\n");
		if (setViews != null)
			sb.append(setViews);
		if(setEvents!=null)
			sb.append(setEvents);
		sb.append("	\n");
		if(defines!=null)
			sb.append(defines);
		sb.append("}\n");
		return sb.toString();
	}
	AndroidXmlGenerate xmlgen;
	public void initPart() {
		defines = xmlgen.getDefines();
		setViews = xmlgen.getSetViews();
		setEvents = xmlgen.getSetEvents();
	}

	public void initName() {
		xmlname = xmlgen.getXmlname();
		prename = xmlgen.getPrename();
		classname = xmlgen.getClassname();
	}

}
