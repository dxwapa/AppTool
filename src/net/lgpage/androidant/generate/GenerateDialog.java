package net.lgpage.androidant.generate;

import java.io.File;

import net.lgpage.androidant.generate.AndroidXmlGenerate.SetType;
import net.lgpage.androidant.util.FileUtil;

public class GenerateDialog {

	public void toFile(String dirpath) throws Exception {
		FileUtil.genFile(generateDialog(), dirpath + classname + ".java");
	}

	public GenerateDialog(File file) {
		super();
		this.file = file;
		xmlgen = new AndroidXmlGenerate(file, SetType.Dialog);
		initName();
		initPart();
	}

	public GenerateDialog(File file, String pkg) {
		super();
		this.file = file;
		this.pkg = pkg;
		xmlgen = new AndroidXmlGenerate(file, SetType.Dialog);
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
	String prename = "dg_";
	String header;
	String loadData;
	public String generateDialog() {
		StringBuilder sb = new StringBuilder();
		if(pkg!=null)
			sb.append("package " + pkg + ";\n");
			sb.append("\n");
		sb.append("public class "+classname+" extends Dialog{\n");
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
		sb.append("	public "+classname+"(Context context) {\n");
		sb.append("		super(context,R.style.dialog);\n");
		sb.append("		onCreate();\n");
		sb.append("	}\n");
		sb.append("	\n");
		sb.append("	public void onCreate() {\n");
		sb.append("		view = LayoutInflater.from(getContext()).inflate(R.layout." + xmlname
				+ ", null);\n");
		if (setViews != null)
			sb.append("		setViews();\n");
		if(setEvents!=null)
			sb.append("		setEvents();\n");
		if(loadData!=null)
			sb.append("		loadData();\n");
		sb.append("		setContentView(view);\n");
		sb.append("	}\n");
		if (setViews != null)
			sb.append(setViews);
		if(setEvents!=null)
			sb.append(setEvents);
		if(loadData!=null){
			sb.append(loadData + "\n");
		}
		sb.append("\n");
		sb.append("	public View getView() {\n");
		sb.append("		return view;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	public void setView(View view) {\n");
		sb.append("		this.view = view;\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	View view;\n");
		if(defines!=null)
			sb.append(defines);
		sb.append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	AndroidXmlGenerate xmlgen;
	public void initPart() {
		defines = xmlgen.getDefines();
		setViews = xmlgen.getSetViews();
		setEvents = xmlgen.getSetEvents();
		loadData = xmlgen.loadData();
	}

	public void initName() {
		xmlname = xmlgen.getXmlname();
		prename = xmlgen.getPrename();
		classname = xmlgen.getClassname();
	}

}
