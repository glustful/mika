package com.miicaa.home.ui.photoGrid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.photoGrid.PictureLoader.Type;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.AuthorityUtils.AuthorityState;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yxst.epic.yixin.MyApplication;

/**
 * Created by LM on 14-8-13.
 */
public class ContentImageAdapter extends BaseAdapter {
	static String TAG = "ContentImageAdapter";

	private DisplayImageOptions displayImageOptions;
	final static int PHOTO = 0;
	final static int PICTURE = 1;
	Boolean isRefresh = false;
	List<ImageItem> imageList;
	SparseArray<Boolean> jilu;
	BitMapCache bitMapCache;
	View tmpView;
	int mCount = 0;
	Handler mHandler;
	OnImgChangeListener onImgChangeListener;
	public int state;
	public HashMap<String, String> imgMap;
	LayoutInflater inflater;
	Resources resources;

	public ContentImageAdapter(Context context, List<ImageItem> imageItems,
			final Handler handler, int state) {
		initDisplayImageOptions();
		bitMapCache = new BitMapCache();
		this.state = state;
		jilu = new SparseArray<Boolean>();
		imageList = new ArrayList<ImageItem>();
		imageList.clear();
		imageList.addAll(imageItems);
		imgMap = new HashMap<String, String>();
		mHandler = handler;
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	public void refresh(List<ImageItem> imageItems, int state,
			int serverImgCount) {
		isRefresh = true;
		imageList.clear();
		imageList.addAll(imageItems);
		this.state = state;
		imgMap.clear();
		mCount = serverImgCount;
		if (onImgChangeListener != null) {
			onImgChangeListener.contentImgCallback(mCount);
		}
		this.notifyDataSetChanged();
	}

	public void changeBitmapCount(int bitmapcount) {
		mCount += bitmapcount;
	}

	public int getSelectCount() {
		return mCount;
	}

	private void initDisplayImageOptions() {
		displayImageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheInMemory(false)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		int count;
		if (state == PhotoGridContentActivity.ALL) {
			count = (imageList != null) ? (imageList.size() + 1) : 1;
		} else {
			count = (imageList != null) ? imageList.size() : 0;
		}
		return count;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		int type = -1;
		if (state == PhotoGridContentActivity.ALL) {
			type = (position == 0) ? PHOTO : PICTURE;
		} else if (state == PhotoGridContentActivity.SIMPLE) {
			type = PICTURE;
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {
		return (state == PhotoGridContentActivity.ALL) ? 2 : 1;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		ViewHolder holder = null;
		int type = getItemViewType(i);
		Log.d(TAG, " getItemViewType(i) and position is " + type + "...." + i);
		if (view == null) {
			switch (type) {
			case PHOTO:
				view = inflater.inflate(R.layout.take_photo_view, null);
				break;
			case PICTURE:
				holder = new ViewHolder();
				view = inflater.inflate(R.layout.photo_gird_list_view, null);
				holder.checkView = (CheckBox) view
						.findViewById(R.id.photo_grid_list_checkview);
				holder.imgView = (ImageView) view
						.findViewById(R.id.photo_grid_list_image);
				holder.countView = (TextView) view
						.findViewById(R.id.photo_grid_list_imgCount);
				holder.nameView = (TextView) view
						.findViewById(R.id.photo_grid_list_imgName);
				view.setTag(holder);
				break;
			default:
				break;
			}

		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (type == PICTURE) {
			holder.countView.setVisibility(View.GONE);
			holder.nameView.setVisibility(View.GONE);
			ImageItem item = null;
			if (PhotoGridContentActivity.ALL == state) {
				item = imageList.get(i - 1);
			} else if (PhotoGridContentActivity.SIMPLE == state) {
				item = imageList.get(i);
			}
			if (imgMap.containsKey(item.image_path)) {
				item.isSelected = true;
				holder.checkView.setChecked(true);
			} else {
				holder.checkView.setChecked(false);
				item.isSelected = false;
			}
			final ImageItem imageItem = item;
			// final String thumbnailPath = imageItem.thumbnailPath;
			final String imagePath = imageItem.image_path;
			holder.imgView.setTag(i);
			holder.imgView.setImageDrawable(resources
					.getDrawable(R.drawable.ic_empty));
			final ViewHolder mHolder = holder;
			PictureLoader.getInstance(3, Type.LIFO).loadImage(imagePath,
					holder.imgView);
			// bitMapCache.setBitMap(holder.imgView, imagePath, new
			// BitMapCache.OnBitmapCacheLoadListener() {
			//
			// @Override
			// public void loading(Integer loadingCount) {
			//
			// }
			//
			// @Override
			// public void loaded(ImageView imageView,Bitmap bitmap) {
			// imageView.setImageBitmap(bitmap);
			// }
			//
			// @Override
			// public void beforeLoad() {
			// mHolder.imgView.setImageDrawable(resources.getDrawable(R.drawable.ic_empty));
			// }
			// });
			holder.checkView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (Bimp.drr.size() + mCount < 9) {
						imageItem.isSelected = !imageItem.isSelected;
						if (!imgMap.containsKey(imagePath)) {
							if (!allowSelect(imagePath)) {
								if (MyApplication.getInstance().getAuthority(
										AuthorityState.ePhoto) == AllUtils.NORMAL_User)
									mHandler.obtainMessage(2).sendToTarget();
								else
									mHandler.obtainMessage(3).sendToTarget();
								
								mHolder.checkView.setChecked(false);
							} else {
								mCount++;
								mHolder.checkView.setChecked(true);
								onImgChangeListener.contentImgCallback(mCount);
								imgMap.put(imagePath, imagePath);
								mHandler.obtainMessage(4).sendToTarget();
							}
						} else {
							mCount--;
							mHolder.checkView.setChecked(false);
							onImgChangeListener.contentImgCallback(mCount);
							imgMap.remove(imagePath);
							if(mCount == 0){
								mHandler.obtainMessage(5).sendToTarget();
							}
						}
					} else if (Bimp.drr.size() + mCount >= 9) {
						imageItem.isSelected = !imageItem.isSelected;
						if (!imgMap.containsKey(imagePath)) {
							imageItem.isSelected = !imageItem.isSelected;
							mHolder.checkView.setChecked(imageItem.isSelected);
							Message msg = Message.obtain(mHandler, 1);
							msg.sendToTarget();
						} else {
							mCount--;
							mHolder.checkView.setChecked(false);
							imgMap.remove(imagePath);
							onImgChangeListener.contentImgCallback(mCount);
						}
					}
				}
			});
		}
		return view;
	}

	class PhotoViewHolder {

	}

	class ViewHolder {
		ImageView imgView;
		CheckBox checkView;
		TextView nameView;
		TextView countView;
	}

	private Boolean allowSelect(String path) {
		File file = new File(path);
		int userAuthority = MyApplication.getInstance().getAuthority(
				AuthorityState.ePhoto);
		if (file.exists()) {
			if (AllUtils.NORMAL_User == userAuthority) {
				if (file.length() / AllUtils.MB > PhotoGridContentActivity.allowNormalUser)
					return false;
			} else if (file.length() / AllUtils.MB > PhotoGridContentActivity.allowPayForUser) {
				return false;
			}
		}
		return true;
	}

	public interface OnImgChangeListener {
		public void contentImgCallback(int count);
	}

	public void setImgChangeListener(OnImgChangeListener onImgChangeListener) {
		this.onImgChangeListener = onImgChangeListener;
	}

}
