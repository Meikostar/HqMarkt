package com.hqmy.market.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.SortBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

/**
 * @author: xp
 * @date: 2018/7/19
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SortBean> mData;
    private Context        mContext;
    private boolean        isSel;
    private boolean        isSingleCheck;
    private int singlePosition = -1;

    public SortAdapter(Context context, List<SortBean> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }

    public void setSingleCheck(boolean singleCheck) {
        isSingleCheck = singleCheck;
    }

    public void setSinglePosition(int singlePosition) {
        this.singlePosition = singlePosition;
        notifyDataSetChanged();
    }

    public int getSinglePosition() {
        return singlePosition;
    }

    @Override
    public SortAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_sort_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTag = (TextView) view.findViewById(R.id.item_sort_tag);
        viewHolder.ll_user_layout = view.findViewById(R.id.ll_user_layout);
        viewHolder.tvName = (TextView) view.findViewById(R.id.item_sort_name);
        viewHolder.tvRemarkName = (TextView) view.findViewById(R.id.item_sort_remark_name);
        viewHolder.tvIcon = (ImageView) view.findViewById(R.id.item_sort_user_icon);
        viewHolder.img_sel = (ImageView) view.findViewById(R.id.img_sel);
        if (isSel) {
            viewHolder.img_sel.setVisibility(View.VISIBLE);
        }else{
            viewHolder.img_sel.setVisibility(View.GONE);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SortAdapter.ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.ll_user_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }
        holder.tvName.setText(this.mData.get(position).getName());
        holder.tvRemarkName.setText(this.mData.get(position).getRemarkName());
        if (isSingleCheck) {
            if (position == singlePosition) {
                holder.img_sel.setSelected(true);
            } else {
                holder.img_sel.setSelected(false);
            }
        } else {
            holder.img_sel.setSelected(this.mData.get(position).isSel());
        }

        GlideUtils.getInstances().loadRoundCornerImg(mContext, holder.tvIcon, 2.5f, Constants.WEB_IMG_URL_UPLOADS + mData.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName, tvRemarkName;
        ImageView tvIcon, img_sel;
        View ll_user_layout;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     *
     * @param list
     */
    public void updateList(List<SortBean> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public SortBean getItem(int position) {
        return mData.get(position);
    }

    public List<SortBean> getmData() {
        return mData;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
