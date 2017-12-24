package org.openecomp.vid.mso.rest.OperationalEnvironment;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class RelatedInstanceList {
    private List<RelatedInstance> relatedInstance;

    public RelatedInstanceList() {
        relatedInstance = new ArrayList<RelatedInstance>() ;
    }

    @JsonProperty("relatedInstanceList")
    public List<RelatedInstance> getRelatedInstance() {
        return relatedInstance;
    }

    public void setRelatedInstance(List<RelatedInstance> relatedInstance) {
        this.relatedInstance = relatedInstance;
    }

    public void addItem(RelatedInstance relatedInstanceItem){
        relatedInstance.add(relatedInstanceItem);
    }
}
