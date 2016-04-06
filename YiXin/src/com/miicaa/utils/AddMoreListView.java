package com.miicaa.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.miicaa.home.R;


public class AddMoreListView extends ListView implements AbsListView.OnScrollListener,
View.OnClickListener {
	
	boolean open = false;
// 拖拉ListView枚举所有状态

// 点击加载更多枚举所有状态
private enum DListViewLoadingMore {
    LV_NORMAL, // 普通状态
    LV_LOADING, // 加载状态
    LV_OVER; // 结束状态
}


Context context;

private  View mFootView;// 尾部mFootView
private View mLoadMoreView;// mFootView 的view(mFootView)
private TextView mLoadMoreTextView;// 加载更多.(mFootView)
private View mLoadingView;// 加载中...View(mFootView)
private ImageView bottomView;

private Animation animation, reverseAnimation;// 旋转动画，旋转动画之后旋转动画.

private int mFirstItemIndex = -1;// 当前视图能看到的第一个项的索引

// 用于保证startY的值在一个完整的touch事件中只被记录一次
private boolean mIsRecord = false;

private int mStartY, mMoveY;// 按下是的y坐标,move时的y坐标


private DListViewLoadingMore loadingMoreState = DListViewLoadingMore.LV_NORMAL;// 加载更多默认状态.

//private final static int RATIO = 2;// 手势下拉距离比.

//private boolean mBack = false;// headView是否返回.

private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// 下拉刷新接口（自定义）

private boolean isScroller = true;// 是否屏蔽ListView滑动。

public AddMoreListView(Context context) {
    super(context, null);
    this.context = context;
    initDragListView(context);
    
}

public AddMoreListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    initDragListView(context);
}

//	 注入下拉刷新接口
public void setOnRefreshListener(
        OnRefreshLoadingMoreListener onRefreshLoadingMoreListener) {
    this.onRefreshLoadingMoreListener = onRefreshLoadingMoreListener;
}

/***
 * 初始化ListView
 */
public void initDragListView(Context context) {

    String time = "1994.12.05";// 更新时间

//initHeadView(context, time);// 初始化该head.

    initLoadMoreView(context);// 初始化footer
    setOnScrollListener(this);// ListView滚动监听
}


/***
 * 初始化底部加载更多控件
 */
private void initLoadMoreView(Context context) {
    mFootView = LayoutInflater.from(context).inflate(R.layout.arragement_progress_listview, null);

    mLoadMoreView = mFootView.findViewById(R.id.load_more_view);

    mLoadMoreTextView = (TextView) mFootView
            .findViewById(R.id.load_more_tv);

    mLoadingView = mFootView
            .findViewById(R.id.loading_layout);

    mLoadMoreView.setOnClickListener(this);
    
    bottomView = (ImageView)mFootView.findViewById(R.id.bottomLine);

    addFooterView(mFootView);

}

/***
 * 初始化动画
 */
private void initAnimation() {
    // 旋转动画
    animation = new RotateAnimation(0, -180,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
    animation.setInterpolator(new LinearInterpolator());// 匀速
    animation.setDuration(250);
    animation.setFillAfter(true);// 停留在最后状态.
    // 反向旋转动画
    reverseAnimation = new RotateAnimation(-180, 0,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
    reverseAnimation.setInterpolator(new LinearInterpolator());
    reverseAnimation.setDuration(250);
    reverseAnimation.setFillAfter(true);
}



@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	String tag =  getTag()==null?null:(String)getTag();
	if(tag != null && tag.equals("true")){
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
            MeasureSpec.AT_MOST);  
    super.onMeasure(widthMeasureSpec, expandSpec);  
	}else{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	
	}
}

//是否将listview展开

public  void setOpenView(boolean open){
	this.open = open;
	if(open){
		setListViewHeightBasedOnChildren(this);
	}
	
}

