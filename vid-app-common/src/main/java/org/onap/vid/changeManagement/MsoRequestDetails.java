package org.onap.vid.changeManagement;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oren on 9/5/17.
 */
public class MsoRequestDetails {

    public ModelInfo modelInfo;

    public LeanCloudConfiguration cloudConfiguration;

    public UpdateRequestInfo requestInfo;

    public List<RelatedInstanceList> relatedInstanceList;

    public Object requestParameters;

    public MsoRequestDetails(RequestDetails r) {
        this.modelInfo = new ModelInfo(r.getModelInfo());
        this.cloudConfiguration = new LeanCloudConfiguration(r.getCloudConfiguration());
        this.requestInfo = new UpdateRequestInfo(r.getRequestInfo());
        this.relatedInstanceList = new ArrayList<>();
        this.requestParameters = r.getRequestParameters();
        relatedInstanceList = r.getRelatedInstList();


    }
}
