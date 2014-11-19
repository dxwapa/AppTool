package net.lgpage.androidant.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AndroidXmlGenerate {

	enum SetType {
		Activity, Fragment, Dialog, Pop
	}

	File file;
	String prename;
	String basename;
	String xmlname;
	String classname;
	String context;
	String contextinevent;

	String view;

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public String getBasename() {
		return basename;
	}

	public void setBasename(String basename) {
		this.basename = basename;
	}

	public String getXmlname() {
		return xmlname;
	}

	public void setXmlname(String xmlname) {
		this.xmlname = xmlname;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public AndroidXmlGenerate(File file) {
		super();
		this.file = file;
		initName();
		try {
			initXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AndroidXmlGenerate(File file, SetType type) {
		super();
		this.file = file;
		this.type = type;
		initName();
		switch (type) {
		case Activity:
			view = "this";
			context = "this";
			contextinevent = classname + ".this";
			break;
		case Fragment:
			view = "getView()";
			context = "getActivity()";
			contextinevent = "getActivity()";
			break;
		case Dialog:
			view = "view";
			context = "getContext()";
			contextinevent = "getContext()";
			break;
		case Pop:
			view = "view";
			context = "context";
			contextinevent = "context";
			break;
		default:
			break;
		}
		try {
			initXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SetType getType() {
		return type;
	}

	public void setType(SetType type) {
		this.type = type;
	}

	SetType type = SetType.Activity;

	public void initName() {
		xmlname = file.getName();
		if (xmlname.contains("_")) {
			prename = xmlname.substring(0, xmlname.indexOf("_") + 1);
		} else {
			prename = "";
		}
		xmlname = xmlname.substring(0, xmlname.lastIndexOf("."));
		if (xmlname.startsWith(prename)) {
			basename = xmlname.replace(prename, "");
		} else {
			basename = xmlname;
		}
		StringBuilder sb = new StringBuilder();
		boolean dline = false;
		for (int i = 0; i < basename.length(); i++) {
			char c = basename.charAt(i);
			if (i == 0) {
				sb.append((c + "").toUpperCase());
			} else {
				if (c == '_') {
					dline = true;
				} else {
					if (dline) {
						sb.append((c + "").toUpperCase());
						dline = false;
					} else {
						sb.append(c);
					}
				}
			}
		}
		switch (type) {
		case Activity:
			sb.append("Activity");
			break;
		case Fragment:
			sb.append("Fragment");
			break;
		case Dialog:
			sb.append("Dialog");
			break;
		case Pop:
			sb.append("Pop");
			break;
		default:
			break;
		}
		classname = sb.toString();
	}

	public void initXml() throws Exception {
		viewMap = new HashMap<String, List<String>>();
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder bud = fac.newDocumentBuilder();
		Document doc = bud.parse(file);
		NodeList nodes = doc.getChildNodes();
		dealNodeList(nodes);
	}

	Map<String, List<String>> viewMap;

	public void dealId(Node node) {
		if (node == null)
			return;
		if (node.getAttributes() == null
				|| node.getAttributes().getLength() == 0)
			return;
		Node id = node.getAttributes().getNamedItem("android:id");
		if (id == null)
			return;
		else {
			String value = id.getNodeValue();
			if (value.startsWith("@+id/")) {
				value = value.replace("@+id/", "");
			} else if (value.startsWith("@id/")) {
				value = value.replace("@id/", "");
			}
			if (viewMap.containsKey(node.getNodeName())) {
				List<String> idlist = viewMap.get(node.getNodeName());
				idlist.add(value);
			} else {
				List<String> idlist = new ArrayList<String>();
				idlist.add(value);
				if ("include".equals(node.getNodeName())) {
					viewMap.put("ViewGroup", idlist);
				} else {
					viewMap.put(node.getNodeName(), idlist);
				}
			}
		}
	}

	public void dealNodeList(NodeList list) {
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			dealId(node);
			NodeList childList = node.getChildNodes();
			if (childList != null && childList.getLength() > 0) {
				dealNodeList(childList);
			}
		}
	}

	private class ViewCount {
		String name;
		String realname;
		int count = 1;

		public String getRealname() {
			return realname;
		}

		public void setRealname(String realname) {
			this.realname = realname;
		}

		public ViewCount(String name, String realname) {
			super();
			this.name = name;
			this.realname = realname;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}

	String defines;
	String setViews;
	String setEvents;
	String objTask;
	String listTask;
	String listViewAdapter;
	String gridTask;
	String gridViewAdapter;
	String edittextCheck;

	public String getDefines() {
		StringBuilder defines = new StringBuilder();
		StringBuilder other = new StringBuilder();
		for (Entry<String, List<String>> e : viewMap.entrySet()) {
			String classname = e.getKey();

			if ("EditText".equals(classname)) {
				other.append(getObjTask());
				other.append("\n");
				other.append(getEditTextCheck());
			}

			List<String> idlist = e.getValue();
			Map<String, ViewCount> spitmap = spitMap(idlist);
			Collection<ViewCount> cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					defines.append("	" + classname + " " + v.getRealname()
							+ ";\n");
					if ("ListView".equals(classname)) {
						other.append(getListTask());
						other.append("\n");
						other.append(getListViewAdapter(new File(file
								.getParent() + v.getRealname() + ".xml")));
					}
					if ("GridView".equals(classname)) {
						other.append(getGridTask());
						other.append("\n");
						other.append(getGridViewAdapter(new File(file
								.getParent() + v.getRealname() + ".xml")));
					}
				} else if (v.getCount() > 1) {
					defines.append("	" + classname + " " + v.getName()
							+ "[] = new " + classname + "[" + v.getCount()
							+ "];\n");

					if ("ListView".equals(classname)) {
						for (int i = 0; i < v.getCount(); i++) {
							other.append(getTask("ListTask" + i,
									"listTask" + i, "adapter" + i, "refeshList"
											+ i, "list" + i));
							other.append("\n");
							other.append(getAdapter("LVAdapter" + i, "adapter"
									+ i,
									new File(file.getParent() + v.getName() + i
											+ ".xml"), "list" + i));
						}
					}
					if ("GridView".equals(classname)) {
						for (int i = 0; i < v.getCount(); i++) {
							other.append(getTask("GridTask" + i,
									"gridTask" + i, "gdadapter" + i,
									"refeshGrid" + i, "gdlist" + i));
							other.append("\n");
							other.append(getAdapter("GDAdapter" + i,
									"gdadapter" + i, new File(file.getParent()
											+ v.getName() + i + ".xml"),
									"gdlist" + i));
						}
					}
				}
			}
		}
		defines.append(other);
		return defines.toString();
	}

	public String getSetViews() {
		StringBuilder setViews = new StringBuilder();
		StringBuilder adpdefine = new StringBuilder();
		StringBuilder adpset = new StringBuilder();
		setViews.append("	" + "private void setViews(){\n");
		for (Entry<String, List<String>> e : viewMap.entrySet()) {
			String classname = e.getKey();
			List<String> idlist = e.getValue();
			Map<String, ViewCount> spitmap = spitMap(idlist);
			Collection<ViewCount> cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					setViews.append("		" + v.getRealname() + " = (" + classname
							+ ") " + view + ".findViewById(R.id."
							+ v.getRealname() + ");\n");
					if ("ListView".equals(classname)) {
						adpdefine.append("		adapter = new LVAdapter();\n");
						adpset.append("		" + v.getRealname()
								+ ".setAdapter(adapter);\n");
					}
					if ("GridView".equals(classname)) {
						adpdefine.append("		gdadapter = new GDAdapter();\n");
						adpset.append("		" + v.getRealname()
								+ ".setAdapter(gdadapter);\n");
					}
				} else if (v.getCount() > 1) {
					for (int i = 0; i < v.getCount(); i++) {
						setViews.append("		" + v.getName() + "[" + i + "] = ("
								+ classname + ") " + view
								+ ".findViewById(R.id." + v.getName() + i
								+ ");\n");
						if ("ListView".equals(classname)) {
							adpdefine.append("		adapter" + i
									+ " = new LVAdapter" + i + "();\n");
							adpset.append("		" + v.getName() + "[" + i + "]"
									+ ".setAdapter(adapter" + i + ");\n");
						}
						if ("GridView".equals(classname)) {
							adpdefine.append("		gdadapter" + i
									+ " = new GDAdapter" + i + "();\n");
							adpset.append("		" + v.getRealname()
									+ ".setAdapter(gdadapter" + i + ");\n");
						}
					}
				}
			}
		}
		setViews.append(adpdefine);
		setViews.append(adpset);
		setViews.append("	" + "}\n");
		return setViews.toString();
	}

	public String getSetEvents() {
		StringBuilder setEvents = new StringBuilder();
		setEvents.append("	" + "private void setEvents(){\n");
		for (Entry<String, List<String>> e : viewMap.entrySet()) {
			String classname = e.getKey();
			List<String> idlist = e.getValue();
			Map<String, ViewCount> spitmap = spitMap(idlist);

			Collection<ViewCount> cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					if ("Button".equals(classname)) {
						StringBuilder sb = new StringBuilder();
						sb.append("		"
								+ v.getRealname()
								+ ".setOnClickListener(new OnClickListener() {\n");
						sb.append("\n");
						sb.append("			@Override\n");
						sb.append("			public void onClick(View v) {\n");
						sb.append("			\n");
						sb.append("			}\n");
						sb.append("		});\n");
						setEvents.append(sb);
					} else if ("ListView".equals(classname)) {

					}

				} else if (v.getCount() > 1) {
					for (int i = 0; i < v.getCount(); i++) {
						if ("Button".equals(classname)) {
							StringBuilder sb = new StringBuilder();
							sb.append("		"
									+ v.getName()
									+ "["
									+ i
									+ "]"
									+ ".setOnClickListener(new OnClickListener() {\n");
							sb.append("\n");
							sb.append("			@Override\n");
							sb.append("			public void onClick(View v) {\n");
							sb.append("			\n");
							sb.append("			}\n");
							sb.append("		});\n");
							setEvents.append(sb);
						}
					}
				}
			}
		}
		setEvents.append("	" + "}\n");
		return setEvents.toString();
	}

	public Map<String, ViewCount> spitMap(List<String> idlist) {
		Map<String, ViewCount> spitmap = new HashMap<String, ViewCount>();
		for (int i = 0; i < idlist.size(); i++) {
			String id = idlist.get(i);
			if (id.matches("[A-Za-z_]+\\d+")) {
				String value = id.replaceFirst("\\d+", "");
				ViewCount view = spitmap.get(value);
				if (view == null) {
					view = new ViewCount(value, id);
					spitmap.put(value, view);
				} else {
					view.setCount(view.getCount() + 1);
				}
			} else if (id.matches("[A-Za-z_]+")) {
				ViewCount view = new ViewCount(id, id);
				spitmap.put(id, view);
			}
		}
		return spitmap;
	}

	public String loadData() {
		StringBuilder sb = new StringBuilder();
		sb.append("private void loadData(){\n");
		sb.append("\n");
		sb.append("}\n");
		return sb.toString();
	}

	public String getObjTask() {
		return getTask("PutTask", "putTask", null, "putObj", "obj");
	}

	public String getTask(String classname, String fieldname,
			String adaptername, String refeshname, String listname) {
		addTaskDestory(fieldname,listname);
		StringBuilder sb = new StringBuilder();
		sb.append(classname + " " + fieldname + ";\n");
		sb.append("private void " + refeshname + "() {\n");

		sb.append("		if(" + fieldname + " !=null){\n");
		sb.append("		" + fieldname + ".cancel(true);\n");
		sb.append("		}\n");
		if (type == SetType.Activity) {
			sb.append("		" + fieldname + " = new " + classname + "(this);\n");
		} else if (type == SetType.Fragment) {
			sb.append("		" + fieldname + " = new " + classname
					+ "(getActivity());\n");
		} else if (type == SetType.Dialog) {
			sb.append("		" + fieldname + " = new " + classname + "(context);\n");
		} else if (type == SetType.Pop) {
			sb.append("		" + fieldname + " = new " + classname + "(context);\n");
		}
		sb.append("		" + fieldname + ".execute();\n");
		sb.append("	}\n");
		sb.append("private class " + classname + " extends\n");
		sb.append("			ExeceptionDealAsyncTaskAdapter<Object> {\n");
		sb.append("\n");
		sb.append("		public " + classname + "(Context context) {\n");
		sb.append("			super(context);\n");
		sb.append("		}\n");
		sb.append("\n");
		sb.append("		@Override\n");
		sb.append("		protected Object doInBackgroundX(String... params)\n");
		sb.append("				throws Exception {\n");
		sb.append("			return null;\n");
		sb.append("		}\n");
		sb.append("\n");
		sb.append("		@Override\n");
		sb.append("		protected void onPostExecuteX(Object result) {\n");
		sb.append("			if (result == null) {\n");
		sb.append("				return;\n");
		sb.append("			} else {\n");
		if (adaptername == null) {
			sb.append("				\n");
		} else {

			sb.append("				" + listname + " = result;\n");

			sb.append("				" + adaptername + ".notifyDataSetChanged();\n");
		}

		sb.append("			}\n");
		sb.append("		}\n");
		sb.append("	}\n");
		return sb.toString();
	}

	public String getGridTask() {
		return getTask("GridTask", "gridTask", "gdadapter", "refeshGrid",
				"gdlist");
	}

	public String getListTask() {
		return getTask("ListTask", "listTask", "adapter", "refeshList", "list");
	}

	private StringBuilder destory = new StringBuilder();
	public void addTaskDestory(String taskname,String listname){
		if(destory==null)
			return;
		destory.append("		if("+taskname+"!=null){\n");
		destory.append("			"+taskname+".cancel(true);\n");
		destory.append("			"+taskname+" = null;\n");
		destory.append("			"+listname+" = null;\n");
		destory.append("		}\n");
	}
	
	public String getDestory(){
		StringBuilder sb = new StringBuilder();
		sb.append("@Override\n");
		sb.append("	public void onDestroy() {\n");
		sb.append(destory);
		sb.append("		super.onDestroy();\n");
		sb.append("	}\n");
		destory.setLength(0);
		return sb.toString();
	}
	
	public String getAdapter(String adpclassname, String adpname, File lvFile,
			String listname) {
		String indefine = null;
		String infindView = null;
		String inSetText = null;
		String inEvent = null;
		if (lvFile != null && lvFile.exists()) {
			AndroidXmlLvGenerate gen = new AndroidXmlLvGenerate(lvFile);
			indefine = gen.getDefines();
			infindView = gen.getSetViews();
			inEvent = gen.getSetEvents();
			inSetText = gen.getSetTexts();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("	List " + listname + ";\n");
		sb.append("	" + adpclassname + " " + adpname + ";\n");
		sb.append("	private class " + adpclassname + " extends BaseAdapter {\n");
		sb.append("\n");
		sb.append("		class ViewHolder {\n");
		if (indefine != null)
			sb.append(indefine);
		sb.append("\n");
		sb.append("		}\n");
		sb.append("\n");
		sb.append("		@Override\n");
		sb.append("		public int getCount() {\n");
		sb.append("			if (" + listname + " == null)\n");
		sb.append("				return 0;\n");
		sb.append("			return " + listname + ".size();\n");
		sb.append("		}\n");
		sb.append("\n");
		sb.append("		@Override\n");
		sb.append("		public Object getItem(int position) {\n");
		sb.append("			return null;\n");
		sb.append("		}\n");
		sb.append("\n");
		sb.append("		@Override\n");
		sb.append("		public long getItemId(int position) {\n");
		sb.append("			return 0;\n");
		sb.append("		}\n");
		sb.append("\n");
		sb.append("		@Override\n");
		sb.append("		public View getView(final int position, View cv, ViewGroup parent) {\n");
		sb.append("			if (cv == null) {\n");
		if (type == SetType.Dialog) {
			sb.append("				cv = LayoutInflater.from(" + contextinevent
					+ ").inflate(\n");
		} else {
			sb.append("				cv = " + contextinevent
					+ ".getLayoutInflater().inflate(\n");
		}
		if (lvFile == null) {
			sb.append("						R.layout.lv_login, null);\n");
		} else {
			String name = lvFile.getName();
			name = name.substring(0, name.indexOf("."));
			sb.append("						R.layout." + name + ", null);\n");
		}

		sb.append("				ViewHolder h = new ViewHolder();\n");
		if (infindView != null)
			sb.append(infindView);
		sb.append("\n");
		sb.append("				cv.setTag(h);\n");
		sb.append("			}\n");
		sb.append("			Object obj = list.get(position);\n");
		sb.append("			ViewHolder h = (ViewHolder) cv.getTag();\n");
		if (inSetText != null) {
			sb.append("			if(obj!=null){\n");
			sb.append(inSetText);
			sb.append("			}\n");
		}
		if (inEvent != null) {
			sb.append(inEvent);
		}
		sb.append("\n");
		sb.append("			return cv;\n");
		sb.append("		}\n");
		sb.append("	}\n");
		return sb.toString();
	}

	public String getGridViewAdapter(File file) {
		return getAdapter("GDAdapter", "gdadapter", file, "gdlist");
	}

	public String getListViewAdapter(File file) {
		return getAdapter("LVAdapter", "adapter", file, "list");
	}

	public String getEditTextCheck() {
		StringBuilder sb2 = new StringBuilder();
		sb2.append("	Object obj;\n");
		sb2.append("\n");
		sb2.append("	public void setData(Object obj) {\n");
		sb2.append("		this.obj = obj;\n");
		sb2.append("\n");

		StringBuilder sb = new StringBuilder();
		sb.append("Object copyObj;\n");
		sb.append("public boolean checkEts() {\n");
		sb.append("		if (obj == null){\n");
		sb.append("			copyObj = new Object();\n}");
		sb.append("		else{\n");
		sb.append("			//copyObj = (Object) obj.clone();\n}");
		sb.append("\n");
		List<String> idlist;
		Map<String, ViewCount> spitmap;
		Collection<ViewCount> cs;
		idlist = viewMap.get("TextView");
		if (idlist != null && idlist.size() != 0) {
			spitmap = spitMap(idlist);
			cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					sb2.append("		if(obj!=null){\n");
					sb2.append("			" + v.getRealname()
							+ ".setText(\"\"+obj);\n");
					sb2.append("		}else{\n");
					sb2.append("			" + v.getRealname() + ".setText(\"\");\n");
					sb2.append("		}\n");
				} else if (v.getCount() > 1) {
					for (int i = 0; i < v.getCount(); i++) {
						sb2.append("		if(obj!=null){\n");
						sb2.append("			" + v.getName() + "[" + i
								+ "].setText(\"\"+obj);\n");
						sb2.append("		}else{\n");
						sb2.append("			" + v.getName() + "[" + i
								+ "].setText(\"\");\n");
						sb2.append("		}\n");
					}
				}
			}
		}

		idlist = viewMap.get("EditText");
		if (idlist != null && idlist.size() != 0) {
			spitmap = spitMap(idlist);
			cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					sb.append("		String s" + v.getRealname() + " = "
							+ v.getRealname()
							+ ".getEditableText().toString();\n");
					sb.append("\n");
					sb.append("		if (StringValidateUtil.isNull(s"
							+ v.getRealname() + ")) {\n");
					sb.append("			ToastUtil.noretryToast("+ context + ", \"不能为空\");\n");
					
					sb.append("			return false;\n");
					sb.append("		} else {\n");
					sb.append("\n");
					// sb.append("			copyObj.setGreensinformationname(s[0]);\n");
					sb.append("		}\n");
					sb2.append("		if(obj!=null){\n");
					sb2.append("			" + v.getRealname()
							+ ".setText(\"\"+obj);\n");
					sb2.append("		}else{\n");
					sb2.append("			" + v.getRealname() + ".setText(\"\");\n");
					sb2.append("		}\n");
					sb2.append("		Selection.setSelection(" + v.getRealname()
							+ ".getEditableText(), " + v.getRealname()
							+ ".getEditableText().length());\n");
				} else if (v.getCount() > 1) {
					sb.append("		String s" + v.getName() + "[] = new String["
							+ v.getName() + ".length];\n");

					sb.append("		for (int i = 0; i < s" + v.getName()
							+ ".length; i++) {\n");
					sb.append("			s" + v.getName() + "[i] = " + v.getName()
							+ "[i].getEditableText().toString();\n");
					sb.append("		}\n");

					for (int i = 0; i < v.getCount(); i++) {
						sb.append("\n");
						sb.append("		if (StringValidateUtil.isNull(s"
								+ v.getName() + "[" + i + "])) {\n");
						sb.append("			ToastUtil.noretryToast("+ context + ", \"不能为空\");\n");

						sb.append("			return false;\n");
						sb.append("		} else {\n");
						sb.append("\n");
						// sb.append("			copyObj.setGreensinformationname(s[0]);\n");
						sb.append("		}\n");
						sb2.append("		if(obj!=null){\n");
						sb2.append("			" + v.getName() + "[" + i
								+ "].setText(\"\"+obj);\n");
						sb2.append("		}else{\n");
						sb2.append("			" + v.getName() + "[" + i
								+ "].setText(\"\");\n");
						sb2.append("		}\n");
						sb2.append("		Selection.setSelection(" + v.getName()
								+ "[" + i + "].getEditableText(), "
								+ v.getName() + "[" + i
								+ "].getEditableText().length());\n");
					}
				}
			}
		}

		sb.append("\n");
		sb.append("		return true;\n");
		sb.append("	}\n");
		sb2.append("	}\n");
		sb.append(sb2);
		return sb.toString();
	}
}
