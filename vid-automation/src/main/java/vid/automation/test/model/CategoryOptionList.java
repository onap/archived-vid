package vid.automation.test.model;

import com.google.common.base.MoreObjects;

import java.util.List;

public class CategoryOptionList {
    public List<CategoryOption> categories;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("categories", categories)
                .toString();
    }
}
