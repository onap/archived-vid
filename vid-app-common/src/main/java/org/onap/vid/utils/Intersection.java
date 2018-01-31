package org.onap.vid.utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by moriya1 on 10/10/2017.
 */
public class Intersection<T> {
    public List<T> intersectMultipileArray(List<List<T>> lists) {
        if (lists.size() == 1) {
            return lists.get(0);
        } else {
            List<T> intersectResult = intersectTwoArrays(lists.get(0),lists.get(1));

            lists.remove(0);
            lists.remove(0);
            lists.add(0,intersectResult);
            return intersectMultipileArray(lists);
        }

    }

    public List<T> intersectTwoArrays(List<T> list1, List<T> list2) {

        List<T> intersect = list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
        return intersect;
    }
}
