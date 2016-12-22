package com.aadamsaleem.eatout.LoggedIn.Home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aadamsaleem.eatout.R;

import java.util.List;

/**
 * Created by kirank on 12/22/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Row> rowList;
    private final Context context;
    private final Activity activity;

    public RecyclerViewAdapter(List<Row> modelList, Context context, Activity activity) {
        rowList = modelList;
        this.context = context;
        this.activity = activity;
    }

    public List<Row> getAllRows() {
        return this.rowList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_in_voting, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Activity activityFinal = activity;
        final Row singleRow = rowList.get(position);
        holder.restaurantName.setText(singleRow.getName());
        holder.restaurantVoteCount.setText("vote count: " + singleRow.getVoteCount());

        holder.view.setBackgroundColor(singleRow.isSelected() ? Color.CYAN : Color.WHITE);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleRow.setSelected(!singleRow.isSelected());
                holder.view.setBackgroundColor(singleRow.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(activity, singleRow.getRestaurantID(),Toast.LENGTH_SHORT).show();
                RestaurantInfoDialog cdd = new RestaurantInfoDialog(activityFinal, singleRow);
                cdd.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return rowList == null ? 0 : rowList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView restaurantName;
        private TextView restaurantVoteCount;

        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
            restaurantVoteCount = (TextView) itemView.findViewById(R.id.restaurant_vote_count);
        }
    }
}