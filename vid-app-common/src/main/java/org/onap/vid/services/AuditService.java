package org.onap.vid.services;

import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.RestObject;

import java.util.UUID;

public interface AuditService {

    void setFailedAuditStatusFromMso(UUID jobUuid, String requestId, int statusCode, String msoResponse);
}
