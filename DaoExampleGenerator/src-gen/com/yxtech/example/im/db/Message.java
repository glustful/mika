package com.yxtech.example.im.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MESSAGE.
 */
public class Message {

    private Long id;
    private Long Mid;
    private Long Gid;
    private String MsgId;
    private String ClientMsgId;
    private String FromUserName;
    private String ToUserName;
    private Integer MsgType;
    private String Content;
    private String ObjectContent;
    private Long CreateTime;
    private String MediaId;
    private String Url;
    private String extLocalUserName;
    /** Not-null value. */
    private String extRemoteUserName;
    private String extRemoteDisplayName;
    private Integer extInOut;
    private Long extTime;
    private Integer extRead;
    private Integer extStatus;

    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    public Message(Long id, Long Mid, Long Gid, String MsgId, String ClientMsgId, String FromUserName, String ToUserName, Integer MsgType, String Content, String ObjectContent, Long CreateTime, String MediaId, String Url, String extLocalUserName, String extRemoteUserName, String extRemoteDisplayName, Integer extInOut, Long extTime, Integer extRead, Integer extStatus) {
        this.id = id;
        this.Mid = Mid;
        this.Gid = Gid;
        this.MsgId = MsgId;
        this.ClientMsgId = ClientMsgId;
        this.FromUserName = FromUserName;
        this.ToUserName = ToUserName;
        this.MsgType = MsgType;
        this.Content = Content;
        this.ObjectContent = ObjectContent;
        this.CreateTime = CreateTime;
        this.MediaId = MediaId;
        this.Url = Url;
        this.extLocalUserName = extLocalUserName;
        this.extRemoteUserName = extRemoteUserName;
        this.extRemoteDisplayName = extRemoteDisplayName;
        this.extInOut = extInOut;
        this.extTime = extTime;
        this.extRead = extRead;
        this.extStatus = extStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMid() {
        return Mid;
    }

    public void setMid(Long Mid) {
        this.Mid = Mid;
    }

    public Long getGid() {
        return Gid;
    }

    public void setGid(Long Gid) {
        this.Gid = Gid;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String MsgId) {
        this.MsgId = MsgId;
    }

    public String getClientMsgId() {
        return ClientMsgId;
    }

    public void setClientMsgId(String ClientMsgId) {
        this.ClientMsgId = ClientMsgId;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String FromUserName) {
        this.FromUserName = FromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String ToUserName) {
        this.ToUserName = ToUserName;
    }

    public Integer getMsgType() {
        return MsgType;
    }

    public void setMsgType(Integer MsgType) {
        this.MsgType = MsgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getObjectContent() {
        return ObjectContent;
    }

    public void setObjectContent(String ObjectContent) {
        this.ObjectContent = ObjectContent;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String MediaId) {
        this.MediaId = MediaId;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getExtLocalUserName() {
        return extLocalUserName;
    }

    public void setExtLocalUserName(String extLocalUserName) {
        this.extLocalUserName = extLocalUserName;
    }

    /** Not-null value. */
    public String getExtRemoteUserName() {
        return extRemoteUserName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setExtRemoteUserName(String extRemoteUserName) {
        this.extRemoteUserName = extRemoteUserName;
    }

    public String getExtRemoteDisplayName() {
        return extRemoteDisplayName;
    }

    public void setExtRemoteDisplayName(String extRemoteDisplayName) {
        this.extRemoteDisplayName = extRemoteDisplayName;
    }

    public Integer getExtInOut() {
        return extInOut;
    }

    public void setExtInOut(Integer extInOut) {
        this.extInOut = extInOut;
    }

    public Long getExtTime() {
        return extTime;
    }

    public void setExtTime(Long extTime) {
        this.extTime = extTime;
    }

    public Integer getExtRead() {
        return extRead;
    }

    public void setExtRead(Integer extRead) {
        this.extRead = extRead;
    }

    public Integer getExtStatus() {
        return extStatus;
    }

    public void setExtStatus(Integer extStatus) {
        this.extStatus = extStatus;
    }

}
