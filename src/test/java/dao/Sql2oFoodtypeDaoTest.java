package dao;

import models.Foodtype;
import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oFoodtypeDaoTest {
    private Sql2oRestaurantDao restaurantDao;
    private Sql2oFoodtypeDao foodtypeDao;
    private Connection conn;

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
        Foodtype testFoodType = setupFoodtype();
        int originalId = testFoodType.getId();
        foodtypeDao.add(testFoodType);
        assertNotEquals(originalId, testFoodType.getId());
    }

    @Test
    public void getAll() {
        Foodtype testFoodType = setupFoodtype();
        Foodtype testFoodType2 = setupFoodtype();
        Foodtype testFoodType3 = setupFoodtype();
        foodtypeDao.add(testFoodType);
        foodtypeDao.add(testFoodType2);
        foodtypeDao.add(testFoodType3);
        assertEquals(3, foodtypeDao.getAll().size());
    }

    @Test
    public void deleteById() {
        Foodtype testFoodType = setupFoodtype();
        Foodtype testFoodType2 = setupFoodtype();
        Foodtype testFoodType3 = setupFoodtype();
        foodtypeDao.add(testFoodType);
        foodtypeDao.add(testFoodType2);
        foodtypeDao.add(testFoodType3);
        foodtypeDao.deleteById(2);
        assertEquals(2, foodtypeDao.getAll().size());
    }

    @Test
    public void clearAll() {
        Foodtype testFoodType = setupFoodtype();
        Foodtype testFoodType2 = setupFoodtype();
        Foodtype testFoodType3 = setupFoodtype();
        foodtypeDao.add(testFoodType);
        foodtypeDao.add(testFoodType2);
        foodtypeDao.add(testFoodType3);
        foodtypeDao.clearAll();
        assertEquals(0, foodtypeDao.getAll().size());
    }

    @Test
    public void addFoodTypeToRestaurantAddsTypeCorrectly() {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant altRestaurant = setupAltRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.add(altRestaurant);
        Foodtype testFoodtype = setupFoodtype();
        foodtypeDao.add(testFoodtype);
        foodtypeDao.addFoodtypeToRestaurant(testFoodtype, testRestaurant);
        foodtypeDao.addFoodtypeToRestaurant(testFoodtype, altRestaurant);
        assertEquals(2, foodtypeDao.getAllRestaurantsForAFoodtype(testFoodtype.getId()).size());
    }

    @Test
    public void deleteingFoodtypeAlsoUpdatesJoinTable() throws Exception {
        Foodtype testFoodtype  = new Foodtype("Seafood");
        foodtypeDao.add(testFoodtype);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);

        Restaurant altRestaurant = setupAltRestaurant();
        restaurantDao.add(altRestaurant);

        Foodtype altFoodtype = new Foodtype("Mexican");
        foodtypeDao.add(altFoodtype);

        foodtypeDao.addFoodtypeToRestaurant(testFoodtype,testRestaurant);
        foodtypeDao.addFoodtypeToRestaurant(altFoodtype,testRestaurant);
        foodtypeDao.addFoodtypeToRestaurant(testFoodtype,altRestaurant);
        foodtypeDao.addFoodtypeToRestaurant(altFoodtype,altRestaurant);

        foodtypeDao.deleteById(testFoodtype.getId());
        assertEquals(0, foodtypeDao.getAllRestaurantsForAFoodtype(testFoodtype.getId()).size());
    }

    //helper
    private Foodtype setupFoodtype() {
        return new Foodtype("Mexican");
    }

    private Restaurant setupRestaurant() {
        return new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }

    private Restaurant setupAltRestaurant() {
        return new Restaurant("Knee Burger", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }
}