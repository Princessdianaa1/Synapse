package com.synapse.util;

import com.synapse.model.TestResult;
import java.util.*;

public class AIAnalyzer {
    
    // Calculate Depression Score (PHQ-9 style)
    public static int calculateDepressionScore(Map<Integer, Integer> responses) {
        int total = 0;
        for (Integer value : responses.values()) {
            total += value;
        }
        return total;
    }
    
    public static String getDepressionLevel(int score) {
        if (score <= 4) return "Minimal";
        if (score <= 9) return "Mild";
        if (score <= 14) return "Moderate";
        if (score <= 19) return "Moderately Severe";
        return "Severe";
    }
    
    // Calculate Anxiety Score (GAD-7 style)
    public static int calculateAnxietyScore(Map<Integer, Integer> responses) {
        int total = 0;
        for (Integer value : responses.values()) {
            total += value;
        }
        return total;
    }
    
    public static String getAnxietyLevel(int score) {
        if (score <= 4) return "Minimal";
        if (score <= 9) return "Mild";
        if (score <= 14) return "Moderate";
        return "Severe";
    }
    
    // Calculate Stress Score (PSS style)
    public static int calculateStressScore(Map<Integer, Integer> responses) {
        int total = 0;
        for (Integer value : responses.values()) {
            total += value;
        }
        return total;
    }
    
    public static String getStressLevel(int score) {
        if (score <= 13) return "Low";
        if (score <= 26) return "Moderate";
        return "High";
    }
    
    // Calculate Big Five Personality Traits
    public static Map<String, Double> calculatePersonalityScores(Map<String, Map<Integer, Integer>> responses) {
        Map<String, Double> scores = new HashMap<>();
        
        for (Map.Entry<String, Map<Integer, Integer>> entry : responses.entrySet()) {
            String trait = entry.getKey();
            Map<Integer, Integer> traitResponses = entry.getValue();
            
            double sum = 0;
            for (Integer value : traitResponses.values()) {
                sum += value;
            }
            
            double average = sum / traitResponses.size();
            scores.put(trait, average);
        }
        
        return scores;
    }
    
