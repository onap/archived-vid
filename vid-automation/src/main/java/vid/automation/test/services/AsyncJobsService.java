package vid.automation.test.services;

import com.google.common.collect.ImmutableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import vid.automation.test.utils.DB_CONFIG;

public class AsyncJobsService {

    private static final String MUTE_JOB_STATEMENT =
        "UPDATE `vid_job` SET `TAKEN_BY`='muteAllAsyncJobs', `JOB_STATUS`=CONCAT('MUTED_', `JOB_STATUS`), `AGE`=`AGE`+5 ";

    public void dropAllAsyncJobs() {
        runStatementsInDb(ImmutableList.of(
                "DELETE FROM `vid_service_info`",
                "DELETE FROM `vid_job`",
                "DELETE FROM `vid_job_audit_status`",
                "DELETE FROM `vid_name_counter`"
                ));
    }

    public void runStatementsInDb(List<String> sqlStatements) {
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            System.out.println("Database connected!");

            try (Statement stmt = connection.createStatement()) {
                for (String sql: sqlStatements) {
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void runStatementInDb(String sqlStatement) {
        runStatementsInDb(ImmutableList.of(sqlStatement));
    }


    public void muteAllAsyncJobs() {
        runStatementInDb(MUTE_JOB_STATEMENT + "WHERE `TAKEN_BY` is NULL or `TAKEN_BY`<>'muteAllAsyncJobs'");
    }

    public void muteAsyncJobById(String uuid) {
        runStatementInDb(MUTE_JOB_STATEMENT + String.format("WHERE JOB_ID='%s'",uuid));
    }

    public void dropAllFromNameCounter() {
        runStatementInDb("DELETE FROM `vid_name_counter`");
    }

}
