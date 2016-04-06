package com.miicaa.home.ui.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;

import com.miicaa.common.base.OnEachRow;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.data.business.matter.MatterInfoSql;
import com.miicaa.home.data.storage.CacheDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;

public class ReportDetailInfo implements Serializable{
	private static LocalDatabase db;
	private ArrayList<String> labels;
	private ArrayList<String> labelsId;
	
	private String id;
	private String reportId;
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	private String title;
	private Long planTime;
	private String todoId;
	private String arrangeType;
	private String userCode;
	private String orgCode;
	private String secrecy;
	private String creatorCode;
	private Date remindTime;

	private String creatorName;
	private String dataVersion;
	private Date createTime;
	private Date lastUpdateTime;
	private String dataType;
	private String srcName;
	private String clientName;
	private String status;
	private String approveTypeName;
	private String orgcode;
	private String repeatId;
	private String repeatStr;
	private String hasObserved;
	private String todoStatusStr;
	private String todoStatus;
	private String srcCode;
	private Boolean hasAtt;
	private String operaGroup;
	private Long planTimeEnd;
	private String Observe;
	private String commenter;
	private long commentNum;
	private String autoFinish;
	private Date startTime;
	private Date endTime ;
	private String range;
	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getAutoFinish() {
		return autoFinish;
	}

