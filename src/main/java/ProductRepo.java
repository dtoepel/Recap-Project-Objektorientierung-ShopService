import java.util.*;

public class ProductRepo {
    private final HashMap<Product, Double> products;

    public ProductRepo() {
        products = new HashMap<>();
    }

    public List<Product> getProducts() {
        return new Vector<>(products.keySet());
    }

    public Optional<Product> getProductById(String id) {
        for (Product product : products.keySet()) {
            if (product.id().equals(id)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public Product addProduct(Product newProduct) {
        return this.addProduct(newProduct,  0.);
    }

    public Product addProduct(Product newProduct, double amount) {
        products.put(newProduct, amount);
        return newProduct;
    }

    public void addProductAmount(String id, double amount) {
        Optional<Product> product = getProductById(id);
        if(product.isPresent()) {
            products.put(product.get(), products.get(product.get()) + amount);
        } else {
            throw new RuntimeException("Product "+id+" not found");
        }
    }

    public double getProductAmount(String id) {
        Optional<Product> product = getProductById(id);
        return product.isPresent()?products.get(product.get()):0.0;
    }

    public void removeProduct(String id) {
        Optional<Product> p = getProductById(id);
        if(p.isPresent()) {
            products.remove(p.get());
        }
    }
}
