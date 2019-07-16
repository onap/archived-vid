package org.onap.vid.mso.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
//tenantId and might be null for supporting create configuration API (port mirroring)
//cloudOwner might because MSO enable it and it might be used in some flows (default value in MSO "irma-aic")
data class CloudConfiguration @JvmOverloads constructor(
        var lcpCloudRegionId: String? = null,
        var tenantId:String? = null,
        var cloudOwner: String? = null
)