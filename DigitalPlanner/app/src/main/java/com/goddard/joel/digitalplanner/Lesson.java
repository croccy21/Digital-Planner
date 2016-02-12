package com.goddard.joel.digitalplanner;

import android.database.Cursor;

import java.util.Calendar;

/**
 * Created by Joel Goddard on 05/02/2016.
 *
 * @author Joel Goddard
 */
public class Lesson {
    private long id;
    private boolean canceled;
    private Calendar date;
    private Block block;

    public Lesson(long id, Calendar date, Block block, boolean canceled) {
        this.id = id;
        this.date = date;
        this.block = block;
        this.canceled = canceled;
    }

    public Lesson(Database db, long id){
        Cursor c = DatabaseTableLesson.getByID(db, id);
        c.moveToFirst();
        this.id = id;
        date = Calendar.getInstance();
        date.setTimeInMillis(c.getLong(c.getColumnIndex(DatabaseTableLesson.FIELD_DAY)));
        canceled = c.getInt(c.getColumnIndex(DatabaseTableLesson.FIELD_CANCELED))>0;
        setBlock(db, c.getLong(c.getColumnIndex(DatabaseTableLesson.FIELD_BLOCK_ID)));
    }

    public Lesson(Database db, long blockId, Calendar date){
        Cursor c = DatabaseTableLesson.getByBlockIDAndDate(db, blockId, date);
        this.date = date;
        setBlock(db, blockId);
        if(c.getCount()>0) {
            c.moveToFirst();
            id = c.getLong(c.getColumnIndex(DatabaseTableLesson.FIELD_LESSON_ID));
            canceled = c.getInt(c.getColumnIndex(DatabaseTableLesson.FIELD_CANCELED)) > 0;
        }
        else{
            canceled = false;
            id = DatabaseTableLesson.insert(db, blockId, date, canceled);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setBlock(Database db, long blockId){
        block = new Block(db, blockId);
    }
}
