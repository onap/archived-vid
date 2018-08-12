package org.opencomp.vid.api;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.codestory.http.WebServer;
import org.opencomp.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.opencomp.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.opencomp.vid.more.LoggerFormatTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.JobBulk;
import vid.automation.test.model.JobModel;
import vid.automation.test.services.SimulatorApi;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;

public class AsyncInfraApiTest extends BaseApiTest {

    private static final String PENDING = "PENDING";
    public static final String API_URL = "asyncForTests";

    private boolean asyncJobsIsOn() {
        return Features.FLAG_ASYNC_JOBS.isActive()
                && Features.FLAG_ASYNC_INSTANTIATION.isActive();
    }

    @BeforeClass
    public void login() {
        super.login();
    }

    @BeforeMethod
    protected void deleteAllPendingJobs() {
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            System.out.println("Database connected!");
            try (Statement stmt = connection.createStatement()) {
                stmt.addBatch("DELETE from `vid_service_info`");
                stmt.addBatch("DELETE FROM `vid_job`");

                int[] executeBatch = stmt.executeBatch();
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }


    @Test
    public void createBulkOfJobsViaApi_thenGetEachOneOfThem() {
        int jobCount = 10;
        final String url = "http://localhost:1234/testMe";
        ResponseEntity<JobBulk> result = postJobBulk(jobCount, url, "NoOp");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        Assert.assertNotNull(result.getBody());
        assertEquals(jobCount, getJobChildren(result).size());


        result.getBody().getJobs().forEach(job -> {
            assertThat(job.getUuid(), not(isEmptyOrNullString()));
            assertEquals(PENDING, job.getStatus());
        });

        result.getBody().getJobs().forEach(job -> {
            ResponseEntity<JobModel> jobResult = restTemplate.getForEntity(buildUri(API_URL+"/job/{uuid}"), JobModel.class, job.getUuid());
            assertEquals(job.getUuid(), jobResult.getBody().getUuid());
            //assertEquals(PENDING, jobResult.getBody().getStatus());
        });
    }

    private List<JobModel> getJobChildren(ResponseEntity<JobBulk> result) {
        return result.getBody().getJobs().stream().filter(job -> job.getTemplateId() != null).collect(Collectors.toList());
    }

    private ResponseEntity<JobBulk> postJobBulk(int jobCount, String url, String type) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "service-"+random.nextInt());
        jsonObject.addProperty("count", jobCount);
        jsonObject.addProperty("url", url);
        jsonObject.addProperty("type", type);
        ResponseEntity<JobBulk> result = restTemplate.postForEntity(buildUri(API_URL), jsonObject.toString(), JobBulk.class);
        return result;
    }

    @Test
    public void addFewJobs_verifyCallbacksAreAllAndNoDuplicates() throws InterruptedException {
        final Collection<String> expected = new ConcurrentSkipListSet<>();
        final Collection<String> results = new ConcurrentSkipListSet<>();
        final Collection<String> duplicates = new ConcurrentSkipListSet<>();
        final String targetPath = "/my-mso";

        // https://github.com/CodeStory/fluent-http
        final WebServer server = new WebServer().configure(routes -> routes
                .post(targetPath, (context) -> {
                    final String body = context.extract(String.class);
                    if (!results.add(body)) {
                        duplicates.add(body);
                    }
                    return body;
                })
        ).startOnRandomPort();

        final int jobCount = 10;
        final int childCount = 2;

        InetAddress inetAddress = getExternalInetAddress();

        final String returnUrl = "http://" + inetAddress.getHostAddress() + ":" + server.port() + targetPath;
        System.out.println(returnUrl);

        // POST jobs
        IntStream.range(0, jobCount).parallel().forEach(i -> {
            ResponseEntity<JobBulk> result = postJobBulk(childCount, returnUrl, "HttpCall");
            getJobChildren(result).forEach(child -> {
                expected.add(child.getUuid());
            });
        });
        Wait.waitFor((actual -> actual.size() == expected.size()), results, 75, 200, TimeUnit.MILLISECONDS);
        // wait some another time give change for duplications
        TimeUnit.SECONDS.sleep(5);

        if (asyncJobsIsOn()) {
            assertThat("async jobs is on: should callback for all jobs - no more, no less", results, equalTo(expected));
            assertThat("async jobs is on: should callback for exactly number of jobs", results, hasSize(jobCount * childCount));
            assertThat("async jobs is on: should have no duplicate jobs callback", duplicates, empty());
        } else {
            assertThat("async jobs is off: should not callback for any job", results, empty());
            assertThat("async jobs is off: should have no duplicate jobs callback", duplicates, empty());
        }

        server.stop();

    }

    private InetAddress getExternalInetAddress() {
        // https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
        InetAddress candidateAddress = null;
        try {
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();

                if (iface.getName().contains("docker")) {
                    // ignore local docker virtual gateway IPs
                    continue;
                }

                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();

                    // take only non-loopback, ipv4 addresses
                    if (!inetAddr.isLoopbackAddress() && inetAddr instanceof Inet4Address) {

                        System.out.println("inetAddr (" + iface.getName() + "): " + inetAddr);
                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            System.out.println("inetAddr, site-local (" + iface.getName() + "): " + inetAddr);
                            candidateAddress = inetAddr;
                        }
                        else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return candidateAddress;
    }

    @Test
    public void testGetStatusBadRequest() {
        ResponseEntity<String> jobResult = getJob("1234");
        assertEquals(HttpStatus.BAD_REQUEST, jobResult.getStatusCode());
    }

    @Test
    public void testGetStatusNotFound() {
        ResponseEntity<String> jobResult = getJob(UUID.randomUUID().toString());
        assertEquals(HttpStatus.NOT_FOUND, jobResult.getStatusCode());
    }

    private ResponseEntity<String> getJob(String uuid) {
        return restTemplateErrorAgnostic.getForEntity(buildUri(API_URL + "/job/{uuid}"), String.class , uuid);
    }

    @Test
    public void testExceptionHandlingOfVidRestrictedBaseController() {
        //get logs require user role that may need simulator presets
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet()), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        ResponseEntity<String> jobResult = restTemplateErrorAgnostic.getForEntity(buildUri(API_URL + "/error"), String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, jobResult.getStatusCode());
        assertThat(jobResult.getBody(), containsString("GenericUncheckedException"));
        assertThat(jobResult.getBody(), containsString("dummy error"));
        String logLines = LoggerFormatTest.getLogLines("error", 15, 0, restTemplate, uri);
        assertThat(logLines, containsString("GenericUncheckedException"));
        assertThat(logLines, containsString("dummy error"));
    }
}
