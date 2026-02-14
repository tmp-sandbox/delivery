package libs.ddd;

public interface EventHandler<T> {
    void handle(T event);
}
