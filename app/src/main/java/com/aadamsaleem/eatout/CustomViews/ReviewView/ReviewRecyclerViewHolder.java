package com.aadamsaleem.eatout.CustomViews.ReviewView;

/**
 * Created by kirank on 12/22/16.
 */


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.aadamsaleem.eatout.R;

public class ReviewRecyclerViewHolder extends RecyclerView.ViewHolder  {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public TextView review;
    public RatingBar ratings;
    public ImageView imageview;




    public ReviewRecyclerViewHolder(View view) {
        super(view);
        // Find all views ids

        this.title = (TextView) view
                .findViewById(R.id.title);
        this.review = (TextView) view.findViewById(R.id.review);
        this.ratings = (RatingBar)view.findViewById(R.id.givenRatingBar);
    }
}