package test.xpathvsscala.order;

import java.util.stream.Stream;
import static java.util.Collections.singleton;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;
import static scala.collection.JavaConversions.asJavaCollection;
import static test.xpathvsscala.order.Orders.Folder;
import static test.xpathvsscala.order.Orders.Order;

/**
 * @author Joacim Zschimmer
 */
public class OrdersJava {
    private OrdersJava() {}

    public static Stream<Order> javaSelectOrders(Folder root) {
        return flattenFolders(root)
                .flatMap(folder -> toStream(folder.jobchains()))
                .flatMap(jobchain -> toStream(jobchain.nodes()))
                .flatMap(node -> toStream(node.orderQueue().orders()));
    }

    public static Stream<Folder> flattenFolders(Folder folder) {
        return concat(
                toStream(singleton(folder)),
                toStream(folder.subfolders()).flatMap(OrdersJava::flattenFolders));
    }

    public static <A> Stream<A> toStream(scala.collection.Iterable<A> iterable) {
        return toStream(asJavaCollection(iterable));
    }

    public static <A> Stream<A> toStream(Iterable<A> iterable) {
        return stream(iterable.spliterator(), false);
    }
}
