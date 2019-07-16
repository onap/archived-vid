package org.onap.vid.mso.model;

import java.util.List;

/*
Based on this model:
{
  "requestDetails": {
    "requestInfo": {
      "source": "VID",
      "requestorId": "az2016"
    },
    "relatedInstanceList": [
      {
        "relatedInstance": {
          "instanceId": "{the to be removed/added as member vnf-id}",
          "modelInfo": {
            "modelType": "vnf"
          }
        }
      }
    ]
  }
}
*/

public class AddOrRemoveInstanceGroupMemberRequestDetails extends BaseResourceInstantiationRequestDetails {
    public AddOrRemoveInstanceGroupMemberRequestDetails(RequestInfo requestInfo, List<RelatedInstance> relatedInstanceList) {
        super(null, null, requestInfo, null, null, relatedInstanceList, null);
    }
}
