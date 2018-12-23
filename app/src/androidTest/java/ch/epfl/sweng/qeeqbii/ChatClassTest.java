package ch.epfl.sweng.qeeqbii;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.chat.Chats;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nicol on 26.11.2017.
 */

public class ChatClassTest {
    @Test
    public void testGetSet () {
        String date = "november";
        Chats chat = new Chats (date);
        assertEquals(date,chat.getDate());
        chat.setDate("december");
        assertEquals("december",chat.getDate());
    }

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}

