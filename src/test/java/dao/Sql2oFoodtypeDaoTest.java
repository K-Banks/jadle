package dao;

import models.Foodtype;
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

    //helper
    private Foodtype setupFoodtype() {
        return new Foodtype("Mexican");
    }
}