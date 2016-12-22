package com.aadamsaleem.eatout.LoggedIn.Event;
/**
 * Created by kirank on 12/22/16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.models.Review;

import java.util.ArrayList;

/**
 * Created by SONU on 25/09/15.
 */
public class ReviewRecyclerViewAdapter extends
        RecyclerView.Adapter<ReviewRecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<Review> arrayList;
    private Context context;

    public ReviewRecyclerViewAdapter(Context context,
                                ArrayList<Review> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(ReviewRecyclerViewHolder holder, int position) {
        final Review model = arrayList.get(position);

        ReviewRecyclerViewHolder mainHolder = (ReviewRecyclerViewHolder) holder;// holder

        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                model.getImage());// This will convert drawbale image into
        // bitmap

        // setting title
        mainHolder.title.setText(model.getTitle());

        mainHolder.imageview.setImageBitmap(image);

    }

    @Override
    public ReviewRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.card, viewGroup, false);
        ReviewRecyclerViewHolder listHolder = new ReviewRecyclerViewHolder(mainGroup);
        return listHolder;

    }



}
