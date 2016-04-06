package com.miicaa.home.ui.contactList;

import java.util.ArrayList;

import com.miicaa.home.ui.contactGet.ContactViewShow;

/**
 * Created by LM on 14-9-10.
 */
public class ContactUtil {
    public final static String USER_SELECT = "select";
    public final  static  String SELECT_BACK = "back";



    public static void setSelectPeopleData(ArrayList<ContactViewShow> data,PeopleCallbackListener listener) {
        ArrayList<SamUser> sampUsers = new ArrayList<SamUser>();
        String conUser = "";
//        String user ;
        for (int i = 0; i < data.size(); i++) {
            sampUsers.add(new SamUser(data.get(i).getCode(), data.get(i).getName()));
//            user = data.get(i).getName();
//            if (i < data.size() - 1) {
//                user += ",";
//            }
//            conUser = conUser + user;
        }
        if(data.size() > 1){
        	conUser = data.get(0).getName() + "等" +data.size()+"个人";
        }else if(data.size() == 1){
        	conUser = data.get(0).getName();
        }
        listener.callBack(sampUsers,conUser);
    }

    public interface PeopleCallbackListener{
        public void callBack(ArrayList<SamUser> data,String user);
    }
}
