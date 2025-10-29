package com.synapse.model;

import java.sql.Timestamp;

public class TestSession {
    private int sessionId;
    private int userId;
    private Timestamp sessionDate;
    private String testType;
    private String status;
    
    public TestSession() {}
    
    public TestSession(int userId, String testType) {
        this.userId = userId;
        this.testType = testType;
        this.status = "incomplete";
    }
    
    // Getters and Setters
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public Timestamp getSessionDate() { return sessionDate; }
    public void setSessionDate(Timestamp sessionDate) { this.sessionDate = sessionDate; }
    
    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}