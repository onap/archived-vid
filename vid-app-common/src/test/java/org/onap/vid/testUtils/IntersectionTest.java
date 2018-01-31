package org.onap.vid.testUtils;

import org.junit.Assert;
import org.junit.Test;
import org.onap.vid.utils.Intersection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moriya1 on 10/10/2017.
 */
public class IntersectionTest {

    @Test
    public void testFourArrays(){
        List<String> l1 = new ArrayList<String>();
        l1.add("1");
        l1.add("2");

        List<String> l2 = new ArrayList<String>();
        l2.add("2");
        l2.add("3");

        List<String> l3 = new ArrayList<String>();
        l3.add("2");
        l3.add("4");

        List<String> l4 = new ArrayList<String>();
        l4.add("2");
        l4.add("5");

        List<List<String>> all = new ArrayList<>();
        all.add(l1);
        all.add(l2);
        all.add(l3);
        all.add(l4);
        Intersection<String> m = new Intersection<>();
        List<String> ans = m.intersectMultipileArray(all);
        Assert.assertEquals(1,ans.size());
        Assert.assertEquals(ans.get(0),"2");

    }



    @Test
    public void testTwoArrays(){
        List<String> l1 = new ArrayList<String>();
        l1.add("1");
        l1.add("2");

        List<String> l2 = new ArrayList<String>();
        l2.add("2");
        l2.add("3");

        List<List<String>> all = new ArrayList<>();
        all.add(l1);
        all.add(l2);
        Intersection<String> m = new Intersection<>();
        List<String> l3 = m.intersectMultipileArray(all);
        Assert.assertEquals(l3.size(),1);
        Assert.assertEquals(l3.get(0),"2");

    }


    @Test
    public void testNoIntersection(){
        List<String> l1 = new ArrayList<String>();
        l1.add("1");
        l1.add("2");

        List<String> l2 = new ArrayList<String>();
        l2.add("3");
        l2.add("4");

        List<List<String>> all = new ArrayList<>();
        all.add(l1);
        all.add(l2);
        Intersection<String> m = new Intersection<>();
        List<String> l3 = m.intersectMultipileArray(all);
        Assert.assertEquals(l3.size(),0);

    }

    @Test
    public void testOneArrays(){
        List<String> l1 = new ArrayList<String>();
        l1.add("1");
        l1.add("2");
        List<List<String>> all = new ArrayList<>();
        all.add(l1);
        Intersection<String> m = new Intersection<>();
        List<String> l3 = m.intersectMultipileArray(all);
        Assert.assertEquals(l3.size(),2);
        Assert.assertEquals(l3.get(0),"1");
        Assert.assertEquals(l3.get(1),"2");

    }
}
