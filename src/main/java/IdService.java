import java.util.UUID;

public interface IdService {
    public static IdService DEFAULT_ID_SERVICE = () -> UUID.randomUUID();

    public UUID generateID();
}
