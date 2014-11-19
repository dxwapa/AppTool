package net.lgpage.androidant.generate;

import java.io.File;

import net.lgpage.androidant.generate.AndroidXmlGenerate.SetType;
import net.lgpage.androidant.util.FileUtil;

public class GenerateFragment {

	public void toFile(String dirpath) throws Exception {
		FileUtil.genFile(generateFragment(), dirpath + classname + ".java");
	}

	public GenerateFragment(File file) {
		super();
		this.file = file;
		xmlgen = new AndroidXmlGenerate(file, SetType.Fragment);
		initName();
		initPart();
	}

	public GenerateFragment(File file, String pkg) {
		super();
		this.file = file;
		this.pkg = pkg;
		xmlgen = new AndroidXmlGenerate(file, SetType.Fragment);
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
	public String generateFragment() {
		StringBuilder sb = new StringBuilder();
		if(pkg!=null)
		sb.append("package " + pkg + ";\n");
		sb.append("\n");
		sb.append("public class " + classname + " extends Fragment {\n");
		sb.append("\n");
		sb.append("	@Override\n");
		sb.append("	public void onCreate(Bundle savedInstanceState) {\n");
		sb.append("		super.onCreate(savedInstanceState);\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("\n");
		sb.append("	@Override\n");
		sb.append("	public View onCreateView(LayoutInflater inflater, ViewGroup container,\n");
		sb.append("			Bundle savedInstanceState) {\n");
		sb.append("		return inflater.inflate(R.layout." + xmlname
				+ ", container, false);\n");
		sb.append("	}\n");
		sb.append("\n");
		sb.append("	@Override\n");
		sb.append("	public void onActivityCreated(Bundle savedInstanceState) {\n");
		sb.append("		super.onActivityCreated(savedInstanceState);\n");
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
		if (setEvents != null)
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
