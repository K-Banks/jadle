package dao;

import models.Restaurant;
import org.sql2o.Sql2o;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;
import models.Foodtype;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sql2oRestaurantDao implements RestaurantDao {
    private final Sql2o sql2o;


    public Sql2oRestaurantDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (name, address, zipcode, phone, website, email) VALUES (:name, :address, :zipcode, :phone, :website, :email)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(restaurant)
                    .executeUpdate()
                    .getKey();
            restaurant.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Restaurant> getAll() {
        String sql = "SELECT * FROM restaurants";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Restaurant.class);
        }
    }

    @Override
    public Restaurant findById(int id) {
        String sql = "SELECT * FROM restaurants WHERE id=:id";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Restaurant.class);
        }
    }

    @Override
    public void update(int id, String name, String address, String zipcode, String phone, String website, String email) {
        String sql = "UPDATE restaurants SET (name, address, zipcode, phone, website, email) = (:name, :address, :zipcode, :phone, :website, :email) WHERE id = :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("address", address)
                    .addParameter("zipcode", zipcode)
                    .addParameter("phone", phone)
                    .addParameter("website", website)
                    .addParameter("email", email)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch(Sql2oException ex) {
            System.out.print(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM restaurants WHERE id=:id";
        String deleteJoin = "DELETE FROM restaurants_foodtypes WHERE restaurantid = :restaurantId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(deleteJoin)
                    .addParameter("restaurantId", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM restaurants";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.print(ex);
        }
    }

    @Override
    public void addRestaurantToFoodType(Restaurant restaurant, Foodtype foodtype) {
        String sql = "INSERT INTO restaurants_foodtypes  (restaurantid, foodtypeid) VALUES (:restaurantid, :foodtypeid)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("restaurantid", restaurant.getId())
                    .addParameter("foodtypeid", foodtype.getId())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Foodtype> getAllFoodtypesForARestaurant(int restaurantId) {
        ArrayList<Foodtype> foodtypes = new ArrayList<>();

        String joinQuery = "SELECT foodtypeid FROM restaurants_foodtypes WHERE restaurantid = :restaurantId";

        try (Connection con = sql2o.open()) {
            List<Integer> allFoodtypesIds = con.createQuery(joinQuery)
                    .addParameter("restaurantId", restaurantId)
                    .executeAndFetch(Integer.class);
            for (Integer foodId: allFoodtypesIds) {
                String foodtypeQuery = "SELECT * FROM foodtypes WHERE id=:foodtypeId";
                foodtypes.add(
                        con.createQuery(foodtypeQuery)
                            .addParameter("foodtypeId", foodId)
                            .executeAndFetchFirst(Foodtype.class)
                );
            }
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

        return foodtypes;
    }
}
