package cleaner.ui;

import cleaner.io.Filter;
import cleaner.util.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class View implements Observer<Filter> {

    private Collection<Filter> status;

    public View() {
        this.status = new ArrayList<>();
    }

    @Override
    public void update() {
        StringBuilder message = new StringBuilder("Cleaning ");
        this.status.forEach(filter -> message.append(filter).append(", "));
        message.delete(message.length() - 2, message.length());
        message.replace(message.lastIndexOf(", "), message.lastIndexOf(", ") + 2, " and ");
        message.append(": [");
        double status = this.status.stream().collect(Collectors.summingDouble(Filter::status)) / this.status.size();
        status *= 100;
        for (int i = 0; i < 100; i++) {
            if (i < status) message.append("#");
            else message.append("-");
        }
        message.append("]");
        System.out.print(message + "\r");

    }

    @Override
    public void addObservable(Filter o) {
        this.status.add(o);
        o.registerObserver(this);
    }

    @Override
    public void removeObservable(Filter o) {
        this.status.remove(o);
        o.removeObserser(this);
    }
}
