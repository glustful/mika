package com.yxst.epic.yixin.push.rest.result;

import java.util.List;

import com.yxst.epic.yixin.push.cli.PushMessage;


/**
 * @author liuxue
 *
 *{
 *	"data" : {
 *		"msgs" : [{
 *				"msg" : {
 *					"content" : "[微笑]",
 *					"from" : "045",
 *					"uuid" : "d3721951-f628-4939-9c84-a3efa289f4c5",
 *					"to" : "045",
 *					"timeMillis" : 1407392313186,
 *					"class" : "com.yxtech.example.bean.CustomMsg"
 *				},
 *				"mid" : 1407392306172234,
 *				"gid" : 0
 *			}
 *		]
 *	},
 *	"ret" : 0
 *}
 *
 */
public class ResultMsg extends Result {

	private static final long serialVersionUID = -6189636567005346415L;
	
	public Data data;
	
	public static class Data{
		
		/**
		 * string数组	 私有离线消息
		 */
		public List<PushMessage> msgs;
		
		/**
		 * string数组	 公共离线消息
		 */
		public List<PushMessage> pmsgs;
	}

}
