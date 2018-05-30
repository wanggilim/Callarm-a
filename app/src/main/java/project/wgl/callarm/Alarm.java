package project.wgl.callarm;

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
    private String contactName = "";
    private String contactNumber = "";
    private String split_ar = "";

//    private long dateInMillis = 0l; // 연도 + 날짜
//    private long timeInMillis = 0l; // 시간
//    private long dateTime = 0l; // dateInMillis + timeInMillis
//    private String contact = "";

    public Alarm() {
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public long getTime_l() {
        return time_l;
    }

    public void setTime_l(long time_l) {
        this.time_l = time_l;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public boolean isVibe() {
        return isVibe;
    }

    public void setVibe(boolean vibe) {
        isVibe = vibe;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public String getVolPattern() {
        return volPattern;
    }

    public void setVolPattern(String volPattern) {
        this.volPattern = volPattern;
    }

    public String getContactsUri() {
        return contactsUri;
    }

    public void setContactsUri(String contactsUri) {
        this.contactsUri = contactsUri;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSplit_ar() {
        return split_ar;
    }

    public void setSplit_ar(String split_ar) {
        this.split_ar = split_ar;
    }

}
