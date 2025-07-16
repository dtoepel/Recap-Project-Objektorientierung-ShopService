public record OrderItem<OType>(
        OType product,
        Double amount
) {
}
