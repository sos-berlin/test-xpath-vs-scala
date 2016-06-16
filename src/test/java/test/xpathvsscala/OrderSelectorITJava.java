package test.xpathvsscala;

import java.util.Arrays;
import test.xpathvsscala.testdata.Data;
import static test.xpathvsscala.order.Orders.Folder;
import static test.xpathvsscala.order.Orders.Order;
import static test.xpathvsscala.order.OrdersJava.javaSelectOrders;

/**
 * @author Joacim Zschimmer
 */
class OrderSelectorITJava {

    private OrderSelectorITJava() {}

    static void testFilterSuspended(Folder folder) {
        Order[] suspendedOrders = javaSelectOrders(folder).filter(Order::isSuspended).toArray(Order[]::new);
        if (!Arrays.equals(suspendedOrders, Data.suspendedOrdersArray())) throw new RuntimeException("NOT EQUAL");
    }
}
