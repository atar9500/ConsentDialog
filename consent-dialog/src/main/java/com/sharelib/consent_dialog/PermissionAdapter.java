package com.sharelib.consent_dialog;

import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.PermissionHolder> {

    /**
     * Data
     */
    private List<PermissionItem> mList;
    private int mNameColor;
    private float mNameSize;

    /**
     * Constructor
     */
    public PermissionAdapter(List<PermissionItem> list,
            @ColorInt int nameColor, float nameSize){
        mList = list;
        mNameColor = nameColor == 0 ? R.color.cardview_dark_background : nameColor;
        mNameSize = nameSize == 0 ? 14 : nameSize;
    }

    /**
     * RecyclerView.Adapter Methods
     */
    @Override
    public PermissionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PermissionHolder(LayoutInflater.from
                (parent.getContext()).inflate(R.layout.permission_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PermissionHolder holder, int position) {
        PermissionItem permissionItem = mList.get(position);
        holder.mName.setText(permissionItem.getName());
        holder.mName.setTextColor(mNameColor);
        holder.mName.setTextSize(mNameSize);
        holder.mImg.setImageResource(permissionItem.getDrawableResource());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    /**
     * Inner Classes
     */
    class PermissionHolder extends RecyclerView.ViewHolder{

        /**
         * UI Widgets
         */
        private ImageView mImg;
        private TextView mName;

        /**
         * Constructor
         */
        PermissionHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.pei_img);
            mName = itemView.findViewById(R.id.pei_name);
        }
    }

}