package com.bkozyrev.testtask.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkozyrev.testtask.R;
import com.bumptech.glide.Glide;

/**
 * Created by 123 on 19.12.2015.
 */
public class StaffRecyclerAdapter extends RecyclerView.Adapter<StaffRecyclerAdapter.StaffViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private View.OnClickListener mListener;

    public StaffRecyclerAdapter(Context context, Cursor cursor, View.OnClickListener listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_list, parent, false);
        return new StaffViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(StaffViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String firstName, secondName, lastName, imagePath;
        firstName = mCursor.getString(mCursor.getColumnIndex("firstName"));
        secondName = mCursor.getString(mCursor.getColumnIndex("secondName"));
        lastName = mCursor.getString(mCursor.getColumnIndex("lastName"));
        imagePath = mCursor.getString(mCursor.getColumnIndex("imagePath"));

        holder.setNameText(firstName + " " + secondName + " " + lastName);
        holder.setImageStaff(imagePath);
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder{

        //@Bind(R.id.name_view) TextView mName;
        TextView mName;
        ImageView mImageStaff;

        public StaffViewHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);

            mName = (TextView)itemView.findViewById(R.id.name_view);
            mImageStaff = (ImageView)itemView.findViewById(R.id.image_staff);
            //ButterKnife.bind(mContext, itemView);
        }

        public void setNameText(String nameText){
            mName.setText(nameText);
        }

        public void setImageStaff(String path){
            Glide.with(mContext).load(Uri.parse(path)).into(mImageStaff);
        }
    }
}
