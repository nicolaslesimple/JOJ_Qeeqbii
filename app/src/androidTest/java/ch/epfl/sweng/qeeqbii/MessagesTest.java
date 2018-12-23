package ch.epfl.sweng.qeeqbii;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.chat.Messages;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nicol on 27.11.2017.
 */

public class MessagesTest {
    @Test
    public void test() {
        Messages message1 = new Messages("coucou");
        message1.setFrom("coucou2");
        assertEquals(message1.getFrom(),"coucou2");
        Messages message2 = new Messages("nein", "text", 10, true);
        message2.setMessage("ya");
        message2.setType("image");
        message2.setTime(1);
        message2.setSeen(false);
        assertEquals(message2.getMessage(),"ya");
        assertEquals(message2.getType(),"image");
        assertEquals(message2.getTime(),1);
        assertEquals(message2.isSeen(),false);
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

