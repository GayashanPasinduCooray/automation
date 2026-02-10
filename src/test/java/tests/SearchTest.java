package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import webpages.Search;

public class SearchTest extends BaseTest {

    @Test
    public void validateSearchEndToEndFlow() {

        Search search = new Search(driver);

        search.acceptCookies();
        search.searchUsingPredictive("IFS");

        Assert.assertTrue(
                search.areMainFiltersVisible(),
                "❌ Main filters not visible"
        );

        search.applyRandomIndustryFilterSoftValidation();

        // Existing capture
        search.captureAndPrintFirst3Results();

        // SORT VALIDATIONS
        search.sortAndCaptureResults("Title A-Z", 5);
        search.sortAndCaptureResults("Title Z-A", 5);

        Assert.assertTrue(
                search.areResultsDisplayedCorrectly(),
                "❌ Results validation failed"
        );

        String url = search.openRandomResultAndGetUrl();
        Assert.assertTrue(
                url.toLowerCase().contains("ifs"),
                "❌ Opened URL is incorrect"
        );

        System.out.println("✅ Opened result URL: " + url);
    }
}
