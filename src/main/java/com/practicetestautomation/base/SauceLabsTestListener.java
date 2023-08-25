package com.practicetestautomation.base;

import com.saucelabs.saucerest.SauceREST;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import static com.practicetestautomation.base.SauceLabsFactory.AUTHENTICATION;

public class SauceLabsTestListener extends TestListenerAdapter {

    private boolean isSauce;
    private String sessionId;
    private SauceREST sauceREST;

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        isSauce = (boolean) result.getTestContext().getAttribute("sauce");
        if (isSauce) {
            this.sessionId = (String) result.getTestContext().getAttribute("sessionId");
            this.sauceREST = new SauceREST(AUTHENTICATION.getUsername(), AUTHENTICATION.getAccessKey());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        super.onTestSuccess(result);
        if (isSauce) sauceREST.jobPassed(sessionId);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        super.onTestFailure(result);
        if (isSauce) {
            sauceREST.jobFailed(sessionId);
            Throwable throwable = result.getThrowable();
            String message = throwable.getMessage();
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            String link = "SauceLabs job link: https://app.eu-central-1.saucelabs.com/tests/" + sessionId;
            String sauceTestName = result.getTestContext().getName() + " | " + result.getTestContext().getName();

            String newMessage = sauceTestName + "\n" + link + "\n" + message;
            Throwable newThrowable = new Throwable(newMessage, throwable);
            newThrowable.setStackTrace(stackTrace);
            result.setThrowable(newThrowable);
        }

    }
}
