package vid.automation.test.utils;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class ExtendedHamcrestMatcher {


    //this method return matcher for has items that support collection as input (Instead of ...)
    public static <T> Matcher<Iterable<T>> hasItemsFromCollection(Collection<T> items) {
        List<Matcher<? super Iterable<T>>> all = new ArrayList<>(items.size());
        for (T element : items) {
            all.add(hasItem(element));
        }

        return allOf(all);
    }
}
