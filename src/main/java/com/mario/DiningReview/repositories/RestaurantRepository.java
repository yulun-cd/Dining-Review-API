package com.mario.DiningReview.repositories;

import com.mario.DiningReview.models.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Boolean existsRestaurantByZipcodeAndName(String zipcode, String name);
    List<Restaurant> findByZipcodeAndPeanutScoreGreaterThanEqualAndEggScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(String zipcode, double peanutScore, double eggScore, double dairyScore);
    List<Restaurant> findByZipcodeAndPeanutScoreGreaterThanEqualAndEggScoreGreaterThanEqual(String zipcode, double peanutScore, double eggScore);
    List<Restaurant> findByZipcodeAndPeanutScoreGreaterThanEqual(String zipcode, double peanutScore);
    List<Restaurant> findByZipcodeAndPeanutScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(String zipcode, double peanutScore, double dairyScore);
    List<Restaurant> findByZipcodeAndEggScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(String zipcode, double eggScore, double dairyScore);
    List<Restaurant> findByZipcodeAndDairyScoreGreaterThanEqual(String zipcode, double dairyScore);
    List<Restaurant> findByZipcodeAndEggScoreGreaterThanEqual(String zipcode, double eggScore);

}
