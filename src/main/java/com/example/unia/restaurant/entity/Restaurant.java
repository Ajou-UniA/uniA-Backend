package com.example.unia.restaurant.entity;

import com.example.unia.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "restaurant")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantName;
    private String restaurantAddress;
    private double latitude;
    private double longitude;

    public void changeRestaurantAddress(String address){
        this.restaurantAddress = address;
    }
}

