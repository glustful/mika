package com.miicaa.home.data.business.org;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.Debugger;
import com.miicaa.common.base.JSONValue;
import com.miicaa.common.base.OnEachRow;
import com.miicaa.common.base.OnTransaction;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.OnFinish;
import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.service.CacheCtrlSrv;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;
import com.miicaa.home.ui.HomeApplication;

/**
 * Created by Administrator on 13-12-20.
 */
public class UserInfo {
	public static LocalDatabase db;

	public static void init() {
		db = ConfigDatabase.instance();

		if (!db.isTableExsit(UserInfoSql.tb_name_user_info)) {
			db.execCmd(UserInfoSql.createTable());
		}
	}

	public static void showAll() {
		ArrayList<ContentValues> rows = db.queryCmd(UserInfoSql.showAll());
		SqlCmd.showRows(rows);
	}

	public static void cacheAll() {
		OrgRequest.requestUsers(new OnBusinessResponse() {
			@Override
			public void onProgress(ProgressMessage msg) {

			}

			@Override
			public void onResponse(ResponseData data) {
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					final JSONArray userList = data.getJsonArray();
					db.onTransaction(new OnTransaction() {
						@Override
						public void transaction() {
							for (int i = 0; i < userList.length(); i++) {
								JSONValue user = JSONValue.from(userList.optJSONObject(i));
								if (user != null) {
									new UserInfo(
											user.getLong(UserInfoSql.col_name_id),
											user.getString(UserInfoSql.col_name_code),
											user.getString(UserInfoSql.col_name_name),
											user.getString(UserInfoSql.col_name_email),
											user.getString(UserInfoSql.col_name_cellphone),
											user.getLong(UserInfoSql.col_name_status),
											user.getString(UserInfoSql.col_name_avatar),
											user.getLong(UserInfoSql.col_name_create_time),
											user.getString(UserInfoSql.col_name_org_code),
											user.getString(UserInfoSql.col_name_gender),
											user.getLong(UserInfoSql.col_name_birthday),
											user.getString(UserInfoSql.col_name_qq),
											user.getString(UserInfoSql.col_name_phone),
											user.getString(UserInfoSql.col_name_addr)
									).save();
								}
							}
						}
					});
				}
			}
		}, AccountInfo.instance().getLastOrgInfo());
	}

	public static UserInfo findByCode(String code) {
		ContentValues row = db.firstRow(UserInfoSql.findByCode(code));
		return UserInfoSql.fromRow(row);
	}

    public static UserInfo findById(Long id) {
        ContentValues row = db.firstRow(UserInfoSql.findById(id));
        return UserInfoSql.fromRow(row);
    }
    
    public static String findByIdForUserCode(Long id){
    	ContentValues row = db.firstRow(UserInfoSql.findById(id));
    	if(row != null){
    	return row.getAsString(UserInfoSql.col_name_code);
    	}return "";    	
    }
    
    public static String findByCodeForUserName(String userCode){
    	ContentValues row = db.firstRow(UserInfoSql.findByCode(userCode));
    	if(row != null){
    	return row.getAsString(UserInfoSql.col_name_name);
    	}return "";
    }

	public static UserInfo findByEmailAndOrgCode(String email, String code) {
		ContentValues row = db.firstRow(UserInfoSql.findByEmailAndOrgCode(email, code));
		return UserInfoSql.fromRow(row);
	}

	public static ArrayList<ContentValues> usersInOrg(OrgInfo info, OnEachRow onEachRow, Object cbData) {
		return db.queryCmd(UserInfoSql.usersInOrg(info), onEachRow, cbData);
	}
	

	public static ArrayList<ContentValues> usersInUnit(UnitInfo info, OnEachRow onEachRow, Object cbData) {
		return db.queryCmd(UserInfoSql.usersInUnit(info), onEachRow, cbData);
	}

	public static ArrayList<ContentValues> usersInGroup(GroupInfo info, OnEachRow onEachRow, Object cbData) {
		return db.queryCmd(UserInfoSql.usersInGroup(info), onEachRow, cbData);
	}

	public static void userCommit(String id, final String code, final String key, Object value, final OnFinish onFinish) {
		if (CacheCtrlSrv.isNetBreak) {    // 断网状态下
			onFinish.onFailed(HomeApplication.getInstance().getResources().getString(R.string.net_disconnection));
		} else {    // 联网状态下
			final String val = Tools.stringOfObject(value);
			UserRequset.requestPersonCommit(new OnBusinessResponse() {
				@Override
				public void onProgress(ProgressMessage msg) {
				}

				@Override
				public void onResponse(ResponseData data) {
					if (data.getResultState() == ResponseData.ResultState.eSuccess) {
						// 更新缓存
						UserInfo user = findByCode(code);
						user.updateField(key, val);

						onFinish.onSuccess(null);
					} else {
						onFinish.onFailed(data.getMsg());
					}
				}
			}, id, code, key, val);
		}
	}

	public UserInfo() {
	}

	private UserInfo(long id,
					 String code,
					 String name,
					 String email,
					 String cellphone,
					 long status,
					 String avatar,
					 String orgCode,
					 String gender,
					 String qq,
					 String phone,
					 String addr) {
		this.id = id;
		this.code = code;
		setName(name);
		this.email = email;
		this.cellphone = cellphone;
		this.status = status;
		this.avatar = avatar;
		this.createTime = DateHelper.getDate();
		this.orgCode = orgCode;
		this.gender = gender;
		this.birthday = DateHelper.getDate();
		this.qq = qq;
		this.phone = phone;
		this.addr = addr;
	}

	public UserInfo(long id,
					String code,
					String name,
					String email,
					String cellphone,
					long status,
					String avatar,
					long createTime,
					String orgCode,
					String gender,
					long birthday,
					String qq,
					String phone,
					String addr) {
		this.id = id;
		this.code = code;
		setName(name);
		this.email = email;
		this.cellphone = cellphone;
		this.status = status;
		this.avatar = avatar;
		this.createTime = SqlCmd.dateOfCol(createTime);
		this.orgCode = orgCode;
		this.gender = gender;
		this.birthday = SqlCmd.dateOfCol(birthday);
		this.qq = qq;
		this.phone = phone;
		this.addr = addr;
	}

	public Boolean exist() {
		return db.hasRow(UserInfoSql.exist(this));
	}

	public Boolean save() {
		if (exist()) {
			Debugger.d("UserInfo Update", toString(false));
			return db.execCmd(UserInfoSql.update(this));
		} else {
			Debugger.d("UserInfo Insert", toString(false));
			return db.execCmd(UserInfoSql.insertInto(this));
		}
	}

	public Boolean delete() {
		Debugger.d("UserInfo Delete", toString(false));
		return db.execCmd(UserInfoSql.delete(this));
	}

    public static Boolean deleteByOrgCode(String orgCode){
        Debugger.d("UserInfo Delete", orgCode);
        return db.execCmd(UserInfoSql.deleteByOrgCode(orgCode));
    }

	public void updateField(String key, String val) {
		if (key.equals("name")) {
			setName(val);
			save();
		} else if (key.equals("gender")) {
			setGender(val);
			save();
		} else if (key.equals("qq")) {
			setQq(val);
			save();
		} else if (key.equals("cellphone")) {
			setCellphone(val);
			save();
		} else if (key.equals("phone")) {
			setPhone(val);
			save();
		} else if (key.equals("addr")) {
			setAddr(val);
			save();
		} else if (key.equals("birthday")) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				setBirthday(df.parse(val));
				save();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (key.equals("unit")) {
			UnitUserInfo.delete(this);

			String[] units = val.split(",");
			for (String unitCode : units) {
				new UnitUserInfo(0, unitCode, code, 0, 0).save();
			}
		} else if (key.equals("group")) {
			GroupUserInfo.delete(this);

			String[] groups = val.split(",");
			for (String gcode : groups) {
				new GroupUserInfo(0, "", gcode, code, 0).save();
			}
		}
	}

	public ArrayList<UnitInfo> getUnits() {
		return UnitUserInfo.unitsForUser(this);
	}

	public ArrayList<GroupInfo> getGroups() {
		return GroupUserInfo.groupsForUser(this);
	}

	public String toString(boolean isFull) {
		if (isFull) {
			return String.format("[%d][%s][%s][%s][%s][%s][%s][%d][%s][%s][%s][%s][%s][%s][%s][%s]",
					id,
					code,
					name,
					nameFPY,
					namePY,
					email,
					cellphone,
					status,
					avatar,
					createTime.toString(),
					orgCode,
					gender,
					birthday.toString(),
					qq,
					phone,
					addr);
		} else {
			return String.format("[%d][%s][%s][%s][%s]",
					id,
					code,
					name,
					avatar,
					gender);
		}
	}

	private long id;
	private String code;
	private String name;
	private String nameFPY;
	private String namePY;
	private String email;
	private String cellphone;
	private long status;
	private String avatar;
	private Date createTime;
	private String orgCode;
	private String gender;
	private Date birthday;
	private String qq;
	private String phone;
	private String addr;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.nameFPY = Tools.getPingYinFirst(this.name);
		Log.d("UserInfo", "name and namefpy and namepy"+"..."+this.name+"..."+this.nameFPY+"..."+this.namePY);
		this.namePY = Tools.getPingYin(this.name);
	
	}

	public void setNames(String name, String nameFPY, String namePY) {
		this.name = name;
		this.nameFPY = nameFPY;
		this.namePY = namePY;
	}

	public String getNameFPY() {
		return nameFPY;
	}

	public String getNamePY() {
		return namePY;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@Override
	public String toString(){
		return this.name + "="+this.email;
	}
}
