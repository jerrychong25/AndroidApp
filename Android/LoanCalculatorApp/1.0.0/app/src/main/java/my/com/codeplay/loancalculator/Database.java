package my.com.codeplay.loancalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Database {
    public static final String TABLENAME = "loanhistory";
    public static final String COL_MONTHLYPAYMENT = "monthly_payment";
    public static final String COL_TOTALREPAYMENT = "total_repayment";
    public static final String COL_TOTALINTEREST = "total_interest";
    public static final String COL_AVERAGEMONTHLYINTEREST = "average_monthly_interest";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLENAME + " (" + 
			BaseColumns._ID + " integer primary key autoincrement, "
            + COL_MONTHLYPAYMENT + " text not null, "
            + COL_TOTALREPAYMENT + " text not null, "
            + COL_TOTALINTEREST + " text not null, "
            + COL_AVERAGEMONTHLYINTEREST + " text not null);";
    private MySQLiteOpenHelper dbHelper;

    private class MySQLiteOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "database.db";
        private static final int DATABASE_VERSION = 1;

        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);

            onCreate(db);
        }
    }

    public Database(Context context) {
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void insert(ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(TABLENAME, null, contentValues);
    }

    public Cursor query() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                TABLENAME,  // The table to query
                null,       // The columns to return (null to return all columns)
                null,       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // the row groups
                null,       // filter by row groups
                null        // The sort order
        );
    }

    public void close() {
        if(dbHelper != null)
            dbHelper.close();
    }
}
