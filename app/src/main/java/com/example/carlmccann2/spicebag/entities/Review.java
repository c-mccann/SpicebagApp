package com.example.carlmccann2.spicebag.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Created by carlmccann2 on 18/06/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class Review {
    private String review;
    private byte[] spicebag_photo;
    private Integer star_rating;
    private Timestamp created;
    private Restaurant restaurant;
    private User user;


    public Review(String review, byte[] spicebag_photo, Integer star_rating, Timestamp created, Restaurant restaurant, User user) {
        this.review = review;
        this.spicebag_photo = spicebag_photo;
        this.star_rating = star_rating;
        this.created = created;
        this.restaurant = restaurant;
        this.user = user;
    }


    public Review() {
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "review='" + review + '\'' +
                //", spicebag_photo=" + Arrays.toString(spicebag_photo) +
                ", star_rating=" + star_rating +
                ", created=" + created +
                ", restaurant=" + restaurant +
                ", user=" + user +
                '}';
    }
}
