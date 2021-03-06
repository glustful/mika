package com.yxst.epic.yixin.data.dto.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 联系人信息<br/>
 * 群信息<br/>
 * 群成员（联系人）信息<br/>
 * 
 * @author liuxue
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member implements Serializable {

	private static final long serialVersionUID = -601306827832038996L;

	public static final String SUFFIX_TENANT = "@tenant";
	public static final String SUFFIX_USER = "@user";
	public static final String SUFFIX_ORG = "@org";
	public static final String SUFFIX_QUN = "@qun";
	public static final String SUFFIX_APP = "@app";

	@JsonIgnore(true)
	public static boolean isTypeQun(String userName) {
		return userName.endsWith(SUFFIX_QUN);
	}

	@JsonIgnore(true)
	public static boolean isTypeUser(String userName) {
		return userName.endsWith(SUFFIX_USER);
	}
	
	@JsonIgnore(true)
	public static boolean isTypeApp(String userName) {
		return userName.endsWith(SUFFIX_APP);
	}

	@JsonIgnore(true)
	public boolean isTypeTenant() {
		return this.UserName.endsWith(SUFFIX_TENANT);
	}

	@JsonIgnore(true)
	public boolean isTypeUser() {
		return this.UserName.endsWith(SUFFIX_USER);
	}

	@JsonIgnore(true)
	public boolean isTypeOrg() {
		return this.UserName.endsWith(SUFFIX_ORG);
	}

	@JsonIgnore(true)
	public boolean isTypeQun() {
		return this.UserName.endsWith(SUFFIX_QUN);
	}
	
	@JsonIgnore(true)
	public boolean isTypeApp() {
		return this.UserName.endsWith(SUFFIX_APP);
	}

	/**
	 * 用户唯一ID
	 */
	@JsonProperty("uid")
	public String Uid;

	/**
	 * 用户名
	 */
	@JsonProperty("userName")
	public String UserName;

	/**
	 * 昵称
	 */
	@JsonProperty("nickName")
	public String NickName;

	@JsonProperty("Name")
	public String Name;
	
	@JsonProperty("displayName")
	public String DisplayName;
	
	/**
	 * 头像地址
	 */
	@JsonProperty("headImgUrl")
	public String HeadImgUrl;

	/**
	 * 子成员总数（暂时只有群聊信息有子成员）
	 */
	@JsonProperty("memberCount")
	public int MemberCount;

	/**
	 * 子成员列表（暂时只有群聊信息有子成员）
	 */
	@JsonProperty("memberList")
	public List<Member> MemberList;

	/**
	 * 性别
	 */
	@JsonProperty("sex")
	public int Sex;

	/**
	 * 签名
	 */
	@JsonProperty("signature")
	public String Signature;

	/**
	 * WEB版上,"应用"的该字段都是24
	 */
	@JsonProperty("verifyFlag")
	public int VerifyFlag;

	/**
	 * NickName汉子拼音首字母与英文<br/>
	 * 如：张涵han，为ZHHAN
	 */
	@JsonProperty(value = "pYInitial")
	public String PYInitial;

	/**
	 * NickName汉子拼音与英文<br/>
	 * 如：张涵han，为zhanghanhan
	 */
	@JsonProperty(value = "pYQuanPin")
	public String PYQuanPin;

	/**
	 * 用来区分是否是"应用"?
	 */
	@JsonProperty("appAccountFlag")
	public int AppAccountFlag;

	@JsonProperty("status")
	public int Status;

	/**
	 * 省份
	 */
	@JsonProperty("province")
	public String Province;

	/**
	 * 城市
	 */
	@JsonProperty("city")
	public String City;

	/**
	 * 别名
	 */
	@JsonProperty("alias")
	public String Alias;

	/**
	 * 备注名称
	 */
	@JsonProperty("remarkName")
	public String RemarkName;

	/**
	 * RemarkName汉子拼音首字母与英文
	 */
	@JsonProperty("remarkPYInitial")
	public String RemarkPYInitial;

	/**
	 * RemarkName汉子拼音与英文
	 */
	@JsonProperty("remarkPYQuanPin")
	public String RemarkPYQuanPin;

	/**
	 * 是否加星
	 */
	@JsonProperty("starFriend")
	public int StarFriend;
	/**
	 * email
	 */
	@JsonProperty("email")
	public String Email;
	
	/**
	 * mobile
	 */
	@JsonProperty("mobile")
	public String Mobile;

	public int sort;
	
	@JsonProperty("orgName")
	public String OrgName;
	
	@JsonProperty("tel")
	public String Tel;

	@JsonProperty("description")
	public String Description;
	
	@JsonProperty("avatar")
	public String Avatar;
	
	@Override
	public String toString() {
		return "Member [Uid=" + Uid + ", UserName=" + UserName + ", NickName="
				+ NickName + ", Name=" + Name + ", DisplayName=" + DisplayName
				+ ", HeadImgUrl=" + HeadImgUrl + ", MemberCount=" + MemberCount
				+ ", MemberList=" + MemberList + ", Sex=" + Sex
				+ ", Signature=" + Signature + ", VerifyFlag=" + VerifyFlag
				+ ", PYInitial=" + PYInitial + ", PYQuanPin=" + PYQuanPin
				+ ", AppAccountFlag=" + AppAccountFlag + ", Status=" + Status
				+ ", Province=" + Province + ", City=" + City + ", Alias="
				+ Alias + ", RemarkName=" + RemarkName + ", RemarkPYInitial="
				+ RemarkPYInitial + ", RemarkPYQuanPin=" + RemarkPYQuanPin
				+ ", StarFriend=" + StarFriend + ", Email=" + Email
				+ ", Mobile=" + Mobile + ", sort=" + sort + ", OrgName="
				+ OrgName + "]";
	}

}
