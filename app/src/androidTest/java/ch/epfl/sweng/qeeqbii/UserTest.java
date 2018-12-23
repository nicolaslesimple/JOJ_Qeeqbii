package ch.epfl.sweng.qeeqbii;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.chat.Users;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nicol on 27.11.2017.
 */

public class UserTest {

    String name="Nico";
    String image="image";
    String status="hi";
    String thumb_image="snake";
    String age="22";
    String allergies="nothing";
    String degout="cacahete";

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Test
    public void test(){
        Users jonnie = new Users();
        Users user = new Users(name, image, status, thumb_image,age, allergies, degout);
        user.setName("Marcel");
        user.setImage("monkey");
        user.setStatus("Hello");
        user.setThumb_image("lezard");
        user.setAge("70");
        user.setAllergies("fruits");
        user.setDegout("meat");
        assertEquals(user.getName(),"Marcel");
        assertEquals(user.getImage(),"monkey");
        assertEquals(user.getStatus(),"Hello");
        assertEquals(user.getThumb_image(),"lezard");
        assertEquals(user.getAge(),"70");
        assertEquals(user.getAllergies(),"fruits");
        assertEquals(user.getDegout(),"meat");

    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
