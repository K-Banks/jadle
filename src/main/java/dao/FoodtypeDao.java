package dao;

import models.Foodtype;
import models.Restaurant;
import java.util.List;

public interface FoodtypeDao {

    //create
    void add(Foodtype foodtype);
    void addFoodtypeToRestaurant(Foodtype foodtype, Restaurant restaurant);

    //read
    List<Foodtype> getAll();
    List<Restaurant> getAllRestaurantsForAFoodtype(int foodtypeId);
    Foodtype findById(int foodtypeId);

    //update
    //omit for now

    //delete
    void deleteById(int id);
    void clearAll();
}