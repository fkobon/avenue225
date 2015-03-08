package net.evoir.avenue225.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.field.DatabaseField;

public class Model {

    public static DatabaseHelper sDatabaseHelper = null;

    public static DatabaseHelper getHelper(Context context) {
        if (sDatabaseHelper == null) {
            sDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return sDatabaseHelper;
    }

    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
