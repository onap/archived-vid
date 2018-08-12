package vid.automation.test.services;

import com.google.common.primitives.Ints;
import vid.automation.test.model.CategoryOption;
import vid.automation.test.model.CategoryOptionList;
import vid.automation.test.utils.DB_CONFIG;
import vid.automation.test.utils.ReadFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by itzikliderman on 08/09/2017.
 */
public class CategoryParamsService {
    private List<CategoryOption> categoryParams;

    public CategoryParamsService() {
        categoryParams = getCategoryParamsFromJson();
        categoryParams.forEach(this::prepareCategoryParam);
    }

    List<CategoryOption> getCategoryParamsFromJson() {
        CategoryOptionList categoryParamsObject = null;
        categoryParamsObject = ReadFile.getJsonFile("categoryParams", CategoryOptionList.class);
        return categoryParamsObject.categories;
    }

    private void prepareCategoryParam(CategoryOption categoryParam) {
        /*
        Creates a category parameter option in the DB.
         */

        dropCategoryParam(categoryParam);

        System.out.println("Preparing category parameter '" + categoryParam.categoryId + "': " + categoryParam.name);
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {

            System.out.println("Database connected!");

            ///////////////////////////////
            // Add category param option
            Statement stmt = connection.createStatement();

            int id = getId(categoryParam.name);
            stmt.addBatch("INSERT INTO `vid_category_parameter_option` (`CATEGORY_OPT_DB_ID`, `CATEGORY_OPT_APP_ID`, `NAME`, `CATEGORY_ID`) " +
                    "VALUES (" + id + ", '" + (categoryParam.appId != null ? categoryParam.appId : categoryParam.name) + "', '" + categoryParam.name + "', '" + categoryParam.categoryId + "') " +
                    "ON DUPLICATE KEY UPDATE NAME='"+categoryParam.name+"'");

            int[] executeBatch = stmt.executeBatch();
            assertThat(Ints.asList(executeBatch), everyItem(greaterThan(0)));

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    private void dropCategoryParam(CategoryOption categoryParam) {
        System.out.println("Dropping categoryParam '" + categoryParam.name + "'");
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {

            System.out.println("Database connected!");

            Statement stmt = connection.createStatement();
            int id = getId(categoryParam.name);
            stmt.addBatch("DELETE FROM `vid_category_parameter_option` WHERE `CATEGORY_OPT_DB_ID` = '" + id + "'");
            int[] executeBatch = stmt.executeBatch();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    private int getId(String id) {
        return (Math.abs(id.hashCode()) % 100000) * 1000;
    }

}
