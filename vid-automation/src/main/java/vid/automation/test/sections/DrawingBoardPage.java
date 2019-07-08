package vid.automation.test.sections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.testng.AssertJUnit.assertTrue;
import static vid.automation.test.Constants.DrawingBoard.DEPLOY_BUTTON;
import static vid.automation.test.Constants.DrawingBoard.HIGHLIGHTED_COLOR;
import static vid.automation.test.Constants.DrawingBoard.QUANTITY_LABEL_TEST_ID;
import static vid.automation.test.Constants.DrawingBoard.QUANTITY_LABEL_VALUE;
import static vid.automation.test.Constants.DrawingBoard.SERVICE_INSTANCE_TEST_ID;
import static vid.automation.test.Constants.DrawingBoard.SERVICE_INSTANCE_VALUE;
import static vid.automation.test.Constants.DrawingBoard.SERVICE_NAME;
import static vid.automation.test.Constants.DrawingBoard.SERVICE_QUANTITY;
import static vid.automation.test.Constants.DrawingBoard.SERVICE_STATUS;
import static vid.automation.test.Constants.DrawingBoard.STATUS_TEXT;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Wait;
import vid.automation.test.test.NewServiceInstanceTest;

public class DrawingBoardPage extends VidBasePage {

    private static final Logger logger = LogManager.getLogger(DrawingBoardPage.class);

    public DrawingBoardPage(){
        super();
    }

    public void expandTreeByClickingNode(String nodeName, String... children) {
        checkNodesVisible(children, false);
        clickNode(nodeName);
        checkNodesVisible(children, true);
    }

    public void clickNode(String nodeName) {
        Click.byTestId(Constants.DrawingBoard.NODE_PREFIX + nodeName);
    }

    public void expandFirstItemInTreeByExpanderIcon(String treeDataTestId, String... children) {
        checkNodesVisible(children, false);
        Click.byXpath("//tree-root[@data-tests-id='" + treeDataTestId + "']//span[@class='" + Constants.DrawingBoard.TOGGLE_CHILDREN + "']");
        checkNodesVisible(children, true);
    }

    public void checkLeafNodeHasNoExpander(String nodeName){
        WebElement webElement = Get.byXpath("//div[contains(@class, '" + Constants.DrawingBoard.TREE_NODE_LEAF + "') and .//div[@data-tests-id='" + Constants.DrawingBoard.NODE_PREFIX + nodeName + "']]");
        Assert.assertNotNull(webElement, "There is an expander to node " + nodeName + " without children");
    }

    public void verifyNonCollapsableTreeByClickingNode(String nodeName, String... children) {
        checkNodesVisible(children, true);
        clickNode(nodeName);
        checkNodesVisible(children, true);
    }

    public void collapseFirstItemInTreeByCollapseIcon(String treeDataTestId, String... children) {
        checkNodesVisible(children, true);
        Click.byXpath("//tree-root[@data-tests-id='" + treeDataTestId + "']//span[@class='" + Constants.DrawingBoard.TOGGLE_CHILDREN + "']");
        checkNodesVisible(children, false);
    }

    public void RefreshPage(){
        GeneralUIUtils.getDriver().navigate().refresh();
    }

    public void assertInitalTextOfTree(String treeDataTestId, String[] initialElements) {
        WebElement webElement = Get.byTestId(treeDataTestId);
        String expected = String.join("\n", initialElements);
        Wait.byText(expected);
        Assert.assertEquals(webElement.getText(), expected);
    }

    public void checkAddButton(String[] rootElements){
        String previousAddButton = null;
        for (String root : rootElements) {
            String currentButton = Constants.DrawingBoard.NODE_PREFIX + root + Constants.DrawingBoard.ADD_BUTTON;
            checkThatButtonNotExist(currentButton);
            GeneralUIUtils.hoverOnAreaByTestId(Constants.DrawingBoard.NODE_PREFIX + root);
            checkThatButtonExist(currentButton);
            if (previousAddButton != null) {
                checkThatButtonNotExist(previousAddButton);
            }
            Click.byTestId(currentButton);
            previousAddButton = currentButton;
        }
    }

    public void clickAddButtonByNodeName(String treeNodeId) {
        String nodeElement = "node-"+ treeNodeId;
        String addButtonTestId = Constants.DrawingBoard.NODE_PREFIX + treeNodeId + Constants.DrawingBoard.ADD_BUTTON;
        GeneralUIUtils.hoverOnAreaByTestId(nodeElement);
        GeneralUIUtils.hoverOnAreaByTestId(addButtonTestId);
        Click.byTestId(addButtonTestId);
    }

