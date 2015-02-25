package cleaner.util;

public interface Observable {
    public void registerObserver(Observer o);

    public void removeObserser(Observer o);

}
