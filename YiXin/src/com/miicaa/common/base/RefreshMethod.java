package com.miicaa.common.base;

/**
 * Created by LM on 14-5-9.
 */
public class RefreshMethod {

    int intValue;
       public final static int  LOAD_CREATE = 0x1;
        public final static int LOAD_CONTINUE = 0x2;
        public final static int LOAD_AGAIN = 0x3;
    public void setIntValue(int value){
        intValue = value;
    }
    public int getIntValue(){
        return  intValue;
    }
}
