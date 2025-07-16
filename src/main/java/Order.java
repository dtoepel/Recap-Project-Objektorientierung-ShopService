import java.time.Instant;
import java.util.List;

import lombok.With;

public record Order(
     String id,
     List<OrderItem<Product>> products,
     @With OrderStatus status,
     Instant creationTime
) {
}