    private void checkThatButtonNotExist(String dataTestId){
//        Assert.assertFalse(GeneralUIUtils.isElementVisibleByTestId(dataTestId),"button " + dataTestId + " should not exist");
    }

    private void checkThatButtonExist(String dataTestId){
//        Assert.assertTrue(GeneralUIUtils.isElementVisibleByTestId(dataTestId), "button " + dataTestId + " should exist");
    }

    private void checkThatPseudoElementNotExist(String dataTestId) {
        assertPseudoElementDisplayProp(dataTestId, "none");
    }

    private void assertPseudoElementDisplayProp(String dataTestId, String expectedCssDisplayProp){
        final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) GeneralUIUtils.getDriver();
        final Object cssDisplayProp = javascriptExecutor.executeScript("" +
                "return window.getComputedStyle(" +
                "   document.querySelector('[data-tests-id=\""+dataTestId+"\"]'),':before'" +
                ").getPropertyValue('display')"
        );
        assertThat("button " + dataTestId + " should exist", cssDisplayProp, is(expectedCssDisplayProp));
    }

    private void checkThatPseudoElementExist(String dataTestId) {
        assertPseudoElementDisplayProp(dataTestId, "inline-block");
    }

    public void checkThatContextMenuExist(String contextMenu){
        Assert.assertTrue(GeneralUIUtils.isWebElementExistByTestId(contextMenu), "context menu should appear");
    }

    public void checkThatContextMenuNotExist(String contextMenu){
        Assert.assertFalse(GeneralUIUtils.isWebElementExistByTestId(contextMenu), "context menu should not appear");
    }

    public void checkNodesVisible(String[] children, boolean shouldExist) {
        checkElements(ImmutableList.copyOf(children),
                childName -> GeneralUIUtils.isWebElementExistByTestId(Constants.DrawingBoard.NODE_PREFIX + childName) ? "exists" : "absent",
                shouldExist ? "exists" : "absent", "visibility");
    }

    public void checkNodesHighlighted(String[] children) {
        checkElements(ImmutableList.copyOf(children),
                childName -> {
                    final WebElement webElement = Get.byTestId(Constants.DrawingBoard.NODE_PREFIX + childName);
                    final String color = webElement.getCssValue("color");
                    return color;
                },
                HIGHLIGHTED_COLOR, "highlightning");
    }

    public void checkNodesVisibleAndMatchIsHighlighted(String searchString, String... children) {
        checkElements(ImmutableList.copyOf(children),
                childName -> {
                    final WebElement webElement = Get.byTestId(Constants.DrawingBoard.NODE_PREFIX + childName);
                    String visible = webElement.isDisplayed() ? "visible" : "hidden";
                    String highlightedText;
                    String bgColor;
                    try {
                        final WebElement highlighted = webElement.findElement(By.cssSelector(".highlight"));
                        highlightedText = highlighted.getText();
                        bgColor = highlighted.getCssValue("background-color");
                    } catch (NoSuchElementException e) {
                        highlightedText = "";
                        bgColor = "none";
                    }
                    return String.join("", visible, " and '", highlightedText, "' in ", bgColor);
                },
                "visible and '" + searchString + "' in rgb(157, 217, 239)", "match highlightning");
    }

    private void checkElements(Collection<String> elements, Function<String, String> predicate, String expected, final String description) {
        final Map<String, String> expectedMap = elements.stream().collect(Collectors.toMap(
                childName -> childName,
                child -> expected
        ));
        final Map<String, String> actual = elements.stream().collect(Collectors.toMap(
                childName -> childName,
                predicate
        ));

        assertThat("There was an error in " + description + " of elements", actual, equalTo(expectedMap));
    }

    public void navigateToServicePlanningPage() {
        navigateTo("/vid/app/ui/#/servicePlanning");
    }

    public void navigateToEmptyServicePlanningPage() {
        navigateTo("/vid/app/ui/#/servicePlanningEmpty");
    }

    public void checkContextMenu(String node){
        String contextMenuButton = Constants.DrawingBoard.NODE_PREFIX + node + Constants.DrawingBoard.CONTEXT_MENU_BUTTON;
        final String contextMenu = Constants.DrawingBoard.CONTEXT_MENU_EDIT;

        checkThatPseudoElementNotExist(contextMenuButton);
        checkThatContextMenuNotExist(contextMenu);

        GeneralUIUtils.hoverOnAreaByTestId(Constants.DrawingBoard.NODE_PREFIX + node);
        checkThatPseudoElementExist(contextMenuButton);
        Click.byTestId(contextMenuButton);

        checkThatContextMenuExist(contextMenu);
    }

    public void checkSearch(){
        String searchElement = Constants.DrawingBoard.SEARCH_LEFT_TREE;//  TODO - should add that it is on the left tree and should create the id of the search element???
        Assert.assertTrue(GeneralUIUtils.isWebElementExistByTestId(searchElement), "search " + searchElement + " should exist");
    }

    public void showTooltipByHoverAboveAlertIcon(String element){
        assertThat("tooltip should not appear before click",
                GeneralUIUtils.getDriver().findElements(By.xpath("//*[contains(@class, '" + "tooltip-inner" + "')]")),
                is(empty())
        );

        GeneralUIUtils.hoverOnAreaByTestId(Constants.DrawingBoard.NODE_PREFIX + element + Constants.DrawingBoard.ALERT_ICON);

        final WebElement webElement = GeneralUIUtils.getWebElementByContainsClassName("tooltip-inner");
        assertThat(webElement.getText(), is("Missing required information. Please open and fill in the details."));
    }

    private void clickDeployButton(){
        GeneralUIUtils.ultimateWait();

        try {
            GeneralUIUtils.clickOnElementByTestId(DEPLOY_BUTTON);
        } catch (org.openqa.selenium.WebDriverException e) {
            // "deploy" replaces the iframe, so "TypeError: can't access dead object" exception is eventually thrown
            if (!e.getMessage().startsWith("TypeError: can't access dead object")) {
                throw e;
            }
        }

    }

    public void deploy() {
        try {
            logger.info("Redux state before deploy:");
            logger.info(getReduxState());
        }
        catch (Exception e) {
            //do nothing just logging
        }
        clickDeployButton();

        VidBasePage.goOutFromIframe();
        GeneralUIUtils.ultimateWait();
        VidBasePage.goToIframe();
        GeneralUIUtils.ultimateWait();
    }

    public void checkDeployButtonDisabled(){
        Assert.assertFalse(Get.byTestId(DEPLOY_BUTTON).isEnabled(),"Deploy button is enabled and should be disabled");
    }

    public void checkExistsAndEnabled(String dataTestId){
        Assert.assertFalse(GeneralUIUtils.isElementDisabled(dataTestId),"Element " + dataTestId + " should exist and be enabled");
    }

    public void checkServiceInstanceName(String expectedServiceName){       
        Assert.assertEquals(SERVICE_INSTANCE_VALUE, Get.byTestId(SERVICE_INSTANCE_TEST_ID).getText());
        Assert.assertEquals(Get.byTestId(SERVICE_NAME).getText(),expectedServiceName);
    }

    public void checkServiceStatus() {
        Assert.assertEquals(Get.byTestId(SERVICE_STATUS).getText(),STATUS_TEXT);
    }

    public void checkQuantityNumberIsCorrect(int expectedQuantity) {
        Assert.assertEquals(Get.byTestId(QUANTITY_LABEL_TEST_ID).getText(), (String.valueOf(QUANTITY_LABEL_VALUE)));
        Assert.assertEquals(Get.byTestId(SERVICE_QUANTITY).getText(), (String.valueOf(expectedQuantity)));
    }

    public static class  ServiceStatusChecker implements Predicate<Boolean> {
        private String uniqueText;
        private Set<String> expectedStatuses;
        private Set<String> columnClassesSet;

        public ServiceStatusChecker(String uniqueText, Set<String> expectedStatuses) {
            this.uniqueText = uniqueText;
            this.expectedStatuses = expectedStatuses;
        }

        @Override
        public boolean test(Boolean noMeaning) {
            InstantiationStatusPage.clickRefreshButton();
            final WebElement row = InstantiationStatusPage.getInstantiationStatusRow(uniqueText);
            if (row == null) {
                System.err.println("**********************" + uniqueText + "************************************************");
                columnClassesSet = Collections.singleton(uniqueText + " NOT FOUND");
                return false; // treat missing row as if test condition not fulfilled
            } else {
                columnClassesSet = new HashSet<>(Arrays.asList(
                        row.findElements(By.xpath(".//*[@id='" + "jobStatus" + "']")).get(0).getAttribute("class").split(" ")));
                return !(Sets.intersection(expectedStatuses, columnClassesSet).isEmpty());
            }
        }

        public Set<String> getColumnClassesSet() {
            return columnClassesSet;
        }
    }

    public void verifyServiceCompletedOnTime(String uniqueStatusText, String nameToDisplay) {
        DrawingBoardPage.ServiceStatusChecker serviceStatusChecker =
            new DrawingBoardPage.ServiceStatusChecker(uniqueStatusText, Collections.singleton(NewServiceInstanceTest.COMPLETED));
        boolean statusIsShown = Wait.waitFor(serviceStatusChecker, null, 20, 2);
        assertTrue(nameToDisplay + " wasn't completed in time", statusIsShown);
        VidBasePage.goOutFromIframe();
    }

}
