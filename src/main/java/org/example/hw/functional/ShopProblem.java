package org.example.hw.functional;

import java.util.List;

public class ShopProblem {

    public CoffeeShop findCoffeeNearMe(Location you, List<CoffeeShop> shops) {
       return null;
    }

    interface Location {
        long distanceBetween(Location other);
    }

    interface CoffeeShop {
        Location location();
    }

}
