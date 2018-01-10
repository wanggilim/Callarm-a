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

    boolean isRepeat = false;
    long time = 0l; // 날짜 + 시간, 또는 요일 + 시간 + 반복
    String sRingtoneUri = "";
    String contact = "";

    public Alarm() {
    }

    public Alarm(boolean isRepeat, long time, String sRingtoneUri, String contact) {
        this.isRepeat = isRepeat;
        this.time = time;
        this.sRingtoneUri = sRingtoneUri;
        this.contact = contact;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getsRingtoneUri() {
        return sRingtoneUri;
    }

    public void setsRingtoneUri(String sRingtoneUri) {
        this.sRingtoneUri = sRingtoneUri;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
