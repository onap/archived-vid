package vid.automation.test.model;

import java.util.List;

public class JobBulk {

    private List<JobModel> jobs;

    public JobBulk() {
    }

    public JobBulk(List<JobModel> jobs) {
        this.jobs = jobs;
    }

    public List<JobModel> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobModel> jobs) {
        this.jobs = jobs;
    }
}
