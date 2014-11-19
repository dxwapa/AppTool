package net.lgpage.androidant.generate;

import java.io.File;

import net.lgpage.androidant.generate.AndroidXmlGenerate.SetType;
import net.lgpage.androidant.util.FileUtil;

public class GenerateActivity {


	public void toFile(String dirpath) throws Exception {
		FileUtil.genFile(generateActivity(), dirpath + classname + ".java");
	}
	
	public GenerateActivity(File file) {
		super();
		this.file = file;
		xmlgen = new AndroidXmlGenerate(file, SetType.Activity);
		initName();
		initPart();
	}

	public GenerateActivity(File file, String pkg) {
		super();
		this.file = file;
		this.pkg = pkg;
		xmlgen = new AndroidXmlGenerate(file, SetType.Activity);
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
	String prename = "fm_";
	String header;
	String loadData;
	String destory;
	public String generateActivity() {
		StringBuilder sb = new StringBuilder();
		if(pkg!=null)
		sb.append("package " + pkg + ";\n");
		sb.append("\n");
		sb.append("public class " + classname + " extends Activity {\n");
		sb.append("\n");
		sb.append("	@Override\n");
		sb.append("	protected void onCreate(Bundle savedInstanceState) {\n");
		sb.append("		super.onCreate(savedInstanceState);\n");
		sb.append("		setContentView(R.layout." + xmlname + ");\n");
		if (setViews != null)
			sb.append("		setViews();\n");
		if(setEvents!=null)
			sb.append("		setEvents();\n");
		if(loadData!=null)
			sb.append("		loadData();\n");
		sb.append("	}\n");
		if (defines != null) {
			sb.append(defines + "\n");
		}
		if (setViews != null)
			sb.append(setViews + "\n");
		if(setEvents!=null)
			sb.append(setEvents + "\n");
		if(loadData!=null){
			sb.append(loadData + "\n");
		}
		if(destory!=null){
			sb.append(destory + "\n");
		}
		sb.append("}");
		return sb.toString();
	}
	AndroidXmlGenerate xmlgen;
	public void initPart() {
		defines = xmlgen.getDefines();
		setViews = xmlgen.getSetViews();
		setEvents = xmlgen.getSetEvents();
		loadData = xmlgen.loadData();
		destory = xmlgen.getDestory();
	}

	public void initName() {
		xmlname = xmlgen.getXmlname();
		prename = xmlgen.getPrename();
		classname = xmlgen.getClassname();
	}
	
}
