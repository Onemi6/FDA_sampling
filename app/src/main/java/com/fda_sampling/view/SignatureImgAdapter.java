package com.fda_sampling.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fda_sampling.R;
import com.fda_sampling.model.ImageInfoAdd;

import java.util.List;

/**
 * Created by yy on 2019/11/20.
 */

public class SignatureImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    private static final int VIEW_TYPE = -1;
    private List<ImageInfoAdd> imgList;
    private Activity mActivity;
    private SignatureImgAdapter.OnClickListener mOnClickListener = null;
    private SignatureImgAdapter.OnLongClickListener mOnLongClickListener = null;

    public SignatureImgAdapter(Activity activity, List<ImageInfoAdd> imgList) {
        this.mActivity = activity;
        this.imgList = imgList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_signature, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new SignatureImgAdapter.ViewHolder(view);
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SignatureImgAdapter.ViewHolder) {
            String picPath = imgList.get(position).getPATH();
            if (picPath.equals("加号")) {
                Glide.with(mActivity).load(R.mipmap.ic_add)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.error)
                        .into(((SignatureImgAdapter.ViewHolder) holder).img_add);
            } else {
                //需要替换图片所以要清缓存
                Glide.with(mActivity).load(picPath).skipMemoryCache(true).diskCacheStrategy
                        (DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.error)
                        .into(((SignatureImgAdapter.ViewHolder) holder).img_add);
            }
            if (position == 0) {
                ((SignatureImgAdapter.ViewHolder) holder).imageType.setText(String.valueOf
                        ("抽样员本人"));
            } else if (position == 1) {
                ((SignatureImgAdapter.ViewHolder) holder).imageType.setText(String.valueOf
                        ("被抽样单位人员"));
            } else {
                ((SignatureImgAdapter.ViewHolder) holder).imageType.setText(String.valueOf
                        ("同行抽样人员" + (position - 1)));
            }
            ((SignatureImgAdapter.ViewHolder) holder).itemView.setTag(position);
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

    public List<ImageInfoAdd> getImgList() {
        return this.imgList;
    }

    public void removeItem(int position) {
        this.imgList.remove(position);
        notifyDataSetChanged();
    }

    public void changeList_add(List<ImageInfoAdd> imgList) {
        this.imgList = imgList;
        notifyDataSetChanged();
    }


    /************
     * Listener
     */

    public void setOnClickListener(SignatureImgAdapter.OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(view, (int) view.getTag());
        }
    }

    public void setOnLongClickListener(SignatureImgAdapter.OnLongClickListener listener) {
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
        private TextView imageType;

        public ViewHolder(View view) {
            super(view);
            img_add = view.findViewById(R.id.imageView);
            imageType = view.findViewById(R.id.imageType);
        }
    }

}