    // Generate AI-driven Overall Assessment
    public static String generateOverallAssessment(TestResult result) {
        StringBuilder assessment = new StringBuilder();
        
        assessment.append("COMPREHENSIVE MENTAL HEALTH ASSESSMENT\n\n");
        
        // Depression Analysis
        assessment.append("Depression Analysis: Your score of ").append(result.getDepressionScore())
                  .append(" indicates ").append(result.getDepressionLevel().toLowerCase())
                  .append(" depression symptoms. ");
        
        if (result.getDepressionScore() <= 4) {
            assessment.append("You're experiencing minimal depressive symptoms, which is a positive indicator of mental well-being. ");
        } else if (result.getDepressionScore() <= 9) {
            assessment.append("You're experiencing mild symptoms that may benefit from lifestyle modifications and self-care practices. ");
        } else if (result.getDepressionScore() <= 14) {
            assessment.append("You're experiencing moderate symptoms that warrant attention and possibly professional consultation. ");
        } else {
            assessment.append("You're experiencing significant symptoms that strongly indicate the need for professional mental health support. ");
        }
        
        assessment.append("\n\n");
        
        // Anxiety Analysis
        assessment.append("Anxiety Analysis: Your score of ").append(result.getAnxietyScore())
                  .append(" indicates ").append(result.getAnxietyLevel().toLowerCase())
                  .append(" anxiety levels. ");
        
        if (result.getAnxietyScore() <= 4) {
            assessment.append("Your anxiety levels are well-managed. ");
        } else if (result.getAnxietyScore() <= 9) {
            assessment.append("You're experiencing mild anxiety that can typically be managed with stress-reduction techniques. ");
        } else if (result.getAnxietyScore() <= 14) {
            assessment.append("You're experiencing moderate anxiety that may interfere with daily functioning and could benefit from intervention. ");
        } else {
            assessment.append("You're experiencing severe anxiety that significantly impacts your quality of life and requires professional attention. ");
        }
        
        assessment.append("\n\n");
        
        // Stress Analysis
        assessment.append("Stress Analysis: Your score of ").append(result.getStressScore())
                  .append(" indicates ").append(result.getStressLevel().toLowerCase())
                  .append(" stress levels. ");
        
        if (result.getStressScore() <= 13) {
            assessment.append("You're managing stress effectively. ");
        } else if (result.getStressScore() <= 26) {
            assessment.append("You're experiencing moderate stress that may benefit from improved coping strategies. ");
        } else {
            assessment.append("You're experiencing high stress levels that require immediate attention and stress management interventions. ");
        }
        
        assessment.append("\n\n");
        
        // Personality Analysis
        assessment.append("Personality Profile:\n");
        assessment.append("• Openness: ").append(String.format("%.1f", result.getOpennessScore())).append("/5.0 - ");
        if (result.getOpennessScore() >= 3.5) {
            assessment.append("You're highly creative, curious, and open to new experiences.\n");
        } else {
            assessment.append("You prefer routine and familiar experiences.\n");
        }
        
        assessment.append("• Conscientiousness: ").append(String.format("%.1f", result.getConscientiousnessScore())).append("/5.0 - ");
        if (result.getConscientiousnessScore() >= 3.5) {
            assessment.append("You're organized, disciplined, and goal-oriented.\n");
        } else {
            assessment.append("You may benefit from improved organizational strategies.\n");
        }
        
        assessment.append("• Extraversion: ").append(String.format("%.1f", result.getExtraversionScore())).append("/5.0 - ");
        if (result.getExtraversionScore() >= 3.5) {
            assessment.append("You're outgoing, energetic, and thrive in social situations.\n");
        } else {
            assessment.append("You prefer solitude and smaller social circles.\n");
        }
        
        assessment.append("• Agreeableness: ").append(String.format("%.1f", result.getAgreeablenessScore())).append("/5.0 - ");
        if (result.getAgreeablenessScore() >= 3.5) {
            assessment.append("You're compassionate, cooperative, and empathetic.\n");
        } else {
            assessment.append("You tend to be more competitive and assertive.\n");
        }
        
        assessment.append("• Neuroticism: ").append(String.format("%.1f", result.getNeuroticismScore())).append("/5.0 - ");
        if (result.getNeuroticismScore() >= 3.5) {
            assessment.append("You may experience emotional instability and should focus on stress management.\n");
        } else {
            assessment.append("You're emotionally stable and resilient.\n");
        }
        
        return assessment.toString();
    }
    
