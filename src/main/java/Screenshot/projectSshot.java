package Screenshot;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class projectSshot {

    // Saves into the "Screenshots" folder at your project root
    private static final String SCREENSHOT_DIR = System.getProperty("user.dir")
            + File.separator + "Screenshots" + File.separator;

    // Flag to ensure folder is cleaned only ONCE per run
    private static boolean cleaned = false;

    private projectSshot() {} // Utility class – no instantiation

    /**
     * Captures a screenshot and saves it with a timestamp.
     * On the first call of each run, clears old screenshots automatically.
     *
     * @param driver the active WebDriver
     * @param label  a short name, e.g. "AfterSearch", "AfterFilters"
     */
    public static void capture(WebDriver driver, String label) {
        if (driver == null) {
            System.out.println("Screenshot skipped: driver is null.");
            return;
        }

        // Delete old screenshots on the very first capture of this run
        if (!cleaned) {
            cleanOldScreenshots();
            cleaned = true;
        }

        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destPath  = SCREENSHOT_DIR + label + "_" + timestamp + ".png";

            File destFile = new File(destPath);
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(src, destFile);

            System.out.println("Screenshot saved: " + destPath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    /** Deletes all previous screenshots in the Screenshots folder. */
    private static void cleanOldScreenshots() {
        File folder = new File(SCREENSHOT_DIR);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile() && f.delete()) {
                        System.out.println("Deleted old screenshot: " + f.getName());
                    }
                }
            }
        }
    }
}