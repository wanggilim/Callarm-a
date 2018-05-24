package project.wgl.callarm;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.Calendar;

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

    /**
     * 전화번호 검색 커서 가져오기
     * @param contactsUri
     * @return  Cursor cursor
     */
    public Cursor getContactsCursorFromUri(String contactsUri) {
        String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                //rawUri,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + contactsUri.toString() + "'",
                null,
                order);
        return cursor;
    }

    /**
     * 문자메시지, 전화 등록 상태 가져오기 (String 배열)
     * @param split_ar
     * @return split 결과
     */
    public String[] getSplitString(String split_ar) {
        return split_ar.split(":");
    }


    //////////////////////////////////////////////////////

    /**
     * 전화번호 검색 후 이름 출력
     * @param contactsUri
     * @return
     */
    public String getContactsName(String contactsUri) {
        cursor = getContactsCursorFromUri(contactsUri);

        String name = "";
        while (cursor!=null && cursor.moveToNext()) {
            //Name = cursor.getString(1)
            //Phone = cursor.getString(2)
            name = cursor.getString(1);
        }
        return name;
    }

    /**
     * 벨소리 제목 출력
     * @param ringtoneUri
     * @return
     */
    public String getRingtoneName(String ringtoneUri) {
        Uri rawUri = Uri.parse(ringtoneUri);
        Ringtone ringtone = RingtoneManager.getRingtone(context, rawUri);
        String title = ringtone.getTitle(context);

        return title;
    }

    /**
     * 시간 출력 (24시간제)
     * @param time
     * @return
     */
    public String[] getTimeString(long time) {
        String[] result = new String[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour <= 9) {
            result[0] = "0" + String.valueOf(hour);
        } else {
            result[0] = String.valueOf(hour);
        }

        int minute = calendar.get(Calendar.MINUTE);
        if (minute <= 9) {
            result[1] = "0" + String.valueOf(minute);
        } else {

            result[1] = String.valueOf(minute);
        }

        return result;
    }


    /**
     *
     * @param beforeSplit
     * @param index
     *
     * - split index 정보 (2018. 5. 23)
     * (ex : 0:60000:false:저를 깨워주세요!:true:true)
     *      0 : 알람 설정시 Spinner 선택 번호
     *          (1분 후, 3분 후, 5분 후, 10분 후, 15분 후, 20분 후, 30분 후, 45분 후, 60분 후)
     *      1 : Spinner의 id 값이자, 1번 선택에 대한 long 타입의 시간 값
     *      2 : 전화걸기 예약 O/X
     *      3 : 메시지 내용
     *      4 : 메시지 예약 O/X
     *      5 : 메시지 자동 체크 O/X (-> "저를 깨워주세요!")
     *
     * @return result (split 값 출력)
     */
    public String getMPInfo(String beforeSplit, int index) {
        /**
         * TODO
         * strings - regex
         */
        String[] split = beforeSplit.split(":");
        Log.d(TAG, "getMPInfo: split.length = " + split.length);
        for (int i = 0; i < split.length; i++) {
            Log.d(TAG, "getMPInfo: " + i  + " => " + split[i]);
        }

        String result = "";

        /**
         * TODO
         * strings
         */
        if (index == 0) {
            result = split[0];
        } else if (index == 1) {
            result = String.valueOf(Integer.parseInt(split[1]) / 60000); // "분 후"
        } else if (index == 2) {
            result = (Boolean.valueOf(split[2]) == true ? "전화" : "");
        } else if (index == 3) {
            result = split[3];
        } else if (index == 4) {
            result = (Boolean.valueOf(split[4]) == true ? "문자메시지" : "");
        } else if (index == 5) {

        }

        return result;
    }

}

