package dao;

import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oRestaurantDaoTest {
    private Connection conn;
    private Sql2oRestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
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

    //helper
    private Restaurant setupRestaurant() {
        return new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }
}