    // Generate Personalized Recommendations
    public static String generateRecommendations(TestResult result) {
        StringBuilder recommendations = new StringBuilder();
        
        recommendations.append("PERSONALIZED RECOMMENDATIONS\n\n");
        
        // Depression Recommendations
        if (result.getDepressionScore() > 9) {
            recommendations.append("For Depression Management:\n");
            recommendations.append("• Consult with a mental health professional for proper assessment and treatment\n");
            recommendations.append("• Consider cognitive behavioral therapy (CBT) or other evidence-based therapies\n");
            recommendations.append("• Maintain a regular sleep schedule (7-9 hours per night)\n");
            recommendations.append("• Engage in regular physical exercise (30 minutes daily)\n");
            recommendations.append("• Practice mindfulness and meditation\n");
            recommendations.append("• Stay connected with supportive friends and family\n\n");
        }
        
        // Anxiety Recommendations
        if (result.getAnxietyScore() > 9) {
            recommendations.append("For Anxiety Management:\n");
            recommendations.append("• Practice deep breathing exercises and progressive muscle relaxation\n");
            recommendations.append("• Limit caffeine and alcohol intake\n");
            recommendations.append("• Consider therapy options like CBT or exposure therapy\n");
            recommendations.append("• Establish a regular routine to create predictability\n");
            recommendations.append("• Use grounding techniques during anxious moments\n");
            recommendations.append("• Journal your thoughts and triggers\n\n");
        }
        
        // Stress Recommendations
        if (result.getStressScore() > 13) {
            recommendations.append("For Stress Management:\n");
            recommendations.append("• Implement time management techniques and prioritize tasks\n");
            recommendations.append("• Take regular breaks throughout the day\n");
            recommendations.append("• Practice yoga or tai chi\n");
            recommendations.append("• Set healthy boundaries in work and personal life\n");
            recommendations.append("• Engage in hobbies and activities you enjoy\n");
            recommendations.append("• Consider massage therapy or other relaxation techniques\n\n");
        }
        
        // Personality-based Recommendations
        if (result.getNeuroticismScore() >= 3.5) {
            recommendations.append("Based on Your Personality:\n");
            recommendations.append("• Your higher neuroticism score suggests you may benefit from emotion regulation strategies\n");
            recommendations.append("• Practice reframing negative thoughts into more balanced perspectives\n");
            recommendations.append("• Build resilience through gradual exposure to challenging situations\n\n");
        }
        
        if (result.getExtraversionScore() < 2.5 && (result.getDepressionScore() > 9 || result.getAnxietyScore() > 9)) {
            recommendations.append("• While you prefer solitude, consider gradually increasing social connections\n");
            recommendations.append("• Quality over quantity: focus on meaningful one-on-one relationships\n\n");
        }
        
        // General Wellness Recommendations
        recommendations.append("General Wellness Tips:\n");
        recommendations.append("• Maintain a balanced diet rich in omega-3 fatty acids, vegetables, and whole grains\n");
        recommendations.append("• Limit screen time, especially before bed\n");
        recommendations.append("• Spend time in nature regularly\n");
        recommendations.append("• Practice gratitude daily\n");
        recommendations.append("• Consider joining support groups or community activities\n\n");
        
        // Professional Help Recommendation
        if (result.getDepressionScore() > 14 || result.getAnxietyScore() > 14 || result.getStressScore() > 26) {
            recommendations.append("IMPORTANT: Your scores indicate significant distress. We strongly recommend:\n");
            recommendations.append("• Consulting with a licensed mental health professional immediately\n");
            recommendations.append("• If experiencing thoughts of self-harm, please contact:\n");
            recommendations.append("  - National Suicide Prevention Lifeline: 988 (US)\n");
            recommendations.append("  - Crisis Text Line: Text HOME to 741741\n");
            recommendations.append("  - Emergency Services: 911 or your local emergency number\n");
        }
        
        return recommendations.toString();
    }
    
    // Complete analysis method
    public static TestResult performCompleteAnalysis(int sessionId, 
                                                     Map<Integer, Integer> depressionResponses,
                                                     Map<Integer, Integer> anxietyResponses,
                                                     Map<Integer, Integer> stressResponses,
                                                     Map<String, Map<Integer, Integer>> personalityResponses) {
        
        TestResult result = new TestResult();
        result.setSessionId(sessionId);
        
        // Calculate scores
        int depScore = calculateDepressionScore(depressionResponses);
        result.setDepressionScore(depScore);
        result.setDepressionLevel(getDepressionLevel(depScore));
        
        int anxScore = calculateAnxietyScore(anxietyResponses);
        result.setAnxietyScore(anxScore);
        result.setAnxietyLevel(getAnxietyLevel(anxScore));
        
        int stressScore = calculateStressScore(stressResponses);
        result.setStressScore(stressScore);
        result.setStressLevel(getStressLevel(stressScore));
        
        Map<String, Double> personalityScores = calculatePersonalityScores(personalityResponses);
        result.setOpennessScore(personalityScores.getOrDefault("openness", 0.0));
        result.setConscientiousnessScore(personalityScores.getOrDefault("conscientiousness", 0.0));
        result.setExtraversionScore(personalityScores.getOrDefault("extraversion", 0.0));
        result.setAgreeablenessScore(personalityScores.getOrDefault("agreeableness", 0.0));
        result.setNeuroticismScore(personalityScores.getOrDefault("neuroticism", 0.0));
        
        // Generate assessment and recommendations
        result.setOverallAssessment(generateOverallAssessment(result));
        result.setRecommendations(generateRecommendations(result));
        
        return result;
    }
}