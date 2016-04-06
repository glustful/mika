package com.miicaa.common.base;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;

import com.yxst.epic.yixin.MyApplication;

/**
 * Created by Administrator on 13-11-5.
 */
public class DatabaseOption
{
    Context mContext;
    private static final String DEFAULT_DB_NAME = "local_moa_values";
    public static DatabaseOption mInstance = null;
    private DatabaseManager dbManager = null;

    public static DatabaseOption getIntance()
    {
        if(mInstance == null)
        {
            synchronized (DatabaseOption.class)
            {
                if (mInstance == null)
                {
                    mInstance = new DatabaseOption(MyApplication.getInstance());
                }
            }

        }
        return mInstance;
    }

    private DatabaseOption(Context context)
    {
        mContext = context;
        initDB(context, DEFAULT_DB_NAME);
    }

    public void initDB(Context context, String dbName)
    {
        dbManager = new DatabaseManager(context);
        if (dbManager.openOrCreateDB("", dbName))
        {
            if (dbManager.createTable("CREATE TABLE value_map(name TEXT, value TEXT)"))
            {
                setValue("author", "yayowd");
                setValue("version", "0.1");
                setValue("flag", "0");
            }
        }
    }

    public void deleteDB()
    {
        dbManager.dropDB("", DEFAULT_DB_NAME);
    }

    public boolean setValue(String name, String value)
    {
        if (getValue(name) == null)
        {
            return dbManager.insert("INSERT INTO value_map VALUES(?, ?)", name, value);
        }
        else
        {
            return dbManager.update("UPDATE value_map  SET value=? WHERE name = ?", value, name);
        }
    }

    public String getValue(String name)
    {
        ArrayList<ContentValues> vl = dbManager.query("SELECT * FROM value_map WHERE name = ?", name);
        if (vl.size() > 0)
        {
            ContentValues rv = vl.get(0);
            return rv.getAsString("value");
        }
        return null;
    }
}
