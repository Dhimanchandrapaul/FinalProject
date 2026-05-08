package Screenshot;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class projectSshot {
    private static final String DIR = System.getProperty("user.dir") + "/Screenshots/";
    private static boolean cleaned = false;
    private static int count = 1;

    public static void capture(WebDriver driver, String label) {
        try {
            if (!cleaned) {
                File folder = new File(DIR);
                if (folder.exists()) {
                    for (File f : folder.listFiles()) f.delete();
                }
                cleaned = true;
            }

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(DIR + count++ + "_" + label + ".png");
            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);

            System.out.println("Screenshot saved: " + label);
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}