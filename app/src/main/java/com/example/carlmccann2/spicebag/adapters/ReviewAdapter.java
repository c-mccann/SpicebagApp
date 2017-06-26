package com.example.carlmccann2.spicebag.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.carlmccann2.spicebag.R;
import com.example.carlmccann2.spicebag.entities.Review;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by carlmccann2 on 26/06/2017.
 */

// https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/

public class ReviewAdapter extends ArrayAdapter<Review> {
    private ArrayList<Review> reviews;
    public ReviewAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Review> reviews) {
        super(context, resource, reviews);
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.review_in_stream, null);
        }

        Review review = reviews.get(position);


        if(review != null){
            TextView textView = (TextView) view.findViewById(R.id.text_view_review_in_stream);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view_review_in_stream);
            TextView textView2 = (TextView) view.findViewById(R.id.text_view_review_in_stream_actual_review);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_review_in_stream);
            RatingBar ratingBar = (RatingBar) relativeLayout.findViewById(R.id.rating_bar_review_in_stream);
            Button likeButton = (Button) relativeLayout.findViewById(R.id.button_review_in_stream);


            if(textView != null){
                textView.setText(review.getUser().getFirst_name() + " " + review.getUser().getLast_name());
            }
            if(imageView != null){
                // set image bitmap
                //imageView.setImageBitmap();

                // will just set chili pepper image as template
//                https://stackoverflow.com/questions/16804404/create-a-bitmap-drawable-from-file-path



                Bitmap myBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.chili_pepper);
                imageView.setImageBitmap(myBitmap);

            }
            if(textView2 != null){
                textView2.setText(review.getReview());
            }

            if(ratingBar != null){
                ratingBar.setRating(review.getStar_rating());
            }
            if(likeButton != null){

            }

        }

        return view;
    }
}
