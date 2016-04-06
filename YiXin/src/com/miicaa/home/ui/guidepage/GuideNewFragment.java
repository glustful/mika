package com.miicaa.home.ui.guidepage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.miicaa.home.R;
@EFragment(R.layout.new_guide)
public class GuideNewFragment extends Fragment{
//@ViewById(R.id.guiBg)
//ImageView bg;
//@ViewById(R.id.guiPic)
//ImageView picture;
//@ViewById(R.id.guiText)
//ImageView textImg;
@ViewById
RelativeLayout rootgroup;
@FragmentArg
int count;
@SystemService
LayoutInflater inflater;

//View rootview;
Resources rs;

@AfterInject
void afterInject(){
	rs = getActivity().getResources();
}

@AfterViews
void afterViews(){
	switch (count) {
//	case 0:
////		setBg(R.drawable.n_g_1,R.drawable.n_p_1,R.drawable.n_t_1);
//		rootgroup.addView(inflater.inflate(R.layout.n_g_1, null));
//		break;
//	case 1:
//		rootgroup.addView(inflater.inflate(R.layout.n_g_2, null));
//		break;
//	case 2:
//		rootgroup.addView(inflater.inflate(R.layout.n_g_3, null));
//		break;
//	case 3:
//		View v = inflater.inflate(R.layout.n_g_4, null);
//		ImageButton i = (ImageButton)v.findViewById(R.id.guiLogin);
//		i.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				HomeLoginActivity_.intent(getActivity()).start();
//				getActivity().finish();
//			}
//		});
//		rootgroup.addView(v);
//	default:
//		break;
	}
}

//@SuppressLint("NewApi")
//@UiThread
//void setBg(int idb,int idp,int idt){
//	bg.setBackground(rs.getDrawable(idb));
//	picture.setBackground(rs.getDrawable(idp));
//	textImg.setBackground(rs.getDrawable(idt));
//}

}
