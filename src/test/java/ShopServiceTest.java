import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void updateOderTest() {

        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        Order oldOrder = shopService.addOrder(productsIds);

        Order newOrder = shopService.updateOrder(oldOrder, OrderStatus.IN_DELIVERY);

        assertNotEquals(oldOrder, newOrder);
        assertEquals(oldOrder.id(), newOrder.id());
        assertEquals(oldOrder.products(), newOrder.products());
        assertNotEquals(oldOrder.status(), newOrder.status());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN+THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }
}