	public void setAutoFinish(String autoFinish) {
		this.autoFinish = autoFinish;
	}
    public long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}

	public Long getActivityNum() {
		return activityNum;
	}

	public void setActivityNum(Long activityNum) {
		this.activityNum = activityNum;
	}

	public Long getDiscussionNum() {
		return discussionNum;
	}

	public void setDiscussionNum(long discussionNum) {
		this.discussionNum = discussionNum;
	}

	private long activityNum;
    private long discussionNum;
	private String reportType;
	private long summriazeStartTime;
	private long summriazeEndTime;
	private long tomorrowStartTime;
	private long tomorrowEndTime;
	public long getSummriazeStartTime() {
		return summriazeStartTime;
	}

	public void setSummriazeStartTime(long summriazeStartTime) {
		this.summriazeStartTime = summriazeStartTime;
	}

	public long getSummriazeEndTime() {
		return summriazeEndTime;
	}

	public void setSummriazeEndTime(long summriazeEndTime) {
		this.summriazeEndTime = summriazeEndTime;
	}

	public long gettomorrowStartTime() {
		return tomorrowStartTime;
	}

	public void settomorrowStartTime(long planStartTime) {
		this.tomorrowStartTime = planStartTime;
	}

	public long getTomorrowEndTime() {
		return tomorrowEndTime;
	}

	public void settomorrowEndTime(long planEndTime) {
		this.tomorrowEndTime = planEndTime;
	}

	private ArrayList<String> todayList;
	private ArrayList<String> tomorrowList;
	private String desc = "";
	
	public ArrayList<String> getTodayList() {
		return todayList;
	}

	public void setTodayList(ArrayList<String> todayList) {
		this.todayList = todayList;
	}

	public ArrayList<String> getTomorrowList() {
		return tomorrowList;
	}

	public void setTomorrowList(ArrayList<String> tomorrowList) {
		this.tomorrowList = tomorrowList;
	}

	public String getReportType() {
		if(reportType.equals(WorkReportActivity.REPORT_TYPE_DAY))
		return "日报";
		else if(reportType.equals(WorkReportActivity.REPORT_TYPE_WEEK))
			return "周报";
		else if(reportType.equals(WorkReportActivity.REPORT_TYPE_MONTH))
			return "月报";
		else if(reportType.equals(WorkReportActivity.REPORT_TYPE_CUSTOM))
			return "自定义报告";
		else
			return "工作报告";
	}
	
	public String getReportType(int type) {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public static void init() {
		db = CacheDatabase.instance();

		if (!db.isTableExsit(MatterInfoSql.tb_name_matter_info)) {
			db.execCmd(MatterInfoSql.createTable());

			initTest();
		}
	}

	public static void initTest() {
		showAll();
	}

	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(MatterInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static MatterInfo findById(String id) {

		SqlCmd cmd = new SqlCmd(MatterInfoSql.tb_name_matter_info);
		SqlCmd sqlCmd = cmd.select("*")
				.where("%s = ?", MatterInfoSql.col_name_id).ps(id);
		ArrayList<ContentValues> rows = db.queryCmd(sqlCmd);
		if (rows != null && rows.size() > 0) {
			return MatterInfoSql.fromRow(rows.get(0));
		}
		return null;
	}

	public static ArrayList<ContentValues> matterList(MatterInfo info,
			OnEachRow onEachRow) {
		return db.queryCmd(MatterInfoSql.findById(info), onEachRow, null);
	}

	public void setOperateGroup(String operateGroup) {
		this.operaGroup = operateGroup;
	}

	public String getOperateGroup() {
		return this.operaGroup;
	}

	public void setUserCode(String dUserCode) {
		userCode = dUserCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setTodoId(String todoId) {
		this.todoId = todoId;
	}

	public String getTodoId() {
		return todoId;
	}

	// public void setOrgCode(String orgcode){
	// this.orgCode = orgcode;
	// }
	// public String getOrgCode(){
	// return orgCode;
	// }
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	public ArrayList<String> getLabels() {
		return this.labels;
	}

	public static LocalDatabase getDb() {
		return db;
	}

	public static void setDb(LocalDatabase db) {
		ReportDetailInfo.db = db;
	}

	public void setHasAtt1(Boolean hasAtt) {
		this.hasAtt = hasAtt;
	}

	public Boolean getHasAtt() {
		return hasAtt;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public Date getRemindTime() {
		return this.remindTime;
	}

	public void setTodoStatus(String status) {
		todoStatus = status;
	}

	public String getTodoStatus() {
		return todoStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}

	public String getSrcCode() {
		return srcCode;
	}

	

	public String getSecrecy() {
		return secrecy;
	}

	public void setSecrecy(String secrecy) {
		this.secrecy = secrecy;
	}

	public String getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getApproveTypeName() {
		return approveTypeName;
	}

	public void setApproveTypeName(String approveTypeName) {
		this.approveTypeName = approveTypeName;
	}

	

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getRepeatId() {
		return repeatId;
	}

	public void setRepeatId(String repeatId) {
		this.repeatId = repeatId;
	}

	public String getRepeatStr() {
		return repeatStr;
	}

	public void setRepeatStr(String repeatStr) {
		this.repeatStr = repeatStr;
	}

	public String getHasObserved() {
		return hasObserved;
	}

	public void setHasObserved(String hasObserved) {
		this.hasObserved = hasObserved;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTodoStatusStr() {
		return todoStatusStr;
	}

	

	public void setTodoStatusStr(String todoStatusStr) {
		this.todoStatusStr = todoStatusStr;
	}

	public Long getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Long planTime) {
		this.planTime = planTime;
	}

	public void setPlanTimeEnd(Long planTime) {
		this.planTimeEnd = planTime;
	}

	public Long getPlanTimeEnd() {
		return planTimeEnd;
	}

	public void setObserve(String obServe) {
		this.Observe = obServe;

	}

	public String getObServe() {
		return this.Observe;
	}
	
	public void setCommenter(String name){
		this.commenter = name;
	}

	public String getCommenter() {
		
		return this.commenter;
	}
	
	 public String getArrangeType() {
	        return arrangeType;
	    }

	    public void setArrangeType(String arrangeType) {
	        this.arrangeType = arrangeType;
	    }
	    
	    public Date getStartTime() {
	        return startTime;
	    }

	    public void setStartTime(Date startTime) {
	        this.startTime = startTime;
	    }

	    public Date getEndTime() {
	        return endTime;
	    }

	    public void setEndTime(Date endTime) {
	        this.endTime = endTime;
	    }

		public void setLabelsId(ArrayList<String> labelsId) {
			this.labelsId = labelsId;
			
		}
		public ArrayList<String> getLabelsId() {
			return labelsId;
		}

		public void setDesc(String desc){
			this.desc = desc.replaceAll("<br>", "\n");
		}
		
		public String getDesc() {
			
			return this.desc ;
		}

}
