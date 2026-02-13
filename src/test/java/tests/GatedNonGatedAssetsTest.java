package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import webpages.GatedNonGatedAssets;

public class GatedNonGatedAssetsTest extends BaseTest {

    @Test
    public void verifyGatedAssetDownload() {

        // ================= START MESSAGE =================
        System.out.println("================+ GatedNonGatedAsset TESTING START +================" );

        driver.get(
                "https://www.ifs.com/en/insights/assets/" +
                        "10-questions-to-ask-when-evaluating-your-next-field-workforce-planning-and-scheduling-solution"
        );

        GatedNonGatedAssets assets = new GatedNonGatedAssets(driver);

        assets.fillGatedForm();

        String pdfUrl = assets.downloadGatedAssetAndCapturePdfUrl();
        String successMsg = assets.getGatedSuccessMessage();

        Assert.assertTrue(pdfUrl.contains(".pdf"),
                "Gated asset PDF not opened");

        Assert.assertEquals(
                successMsg.trim(),
                "Thank you for completing the form. Your download has started."
        );

        System.out.println("✅ Gated asset validated successfully");
        System.out.println("💬 Gated asset success message: " + successMsg);
    }

    @Test
    public void verifyNonGatedAssetDownload() {

        driver.get(
                "https://www.ifs.com/en/insights/assets/2024-predictions-trends-eur-sector"
        );

        GatedNonGatedAssets assets = new GatedNonGatedAssets(driver);

        String pdfUrl = assets.downloadNonGatedAssetAndCapturePdfUrl();

        Assert.assertTrue(pdfUrl.contains(".pdf"),
                "Non-gated asset PDF not opened");

        Assert.assertTrue(pdfUrl.contains("sitecorecontenthub"),
                "PDF not served from Content Hub");

        System.out.println("✅ Non-gated asset validated successfully");

        //================= End MESSAGE =================
        System.out.println("================+ GatedNonGatedAsset TESTING END +================" );
    }
}
