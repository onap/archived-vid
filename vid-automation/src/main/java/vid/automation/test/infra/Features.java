package vid.automation.test.infra;

import org.togglz.core.Feature;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {

    CREATE_INSTANCE_TEST,
    EMPTY_DRAWING_BOARD_TEST,
    FLAG_REGION_ID_FROM_REMOTE,
    FLAG_ASYNC_JOBS,
    FLAG_ADD_MSO_TESTAPI_FIELD,
    FLAG_ASYNC_INSTANTIATION,
    FLAG_UNASSIGN_SERVICE,
    FLAG_SERVICE_MODEL_CACHE,
    FLAG_NETWORK_TO_ASYNC_INSTANTIATION,
    FLAG_COLLECTION_RESOURCE_SUPPORT,
    FLAG_SHOW_ASSIGNMENTS,
    FLAG_SHOW_VERIFY_SERVICE,
    FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
