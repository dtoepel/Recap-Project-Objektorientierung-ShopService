import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderListRepoTest {

    @Test
    void getOrders() {
        //GIVEN
        OrderListRepo repo = new OrderListRepo();
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
        OrderListRepo repo = new OrderListRepo();
        Instant now = Instant.now();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(new OrderItem<>(product, 1.5)), OrderStatus.COMPLETED, now);
        repo.addOrder(newOrder);

        //WHEN
        Order actual = repo.getOrderById("1");

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(new OrderItem<>(product1, 1.5)), OrderStatus.COMPLETED, now);

        assertEquals(actual, expected);
    }

    @Test
    void addOrder() {
        //GIVEN
        OrderListRepo repo = new OrderListRepo();
        Product product = new Product("1", "Apfel");
        Instant now = Instant.now();
        Order newOrder = new Order("1", List.of(new OrderItem<>(product, 1.5)), OrderStatus.IN_DELIVERY, now);

        //WHEN
        Order actual = repo.addOrder(newOrder);

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(new OrderItem<>(product1, 1.5)), OrderStatus.IN_DELIVERY, now);
        assertEquals(actual, expected);
        assertEquals(repo.getOrderById("1"), expected);
    }

    @Test
    void removeOrder() {
        //GIVEN
        OrderListRepo repo = new OrderListRepo();

        //WHEN
        repo.removeOrder("1");

        //THEN
        assertNull(repo.getOrderById("1"));
    }
}
