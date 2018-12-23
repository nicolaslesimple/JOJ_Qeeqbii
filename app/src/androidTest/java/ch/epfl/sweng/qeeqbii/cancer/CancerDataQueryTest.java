package ch.epfl.sweng.qeeqbii.cancer;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.qeeqbii.ActivityFinisher;
import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;
import ch.epfl.sweng.qeeqbii.activities.CancerDataQueryActivity;
import ch.epfl.sweng.qeeqbii.cancer.query.LevenshteinQueryCancerDB;
import ch.epfl.sweng.qeeqbii.cancer.query.RatcliffQueryCancerDB;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class CancerDataQueryTest {
    //public final ActivityTestRule<CancerDataQueryActivity> mActivityRule =
    //       new ActivityTestRule<>(CancerDataQueryActivity.class);
    @Rule
    public final ActivityTestRule<CancerDataQueryActivity> mActivityRule =
              new ActivityTestRule<>(CancerDataQueryActivity.class);

    // disable product adding for these tests
    @BeforeClass
    public static void run_before() {
        BarcodeToProductActivity.setProductAddingAllowed(false);
    }

    @Before
    public void Initialize() {
        CancerDataBase.readCSVFile(mActivityRule.getActivity().getApplicationContext());
    }


    @Test
    public void testPerfectMatchCancerDataBase() {
        // Useful way of accessing resources
        //onView(withId(R.id.cancerDataQuery)).perform(click());

        List<String[]> query_ans_pairs = new ArrayList<>();

        query_ans_pairs.add(new String[]{"Formaldéhyde", "Substance{mId = 0, mAgent = 'Formaldéhyde', mGroup = '1'}"});
        query_ans_pairs.add(new String[]{"", "Substance{mId = -1, mAgent = 'empty', mGroup = 'empty'}"});
        query_ans_pairs.add(new String[]{"Fluorure de vinyle", "Substance{mId = 98, mAgent = 'Fluorure de vinyle', mGroup = '2A'}"});
        query_ans_pairs.add(new String[]{"Uréthane", "Substance{mId = 889, mAgent = 'Uréthane', mGroup = '2A'}"});
        //query_ans_pairs.add(new String[]{null, "Substance{mId = -1, mAgent = 'empty', mGroup = 'empty'}"});
        // Apparently the replaceText method do not work with a null pointer which looks logical.

        for (String[] iter : query_ans_pairs) {
            onView(ViewMatchers.withId(R.id.cancerDataQueryTextField)).perform(replaceText(iter[0]));
            onView(withId(R.id.cancerDataQueryButton)).perform(click());
            onView(withId(R.id.queryCancerDataAnswerArea)).check(matches(withText(iter[1])));
        }
    }


    @Test
    public void testLevenshteinCancerDataBase() {
        //onView(withId(R.id.cancerDataQuery)).perform(click());
        List<String[]> query_ans_pairs = new ArrayList<>();

        CancerSubstance levenshteinOutput1 = new CancerSubstance();
        CancerSubstance levenshteinOutput2 = new CancerSubstance();
        LevenshteinQueryCancerDB levQuery = new LevenshteinQueryCancerDB();
        try {
            levenshteinOutput1 = levQuery.query("");
            levenshteinOutput2 = levQuery.query("caffeine");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        query_ans_pairs.add(new String[]{"", levenshteinOutput1.toString()});
        query_ans_pairs.add(new String[]{"caffeine", levenshteinOutput2.toString()});


        for (String[] iter : query_ans_pairs) {
            onView(withId(R.id.cancerDataQueryTextField)).perform(replaceText(iter[0]));
            onView(withId(R.id.levenshteinCancerDataQueryButton)).perform(click());
            onView(withId(R.id.queryCancerDataAnswerArea)).check(matches(withText(iter[1])));
        }
    }

    @Test
    public void testRatcliffCancerDataBase() {
        //onView(withId(R.id.cancerDataQuery)).perform(click());
        List<String[]> query_ans_pairs = new ArrayList<>();

        CancerSubstance ratcliffOutput1 = new CancerSubstance();
        CancerSubstance ratcliffOutput2 = new CancerSubstance();
        CancerSubstance ratcliffOutput3 = new CancerSubstance();
        RatcliffQueryCancerDB ratcliffQuery = new RatcliffQueryCancerDB();
        try {
            ratcliffOutput1 = ratcliffQuery.query("");
            ratcliffOutput2 = ratcliffQuery.query("caffeine");
            ratcliffOutput3 = ratcliffQuery.query("Formaldehyd");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        query_ans_pairs.add(new String[]{"", ratcliffOutput1.toString()});
        query_ans_pairs.add(new String[]{"caffeine", ratcliffOutput2.toString()});
        query_ans_pairs.add(new String[]{"Formaldehyd", ratcliffOutput3.toString()});



        for (String[] iter : query_ans_pairs) {
            onView(withId(R.id.cancerDataQueryTextField)).perform(replaceText(iter[0]));
            onView(withId(R.id.ratcliffCancerDataQueryButton)).perform(click());
            onView(withId(R.id.queryCancerDataAnswerArea)).check(matches(withText(iter[1])));
        }
    }

    @Test//(expected = Exception.class)
    public void testRatcliffNegativeThreshold() {
        double threshold = -0.5;
        List<String[]> query_ans_pairs = new ArrayList<>();

        CancerSubstance ratcliffOutput1 = new CancerSubstance();
        CancerSubstance ratcliffOutput2 = new CancerSubstance();
        CancerSubstance ratcliffOutput3 = new CancerSubstance();
        RatcliffQueryCancerDB ratcliffQuery = new RatcliffQueryCancerDB(threshold);
        try {
            ratcliffOutput1 = ratcliffQuery.query("");
            ratcliffOutput2 = ratcliffQuery.query("caffeine");
            ratcliffOutput3 = ratcliffQuery.query("Formaldehyd");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        query_ans_pairs.add(new String[]{"", ratcliffOutput1.toString()});
        query_ans_pairs.add(new String[]{"caffeine", ratcliffOutput2.toString()});
        query_ans_pairs.add(new String[]{"Formaldehyd", ratcliffOutput3.toString()});

        // With a negative threshold
        onView(withId(R.id.ratcliff_threshold_text)).perform(replaceText(Double.toString(threshold)));
        for (String[] iter : query_ans_pairs) {
            onView(withId(R.id.cancerDataQueryTextField)).perform(replaceText(iter[0]));
            onView(withId(R.id.ratcliffCancerDataQueryButton)).perform(click());
            onView(withId(R.id.queryCancerDataAnswerArea)).check(matches(withText(iter[1])));
        }
    }


    @Test//(expected = Exception.class)
    public void testRatcliffTooBigThreshold() {
        double threshold = 10;

        List<String[]> query_ans_pairs = new ArrayList<>();

        CancerSubstance ratcliffOutput1 = new CancerSubstance();
        CancerSubstance ratcliffOutput2 = new CancerSubstance();
        CancerSubstance ratcliffOutput3 = new CancerSubstance();
        RatcliffQueryCancerDB ratcliffQuery = new RatcliffQueryCancerDB(threshold);
        try {
            ratcliffOutput1 = ratcliffQuery.query("");
            ratcliffOutput2 = ratcliffQuery.query("caffeine");
            ratcliffOutput3 = ratcliffQuery.query("Formaldehyd");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        query_ans_pairs.add(new String[]{"", ratcliffOutput1.toString()});
        query_ans_pairs.add(new String[]{"caffeine", ratcliffOutput2.toString()});
        query_ans_pairs.add(new String[]{"Formaldehyd", ratcliffOutput3.toString()});

        // With a negative threshold
        onView(withId(R.id.ratcliff_threshold_text)).perform(replaceText(Double.toString(threshold)));
        for (String[] iter : query_ans_pairs) {
            onView(withId(R.id.cancerDataQueryTextField)).perform(replaceText(iter[0]));
            onView(withId(R.id.ratcliffCancerDataQueryButton)).perform(click());
            onView(withId(R.id.queryCancerDataAnswerArea)).check(matches(withText(iter[1])));
        }
    }

    @AfterClass
    public static void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }
}
