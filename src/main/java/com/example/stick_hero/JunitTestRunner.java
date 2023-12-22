package com.example.stick_hero;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class JunitTestRunner
{
    public static void main(String[] args)
    {
        HelloApplication.main(args);
        Result result = JUnitCore.runClasses(JunitTest.class);
        System.out.println("Test Result: " + result.wasSuccessful());
        for (Failure failure : result.getFailures())
        {
            System.out.println(failure.toString());
        }
    }
}
