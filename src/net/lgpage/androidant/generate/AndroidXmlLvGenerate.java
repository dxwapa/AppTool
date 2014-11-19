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

public class AndroidXmlLvGenerate {

	enum SetType {
		ListView, GridView
	}

	File file;
	String prename;// lv_
	String basename;//
	String xmlname;//

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

	public AndroidXmlLvGenerate(File file) {
		super();
		this.file = file;
		initName();
		try {
			initXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AndroidXmlLvGenerate(File file, SetType type) {
		super();
		this.file = file;
		this.type = type;
		initName();
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

	SetType type = SetType.ListView;

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
			List<String> idlist = e.getValue();
			Map<String, ViewCount> spitmap = spitMap(idlist);
			Collection<ViewCount> cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					defines.append("	" + classname + " " + v.getRealname()
							+ ";\n");
				} else if (v.getCount() > 1) {
					defines.append("	" + classname + " " + v.getName()
							+ "[] = new " + classname + "[" + v.getCount()
							+ "];\n");
				}
			}
		}

		defines.append(other);
		return defines.toString();
	}

	public String getSetViews() {
		StringBuilder setViews = new StringBuilder();
		for (Entry<String, List<String>> e : viewMap.entrySet()) {
			String classname = e.getKey();
			List<String> idlist = e.getValue();
			Map<String, ViewCount> spitmap = spitMap(idlist);
			Collection<ViewCount> cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					String view = "cv";
					setViews.append("		h." + v.getRealname() + " = ("
							+ classname + ") " + view + ".findViewById(R.id."
							+ v.getRealname() + ");\n");
					if ("ListView".equals(classname)) {
						setViews.append("		adapter = new LVAdapter();\n");
						setViews.append("		" + v.getRealname()
								+ ".setAdapter(adapter);\n");
					}
				} else if (v.getCount() > 1) {
					String view = "cv";
					for (int i = 0; i < v.getCount(); i++) {
						setViews.append("		h." + v.getName() + "[" + i
								+ "] = (" + classname + ") " + view
								+ ".findViewById(R.id." + v.getName() + i
								+ ");\n");
						if ("ListView".equals(classname)) {
							setViews.append("		adapter = new LVAdapter();\n");
							setViews.append("		" + v.getName() + "[" + i + "]"
									+ ".setAdapter(adapter);\n");
						}

					}
				}
			}
		}
		return setViews.toString();
	}

	public String getSetTexts() {
		StringBuilder setTexts = new StringBuilder();
		for (Entry<String, List<String>> e : viewMap.entrySet()) {
			String classname = e.getKey();
			List<String> idlist = e.getValue();
			Map<String, ViewCount> spitmap = spitMap(idlist);
			Collection<ViewCount> cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					if ("TextView".equals(classname)) {
						setTexts.append("		if(obj!=null){\n");
						setTexts.append("			h." + v.getRealname()
								+ ".setText(obj+\"\");\n");
						setTexts.append("		}else{\n");
						setTexts.append("			h." + v.getRealname()
								+ ".setText(\"\");\n");
						setTexts.append("		}\n");
					}

				} else if (v.getCount() > 1) {
					String view = "cv";
					for (int i = 0; i < v.getCount(); i++) {
						if ("TextView".equals(classname)) {
							setTexts.append("		if(obj!=null){\n");
							setTexts.append("			h." + v.getName() + "[" + i
									+ "].setText(obj+\"\");\n");
							setTexts.append("		}else{\n");
							setTexts.append("			h." + v.getName() + "[" + i
									+ "].setText(\"\");\n");
							setTexts.append("		}\n");
						}
					}
				}
			}
		}
		return setTexts.toString();
	}

	public String getSetEvents() {
		StringBuilder setEvents = new StringBuilder();
		for (Entry<String, List<String>> e : viewMap.entrySet()) {
			String classname = e.getKey();
			List<String> idlist = e.getValue();
			Map<String, ViewCount> spitmap = spitMap(idlist);

			Collection<ViewCount> cs = spitmap.values();
			for (ViewCount v : cs) {
				if (v.getCount() == 1) {
					if ("Button".equals(classname)) {
						StringBuilder sb = new StringBuilder();
						sb.append("		h."
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
							sb.append("		h."
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

}
