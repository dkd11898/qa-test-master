package mission.pages;

import mission.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutOverviewPage extends BasePage {

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> itemPriceElements;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement itemTotalLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    public CheckoutOverviewPage() {
        PageFactory.initElements(driver, this);
    }

    public double calculateDisplayedItemTotal() {
        return itemPriceElements.stream()
                .mapToDouble(element -> parsePrice(element.getText()))
                .sum();
    }

    public double getItemTotalValue() {
        return parsePrice(itemTotalLabel.getText().replace("Item total: ", ""));
    }

    public double getTaxValue() {
        return parsePrice(taxLabel.getText().replace("Tax: ", ""));
    }

    public void verifyItemTotal() {
        double expectedTotal = calculateDisplayedItemTotal();
        double displayedTotal = getItemTotalValue();
        if (Math.abs(displayedTotal - expectedTotal) > 0.01) {
            throw new AssertionError(String.format("Expected item total %.2f but was %.2f", expectedTotal, displayedTotal));
        }
    }

    public void verifyTaxRate(double expectedPercentage) {
        double itemTotal = getItemTotalValue();
        double tax = getTaxValue();
        double actualRate = (tax / itemTotal) * 100.0;
        if (Math.abs(actualRate - expectedPercentage) > 0.5) {
            throw new AssertionError(String.format("Expected tax rate %.1f%% but was %.1f%%", expectedPercentage, actualRate));
        }
    }

    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replaceAll("[^0-9.]+", ""));
    }
}
