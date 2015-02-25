package cleaner.util;

import cleaner.io.Cleaner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mateus on 10/02/15.
 */
public class NewEntry {
    private Date date;
    private Double lat, lng, vel;

    public NewEntry(Date date, double lat, double lng, double vel) {
        this.date = date;
        this.lat = lat;
        this.lng = lng;
        this.vel = vel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewEntry oldEntry = (NewEntry) o;

        return date.equals(oldEntry.date);
    }

    public Date date() {
        return date;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lng != null ? lng.hashCode() : 0);
        result = 31 * result + (vel != null ? vel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "{\"time\": \"%s\", \"lat\": %f, \"lng\": %f, \"vel\": %f}", (new SimpleDateFormat(Cleaner.COMPACT_DATA_FORMAT)).format(this.date), this.lat, this.lng, this.vel);
    }
}
