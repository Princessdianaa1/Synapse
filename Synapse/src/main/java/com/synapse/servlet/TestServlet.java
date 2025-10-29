package com.synapse.servlet;

import com.synapse.dao.TestDAO;
import com.synapse.model.TestResult;
import com.synapse.util.AIAnalyzer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/test/*")
public class TestServlet extends HttpServlet {
    private TestDAO testDAO;
    
    @Override
    public void init() throws ServletException {
        testDAO = new TestDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Not logged in\"}");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/depression".equals(pathInfo)) {
                handleDepressionTest(request, response, userId);
            } else if ("/anxiety".equals(pathInfo)) {
                handleAnxietyTest(request, response, userId);
            } else if ("/stress".equals(pathInfo)) {
                handleStressTest(request, response, userId);
            } else if ("/personality".equals(pathInfo)) {
                handlePersonalityTest(request, response, userId);
            } else if ("/complete".equals(pathInfo)) {
                handleCompleteAnalysis(request, response, userId);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid test endpoint\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    private void handleDepressionTest(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws IOException {
        
        // Create session
        int sessionId = testDAO.createTestSession(userId, "depression");
        
        // Parse responses
        Map<Integer, Integer> responses = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            String value = request.getParameter("q" + i);
            if (value != null && !value.isEmpty()) {
                responses.put(i, Integer.parseInt(value));
            }
        }
        
        // Save responses
        boolean saved = testDAO.saveDepressionResponses(sessionId, responses);
        
        if (saved) {
            // Store sessionId in session for later complete analysis
            request.getSession().setAttribute("depressionSessionId", sessionId);
            
            response.getWriter().write("{\"success\": true, \"message\": \"Depression test saved\", \"sessionId\": " + sessionId + "}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to save responses\"}");
        }
    }
    
    private void handleAnxietyTest(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws IOException {
        
        int sessionId = testDAO.createTestSession(userId, "anxiety");
        
        Map<Integer, Integer> responses = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            String value = request.getParameter("q" + i);
            if (value != null && !value.isEmpty()) {
                responses.put(i, Integer.parseInt(value));
            }
        }
        
        boolean saved = testDAO.saveAnxietyResponses(sessionId, responses);
        
        if (saved) {
            request.getSession().setAttribute("anxietySessionId", sessionId);
            response.getWriter().write("{\"success\": true, \"message\": \"Anxiety test saved\", \"sessionId\": " + sessionId + "}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to save responses\"}");
        }
    }
    
    private void handleStressTest(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws IOException {
        
        int sessionId = testDAO.createTestSession(userId, "stress");
        
        Map<Integer, Integer> responses = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            String value = request.getParameter("q" + i);
            if (value != null && !value.isEmpty()) {
                responses.put(i, Integer.parseInt(value));
            }
        }
        
        boolean saved = testDAO.saveStressResponses(sessionId, responses);
        
        if (saved) {
            request.getSession().setAttribute("stressSessionId", sessionId);
            response.getWriter().write("{\"success\": true, \"message\": \"Stress test saved\", \"sessionId\": " + sessionId + "}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to save responses\"}");
        }
    }
    
    private void handlePersonalityTest(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws IOException {
        
        int sessionId = testDAO.createTestSession(userId, "personality");
        
        Map<String, Map<Integer, Integer>> responses = new HashMap<>();
        
        // Parse Big Five traits (10 questions each)
        String[] traits = {"openness", "conscientiousness", "extraversion", "agreeableness", "neuroticism"};
        
        for (String trait : traits) {
            Map<Integer, Integer> traitResponses = new HashMap<>();
            for (int i = 1; i <= 10; i++) {
                String paramName = trait + "_q" + i;
                String value = request.getParameter(paramName);
                if (value != null && !value.isEmpty()) {
                    traitResponses.put(i, Integer.parseInt(value));
                }
            }
            responses.put(trait, traitResponses);
        }
        
        boolean saved = testDAO.savePersonalityResponses(sessionId, responses);
        
        if (saved) {
            request.getSession().setAttribute("personalitySessionId", sessionId);
            response.getWriter().write("{\"success\": true, \"message\": \"Personality test saved\", \"sessionId\": " + sessionId + "}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to save responses\"}");
        }
    }
    
    private void handleCompleteAnalysis(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws IOException {
        
        HttpSession session = request.getSession();
        
        // Get all session IDs
        Integer depSessionId = (Integer) session.getAttribute("depressionSessionId");
        Integer anxSessionId = (Integer) session.getAttribute("anxietySessionId");
        Integer stressSessionId = (Integer) session.getAttribute("stressSessionId");
        Integer persSessionId = (Integer) session.getAttribute("personalitySessionId");
        
        if (depSessionId == null || anxSessionId == null || stressSessionId == null || persSessionId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Not all tests completed\"}");
            return;
        }
        
        // Retrieve responses from database
        Map<Integer, Integer> depResponses = getResponsesFromRequest(request, "dep", 9);
        Map<Integer, Integer> anxResponses = getResponsesFromRequest(request, "anx", 7);
        Map<Integer, Integer> stressResponses = getResponsesFromRequest(request, "stress", 10);
        
        Map<String, Map<Integer, Integer>> persResponses = new HashMap<>();
        String[] traits = {"openness", "conscientiousness", "extraversion", "agreeableness", "neuroticism"};
        for (String trait : traits) {
            persResponses.put(trait, getResponsesFromRequest(request, trait, 10));
        }
        
        // Create main session for complete analysis
        int mainSessionId = testDAO.createTestSession(userId, "complete_analysis");
        
        // Perform AI analysis
        TestResult result = AIAnalyzer.performCompleteAnalysis(
            mainSessionId, depResponses, anxResponses, stressResponses, persResponses
        );
        
        // Save result
        boolean saved = testDAO.saveTestResult(result);
        testDAO.updateSessionStatus(mainSessionId, "completed");
        
        if (saved) {
            // Clear session test IDs
            session.removeAttribute("depressionSessionId");
            session.removeAttribute("anxietySessionId");
            session.removeAttribute("stressSessionId");
            session.removeAttribute("personalitySessionId");
            
            response.getWriter().write("{\"success\": true, \"message\": \"Analysis complete\", \"resultId\": " + mainSessionId + "}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to save results\"}");
        }
    }
    
    private Map<Integer, Integer> getResponsesFromRequest(HttpServletRequest request, String prefix, int count) {
        Map<Integer, Integer> responses = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            String value = request.getParameter(prefix + "_q" + i);
            if (value != null && !value.isEmpty()) {
                responses.put(i, Integer.parseInt(value));
            }
        }
        return responses;
    }
}