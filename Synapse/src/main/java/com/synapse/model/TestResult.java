package com.synapse.model;

import java.sql.Timestamp;

public class TestResult {
    private int resultId;
    private int sessionId;
    private int depressionScore;
    private String depressionLevel;
    private int anxietyScore;
    private String anxietyLevel;
    private int stressScore;
    private String stressLevel;
    private double opennessScore;
    private double conscientiousnessScore;
    private double extraversionScore;
    private double agreeablenessScore;
    private double neuroticismScore;
    private String overallAssessment;
    private String recommendations;
    private Timestamp createdAt;
    
    public TestResult() {}
    
    // Getters and Setters
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }
    
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
    
    public int getDepressionScore() { return depressionScore; }
    public void setDepressionScore(int depressionScore) { this.depressionScore = depressionScore; }
    
    public String getDepressionLevel() { return depressionLevel; }
    public void setDepressionLevel(String depressionLevel) { this.depressionLevel = depressionLevel; }
    
    public int getAnxietyScore() { return anxietyScore; }
    public void setAnxietyScore(int anxietyScore) { this.anxietyScore = anxietyScore; }
    
    public String getAnxietyLevel() { return anxietyLevel; }
    public void setAnxietyLevel(String anxietyLevel) { this.anxietyLevel = anxietyLevel; }
    
    public int getStressScore() { return stressScore; }
    public void setStressScore(int stressScore) { this.stressScore = stressScore; }
    
    public String getStressLevel() { return stressLevel; }
    public void setStressLevel(String stressLevel) { this.stressLevel = stressLevel; }
    
    public double getOpennessScore() { return opennessScore; }
    public void setOpennessScore(double opennessScore) { this.opennessScore = opennessScore; }
    
    public double getConscientiousnessScore() { return conscientiousnessScore; }
    public void setConscientiousnessScore(double conscientiousnessScore) { this.conscientiousnessScore = conscientiousnessScore; }
    
    public double getExtraversionScore() { return extraversionScore; }
    public void setExtraversionScore(double extraversionScore) { this.extraversionScore = extraversionScore; }
    
    public double getAgreeablenessScore() { return agreeablenessScore; }
    public void setAgreeablenessScore(double agreeablenessScore) { this.agreeablenessScore = agreeablenessScore; }
    
    public double getNeuroticismScore() { return neuroticismScore; }
    public void setNeuroticismScore(double neuroticismScore) { this.neuroticismScore = neuroticismScore; }
    
    public String getOverallAssessment() { return overallAssessment; }
    public void setOverallAssessment(String overallAssessment) { this.overallAssessment = overallAssessment; }
    
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}