package com.miicaa.common.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.GroupInfo;
import com.miicaa.home.data.business.org.GroupUserInfo;
import com.miicaa.home.data.business.org.UnitInfo;
import com.miicaa.home.data.business.org.UnitUserInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.business.org.UserRequset;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.person.PersonHome_;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;


/**
 * Created by Administrator on 13-11-28.
 */
public class Tools {
	public static boolean hasSdCard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String stringOfObject(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof Boolean) {
			return String.valueOf(obj);
		} else if (obj instanceof Long) {
			return String.valueOf(obj);
		} else if (obj instanceof Date) {
			return String.valueOf(((Date) obj).getTime());
		} else if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	public static String getMd5(String value) {
		if (value != null && value.length() > 0) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] hash = md.digest(value.getBytes("UTF8"));
				BigInteger i = new BigInteger(1, hash);
				return String.format("%1$032x", i); // a01610228fe998f515a72dd730294d87
//				return i.toString(16); // a01610228fe998f515a72dd730294d87
//				return Base64.encodeToString(hash, Base64.DEFAULT); // oBYQIo/pmPUVpy3XMClNhw==
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getPingYinFirst(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					if (java.lang.Character.toString(nameChar[i]).matches("[\\u4E00-\\u9FA5]+")) {
						Log.d("Tools", "name Char:"+nameChar[i]);
//						if(nameChar[i] == '乄')
//							Log.d("Tools", "乄  s quanpin is ");
						
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
						Log.d("Tools", "...."+pinyinName);
						
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}catch(Exception e){
//					Log.d("Tools", "notFoundQuanPin in Pinyin4j");
					pinyinName += "#";
//					Log.d("Tools", "catch exception pinyinFirst:"+pinyinName);
//					continue;
//					e.printStackTrace();
				}finally{
					
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					output += temp[0];
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}catch (Exception e) {
//			Log.d("Tools", "getPingyin is not this zimu");
			output += "#";
		}
		return output;
	}

    public static void setHeadImgChange(final String userCode,ImageView view){

    }

