package com.miicaa.home.ui.discover;

import java.util.ArrayList;
import java.util.List;

import com.miicaa.home.BaseKeyVaule;
import com.miicaa.home.ui.menu.SelectPersonInfo;

public class ScreenResult {
	public List<BaseKeyVaule> reportTypeList;
		public List<BaseKeyVaule> tmpReportTypeList;
		public List<SelectPersonInfo>  peopleList;
		public List<SelectPersonInfo> tmpPeopleList;
		
		public ScreenResult(){
			tmpPeopleList = new ArrayList<SelectPersonInfo>();
			tmpReportTypeList = new ArrayList<BaseKeyVaule>();
			reportTypeList = new ArrayList<BaseKeyVaule>();
			peopleList = new ArrayList<SelectPersonInfo>();
		}
		
		public void addReportType(BaseKeyVaule type){
			tmpReportTypeList.add(type);
		}
		
		public void addPeopeList(SelectPersonInfo info){
			tmpPeopleList.add(info);
		}
		
		
		public void convertToType(){
			reportTypeList.clear();
			reportTypeList.addAll(tmpReportTypeList);
			peopleList.clear();
			peopleList.addAll(tmpPeopleList);
		}
		
		public void convertToTmpType(){
			tmpReportTypeList.clear();
			tmpReportTypeList.addAll(reportTypeList);
			tmpPeopleList.clear();
			tmpPeopleList.addAll(peopleList);
		}
		public void addPeopleList(List<SelectPersonInfo> infoList){
			tmpPeopleList.clear();
			tmpPeopleList.addAll(infoList);
		}
		
		public void removeReportType(BaseKeyVaule key){
			tmpReportTypeList.remove(key);
		}
		
		public void removePeopleType(SelectPersonInfo info){
			tmpPeopleList.remove(info);
		}
		
		public void clearPeopleTypes(){
			peopleList.clear();
			tmpPeopleList.clear();
		}
		
		public void clearReportTypes(){
			reportTypeList.clear();
			tmpReportTypeList.clear();
		}
		
		private String getPeople(Boolean isName){
			String s = "";
			for(SelectPersonInfo info : tmpPeopleList){
				if(isName)
				s += info.mName + ",";
				else
				s += info.mCode + ",";
			}
			if(s.length() > 0)
			s = s.substring(0, s.length()-1);
			return s;
		}
		
		public String getPepoleNames(){
			return getPeople(true);
		}
		
		public String getPeopleCodes(){
			return getPeople(false);
		}
		
		private String getReport(Boolean isName){
			String s = "";
			for( BaseKeyVaule keyVaule : tmpReportTypeList){
				if(isName)
					s += keyVaule.mName + ",";
				else
					s += keyVaule.mCode + ",";
			} 
			if(s.length() > 0)
				s = s.substring(0,s.length() -1);
			
			return s;
		}
		
		public String getReportNames(){
			return getReport(true);
		}
		
		public String getReportCodes(){
			return getReport(false);
		}
}
