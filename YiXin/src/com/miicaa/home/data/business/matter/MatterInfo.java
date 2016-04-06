package com.miicaa.home.data.business.matter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;

import com.miicaa.common.base.OnEachRow;
import com.miicaa.home.data.storage.CacheDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by apple on 13-12-31.
 */
public class MatterInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8791057284817581539L;
	private static LocalDatabase db;
    private ArrayList<String> labels;
    private String id;
    private String title;
    private String content;
    private String arrangeType;
    private long peopleNum;
    private Date startTime;
    private Date endTime ;
    private Long planTime;
    private String todoId;
    private  String userCode;
    private String  orgCode;
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
    private String lastApproveStatus;
    private String orgcode;
	private String repeatId;
    private String repeatStr;
    private String hasObserved;
    private String todoStatusStr;
    private String todoStatus;
    private long progressNum;
    private long flowRecordNum;
    private Long activityNum;
    private Long discussionNum;
    private String srcCode;
    private Boolean hasAtt;
    private String operaGroup;
    private Long planTimeEnd;
    private String Observe;
    private String autoFinish;
    private boolean hasParentTask = false;
    private String parentTask = "";
    private int childTaskCount = 0;
    private String reportTitle = "";
	private String reportId = "";
	private boolean isFixedProcess = false;
	private boolean IsTodoUser = false;
    public String getAutoFinish() {
		return autoFinish;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public void setAutoFinish(String autoFinish) {
		this.autoFinish = autoFinish;
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

    public static MatterInfo findById(String id)
    {

        SqlCmd cmd = new SqlCmd(MatterInfoSql.tb_name_matter_info);
        SqlCmd sqlCmd = cmd.select("*").where("%s = ?", MatterInfoSql.col_name_id).ps(id);
        ArrayList<ContentValues> rows = db.queryCmd(sqlCmd);
        if (rows != null && rows.size() > 0)
        {
            return MatterInfoSql.fromRow(rows.get(0));
        }
        return null;
    }

    public static ArrayList<ContentValues> matterList(MatterInfo info, OnEachRow onEachRow) {
        return db.queryCmd(MatterInfoSql.findById(info), onEachRow,null);
    }

    public MatterInfo() {
    }

    private MatterInfo(String id,
                       String title,
                       String content,
                       String arrangeType,
                       long peopleNum,
                       long startTime,
                       long endTime,
                       long createTime,
                       String secrecy,
                       String creatorCode,
                       String creatorName,
                       String dataVersion,
                       long lastUpdateTime,
                       String dataType,
                       String srcName,
                       String clientName,
                       String status,
                       String approveTypeName,
                       String lastApproveStatus,
                       String orgcode,
                       String repeatStr) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.arrangeType = arrangeType;
        this.peopleNum = peopleNum;
        this.startTime = SqlCmd.dateOfCol(startTime);
        this.endTime = SqlCmd.dateOfCol(endTime);
        this.createTime = SqlCmd.dateOfCol(createTime);
        this.secrecy = secrecy;
        this.creatorCode = creatorCode;
        this.creatorName = creatorName;
        this.dataVersion = dataVersion;
        this.lastUpdateTime = SqlCmd.dateOfCol(lastUpdateTime);
        this.dataType = dataType;
        this.srcName = srcName;
        this.clientName = clientName;
        this.status = status;
        this.approveTypeName = approveTypeName;
        this.lastApproveStatus = lastApproveStatus;
        this.orgcode = orgcode;
        this.repeatStr = repeatStr;
    }

    public void setOperateGroup(String operateGroup){
    	this.operaGroup = operateGroup;
    }
    
    public String getOperateGroup(){
    	return this.operaGroup;
    }
    
    public  void setUserCode(String dUserCode){
        userCode = dUserCode;
    }
    public String getUserCode(){
        return userCode;
    }
    public void setTodoId(String todoId){
        this.todoId = todoId;
    }
    public String getTodoId(){
        return todoId;
    }
//    public void setOrgCode(String orgcode){
//        this.orgCode = orgcode;
//    }
//    public String getOrgCode(){
//        return orgCode;
//    }
    public void setLabels(ArrayList<String> labels){
        this.labels = labels;
    }
    public ArrayList<String> getLabels(){
        return this.labels;
    }
    public static LocalDatabase getDb() {
        return db;
    }

    public static void setDb(LocalDatabase db) {
        MatterInfo.db = db;
    }

    public void setHasAtt(Boolean hasAtt){
        this.hasAtt = hasAtt;
    }
    public Boolean getHasAtt(){
        return hasAtt;
    }

    public void setRemindTime(Date remindTime){
        this.remindTime = remindTime;
    }

    public Date getRemindTime(){
        return this.remindTime;
    }

    public void setTodoStatus(String status){
        todoStatus = status;
    }

    public String getTodoStatus(){
        return todoStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSrcCode(String srcCode){
        this.srcCode = srcCode;
    }

    public String getSrcCode(){
        return srcCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArrangeType() {
        return arrangeType;
    }

    public void setArrangeType(String arrangeType) {
        this.arrangeType = arrangeType;
    }

    public long getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(long peopleNum) {
        this.peopleNum = peopleNum;
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
    
    public void setActivityNum(Long activityNum){
    	this.activityNum = activityNum;
    }
    
    public Long getActivityNum(){
    	return this.activityNum;
    }
    
    public void setDiscussionNum(Long discussNum){
    	this.discussionNum = discussNum;
    }
    
    public Long getDiscussionNum(){
    	return this.discussionNum;
    }

    public String getApproveTypeName() {
        return approveTypeName;
    }

    public void setApproveTypeName(String approveTypeName) {
        this.approveTypeName = approveTypeName;
    }

    public String getLastApproveStatus() {
        return lastApproveStatus;
    }

    public void setLastApproveStatus(String lastApproveStatus) {
        this.lastApproveStatus = lastApproveStatus;
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

    public String getHasObserved()
    {
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

    public long getProgressNum() {
        return progressNum;
    }

    public void setProgressNum(long progressNum) {
        this.progressNum = progressNum;
    }

    public long getFlowRecordNum() {
        return flowRecordNum;
    }

    public void setFlowRecordNum(long flowRecordNum) {
        this.flowRecordNum = flowRecordNum;
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
    
    public void setPlanTimeEnd(Long planTime){
    	this.planTimeEnd = planTime;
    }
    public Long getPlanTimeEnd(){
    	return planTimeEnd;
    }
    public void setObserve(String obServe){
    	this.Observe = obServe;
    
    }
    
    public String getObServe(){
    	return this.Observe;
    }

	public boolean hasParentTask() {
		// TODO Auto-generated method stub
		return hasParentTask;
	}

	public String getParentTask() {
		// TODO Auto-generated method stub
		return parentTask;
	}

	public int getChildTaskCount() {
		// TODO Auto-generated method stub
		return childTaskCount;
	}
	
	public void setParentTask(boolean flag) {
		this.hasParentTask = flag;
	}

	public void setParentTask(String title) {
		this.parentTask = title;
	}

	public void setChildTaskCount(int count) {
		this.childTaskCount = count;
	}

	public void setReportId(String id){
		this.reportId = id;
	}
	
	
	public String getreportId() {
		
		return this.reportId ;
	}
	
	public void setIsFixedProcess(boolean isFixed){
		isFixedProcess = isFixed;
	}
    
	public boolean getIsFixedProcess(){
		return isFixedProcess;
	}

	public void IsTodoUser(boolean b) {
		this.IsTodoUser = b;
		
	}
	
	public boolean IsTodoUser() {
		return this.IsTodoUser;
		
	}
    
}
