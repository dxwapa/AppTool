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

public class GenerateSetView {

	enum SetType {
		Activity, Fragment
	}

	public SetType getType() {
		return type;
	}

	public void setType(SetType type) {
		this.type = type;
	}

	SetType type = SetType.Activity;

	public String getSetView(File file) throws Exception {
		viewMap = new HashMap<String, List<String>>();
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder bud = fac.newDocumentBuilder();
		Document doc = bud.parse(file);
		NodeList nodes = doc.getChildNodes();
		excuteAll(nodes);
		String setView = generateAllViews(viewMap);
		return setView;
	}

	Map<String, List<String>> viewMap;

	public void displayId(Node node) {
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
				if("include".equals(node.getNodeName())){
					viewMap.put("ViewGroup", idlist);
				}else{
					viewMap.put(node.getNodeName(), idlist);
				}
			}
		}
	}

	public void excuteAll(NodeList list) {
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			displayId(node);
			NodeList childList = node.getChildNodes();
			if (childList != null && childList.getLength() > 0) {
				excuteAll(childList);
			}
		}
	}

	class ViewTmpStruct {
		String name;
		String realname;
		int count = 1;

		public String getRealname() {
			return realname;
		}

		public void setRealname(String realname) {
			this.realname = realname;
		}

		public ViewTmpStruct(String name,String realname) {
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

	public String generateAllViews(Map<String, List<String>> viewMap) {
		List<String> defines = new ArrayList<String>();
		List<String> setViews = new ArrayList<String>();
		Iterator<Entry<String, List<String>>> entrys = viewMap.entrySet()
				.iterator();
		while (entrys.hasNext()) {
			Entry<String, List<String>> en = entrys.next();
			TmpStruct ts = generateViews(en.getKey(), en.getValue());
			defines.add(ts.getDefines());
			setViews.add(ts.getSetViews());
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < defines.size(); i++) {
			sb.append(defines.get(i));
		}
		sb.append("	"+"private void setViews(){\n");
		for (int i = 0; i < setViews.size(); i++) {
			sb.append(setViews.get(i));
		}
		sb.append("	"+"}\n");
		return sb.toString();
	}

	private class TmpStruct {
		String defines;
		String setViews;

		public TmpStruct(String defines, String setViews) {
			super();
			this.defines = defines;
			this.setViews = setViews;
		}

		public String getDefines() {
			return defines;
		}

		public void setDefines(String defines) {
			this.defines = defines;
		}

		public String getSetViews() {
			return setViews;
		}

		public void setSetViews(String setViews) {
			this.setViews = setViews;
		}

	}

	public TmpStruct generateViews(String classname, List<String> idlist) {
		StringBuilder defines = new StringBuilder();
		StringBuilder setViews = new StringBuilder();
		Map<String, ViewTmpStruct> namemap = new HashMap<String, ViewTmpStruct>();
		for (int i = 0; i < idlist.size(); i++) {
			String id = idlist.get(i);
			if (id.matches("[A-Za-z_]+\\d+")) {
				String value = id.replaceFirst("\\d+", "");
				ViewTmpStruct view = namemap.get(value);
				if (view == null) {
					view = new ViewTmpStruct(value,id);
					namemap.put(value, view);
				} else {
					view.setCount(view.getCount() + 1);
				}
			}else if(id.matches("[A-Za-z_]+")){
				ViewTmpStruct view = new ViewTmpStruct(id,id);
				namemap.put(id, view);
			}
		}

		Collection<ViewTmpStruct> cs = namemap.values();
		for (ViewTmpStruct v : cs) {
			if (v.getCount() == 1) {
				defines.append("	"+classname + " " + v.getRealname() + ";\n");
				if (type == SetType.Activity) {
					setViews.append("		"+v.getRealname() + " = ("+classname+") findViewById(R.id."
							+ v.getRealname() + ");\n");
				} else if (type == SetType.Fragment) {
					setViews.append("		"+v.getRealname()
							+ " = ("+classname+") getActivity().findViewById(R.id."
							+ v.getRealname() + ");\n");
				}

			} else if (v.getCount() > 1) {
				defines.append("	"+classname + " " + v.getName() + "[] = new "
						+ classname + "[" + v.getCount() + "];\n");
				for (int i = 0; i < v.getCount(); i++) {
					if (type == SetType.Activity) {
						setViews.append("		"+v.getName() + "[" + i
								+ "] = ("+classname+") findViewById(R.id." + v.getName() + i
								+ ");\n");
					} else if (type == SetType.Fragment) {
						setViews.append("		"+v.getName() + "[" + i
								+ "] = ("+classname+") getActivity().findViewById(R.id."
								+ v.getName() + i + ");\n");
					}
				}
			}
		}

		TmpStruct res = new TmpStruct(defines.toString(), setViews.toString());
		return res;
	}
}
