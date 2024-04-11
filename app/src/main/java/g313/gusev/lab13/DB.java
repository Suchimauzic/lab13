package g313.gusev.lab13;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public final class DB extends SQLiteOpenHelper {

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Equals (Id INTEGER PRIMARY KEY AUTOINCREMENT, Answer TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String value) {
        String sql = "INSERT INTO Equals(Answer) VALUES('" + value + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT Id, Answer FROM Equals;";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst() == true) {
            do {
                Note note = new Note();
                note.id = cur.getInt(0);
                note.answer = cur.getString(1);
                notes.add(note);
            } while (cur.moveToNext() == true);
        }
        return notes;
    }

}
