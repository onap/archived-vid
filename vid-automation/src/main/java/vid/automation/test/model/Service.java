package vid.automation.test.model;

import com.google.common.base.MoreObjects;

/**
 * Created by itzikliderman on 19/06/2017.
 */
public class Service {

    public String type;

    public String uuid;

    public String invariantUuid;

    public String name;

    public String version;

    public String category;

    public String description;

    public String serviceRole;

    public Service() {}

    public Service(String type, String uuid, String invariantUuid, String name, String version, String category, String description, String serviceRole) {
        this.type = type;
        this.uuid = uuid;
        this.invariantUuid = invariantUuid;
        this.name = name;
        this.version = version;
        this.category = category;
        this.description = description;
        this.serviceRole = serviceRole;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("uuid", uuid)
                .add("invariantUuid", invariantUuid)
                .add("name", name)
                .add("version", version)
                .add("category", category)
                .add("description", description)
                .add("serviceRole", serviceRole)
                .toString();
    }
}
