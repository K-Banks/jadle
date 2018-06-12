package dao;

import models.Foodtype;
import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Sql2oRestaurantDaoTest {
    private Connection conn;
    private Sql2oRestaurantDao restaurantDao;
    private Sql2oFoodtypeDao foodtypeDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void add() {
        Restaurant testRestaurant = setupRestaurant();
        int originalId = testRestaurant.getId();
        restaurantDao.add(testRestaurant);
        assertNotEquals(originalId, testRestaurant.getId());
    }

    @Test
    public void getAll() {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.add(otherRestaurant);
        assertEquals(2, restaurantDao.getAll().size());
    }

    @Test
    public void findById() {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.add(otherRestaurant);
        assertEquals(otherRestaurant.getId(), restaurantDao.findById(2).getId());
    }

    @Test
    public void update() {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.update(1, "Wish Which", testRestaurant.getAddress(), testRestaurant.getZipcode(), testRestaurant.getPhone(), testRestaurant.getWebsite(), testRestaurant.getEmail());
        assertFalse(restaurantDao.findById(1).getName().equals("Fish Witch"));
    }

    @Test
    public void deleteById() {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.add(otherRestaurant);
        restaurantDao.deleteById(2);
        assertEquals(1, restaurantDao.getAll().size());
    }

    @Test
    public void clearAll() {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant otherRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.add(otherRestaurant);
        restaurantDao.clearAll();
        assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void RestaurantResturnsFoodtypesCorrectly() {
        Foodtype testFoodtype = new Foodtype("Seafood");
        foodtypeDao.add(testFoodtype);
        Foodtype otherFoodtype = new Foodtype("Bar Food");
        foodtypeDao.add(otherFoodtype);
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.addRestaurantToFoodType(testRestaurant, testFoodtype);
        restaurantDao.addRestaurantToFoodType(testRestaurant, otherFoodtype);
        Foodtype[] foodtypes = {testFoodtype, otherFoodtype};
        assertEquals(Arrays.asList(foodtypes), restaurantDao.getAllFoodtypesForARestaurant(testRestaurant.getId()));
    }

    @Test
    public void deleteingRestaurantAlsoUpdatesJoinTable() throws Exception {
        Foodtype testFoodtype  = new Foodtype("Seafood");
        foodtypeDao.add(testFoodtype);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);

        Restaurant altRestaurant = setupAltRestaurant();
        restaurantDao.add(altRestaurant);

        restaurantDao.addRestaurantToFoodType(testRestaurant,testFoodtype);
        restaurantDao.addRestaurantToFoodType(altRestaurant, testFoodtype);

        restaurantDao.deleteById(testRestaurant.getId());
        assertEquals(0, restaurantDao.getAllFoodtypesForARestaurant(testRestaurant.getId()).size());
    }

    //helper
    private Restaurant setupRestaurant() {
        return new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }

    private Restaurant setupAltRestaurant() {
        return new Restaurant("Knee Burger", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }
}