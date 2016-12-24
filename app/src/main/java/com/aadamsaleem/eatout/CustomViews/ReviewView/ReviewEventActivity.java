package com.aadamsaleem.eatout.CustomViews.ReviewView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.models.Review;

import java.util.ArrayList;

public class ReviewEventActivity extends AppCompatActivity {
    private static RecyclerView recyclerView;
    private static Button addReview;
    private Button submitReview;
    private Button postReview;
    private EditText nameOfTheDish;
    private EditText review;
    private RatingBar reviewRating;
    ArrayList<Review> arrayList = new ArrayList<>();

    //String and Integer array for Recycler View Items
//    public static final String[] TITLES= {"Dosa","Idly","Noodles"};
//    public static final Integer[] IMAGES= {R.drawable.cast_ic_notification_forward30,R.drawable.cast_ic_notification_forward30,R.drawable.cast_ic_notification_forward30,R.drawable.cast_ic_notification_forward30,R.drawable.cast_ic_notification_forward30};


    private static String navigateFrom;//String to get Intent Value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_event);
        initViews();
        populatRecyclerView();
    }


    // Initialize the view
    private void initViews() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Set Back Icon on Activity

        navigateFrom = getIntent().getStringExtra("navigateFrom");//Get Intent Value in String

        recyclerView = (RecyclerView)
                findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        postReview = (Button)findViewById(R.id.submitReviewButton);
        addReview = (Button)findViewById(R.id.addReviewButton);

        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReviewEventActivity.this);
                LayoutInflater inflater=ReviewEventActivity.this.getLayoutInflater();
                //this is what I did to added the layout to the alert dialog
                View layout=inflater.inflate(R.layout.make_review,null);
                alertDialogBuilder.setMessage("Please Add a review");
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setView(layout);

                submitReview = (Button) layout.findViewById(R.id.submitReviewButton);
                nameOfTheDish = (EditText)layout.findViewById(R.id.nameET1);
                review = (EditText)layout.findViewById(R.id.nameET2);
                reviewRating = ( RatingBar)layout.findViewById(R.id.ratingBar);
                submitReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameOfDish = nameOfTheDish.getText().toString();
                        String reviewString = review.getText().toString();
                        float ratingF = reviewRating.getRating();
                        arrayList.add(new Review(nameOfDish,reviewString,ratingF));
                        Toast.makeText(getApplicationContext(),"thanks for the review",Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                        populatRecyclerView();

                    }
                });

                alertDialog.show();
            }
        });

        //Set RecyclerView type according to intent value
        if (navigateFrom.equals("horizontal")) {
//            getSupportActionBar().setTitle("Horizontal Recycler View");
            recyclerView.setLayoutManager(new LinearLayoutManager(ReviewEventActivity.this, LinearLayoutManager.VERTICAL, false));
        } else {
           // getSupportActionBar().setTitle("Staggered GridLayout Manager");
            recyclerView
                    .setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));// Here 2 is no. of columns to be displayed
        }
    }

    // populate the list view by adding data to arraylist
    private void populatRecyclerView() {
//        for (int i = 0; i < TITLES.length; i++) {
//            arrayList.add(new Review(TITLES[i],IMAGES[i]));
//        }
        ReviewRecyclerViewAdapter  adapter = new ReviewRecyclerViewAdapter(ReviewEventActivity.this, arrayList);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
