import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), IdService.DEFAULT_ID_SERVICE);
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());

        Instant now = Instant.now();
        Instant orderTime = expected.creationTime();
        assertFalse(now.isBefore(orderTime));
        assertTrue(now.getEpochSecond() - orderTime.getEpochSecond() < 10);
    }

    @Test
    void updateOderTest() {

        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), IdService.DEFAULT_ID_SERVICE);
        List<String> productsIds = List.of("1");
        Order oldOrder = shopService.addOrder(productsIds);

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
        List<String> productsIds = List.of("1", "2");

        //WHEN+THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }
}
