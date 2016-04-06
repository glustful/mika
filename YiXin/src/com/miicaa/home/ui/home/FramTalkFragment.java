package com.miicaa.home.ui.home;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ListView;

import com.miicaa.home.R;
@EFragment(R.layout.home_fram_talk_fragment)
public class FramTalkFragment extends Fragment{
@ViewById(R.id.fram_talk_add)
Button addTalk;
@ViewById(R.id.fram_talk_list)
ListView talkList; 
}
