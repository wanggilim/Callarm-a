package project.wgl.callarm;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * 2018. 4. 20  WGL
 * 데이터베이스 접근 및 사용 도움 클래스
 *
 * TODO
 *
 * 1. 생성자를 통해 파일 이름 가져오기
 * 2. 경로는 아래 클래스에서 정의해야한다.
 * 3. 경로 아래 있는 파일을 찾을 때 파일 이름을 활용하여 찾는다.
 * 4. 파일이 없으면 데이터베이스를 생성한다. / 파일이 있으면 데이터베이스를 불러온다.
 * 5. 데이터베이스와 테이블을 생성한다. / 데이터베이스 활용한다.
 *
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseOpenHelper";

    private Context context;
    private SQLiteDatabase db;

    /**
     * TODO
     * strings : name
     */
    private static final String name = "CALLARM_ALARM_DB";
    /**
     * TODO
     * Constants Class 로 만들기
     */
    private static final int version = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, name, null, version);
        Log.d(TAG, "DatabaseOpenHelper: constructor");
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        // onXXX 보다 제일 먼저 실행됨
        Log.d(TAG, "onConfigure: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: DB 생성");
        //SQL 문장 작성
        StringBuffer sb = new StringBuffer();

        /**
         * TODO
         * Strings
         */
        sb.append("CREATE TABLE CA_S (\n");
        sb.append(" isOn        INTEGER NOT NULL,\n");
        sb.append(" isRepeat    INTEGER NOT NULL,\n");
        sb.append(" time_l      LONG    NOT NULL,\n");
        sb.append(" days        VARCHAR(8),\n");
        sb.append(" isVibe      INTEGER NOT NULL,\n");
        sb.append(" ringtoneUri VARCHAR(255) NOT NULL,\n");
        sb.append(" volPattern  VARCHAR(255),\n");
        sb.append(" contactsUri VARCHAR(255),\n");
        sb.append(" split_ar    VARCHAR(255)\n");
        sb.append(" check (isOn <= 1 AND isRepeat <= 1 AND isVibe <= 1)\n");
        sb.append(");\n");

        execute(db, sb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: DB 업그레이드");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onDowngrade: DB 다운그레이드");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(TAG, "onOpen: DB 열기");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void create(SQLiteDatabase db,
                        String columnNames,
                        int isOn, int isRepeat,
                        long time_l, @Nullable String days,
                        int isVibe, String ringtoneUri,
                        String volPattern, @Nullable String contactsUri,
                        @Nullable String split_ar) {
        Log.d(TAG, "create: SQL 이용 삽입");
        //SQL 문장 작성
        StringBuffer sb = new StringBuffer();

        /**
         * TODO
         * 테스트해보기 (2가지 중 무엇이 나은지 파악하고 반영하기)
         */
        //sb.append("INSERT INTO CA_S (" + columnNames + ") \n");     // (1)
        sb.append("INSERT INTO CA_S \n");                           // (2)

        sb.append("VALUES (\n");
        sb.append(isOn + ", ");         // isOn (0 or 1)        NOT NULL
        sb.append(isRepeat + ", ");     // isRepeat (0 or 1)    NOT NULL
        sb.append(time_l + ", ");       // time_l               NOT NULL
        if (days == null || days.equals(null)) {
            sb.append(days + ", ");
        } else {
            sb.append("'" + days + "', ");
        }   // days
        sb.append(isVibe + ", ");        // isVibe (0 or 1)      NOT NULL
        sb.append("'" + ringtoneUri + "', ");   // ringtoneUri
        sb.append("'" + volPattern + "', ");    // volPattern
        if (contactsUri == null || contactsUri.equals(null)) {
            sb.append(contactsUri + ", ");      // contactsUri
            sb.append(split_ar + ");");      // split_ar
        } else {
            sb.append("'" + contactsUri + "', ");      // contactsUri
            sb.append("'" + split_ar + "');");      // split_ar
        }

        execute(db, sb);
    }

    public Cursor read(SQLiteDatabase db) {
        Log.d(TAG, "read: SQL 이용 검색");
        StringBuffer sb = new StringBuffer();

        String table = "CA_S";
        String[] columns = new String[]{"isOn","isRepeat",
                "time_l","days","isVibe","ringtoneUri",
                "volPattern","contactsUri","split_ar"};

        return db.query(table, columns,
                null, null,
                null, null,
                null);

    }

    public void update(SQLiteDatabase db) {
        Log.d(TAG, "update: SQL 이용 수정");
        StringBuffer sb = new StringBuffer();
        execute(db, sb);
    }

    public void delete(SQLiteDatabase db) {
        Log.d(TAG, "delete: SQL 이용 삭제");
        StringBuffer sb = new StringBuffer();
        execute(db, sb);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void execute(SQLiteDatabase db, StringBuffer sb) {
        try {
            Log.d(TAG, "execute: 쿼리 실행 전 -> \n" + sb.toString());
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            Log.d(TAG, "execute: 쿼리 실행 문제발생");
            e.printStackTrace();
            Log.d(TAG, "execute: -------------");
        } finally {
            Log.d(TAG, "execute: 쿼리 실행 완료");
        }
    }


} // end of class
