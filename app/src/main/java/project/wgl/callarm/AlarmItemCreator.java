package project.wgl.callarm;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by WGL on 2018. 5. 23..
 *
 */
public class AlarmItemCreator {
    private final static String TAG = "AlarmItemCreator";

    private Context context;
    private Cursor cursor;

    // 날짜
    private int year = 0;
    private int month = 0;
    private int dayOfMonth = 0;
    private int hour = 0;
    private int minute = 0;
    private int seconds = 0;
    private int milliseconds = 0;

    private long dateInMillis = 0l; // 연도 + 날짜
    private long timeInMillis = 0l; // 시간
    private long dateTime = 0l; // dateInMillis + timeInMillis

    // 연락처
    private String contactName = "";
    private String contactPhone = "";


    public AlarmItemCreator() {
    }

    public AlarmItemCreator(Context context) {
        this.context = context;
    }

    //////////////////////////////////////////////////////

    public void showResult(String contactsUri) {
        Cursor cursor = getCursorFromUri(contactsUri);

        Log.d(TAG, "showResult: Start");
        while (cursor.moveToNext()) {
            Log.d(TAG, "======================");
            Log.d(TAG, "showResult: Name = " + cursor.getString(1));
            Log.d(TAG, "showResult: Phone = " + cursor.getString(2));
            Log.d(TAG, "======================");
        }
        Log.d(TAG, "showResult: End");
    }


    public Cursor getCursorFromUri(String contactsUri) {
        Log.d(TAG, "getCursorFromUri: Start");
        String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Uri rawUri = Uri.parse(contactsUri);
        String[] selectionArgs = new String[]{contactsUri.toString()};

        cursor = context.getContentResolver().query(
                //ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                rawUri,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                "PHONE=",
                selectionArgs,
                order);

        Log.d(TAG, "getCursorFromUri: End");
        return cursor;
    }

}

