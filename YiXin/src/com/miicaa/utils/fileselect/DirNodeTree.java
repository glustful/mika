package com.miicaa.utils.fileselect;

import java.util.ArrayList;

import android.util.Log;


public class DirNodeTree {
	
	static String TAG = "DirNodeTree";
	
		String infoId;
	    String dirName;
	    String parentId;
	    String time;
	    String creator;
	    public void setTime(String time) {
			this.time = time;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		int numCeng = 0;
	    ArrayList<DirNodeTree> childDirInfos;
	    DirNodeTree parentNode;
	    public DirNodeTree(){
	    	infoId = "all";
	    	dirName = "总目录";
	    }
	    
		public DirNodeTree(String name ,String id){
			infoId = id;
			dirName = name;
		}
		public void addChildNode(DirNodeTree node){
			if(childDirInfos == null){
				childDirInfos = new ArrayList<DirNodeTree>();
			}
			node.setParentNode(this);
			childDirInfos.add(node);
		}
		
		 /* 动态的插入一个新的节点到当前树中 */  
		    public boolean insertJuniorNode(DirNodeTree treeNode) {  
		        String juniorParentId = treeNode.getParentId();  
		        Log.d(TAG, "treeNode.getParentId() : is ..."+juniorParentId);
		        if (this.infoId.equals(juniorParentId)) {  
		           addChildNode(treeNode); 
		            return true;  
		        } else {  
		           ArrayList<DirNodeTree> childList = this.getChildNodes();  
		           if(childList == null){
		        	   return false;
		           }
		           int childNumber = childList.size();  
		          boolean insertFlag;  
		 
		            for (int i = 0; i < childNumber; i++) {  
		                DirNodeTree childNode = childList.get(i);  
		                insertFlag = childNode.insertJuniorNode(treeNode);  
		                if (insertFlag == true)  
		                    return true;  
		            }  
		            return false;  
		        }  
		    }  

		
		public ArrayList<DirNodeTree> getChildNodes(){
			return childDirInfos;
		}
		
		/* 找到一颗树中某个节点 */  
		    public DirNodeTree findTreeNodeById(String id) {  
		       if (this.infoId == id)  
		           return this;  
		        if (childDirInfos == null||childDirInfos.isEmpty()) {  
		            return null;  
		        } else {  
		            int childNumber = childDirInfos.size();  
		            for (int i = 0; i < childNumber; i++) {  
		                DirNodeTree child = childDirInfos.get(i);  
		                DirNodeTree resultNode = child.findTreeNodeById(id);  
		                if (resultNode != null) {  
		                    return resultNode;  
		                }  
		           }  
		            return null;  
		        }  
		    }  
		
		public String getParentId(){
			return parentId;
		}
		
		
		public void setParentNode(DirNodeTree node){
			this.parentNode = node;
		}
		
		public DirNodeTree getParentNode(){
			return this.parentNode;
		}

		public CharSequence getCreator() {
			// TODO Auto-generated method stub
			return creator;
		}

		public CharSequence getTime() {
			// TODO Auto-generated method stub
			return time;
		}

		public String getDirName() {
			return dirName;
		}

		public void setDirName(String dirName) {
			this.dirName = dirName;
		}
		
		
}
