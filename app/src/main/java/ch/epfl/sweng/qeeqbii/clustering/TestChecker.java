package ch.epfl.sweng.qeeqbii.clustering;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by adrien on 05.12.17.
 */

public class TestChecker {

    private static AtomicBoolean isRunningTest;

    // provide as argument:
    // "myApp.package.name.test.class.name"
    public static synchronized boolean isRunningTest (String classTestName) {
        if (null == isRunningTest) {
            boolean istest;

            try {
                Class.forName(classTestName);
                istest = true;
            } catch (ClassNotFoundException e) {
                istest = false;
            }

            isRunningTest = new AtomicBoolean(istest);
        }

        return isRunningTest.get();
    }


}
