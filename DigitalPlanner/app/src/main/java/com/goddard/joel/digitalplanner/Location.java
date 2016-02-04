package com.goddard.joel.digitalplanner;

import android.database.Cursor;

/**
 * Created by Joel Goddard on 25/01/2016.
 *
 * @author Joel Goddard
 */
public class Location {

    public static final long ID_UNDEFINED = -1;

    private long id;
    private String name;

    public Location(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Location(Database db, long id) {
        this.id = id;
        Cursor c = DatabaseTableLocation.getByID(db, id);
        c.moveToFirst();
        name = c.getString(c.getColumnIndex(DatabaseTableLocation.FIELD_NAME));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
