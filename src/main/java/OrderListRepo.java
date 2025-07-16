import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListRepo implements OrderRepo{
    private final List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public Order getOrderById(String id) {
        for (Order order : orders) {
            if (order.id().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public Order addOrder(Order newOrder) {
        orders.add(newOrder);
        return newOrder;
    }

    public void removeOrder(String id) {
        for (Order order : orders) {
            if (order.id().equals(id)) {
                orders.remove(order);
                return;
            }
        }
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
