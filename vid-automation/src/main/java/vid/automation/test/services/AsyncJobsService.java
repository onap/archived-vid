package vid.automation.test.services;

import vid.automation.test.utils.DB_CONFIG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AsyncJobsService {

    public void dropAllAsyncJobs() {
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            System.out.println("Database connected!");

            Statement stmt = connection.createStatement();
            stmt.addBatch("DELETE FROM `vid_service_info`");
            stmt.addBatch("DELETE FROM `vid_job`");
            stmt.addBatch("DELETE FROM `vid_job_audit_status`");
            int[] executeBatch = stmt.executeBatch();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void muteAllAsyncJobs() {
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            System.out.println("Database connected!");

            Statement stmt = connection.createStatement();
            stmt.addBatch("UPDATE `vid_job` set `TAKEN_BY`='muteAllAsyncJobs', `AGE`=`AGE`+5");
            int[] executeBatch = stmt.executeBatch();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

}
