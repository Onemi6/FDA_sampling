package com.fda_sampling.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fda_sampling.R;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View
        .OnClickListener, View.OnLongClickListener {
    private static final int VIEW_TYPE = -1;
    private List<String> imgList;
    private Activity mActivity;
    private ImgAdapter.OnClickListener mOnClickListener = null;
    private ImgAdapter.OnLongClickListener mOnLongClickListener = null;

    public ImgAdapter(Activity activity, List<String> imgList) {
        this.mActivity = activity;
        this.imgList = imgList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_img_add, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ImgAdapter.ViewHolder(view);
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImgAdapter.ViewHolder) {
            String picPath = imgList.get(position);
            if ((position == 0 || position == 1 || position == 2) && picPath.equals("加号")) {
                //((ViewHolder) holder).img_add.setImageResource(R.mipmap.ic_add);
                Glide.with(mActivity).load(R.mipmap.ic_add)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.error)
                        .into(((ViewHolder) holder).img_add);
            } else if (position == 0 && picPath.equals("空白")) {
                //((ViewHolder) holder).img_add.setImageResource(R.mipmap.logo);
            } else {
                Glide.with(mActivity).load(picPath)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.error)
                        .into(((ViewHolder) holder).img_add);
            }
            if (position == 0) {
                ((ViewHolder) holder).imageNum.setText(String.valueOf(""));
            } else {
                ((ViewHolder) holder).imageNum.setText(String.valueOf(position));
            }
            ((ViewHolder) holder).itemView.setTag(position);
        }
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        //获取传入adapter的条目数，没有则返回 1
        //return imgList.size() > 0 ? imgList.size() : 1;
        return imgList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (imgList.size() <= 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    public List<String> getImgList() {
        return this.imgList;
    }

    public void removeItem(int position) {
        this.imgList.remove(position);
        notifyDataSetChanged();
    }

    public void changeList_add(List<String> imgList) {
        this.imgList = imgList;
        notifyDataSetChanged();
    }


    /************
     * Listener
     */

    public void setOnClickListener(ImgAdapter.OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(view, (int) view.getTag());
        }
    }

    public void setOnLongClickListener(ImgAdapter.OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    @Override
    public boolean onLongClick(View view) {
        if (null != mOnLongClickListener) {
            mOnLongClickListener.onLongClick(view, (int) view.getTag());
        }
        // 消耗事件，否则长按逻辑执行完成后还会进入点击事件的逻辑处理
        return true;
    }

    /**
     * 手动添加点击事件
     */
    interface OnClickListener {
        void onClick(View view, int position);
    }

    /**
     * 手动添加长按事件
     */
    interface OnLongClickListener {
        void onLongClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_add;
        private TextView imageNum;

        public ViewHolder(View view) {
            super(view);
            img_add = view.findViewById(R.id.imageView);
            imageNum = view.findViewById(R.id.imageNum);
        }
    }

    public Bitmap getBM(String path) {
        if (!TextUtils.isEmpty(path)) {
            Options opt = new Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opt);
            int imageHeight = opt.outHeight;
            int imageWidth = opt.outWidth;

            Display display = mActivity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            // 该方法已过时，使用getRealSize()方法替代。也可以使用getSize()，但是不能准确的获取到分辨率
            // int screenHeight = display.getHeight();
            // int screenWidth = display.getWidth();
            display.getRealSize(point);
            int screenHeight = point.y;
            int screenWidth = point.x;

            int scale = 1;
            int scaleWidth = imageWidth / screenWidth / 3;
            int scaleHeigh = imageHeight / screenHeight;
            if (scaleWidth >= scaleHeigh && scaleWidth > 1) {
                scale = scaleWidth;
            } else if (scaleWidth < scaleHeigh && scaleHeigh > 1) {
                scale = scaleHeigh;
            }
            opt.inSampleSize = scale;
            opt.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(path, opt);
            return bm;
        }
        return null;
    }

}
