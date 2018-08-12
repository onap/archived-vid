package vid.automation.test.model;

import com.google.common.base.MoreObjects;

public class CategoryOption {
    public String name;
    public String appId;
    public String categoryId;


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("appId", appId)
                .add("categoryId", categoryId)
                .toString();
    }
}
