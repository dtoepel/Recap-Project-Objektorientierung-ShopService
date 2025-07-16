import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ProductRepo repo = new ProductRepo();
        repo.addProduct(new Product("1", "Apfel"), 3.);
        ShopService shopService = new ShopService(repo, new OrderMapRepo(), IdService.DEFAULT_ID_SERVICE);
        List<OrderItem<String>> productsIds = List.of(new OrderItem<>("1", 1.));

        //WHEN
        Order actual = null;
        try{
            actual = shopService.addOrder(productsIds);
        } catch (ProductNotFoundException e){fail();}

        //THEN
        Order expected = new Order("-1", List.of(
                new OrderItem<>(new Product("1", "Apfel"), 1.)),
                OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());

        Instant now = Instant.now();
        Instant orderTime = expected.creationTime();
        assertFalse(now.isBefore(orderTime));
        assertTrue(now.getEpochSecond() - orderTime.getEpochSecond() < 10);
    }

    @Test
    void updateOrderTest() {
        ProductRepo repo = new ProductRepo();
        repo.addProduct(new Product("1", "Apfel"), 3.);

        ShopService shopService = new ShopService(repo, new OrderMapRepo(), IdService.DEFAULT_ID_SERVICE);

        List<OrderItem<String>> productsIds = List.of(new OrderItem<>("1", 1.));
        Order oldOrder = null;
        try{
            oldOrder = shopService.addOrder(productsIds);
        } catch (ProductNotFoundException e){fail();}

        Order newOrder = shopService.updateOrder(oldOrder, OrderStatus.IN_DELIVERY);

        assertNotEquals(oldOrder, newOrder);
        assertEquals(oldOrder.id(), newOrder.id());
        assertEquals(oldOrder.products(), newOrder.products());
        assertEquals(oldOrder.creationTime(), newOrder.creationTime());
        assertNotEquals(oldOrder.status(), newOrder.status());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), IdService.DEFAULT_ID_SERVICE);

        List<OrderItem<String>> productsIds = List.of(
                new OrderItem<>("1", 1.),
                new OrderItem<>("2", 1.));

        //WHEN+THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }
}
