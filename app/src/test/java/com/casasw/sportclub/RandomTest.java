package com.casasw.sportclub;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Junior on 05/04/2017.
 * To test random method
 */
public class RandomTest {

    @Test
    public void randomInt() throws Exception {
        int index;
        for (int i=0;i<1000;i++) {
            index = (int) (Math.random()*7);
            assertTrue("Error: random is > 7", index < 7);

        }

    }
}