    public static void setHeadImgWithoutClick(final String userCode,ImageView view){
    	DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.an_user_head_large)
		.showImageOnFail(R.drawable.an_user_head_large)
		.resetViewBeforeLoading(false)
		.cacheInMemory(true)
		.cacheOnDisk(false)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
    	 String headPath = UserAccount.getLocalDir("imgCache/") + userCode + ".jpg";
    	 headPath = Scheme.FILE.wrap(headPath);
    	ImageLoader.getInstance().displayImage(headPath, view,displayImageOptions);
    }
    public static void setHeadImg(final String userCode,ImageView view){
        setHeadImgWithoutClick(userCode,view);
        if(view.getContext()!=null && userCode!=null && !"".equals(userCode.trim())) {
            View.OnClickListener headClick = new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), PersonHome_.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("userCode", userCode);
//                    intent.putExtra("bundle", bundle);
//                    
//                    view.getContext().startActivity(intent);
                	PersonHome_.intent(view.getContext()).mUserCode(userCode).start();
                    ((Activity) view.getContext()).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
                }
            };
             view.setOnClickListener(headClick);
        }
    }

    
    
    public static void initUnitsData(JSONArray jArray) {
        if (jArray == null) {
            return;
        }
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jItem = jArray.optJSONObject(i);
            UnitInfo info = new UnitInfo();
            info.setId(jItem.optLong("id"));
            info.setCode(jItem.optString("code"));
            info.setCreateTime(new Date(jItem.optLong("createTime")));
            info.setFullName(jItem.optString("fullName"));
            info.setName(jItem.optString("name"));
            info.setOrgCode(jItem.optString("orgCode"));
            info.setParentCode(jItem.optString("parentCode"));
            info.setSort(jItem.optLong("sort"));
            info.setStatus(jItem.optLong("status"));
            info.save();
        }
    }

    public static void initGroupsData(JSONArray jArray) {
        if (jArray == null) {
            return;
        }
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jItem = jArray.optJSONObject(i);
            GroupInfo info = new GroupInfo();
            info.setId(jItem.optLong("id"));
            info.setCode(jItem.optString("code"));
            info.setCreateTime(new Date(jItem.optLong("createTime")));
            info.setFullName(jItem.optString("fullName"));
            info.setName(jItem.optString("name"));
            info.setOrgCode(jItem.optString("orgCode"));
            info.setParentCode(jItem.optString("parentCode"));
            info.setSort(jItem.optLong("sort"));
            info.setStatus(jItem.optLong("status"));
            info.save();
        }
    }

    public static void initUsersData(JSONArray jArray) {
        if (jArray == null) {
            return;
        }
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jItem = jArray.optJSONObject(i);
            UserInfo info = new UserInfo();
            info.setId(Long.parseLong(jItem.optString("id")));
            
            info.setCode(jItem.optString("code"));
            if(info.getCode().equals(AccountInfo.instance().getLastUserInfo().getCode())){
            	   Log.d("uiduid1",info.getId()+"" );
            }
            info.setEmail(jItem.optString("email"));
            info.setCellphone(jItem.optString("cellphone"));
            info.setStatus(jItem.optLong("status"));
            info.setAvatar(jItem.optString("avatar"));
            if (info.getAvatar() != null && info.getAvatar().length() > 0 && !"null".equals(info.getAvatar())) {
                UserRequset.requestUserHead(new UserAvatarReq(), jItem.optString("code"));
            }
            info.setCreateTime(new Date(jItem.optLong("createTime")));
            info.setOrgCode(jItem.optString("orgCode"));
            info.setGender(jItem.optString("gender"));
            info.setBirthday(new Date(jItem.optLong("birthday")));
            info.setQq(jItem.optString("qq"));
            info.setPhone(jItem.optString("phone"));
            info.setAddr(jItem.optString("addr"));
            info.setName(jItem.optString("name"));
            Log.d("Tools", "id:..."+info.getId());
            	Log.d("Tools", "info.getId + ..."+info.getName()+"-----"+jItem.optString("id"));
            Boolean f = info.save();
            Log.d("Tools","save is ......"+f);
        }
    }

    public static void initUnitUsersData(JSONArray jArray) {
        if (jArray == null) {
            return;
        }
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jItem = jArray.optJSONObject(i);
            UnitUserInfo info = new UnitUserInfo();
            info.setId(jItem.optLong("id"));
            info.setUserCode(jItem.optString("userCode"));
            info.setUnitCode(jItem.optString("unitCode"));
            info.setSort(jItem.optLong("sort"));
            info.save();
        }
    }

    public static void initGroupUsersData(JSONArray jArray) {
        if (jArray == null) {
            return;
        }
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jItem = jArray.optJSONObject(i);
            GroupUserInfo info = new GroupUserInfo();
            info.setId(jItem.optLong("id"));
            info.setUserCode(jItem.optString("userCode"));
            info.setUnitCode(jItem.optString("unitCode"));
            info.setGroupCode(jItem.optString("groupCode"));
            info.setSort(jItem.optLong("sort"));
            info.save();
        }
    }

    public static String getDiscussHTML(String content){
        if(content==null || "".equals(content.trim())){
            return "";
        }

        Pattern pattern = Pattern.compile("\\[#i_f[0-5][0-9]\\]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sbr = new StringBuffer();
        while (matcher.find()) {
            String icon = matcher.group().replace("[#","").replace("]","")+".gif";
            matcher.appendReplacement(sbr, "<img src=\"/home/static/cloudeditor/pc/images/component/face/"+icon+"\">");
        }
        matcher.appendTail(sbr);
        return sbr.toString();
    }

    public static String getDiscussText(String content){
        if(content==null || "".equals(content.trim())){
            return "";
        }
        Pattern pattern = Pattern.compile("\\[#i_f[0-5][0-9]\\]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sbr = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sbr, "");
        }
        matcher.appendTail(sbr);
        return sbr.toString();
    }

    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }
}