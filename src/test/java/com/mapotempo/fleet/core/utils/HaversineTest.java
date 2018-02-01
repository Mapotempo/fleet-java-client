package com.mapotempo.fleet.core.utils;

import com.mapotempo.fleet.utils.Haversine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HaversineTest {
    @DisplayName("Only one save action should be call during the laps time * 3")
    @Test
    void test() throws InterruptedException {
        Assertions.assertEquals(14973.209283269452, Haversine.distance(47.6788206, -122.3271205,
                47.6788206, -122.5271205));
        Assertions.assertEquals(0., Haversine.distance(47., -122.,
                47., -122.));
    }
}