public AddMoreListView shownFootView(Boolean isShown){
	if(!isShown){
		mFootView.setVisibility(View.GONE);
	}
	return this;
}



//展开listview
public static  void setListViewHeightBasedOnChildren(ListView listView) {
	
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
        return;
    }
    int totalHeight = 0;
    
    for (int i = 0; i < listAdapter.getCount(); i++) {
        View listItem = listAdapter.getView(i, null, listView);
       listItem.setTag(listView);
        listItem.measure(0, 0);
        totalHeight += listItem.getMeasuredHeight();
   
       
    }
    ViewGroup.LayoutParams params =   listView.getLayoutParams();
    
    
   params.height = totalHeight
       + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+listView.getPaddingBottom()+listView.getPaddingTop();
   
    
    listView.setLayoutParams(params);
}



/***
 * 点击加载更多
 *
 * @param flag
 *            数据是否已全部加载完毕
 */
public void onLoadMoreComplete(boolean flag) {
    if (flag) {
        updateLoadMoreViewState(DListViewLoadingMore.LV_OVER);
    } else {
        updateLoadMoreViewState(DListViewLoadingMore.LV_NORMAL);
    }

}

// 更新Footview视图
private void updateLoadMoreViewState(DListViewLoadingMore state) {
    switch (state) {
        // 普通状态
        case LV_NORMAL:
        	bottomView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.GONE);
            mFootView.setVisibility(View.VISIBLE);
            mLoadMoreTextView.setVisibility(View.VISIBLE);
            mLoadMoreTextView.setText("查看更多");
            break;
        // 加载中状态
        case LV_LOADING:
        	bottomView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
            mLoadMoreTextView.setVisibility(View.GONE);
            break;
        // 加载完毕状态
        case LV_OVER:
//            mLoadingView.setVisibility(View.GONE);
//            mLoadMoreTextView.setVisibility(View.VISIBLE);
//            mLoadMoreTextView.setText("加载完毕");
            mFootView.setVisibility(View.GONE);
            bottomView.setVisibility(View.VISIBLE);
           
//            this.removeFooterView(mFootView);
            break;
        default:
            break;
    }
    loadingMoreState = state;
    
  
}

public void bugToProgressAdapter(){
	 mFootView.setVisibility(View.GONE);
	 mLoadingView.setVisibility(View.GONE);
     mLoadMoreTextView.setVisibility(View.GONE);
     bottomView.setVisibility(View.GONE);
     this.removeFooterView(mFootView);
}

//如果不需要加载更多的判断
public void setMore(Boolean ismore){
	
//	mFootView.setVisibility(ismore?View.VISIBLE:View.GONE);
	if(ismore){
//		mFootView.setVisibility(View.VISIBLE);
		 updateLoadMoreViewState(DListViewLoadingMore.LV_NORMAL);
	}else
//		updateLoadMoreViewState(DListViewLoadingMore.LV_OVER);
		bugToProgressAdapter();
		
}


/***
 * ListView 滑动监听
 */
@Override
public void onScrollStateChanged(AbsListView view, int scrollState) {

}

@Override
public void onScroll(AbsListView view, int firstVisibleItem,
                     int visibleItemCount, int totalItemCount) {
    mFirstItemIndex = firstVisibleItem;
}

/***
 * 底部点击事件
 */
@Override
public void onClick(View v) {
    // 防止重复点击
    if (onRefreshLoadingMoreListener != null
            && loadingMoreState == DListViewLoadingMore.LV_NORMAL) {
        updateLoadMoreViewState(DListViewLoadingMore.LV_LOADING);
        onRefreshLoadingMoreListener.onLoadMore();// 对外提供方法加载更多.
    }

}




public void addBottomLine(){

}

/***
 * 自定义接口
 */
public interface OnRefreshLoadingMoreListener {
    /***
     * // 下拉刷新执行
     */
    void onRefresh();

    /***
     * 点击加载更多
     */
    void onLoadMore();
}

}