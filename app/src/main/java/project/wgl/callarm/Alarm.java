package project.wgl.callarm;

import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by WGL on 2018. 1. 10..
 */

public class Alarm {

    /**
     * 1. 반복유무
     * 2. 1에 따라 날짜 또는 요일 선택
     * 3. 시간 선택
     * 4. 벨소리 선택
     * 5. 진동 유무
     * 6. 연락처 설정
     */

    private boolean isOn = false;
    private boolean isRepeat = false;
    private long time_l;
    private String days = "";
    private boolean isVibe = false;
    private String ringtoneUri = "";
    private String volPattern = "";
    private String contactsUri = "";
    private String split_ar = "";

    private long dateInMillis = 0l; // 연도 + 날짜
    private long timeInMillis = 0l; // 시간
    private long dateTime = 0l; // dateInMillis + timeInMillis
    private String contact = "";

    public Alarm() {
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public void setVibe(boolean vibe) {
        isVibe = vibe;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public long getDateInMillis() {
        return dateInMillis;
    }

    public void setDateInMillis(long dateInMillis) {
        this.dateInMillis = dateInMillis;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public long getDateTime() {
        Calendar ymd = Calendar.getInstance();
        Calendar hms = Calendar.getInstance();

        ymd.setTimeInMillis(getDateInMillis());
        hms.setTimeInMillis(getTimeInMillis());

        Calendar dt = Calendar.getInstance();
        dt.set(ymd.get(Calendar.YEAR), ymd.get(Calendar.MONTH), ymd.get(Calendar.DAY_OF_MONTH),
                hms.get(Calendar.HOUR_OF_DAY), hms.get(Calendar.MINUTE), 0);
        dateTime = dt.getTimeInMillis();

        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
