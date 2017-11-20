package com.example.hliu.myandroidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by H.LIU on 2017-11-18.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = ChatDatabaseHelper.class.getSimpleName();

    public final static String db_FileName = "MyMessages.db";
    public final static int currentVersion = 1;
    public final static String TableName_chart = "Chart_Table";
    public final static String MSGID_colName = "MsgID";
    public final static String MESSAGE_colName = "Message";

    private final String create_db = "CREATE TABLE "
            + TableName_chart + " ( "
            + MSGID_colName + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE_colName + " text)";
    //" INTEGER PRIMARY KEY AUTOINCREMENT, "
//" integer primary key autoincrement, "
    //-------used for my app------
    private SQLiteDatabase mydb = null;
    private final static String[] MSG_ColList = new String[]{
            MSGID_colName,
            MESSAGE_colName
    };

    //---------use signleton pattern

    public ChatDatabaseHelper(Context context) {
        super(context, db_FileName, null, currentVersion);
    }
//    private static ChatDatabaseHelper instance = null;
//    private ChatDatabaseHelper(Context context) {
//        super(context, db_FileName, null, currentVersion);
//    }
//    public static ChatDatabaseHelper getInstance(Context context) {
//        if (instance == null) {
//            instance = new ChatDatabaseHelper(context);
//        }
//        return instance;
//    }

    //----------getters
    public static String getMSGID_colName() {
        return MSGID_colName;
    }

    public static String getMESSAGE_colName() {
        return MESSAGE_colName;
    }

//    public ChatDatabaseHelper(Context context) {
//        super(context, TableName_chart, null, currentVersion);
//    }
//-------------my class for operation------------

    /**
     * return true if database is not null
     * <p></p>
     *
     * @return true if is not null, open and not read only
     */
    private boolean isConnected() {
        if ((mydb != null
                && mydb.isOpen()
                && mydb.isReadOnly() == false)) {
            return true;
        } else {
            Log.e("TAG", "database error get all record; ");
            return false;
        }
    }

    /**
     * open the database of the current db OPen helper
     */
    public void openMydb() {
        try {
            mydb = this.getWritableDatabase();
            Log.i(TAG, "database - my database opened");
        } catch (Exception e) {
            Log.e(TAG, "database error - can not open my database");
            e.printStackTrace();
        }
    }

    /**
     * check the database before closing
     */
    public void closeMydb() {
        if (isConnected()) {
            this.close();
            Log.i(TAG, "database - my database close");
        } else {
            Log.e(TAG, "database error - can not close my database");
        }
    }

    /**
     * check the database before fetching the data
     */
    public Cursor getMydb_allRecod() {
        if (!isConnected()) {
            return null;
        }
        Cursor cursor = mydb.query(TableName_chart, null, null, null, null, null, null, null);
//                Cursor cursor = mydb.rawQuery("select * from Chart_Table ", null);
//        Cursor cursor = myDatabase.query(false, Str_TABLE, new String[]{getStr_MESSAGE_COL()}, null, null,null,null,null,null );
//        Cursor cursor  = myDatabase.rawQuery("select * from ? ", new String[] {Str_TABLE});
        return cursor;
    }

    public long insertEntry_mydb(String msg) {
        if (msg == null || msg.equals("")) {
            Log.e(TAG, "database error insert a null or a empty string");
            return -1;
        }

        if (!isConnected()) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_colName, msg);
        long i = mydb.insert(TableName_chart, "error, no message", contentValues);
/*        if(i == -1){
            Log.e(TAG, "database error insert; insert failed - " + msg);
        }*/
        return i;
    }

    public int updateEntry_mydb_NumOfRows(int id, String msg) {
        if (!isConnected()) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_colName, msg);
        String[] sel_array = {String.valueOf(id)};
        int i = mydb.updateWithOnConflict(TableName_chart, contentValues, MSGID_colName + " =? ", sel_array, SQLiteDatabase.CONFLICT_IGNORE);
//            mydb.update()
        return i;
    }


    public void deleteEntry_mydb_firstEntry() {
        mydb.execSQL("DELETE FROM " + TableName_chart + " WHERE " + MSGID_colName + " = (SELECT MIN(" + MSGID_colName + ") FROM " + TableName_chart + ")");
/*        int i =0;
        String string = "DELETE FROM " + db_TableName +
                        " WHERE " + MSGID_colName +
                        " IN (SELECT " + MSGID_colName +
                        " FROM " + db_TableName +
                        " ORDER BY " + MSGID_colName +
                        " ASC LIMIT " + count + " )";
        mydb.rawQuery(db_TableName, string[]);
        mydb.execSQL(string);
        return deleteEntry_mydb_NumOfRows(i);*/
    }

    public void deleteEntry_mydb_LastEntry() {
        mydb.execSQL("DELETE FROM " + TableName_chart + " WHERE " + MSGID_colName + " = (SELECT MAX(" + MSGID_colName + ") FROM " + TableName_chart + ")");
    }


    //-----------override classes--------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(create_db);
        try {
            db.execSQL(create_db);
            Log.i("database", "database create oncreate string Ok");
        } catch (Exception e) {
            Log.i("database", "database create oncreate error : string wrong");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TableName_chart);
        onCreate(db);
    }

/*    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }*/

/*    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }*/

}
