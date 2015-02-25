package cleaner.util;

import cleaner.io.Cleaner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OldEntry {
    private Date date;
    private String busID;
    private Double lat, lng, vel;

    public OldEntry(Date date, String busID, double lat, double lng, double vel) {
        this.date = date;
        this.busID = busID;
        this.lat = lat;
        this.lng = lng;
        this.vel = vel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OldEntry oldEntry = (OldEntry) o;

        return busID.equals(oldEntry.busID) && date.equals(oldEntry.date);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + busID.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "{\"busID\": \"%s\", \"date\": \"%s\", \"lat\": %f, \"lng\": %f, \"vel\": %f}", this.busID, (new SimpleDateFormat(Cleaner.CORRECT_DATA_FORMAT).format(date)), this.lat, this.lng, this.vel);
    }
}
