import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderMapRepo implements OrderRepo{
    private final Map<String, Order> orders = new HashMap<>();

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order getOrderById(String id) {
        return orders.get(id);
    }

    @Override
    public Order addOrder(Order newOrder) {
        orders.put(newOrder.id(), newOrder);
        return newOrder;
    }

    @Override
    public void removeOrder(String id) {
        orders.remove(id);
    }

    @Override
    public Map<OrderStatus, Order> getOldestOrderPerStatus() {
        HashMap<OrderStatus, Order> map = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            List<Order> sortedAndFilteredList =
                    getOrders().stream()
                    .filter(o -> o.status()==status)
                    .sorted((o1, o2) -> o1.creationTime().compareTo(o2.creationTime()))
                    .toList();
            if(sortedAndFilteredList.isEmpty()) {
                map.put(status, null);
            } else {
                map.put(status, sortedAndFilteredList.get(0));
            }
        }
        return map;
    }
}
