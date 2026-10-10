package factory;

import strategy.*;

public class ShippingStrategyFactory {

    public static ShippingStrategy createStrategy(ShippingType type) {
        if (type == null) {
            return new ShippingStrategyNullObject();
        }

        return switch (type) {
            case STANDARD -> new StandardShipping();
            case EXPRESS -> new ExpressShipping();
            case INTERNATIONAL -> new InternationalShipping();
            case NEXT_DAY -> new NextDayShipping();
        };
    }
}
