package com.synapse.dao;

import com.synapse.model.TestSession;
import com.synapse.model.TestResult;
import java.sql.*;
import java.util.*;

public class TestDAO {
    
    public int createTestSession(int userId, String testType) {
        String sql = "INSERT INTO test_sessions (user_id, test_type, status) VALUES (?, ?, 'incomplete')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, testType);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public boolean saveDepressionResponses(int sessionId, Map<Integer, Integer> responses) {
        String sql = "INSERT INTO depression_responses (session_id, question_number, response_value) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<Integer, Integer> entry : responses.entrySet()) {
                pstmt.setInt(1, sessionId);
                pstmt.setInt(2, entry.getKey());
                pstmt.setInt(3, entry.getValue());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveAnxietyResponses(int sessionId, Map<Integer, Integer> responses) {
        String sql = "INSERT INTO anxiety_responses (session_id, question_number, response_value) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<Integer, Integer> entry : responses.entrySet()) {
                pstmt.setInt(1, sessionId);
                pstmt.setInt(2, entry.getKey());
                pstmt.setInt(3, entry.getValue());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveStressResponses(int sessionId, Map<Integer, Integer> responses) {
        String sql = "INSERT INTO stress_responses (session_id, question_number, response_value) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<Integer, Integer> entry : responses.entrySet()) {
                pstmt.setInt(1, sessionId);
                pstmt.setInt(2, entry.getKey());
                pstmt.setInt(3, entry.getValue());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean savePersonalityResponses(int sessionId, Map<String, Map<Integer, Integer>> responses) {
        String sql = "INSERT INTO personality_responses (session_id, question_number, trait_category, response_value) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<String, Map<Integer, Integer>> traitEntry : responses.entrySet()) {
                String trait = traitEntry.getKey();
                for (Map.Entry<Integer, Integer> qEntry : traitEntry.getValue().entrySet()) {
                    pstmt.setInt(1, sessionId);
                    pstmt.setInt(2, qEntry.getKey());
                    pstmt.setString(3, trait);
                    pstmt.setInt(4, qEntry.getValue());
                    pstmt.addBatch();
                }
            }
            
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveTestResult(TestResult result) {
        String sql = "INSERT INTO test_results (session_id, depression_score, depression_level, " +
                     "anxiety_score, anxiety_level, stress_score, stress_level, " +
                     "openness_score, conscientiousness_score, extraversion_score, " +
                     "agreeableness_score, neuroticism_score, overall_assessment, recommendations) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, result.getSessionId());
            pstmt.setInt(2, result.getDepressionScore());
            pstmt.setString(3, result.getDepressionLevel());
            pstmt.setInt(4, result.getAnxietyScore());
            pstmt.setString(5, result.getAnxietyLevel());
            pstmt.setInt(6, result.getStressScore());
            pstmt.setString(7, result.getStressLevel());
            pstmt.setDouble(8, result.getOpennessScore());
            pstmt.setDouble(9, result.getConscientiousnessScore());
            pstmt.setDouble(10, result.getExtraversionScore());
            pstmt.setDouble(11, result.getAgreeablenessScore());
            pstmt.setDouble(12, result.getNeuroticismScore());
            pstmt.setString(13, result.getOverallAssessment());
            pstmt.setString(14, result.getRecommendations());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public TestResult getLatestResult(int userId) {
        String sql = "SELECT tr.* FROM test_results tr " +
                     "JOIN test_sessions ts ON tr.session_id = ts.session_id " +
                     "WHERE ts.user_id = ? ORDER BY tr.created_at DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                TestResult result = new TestResult();
                result.setResultId(rs.getInt("result_id"));
                result.setSessionId(rs.getInt("session_id"));
                result.setDepressionScore(rs.getInt("depression_score"));
                result.setDepressionLevel(rs.getString("depression_level"));
                result.setAnxietyScore(rs.getInt("anxiety_score"));
                result.setAnxietyLevel(rs.getString("anxiety_level"));
                result.setStressScore(rs.getInt("stress_score"));
                result.setStressLevel(rs.getString("stress_level"));
                result.setOpennessScore(rs.getDouble("openness_score"));
                result.setConscientiousnessScore(rs.getDouble("conscientiousness_score"));
                result.setExtraversionScore(rs.getDouble("extraversion_score"));
                result.setAgreeablenessScore(rs.getDouble("agreeableness_score"));
                result.setNeuroticismScore(rs.getDouble("neuroticism_score"));
                result.setOverallAssessment(rs.getString("overall_assessment"));
                result.setRecommendations(rs.getString("recommendations"));
                result.setCreatedAt(rs.getTimestamp("created_at"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<TestResult> getAllResults(int userId) {
        List<TestResult> results = new ArrayList<>();
        String sql = "SELECT tr.* FROM test_results tr " +
                     "JOIN test_sessions ts ON tr.session_id = ts.session_id " +
                     "WHERE ts.user_id = ? ORDER BY tr.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                TestResult result = new TestResult();
                result.setResultId(rs.getInt("result_id"));
                result.setSessionId(rs.getInt("session_id"));
                result.setDepressionScore(rs.getInt("depression_score"));
                result.setDepressionLevel(rs.getString("depression_level"));
                result.setAnxietyScore(rs.getInt("anxiety_score"));
                result.setAnxietyLevel(rs.getString("anxiety_level"));
                result.setStressScore(rs.getInt("stress_score"));
                result.setStressLevel(rs.getString("stress_level"));
                result.setOpennessScore(rs.getDouble("openness_score"));
                result.setConscientiousnessScore(rs.getDouble("conscientiousness_score"));
                result.setExtraversionScore(rs.getDouble("extraversion_score"));
                result.setAgreeablenessScore(rs.getDouble("agreeableness_score"));
                result.setNeuroticismScore(rs.getDouble("neuroticism_score"));
                result.setOverallAssessment(rs.getString("overall_assessment"));
                result.setRecommendations(rs.getString("recommendations"));
                result.setCreatedAt(rs.getTimestamp("created_at"));
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    
    public void updateSessionStatus(int sessionId, String status) {
        String sql = "UPDATE test_sessions SET status = ? WHERE session_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, sessionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}