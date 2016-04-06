package com.miicaa.home.ui.orgTreeList;

import java.io.Serializable;
import java.util.ArrayList;

import com.miicaa.home.data.business.org.OrgTreeElement;

/**
 * Created by apple on 13-11-25.
 */
public class OrgPersonData implements Serializable {
	private static OrgPersonData instance = new OrgPersonData();
	private OrgTreeElement dataTreeData;
	private ArrayList<OrgTreeElement> selectResultArrayList;

	public static OrgPersonData getInstance() {
		return instance;
	}

	public void creatOrgPersonData(OrgTreeElement root, ArrayList<OrgTreeElement> select) {
		dataTreeData = root;
		selectResultArrayList = select;
		if (selectResultArrayList == null) {
			selectResultArrayList = new ArrayList<OrgTreeElement>();
		}
	}

//	public OrgTreeElement dataCtreatTreeElement() {
//		OrgTreeElement dataTree = new OrgTreeElement();
//		ArrayList<OrgTreeElement> dataArray = new ArrayList<OrgTreeElement>();
//		for (int i = 0; i < 3; i++) {
//			OrgTreeElement sectionData = new OrgTreeElement();
//			sectionData.setParent(dataTree.getId());
//			if (i == 0) {
//				sectionData.setName("产品部");
//			} else if (i == 1) {
//				sectionData.setName("应用开发部");
//			} else if (i == 2) {
//				sectionData.setName("技术研发部");
//			}
//			sectionData.setType(OrgTreeElement.Type.UNIT);
//			if (i < 10) {
//				sectionData.setId(sectionData.getParent() + "0" + String.valueOf(i));
//			} else {
//				sectionData.setId(sectionData.getParent() + String.valueOf(i));
//			}
//			ArrayList<OrgTreeElement> cellDataArray = new ArrayList<OrgTreeElement>();
//			for (int j = 0; j < 3; j++) {
//				OrgTreeElement cellDataMap = new OrgTreeElement();
//				cellDataMap.setName(sectionData.getName() + String.valueOf(j) + "组");
//				cellDataMap.setParent(sectionData.getId());
//				cellDataMap.setType(OrgTreeElement.Type.UNIT);
//				if (j < 10) {
//					cellDataMap.setId(cellDataMap.getParent() + "0" + String.valueOf(j));
//				} else {
//					cellDataMap.setId(cellDataMap.getParent() + String.valueOf(j));
//				}
//				ArrayList<OrgTreeElement> subCellDataArray = new ArrayList<OrgTreeElement>();
//				for (int k = 0; k < 3; k++) {
//					OrgTreeElement subCellDataMap = new OrgTreeElement();
//					if (k == 0) {
//						subCellDataMap.setName(cellDataMap.getName() + "--张三");
//					} else if (k == 1) {
//						subCellDataMap.setName(cellDataMap.getName() + "--李四");
//					} else if (k == 2) {
//						subCellDataMap.setName(cellDataMap.getName() + "--王五");
//					}
//					subCellDataMap.setType(OrgTreeElement.Type.USER);
//					subCellDataMap.setSelect(false);
//					subCellDataMap.setLevel(3);
//					subCellDataMap.setChildren(new ArrayList<OrgTreeElement>());
//					subCellDataMap.setParent(cellDataMap.getId());
//					if (k < 10) {
//						subCellDataMap.setId(subCellDataMap.getParent() + "0" + String.valueOf(k));
//					} else {
//						subCellDataMap.setId(subCellDataMap.getParent() + String.valueOf(k));
//					}
//					subCellDataArray.add(subCellDataMap);
//				}
//				if (j < 2) {
//					cellDataMap.setChildren(subCellDataArray);
//				} else {
//					cellDataMap.setChildren(new ArrayList<OrgTreeElement>());
//				}
//				cellDataMap.setSelect(false);
//				cellDataMap.setLevel(2);
//				cellDataArray.add(cellDataMap);
//			}
//			sectionData.setSelect(false);
//			sectionData.setChildren(cellDataArray);
//			sectionData.setLevel(1);
//			dataArray.add(sectionData);
//		}
//		dataTree.setName("root");
//		dataTree.setChildren(dataArray);
//		dataTree.setSelect(false);
//		dataTree.setLevel(0);
//		return dataTree;
//	}

	public ArrayList<OrgTreeElement> getReturnDataTree(String id, int level) {
//		ArrayList<String> idArray = new ArrayList<String>();
//		for (int i = 0; i <= level; i++) {
//			idArray.add(id.substring(i * 2, (i + 1) * 2));
//		}
		String[] idArray = id.split("/");
		ArrayList<OrgTreeElement> returnTreeData = getSubTree(dataTreeData.getChildren(), idArray, 1);
		return returnTreeData;
	}

	private ArrayList<OrgTreeElement> getSubTree(ArrayList<OrgTreeElement> data, String[] idArray, int index) {
		if (index < idArray.length - 1) {
			int intId = Integer.parseInt(idArray[index]);
			OrgTreeElement cellMap = data.get(intId);
			return getSubTree(cellMap.getChildren(), idArray, index + 1);
		} else {
			if (idArray.length == 1) {
				return data;
			} else {
				int intId = Integer.parseInt(idArray[index]);
				OrgTreeElement retrunCellMap = data.get(intId);
				ArrayList<OrgTreeElement> returnTreeData = retrunCellMap.getChildren();
				return returnTreeData;
			}
		}
	}

	public String getSelectResultStr() {
		StringBuffer stringBuffer = new StringBuffer();
		getSelectResultArrayList();
		for (int i = 0; i < selectResultArrayList.size(); i++) {
			if (i != 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(selectResultArrayList.get(i).getName());
		}
		return stringBuffer.toString();
	}

	public ArrayList<OrgTreeElement> getSelectResultArrayList() {
		selectResultArrayList.clear();
		getSubTreeSelectResult(dataTreeData.getChildren());
		return selectResultArrayList;
	}

	private void getSubTreeSelectResult(ArrayList<OrgTreeElement> data) {
		for (int i = 0; i < data.size(); i++) {
			OrgTreeElement subOrgTreeElement = data.get(i);
			if (subOrgTreeElement.isSelect()) {
				selectResultArrayList.add(subOrgTreeElement);
			}
			if (subOrgTreeElement.hasChild()) {
				getSubTreeSelectResult(subOrgTreeElement.getChildren());
			}
		}
	}
}
