import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapRepoTest {

    @Test
    void getOrders() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();
        Instant now = Instant.now();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(new OrderItem<>(product, 1.5)), OrderStatus.PROCESSING, now);
        repo.addOrder(newOrder);

        //WHEN
        List<Order> actual = repo.getOrders();

        //THEN
        List<Order> expected = new ArrayList<>();
        Product product1 = new Product("1", "Apfel");
        expected.add(new Order("1", List.of(new OrderItem<>(product1, 1.5)), OrderStatus.PROCESSING, now));

        assertEquals(actual, expected);
    }

    @Test
    void getOrderById() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();
        Instant now = Instant.now();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(new OrderItem<>(product, 1.5)), OrderStatus.IN_DELIVERY, now);
        repo.addOrder(newOrder);

        //WHEN
        Order actual = repo.getOrderById("1");

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(new OrderItem<>(product1, 1.5)), OrderStatus.IN_DELIVERY, now);

        assertEquals(actual, expected);
    }

    @Test
    void addOrder() {
        //GIVEN
        Instant now = Instant.now();
        OrderMapRepo repo = new OrderMapRepo();
        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(new OrderItem<>(product, 1.5)), OrderStatus.COMPLETED, now);

        //WHEN
        Order actual = repo.addOrder(newOrder);

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(new OrderItem<>(product1, 1.5)), OrderStatus.COMPLETED, now);
        assertEquals(actual, expected);
        assertEquals(repo.getOrderById("1"), expected);
    }

    @Test
    void removeOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        //WHEN
        repo.removeOrder("1");

        //THEN
        assertNull(repo.getOrderById("1"));
    }

    @Test
    void getOldestOrders() {
        OrderMapRepo repo = new OrderMapRepo();
        repo.addOrder(new Order("1", List.of(new OrderItem<Product>(new Product("1", "Apfel"),1.1)), OrderStatus.COMPLETED, Instant.now()));
        try{Thread.sleep(100);} catch (Exception e) {}
        repo.addOrder(new Order("2", List.of(new OrderItem<Product>(new Product("1", "Apfel"),1.1)), OrderStatus.IN_DELIVERY, Instant.now()));
        try{Thread.sleep(100);} catch (Exception e) {}
        repo.addOrder(new Order("3", List.of(new OrderItem<Product>(new Product("1", "Apfel"),1.1)), OrderStatus.COMPLETED, Instant.now()));
        try{Thread.sleep(100);} catch (Exception e) {}
        repo.addOrder(new Order("4", List.of(new OrderItem<Product>(new Product("1", "Apfel"),1.1)), OrderStatus.COMPLETED, Instant.now()));
        try{Thread.sleep(100);} catch (Exception e) {}
        repo.addOrder(new Order("5", List.of(new OrderItem<Product>(new Product("1", "Apfel"),1.1)), OrderStatus.COMPLETED, Instant.now()));
        try{Thread.sleep(100);} catch (Exception e) {}
        repo.addOrder(new Order("6", List.of(new OrderItem<Product>(new Product("1", "Apfel"),1.1)), OrderStatus.COMPLETED, Instant.now()));

        Map<OrderStatus, Order> map = repo.getOldestOrderPerStatus();

        assertEquals("1", map.get(OrderStatus.COMPLETED).id());
        assertEquals("2", map.get(OrderStatus.IN_DELIVERY).id());
        assertNull(map.get(OrderStatus.PROCESSING));
    }
}
