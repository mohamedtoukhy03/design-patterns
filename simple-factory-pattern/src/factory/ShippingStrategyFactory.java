package factory;

import strategy.*;

/**
 * Simple Factory responsible for creating concrete ShippingStrategy instances.
 */
public class ShippingStrategyFactory {

    public static ShippingStrategy createStrategy(ShippingType type) {
        if (type == null) {
            return new StandardShipping();
        }

        return switch (type) {
            case STANDARD -> new StandardShipping();
            case EXPRESS -> new ExpressShipping();
            case INTERNATIONAL -> new InternationalShipping();
            case NEXT_DAY -> new NextDayShipping();
        };
    }
}
