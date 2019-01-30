package com.fda_sampling.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fda_sampling.R;
import com.fda_sampling.model.Task;

import java.util.List;

public class MaininfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        View.OnClickListener, View.OnLongClickListener {

    private static final int VIEW_TYPE = -1;
    private List<Task> maininfoList;
    private Context mContext;
    private OnClickListener mOnClickListener = null;
    private OnLongClickListener mOnLongClickListener = null;
    private int defItem = -1;

    public MaininfoAdapter(Context context, List<Task> maininfoList) {
        this.mContext = context;
        this.maininfoList = maininfoList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View emptyview = LayoutInflater.from(mContext).inflate(R.layout.rv_empty, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_maininfo, parent, false);
        if (VIEW_TYPE == viewType) {
            return new EmptyViewHolder(emptyview);
        }
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            Task maininfo = maininfoList.get(position);
            ((ViewHolder) holder).CUSTOM_NO.setText(maininfo.getCUSTOM_NO());
            ((ViewHolder) holder).NO.setText(maininfo.getNO());
            ((ViewHolder) holder).BUSINESS_SOURCE.setText(maininfo.getBUSINESS_SOURCE());
            ((ViewHolder) holder).GOODS_NAME.setText(maininfo.getGOODS_NAME());
            ((ViewHolder) holder).num.setText(position + 1 + "");
            ((ViewHolder) holder).itemView.setTag(position);
            if (maininfo.getSTATE() != null) {
                if (maininfo.getSTATE().equals("采样信息审核退回")) {
                    ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#ffff0000"));
                    ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#ffff0000"));
                    ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor
                            ("#ffff0000"));
                    ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#ffff0000"));
                } else if (maininfo.getSTATE().equals("样品审核退回")) {
                    ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#FF8000"));
                    ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#FF8000"));
                    ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor
                            ("#FF8000"));
                    ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#FF8000"));
                }
            } else {
                ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).num.setTextColor(Color.parseColor("#727272"));
            }
            /*if (defItem != -1) {
                if (defItem == position) {
                    ((ViewHolder) holder).item_select.setBackgroundColor(Color.parseColor
                            ("#1E90FF"));
                    ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#FFFFFF"));
                    ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#FFFFFF"));
                    ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor("#FFFFFF"));
                    ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#FFFFFF"));
                    ((ViewHolder) holder).num.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ((ViewHolder) holder).item_select.setBackgroundColor(Color.parseColor
                            ("#eeeeee"));
                    ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#727272"));
                    ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#727272"));
                    ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor("#727272"));
                    ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#727272"));
                    ((ViewHolder) holder).num.setTextColor(Color.parseColor("#727272"));
                }
            }*/
        }
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        //获取传入adapter的条目数，没有则返回 1
        return maininfoList.size() > 0 ? maininfoList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (maininfoList.size() <= 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    /*public Task getItem(int position) {
        this.defItem = position;
        notifyDataSetChanged();
        return maininfoList.get(position);
    }*/

    public void removeItem(int position) {
        this.maininfoList.remove(position);
        notifyDataSetChanged();
    }

    public void changList_add(List<Task> maininfoList) {
        //this.maininfoList.clear();
        this.maininfoList = maininfoList;
        notifyDataSetChanged();
    }


    /****************************************
     * Listener
     */

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(view, (int) view.getTag());
        }
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
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
        private TextView CUSTOM_NO, NO, BUSINESS_SOURCE, GOODS_NAME, num;
        private LinearLayout item_select;

        public ViewHolder(View view) {
            super(view);
            CUSTOM_NO = view.findViewById(R.id.maininfo_CUSTOM_NO);
            NO = view.findViewById(R.id.maininfo_NO);
            BUSINESS_SOURCE = view.findViewById(R.id.maininfo_BUSINESS_SOURCE);
            GOODS_NAME = view.findViewById(R.id.maininfo_GOODS_NAME);
            num = view.findViewById(R.id.maininfo_num);
            item_select = view.findViewById(R.id.item_select);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        //private TextView mEmptyTextView;

        public EmptyViewHolder(View view) {
            super(view);
            //mEmptyTextView = view.findViewById(R.id.rv_empty_text);
        }
    }
}
