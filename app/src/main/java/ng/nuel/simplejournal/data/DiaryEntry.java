package ng.nuel.simplejournal.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DiaryEntry {

    private String id, title, content;
    private String updatedAt;

    public DiaryEntry (){}

//    public DiaryEntry (String id, String content, Date updatedAt) {
//        this.id = id;
//        this.content = content;
//        this.updatedAt = this.dateToString(updatedAt);
//    }

    public DiaryEntry (String title, String content, String id, String updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }

//    public DiaryEntry (String id, String content, String updatedAt) {
//        this.id = id;
//        this.content = content;
//        this.updatedAt = updatedAt;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = this.stringToDate(updatedAt);
    }

    public String stringToDate(String stringDate){
        SimpleDateFormat format = new SimpleDateFormat("dd MMM");
        try {
            Date date = format.parse(stringDate);
            return date.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String dateToString(Date date){
        return this.dateToString(date, "dd MMM");
    }

    public String dateToString(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            String dateTime = dateFormat.format(date);
            return dateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
