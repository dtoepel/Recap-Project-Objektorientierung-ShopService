import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException{
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo  productRepo = new ProductRepo();

        IdService myIdservice = new IdService() {
            int counter = 0;
            @Override
            public UUID generateID() {
                return new UUID(0, counter++);
            }
        };

        ShopService shopService = new ShopService(productRepo, orderRepo, myIdservice);

        productRepo.addProduct(new Product("1", "Quecksilberdraht (Rolle 2m)"));
        productRepo.addProduct(new Product("2", "Adapter 380V-Drehstrom/Gardena"));
        productRepo.addProduct(new Product("3", "USB-Starthilfekabel"));
        productRepo.addProduct(new Product("4", "Bratwurstbratgerät"));

        shopService.addOrder(List.of("1", "1", "2"));
        shopService.addOrder(List.of("2", "2", "3"));
        shopService.addOrder(List.of("1", "3", "3"));

        Path path = Path.of("transactions.txt");
        Stream<String> fileContextStream = java.nio.file.Files.lines(path);

        shopService.importFromFile(fileContextStream);


    }
}
