package cleaner.util;

public interface Observer<T extends Observable> {
    public void update();
    public void addObservable(T o);
    public void removeObservable(T o);
}
