package org.example.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.example.FileReadAndWrite.ScreenShotsCode;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {


    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {


        String reportPath = System.getProperty("user.dir")
                + "\\ExtentReports\\TestReport.html";
        sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Display Bookshelves Report");
        sparkReporter.config().setReportName("Search & Filter Test Results");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setEncoding("UTF-8");


        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Project",      "Display BookShelves");
        extent.setSystemInfo("Tester",       "QA Team");
        extent.setSystemInfo("Browser",      "Chrome");
        extent.setSystemInfo("Base URL",     "https://www.pepperfry.com/");
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("OS",           System.getProperty("os.name"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName  = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest test = (description != null && !description.isEmpty())
                ? extent.createTest(methodName, description)
                : extent.createTest(methodName);

        // Store in ThreadLocal so onTestSuccess / onTestFailure can reach it
        extentTest.set(test);
        extentTest.get().log(Status.INFO, "Test Started : " + methodName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS,
                "Test Passed : " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();

        extentTest.get().log(Status.FAIL, "Test Failed : " + methodName);

        extentTest.get().log(Status.FAIL,
                "Reason : " + result.getThrowable().getMessage());


        extentTest.get().fail(result.getThrowable());

//        try {
//            String screenshotName = "FAIL_" + methodName + "_"
//                    + System.currentTimeMillis() + ".png";
//
//            String screenshotFullPath =
//                    ""
//                            + screenshotName;
//
//            ScreenShotsCode.takeScreenshots(screenshotName);
//
//            extentTest.get().addScreenCaptureFromPath(
//                    screenshotFullPath, "Failure Screenshot");
//
//        } catch (Exception e) {
//            extentTest.get().log(Status.WARNING,
//                    "Screenshot could not be captured : " + e.getMessage());
//        }
//    }
    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP,
                "Test Skipped : " + result.getMethod().getMethodName());

        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP,
                    "Skip Reason : " + result.getThrowable().getMessage());
        }
    }


    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}