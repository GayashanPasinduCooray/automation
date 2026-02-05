package tests;

import base.BaseTest;
import webpages.LanguageSelector;
import org.testng.annotations.Test;

public class LanguageSelectorTest extends BaseTest {

    @Test
    public void validateLanguageSelector() {
        LanguageSelector languageSelector = new LanguageSelector(driver);
        languageSelector.validateAllLanguages();
    }
}
