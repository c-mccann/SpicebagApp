package com.example.carlmccann2.spicebag.entities;

import java.sql.Timestamp;

/**
 * Created by carlmccann2 on 18/06/2017.
 */

public class Review {
    private String review;
    private byte[] spicebag_photo;
    private Integer star_rating;
    private Timestamp created;
    private Integer restaurant_id;
    private Integer user_id;


    public Review(String review, byte[] spicebag_photo, Integer star_rating, Timestamp created, Integer restaurant_id, Integer user_id) {
        this.review = review;
        this.spicebag_photo = spicebag_photo;
        this.star_rating = star_rating;
        this.created = created;
        this.restaurant_id = restaurant_id;
        this.user_id = user_id;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public byte[] getSpicebag_photo() {
        return spicebag_photo;
    }

    public void setSpicebag_photo(byte[] spicebag_photo) {
        this.spicebag_photo = spicebag_photo;
    }

    public Integer getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(Integer star_rating) {
        this.star_rating = star_rating;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
