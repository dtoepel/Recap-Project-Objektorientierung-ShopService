import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;// = new ProductRepo();
    private final OrderRepo orderRepo;// = new OrderMapRepo();
    private final IdService idService;

    // Can be used in two ways:
    // on any Order: returns a new Order
    // on an Order in this Shop: replace the order in the orderRepo, and then return it

    public Order addOrder(List<OrderItem<String>> productIds) throws ProductNotFoundException{
        List<OrderItem<Product>> products = new ArrayList<>();
        for (OrderItem<String> productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId.product());
            if(productToOrder.isEmpty()) {
                throw new ProductNotFoundException("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
            }
            if(productRepo.getProductAmount(productId.product()) < productId.amount()) {
                throw new ProductNotFoundException("Zuwenig Product " + productToOrder.get()
                        + "! im Lager:" + productRepo.getProductAmount(productId.product())
                        + " bestellt: " + productId.amount());
            }
            products.add(new OrderItem<Product>(productToOrder.get(), productId.amount()));
        }

        // if no fail, the reduce stock

        for(OrderItem<Product> productItem : products) {
            productRepo.addProductAmount(productItem.product().id(), -productItem.amount());
        }

        Order newOrder = new Order(idService.generateID().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }
    public Order updateOrder(Order oldOrder, OrderStatus newOrderStatus) {
        Order newOrder = oldOrder.withStatus(newOrderStatus);

        if(orderRepo.getOrders().contains(oldOrder)) {
            orderRepo.removeOrder(oldOrder.id());
            orderRepo.addOrder(newOrder);
        }
        return newOrder;
    }


    public void importFromFile(Stream<String> lines) {

        HashMap<String, String> idMap = new HashMap<>();

        for(String line : lines.toList()) {
            if(line.startsWith("addOrder")) {
                String[] s = line.split(" ");
                Vector<OrderItem<String>> products = new Vector<>();
                for(int i = 2; i < s.length; i+=2) {
                    products.add(new OrderItem<>(s[i], Double.parseDouble(s[i+1])));
                }
                Order o = addOrder(products);
                if(idMap.containsKey(s[1])) throw new RuntimeException("ID "+s[1]+" already in use!");
                idMap.put(s[1], o.id());
            }
            else if(line.startsWith("setStatus")) {
                String[] s = line.split(" ");
                String orderID = idMap.get(s[1]);
                if(orderID == null) throw new RuntimeException("ID "+s[1]+" not in use!");
                Order o = orderRepo.getOrderById(orderID);
                updateOrder(o, OrderStatus.valueOf(s[2]));
            }
            else if(line.startsWith("printOrders")) {
                for(Order o : orderRepo.getOrders()) {
                    System.out.println(o);
                }
            }
            else {
                throw new RuntimeException("Cannot parse line: " + line);
            }
        }
    }
}
