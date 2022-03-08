package com.fda_sampling.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fda_sampling.R;
import com.fda_sampling.model.Task;

import java.util.List;

public class MainInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        View.OnClickListener, View.OnLongClickListener {

    private static final int VIEW_TYPE = -1;
    private List<Task> mainInfoList;
    private Context mContext;
    private OnClickListener mOnClickListener = null;
    private OnLongClickListener mOnLongClickListener = null;
    private int mMode = 0, last_pos = -1;
    private int[] pos = new int[]{-1, -1};

    public MainInfoAdapter(Context context, List<Task> mainInfoList) {
        this.mContext = context;
        this.mainInfoList = mainInfoList;
    }

    //加载item 的布局  创建ViewHolder实例
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View emptyView = LayoutInflater.from(mContext).inflate(R.layout.rv_empty, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_maininfo, parent, false);
        if (VIEW_TYPE == viewType) {
            return new EmptyViewHolder(emptyView);
        }
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            if (mMode == 0) {
                ((ViewHolder) holder).img_check.setVisibility(View.GONE);
                ((ViewHolder) holder).item_buttons.setVisibility(View.VISIBLE);
                /*((ViewHolder) holder).btn_copy.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).btn_paste.setVisibility(View.VISIBLE);*/
            } else if (mMode == 1) {
                ((ViewHolder) holder).img_check.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).item_buttons.setVisibility(View.GONE);
                /*((ViewHolder) holder).btn_copy.setVisibility(View.GONE);
                ((ViewHolder) holder).btn_paste.setVisibility(View.GONE);*/
            }
            Task task = mainInfoList.get(position);
            if (task.getIsSelect() == 0) {
                ((ViewHolder) holder).img_check.setImageResource(R.mipmap.ic_check_no);
            } else if (task.getIsSelect() == 1) {
                ((ViewHolder) holder).img_check.setImageResource(R.mipmap.ic_check_yes);
            }
            ((ViewHolder) holder).CUSTOM_NO.setText(task.getCUSTOM_NO());
            ((ViewHolder) holder).NO.setText(task.getNO());
            ((ViewHolder) holder).BUSINESS_SOURCE.setText(task.getBUSINESS_SOURCE());
            ((ViewHolder) holder).SUPPLIER.setText(task.getSUPPLIER());
            ((ViewHolder) holder).GOODS_NAME.setText(task.getGOODS_NAME());
            ((ViewHolder) holder).num.setText(String.valueOf(position + 1));
            ((ViewHolder) holder).itemView.setTag(position);
            ((ViewHolder) holder).item_select.setTag(position);
            ((ViewHolder) holder).btn_copy.setTag(position);
            ((ViewHolder) holder).btn_paste.setTag(position);

            ((ViewHolder) holder).btn_copy.setText("复制");
            if (pos[1] != -1 && position == pos[1]) {
                ((ViewHolder) holder).btn_copy.setText("已复制");
            }

            if (task.getSTATE() != null) {
                if (task.getSTATE().equals("采样信息审核退回")) {
                    ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#ffff0000"));
                    ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#ffff0000"));
                    ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor
                            ("#ffff0000"));
                    ((ViewHolder) holder).SUPPLIER.setTextColor(Color.parseColor("#ffff0000"));
                    ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#ffff0000"));
                } else if (task.getSTATE().equals("样品审核退回")) {
                    ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#FF8000"));
                    ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#FF8000"));
                    ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor
                            ("#FF8000"));
                    ((ViewHolder) holder).SUPPLIER.setTextColor(Color.parseColor("#FF8000"));
                    ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#FF8000"));
                } else if (task.getSTATE().equals("基础信息审核退回")) {
                    ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#055EDD"));
                    ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#055EDD"));
                    ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor
                            ("#055EDD"));
                    ((ViewHolder) holder).SUPPLIER.setTextColor(Color.parseColor("#055EDD"));
                    ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#055EDD"));
                }
            } else {
                ((ViewHolder) holder).CUSTOM_NO.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).NO.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).BUSINESS_SOURCE.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).SUPPLIER.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).GOODS_NAME.setTextColor(Color.parseColor("#727272"));
                ((ViewHolder) holder).num.setTextColor(Color.parseColor("#727272"));
            }
        }
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        //获取传入adapter的条目数，没有则返回 1
        return mainInfoList.size() > 0 ? mainInfoList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mainInfoList.size() <= 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    /*public Task getItem(int position) {
        this.defItem = position;
        notifyDataSetChanged();
        return mainInfoList.get(position);
    }*/

    public void removeItem(int position) {
        this.mainInfoList.remove(position);
        notifyDataSetChanged();
    }

    public void changList_add(List<Task> mainInfoList) {
        //this.mainInfoList.clear();
        this.mainInfoList = mainInfoList;
        notifyDataSetChanged();
    }

    public void Refresh_item(int position) {
        pos[1] = pos[0];
        pos[0] = position;
        notifyItemChanged(position);
    }

    public void Refresh_all() {
        pos[1] = -1;
        pos[0] = -1;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(view, (int) view.getTag()); //getTag()获取数据
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView CUSTOM_NO, NO, BUSINESS_SOURCE, SUPPLIER, GOODS_NAME, num;
        private ImageView img_check;
        private Button btn_copy, btn_paste;
        private LinearLayout item_select, item_buttons;

        public ViewHolder(View view) {
            super(view);
            CUSTOM_NO = view.findViewById(R.id.mainInfo_CUSTOM_NO);
            NO = view.findViewById(R.id.mainInfo_NO);
            BUSINESS_SOURCE = view.findViewById(R.id.mainInfo_BUSINESS_SOURCE);
            SUPPLIER=view.findViewById(R.id.mainInfo_SUPPLIER);
            GOODS_NAME = view.findViewById(R.id.mainInfo_GOODS_NAME);
            num = view.findViewById(R.id.mainInfo_num);
            img_check = view.findViewById(R.id.mainInfo_check);
            item_buttons = view.findViewById(R.id.item_buttons);
            btn_copy = view.findViewById(R.id.btn_copy);
            btn_paste = view.findViewById(R.id.btn_paste);
            item_select = view.findViewById(R.id.item_select_task);

            // 为ItemView添加点击事件
            //itemView.setOnClickListener(MainInfoAdapter.this);
            item_select.setOnClickListener(MainInfoAdapter.this);
            btn_copy.setOnClickListener(MainInfoAdapter.this);
            btn_paste.setOnClickListener(MainInfoAdapter.this);

            item_select.setOnLongClickListener(MainInfoAdapter.this);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        //private TextView mEmptyTextView;

        public EmptyViewHolder(View view) {
            super(view);
            //mEmptyTextView = view.findViewById(R.id.rv_empty_text);
        }
    }

    public void setMode(int mode) {
        this.mMode = mode;
        notifyDataSetChanged();
    }

    public int getMode() {
        return this.mMode;
    }
}
