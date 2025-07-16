import java.util.List;
import java.util.Map;

public interface OrderRepo {

    List<Order> getOrders();

    Order getOrderById(String id);

    Order addOrder(Order newOrder);

    void removeOrder(String id);

    public Map<OrderStatus, Order> getOldestOrderPerStatus();
}
