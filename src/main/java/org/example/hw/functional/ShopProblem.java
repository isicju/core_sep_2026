package org.example.hw.functional;

import java.util.List;

public class ShopProblem {

    public CoffeeShop findCoffeeNearMe(Location you, List<CoffeeShop> shops) {
        if (you == null || shops == null || shops.isEmpty()) {
            throw new IllegalArgumentException("some empty data!");
        }
        return
                shops.stream()
                        .min((o1, o2) -> {
                            long distanceO1 = you.distanceBetween(o1.location());
                            long distanceO2 = you.distanceBetween(o2.location());

                            return Long.compare(distanceO1, distanceO2);
                        }).orElse(null);

    }

    interface Location {
        long distanceBetween(Location other);
    }

    interface CoffeeShop {
        Location location();
    }

}
