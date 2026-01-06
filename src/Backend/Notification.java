package Backend;

import java.sql.Timestamp;

public class Notification {

    private int id;
    private int userId;
    private String title;
    private String message;
    private String type;
    private boolean read;
    private Timestamp createdAt;

    // Required for TableView / empty creation
    public Notification() {}

    // Used when admin sends notification
    public Notification(int userId, String title, String message, String type) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
    }

    // 🔴 THIS FIXES YOUR DAO ERROR
    public Notification(int id, int userId, String title, String message,
                        String type, Timestamp createdAt, boolean read) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.read = read;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
