package Algorithems;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Task implements Serializable {

    private int id;
    private String description;
    private int priority;
    private Date deadline;
    private Time duration;

    public Task(int id,String description, int priority, Date deadline, Time duration) {
        this.id=id;
        this.description = description;
        setPriority(priority);
        setDeadline(deadline);
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("Priority must be between 1 and 10.");
        }
        this.priority = priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    // Method to get the hour of the deadline
    public int getDeadlineHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deadline);
        calendar.setTime(deadline);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public static Date createDate(int year, int month, int day, int hour, int minute) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return sdf.parse(day + "/" + month + "/" + year + " " + hour + ":" + minute);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        return "Task{" +
                "id:'"+id + "',"  +
                "Description='" + description + "', " +
                "Priority=" + priority + ", " +
                "Deadline=" + sdfDate.format(deadline) + " " + sdfTime.format(deadline) + ", " +
                "Duration=" + duration +
                '}';
    }



}
