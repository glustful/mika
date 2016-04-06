package com.miicaa.home.ui.home;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.miicaa.base.data.LocalDataBase;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.UserInfoSql;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.LocalPath;
import com.miicaa.home.data.storage.SqlCmd;
import com.miicaa.home.ui.contactList.ContactIndexListView;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@EFragment(R.layout.contact_list_view)
public class ContactFragment extends Fragment implements LoaderCallbacks<Cursor>{

@ViewById(R.id.contact_index_list)
ContactIndexListView contactIndexListView;
@ViewById(R.id.contact_list_view_list)
ListView mListView;
@Override
public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	return new ContactLoader(getActivity());
}
@Override
public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
	
}
@Override
public void onLoaderReset(Loader<Cursor> arg0) {
	
}

class ContactCursorAdapter extends CursorAdapter{

	public ContactCursorAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
}
