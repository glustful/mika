package com.yxst.epic.yixin.data.dto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yxst.epic.yixin.data.dto.util.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectContentApp102 extends ObjectContent {

	private static final long serialVersionUID = 8086571385122627894L;

	public String appId;

	public AppInfo appInfo;

	public Head head;

	public List<Body> body = new ArrayList<Body>();

	public List<Operation> operations = new ArrayList<Operation>();

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AppInfo {
		public String appId;
		public String appName;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Head {
		public String content;
		public Long pubTime;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Body {
		public String content;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Operation implements Serializable {
		private static final long serialVersionUID = -624657666302738468L;

		public static String MSG_TYPE_BROWSER = "1";
		public static String MSG_TYPE_REST = "2";

		public String id;
		public String appId;
		public int sort;

		public String content;
//		public String msgType;
		public String operationType;
		public String action;

//		public List<Operation> opertionList = new ArrayList<Operation>();
		public List<Operation> operationList = new ArrayList<Operation>();

		public void addOperation(Operation operation) {
			this.operationList.add(operation);
		}

		@Override
		public String toString() {
			return Utils.writeValueAsString(this);
		}
		
//		@Override
//		public boolean equals(Object o) {
//			if (this == o) {
//				return true;
//			}
//			if (o instanceof Operation) {
//				Operation operation = (Operation) o;
//				return stringEquals(this.id, operation.id)
//						&& stringEquals(this.appId, operation.appId)
//						&& this.sort == operation.sort
//						&& stringEquals(this.content, operation.content)
//						&& stringEquals(this.msgType, operation.msgType)
//						&& stringEquals(this.action, operation.action);
//			}
//			return false;
//		}
//
//		private boolean stringEquals(String str1, String str2) {
//			if(str1 == str2) {
//				return true;
//			}
//			if (str1 != null && str1.equals(str2)) {
//				return true;
//			}
//			return false;
//		}
//
//		@Override
//		public int hashCode() {
//			return super.hashCode();
//		}
	}
}
