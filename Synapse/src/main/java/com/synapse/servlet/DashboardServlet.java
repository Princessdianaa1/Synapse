package com.synapse.servlet;

import com.synapse.dao.TestDAO;
import com.synapse.dao.UserDAO;
import com.synapse.model.TestResult;
import com.synapse.model.User;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private TestDAO testDAO;
    private UserDAO userDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        testDAO = new TestDAO();
        userDAO = new UserDAO();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Not logged in\"}");
            return;
        }
        
        try {
            int userId = (Integer) session.getAttribute("userId");
            
            // Get user info
            User user = userDAO.getUserById(userId);
            
            // Get latest result
            TestResult latestResult = testDAO.getLatestResult(userId);
            
            // Get all results for history
            List<TestResult> allResults = testDAO.getAllResults(userId);
            
            // Build JSON response
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("{\"success\": true, ");
            jsonResponse.append("\"user\": ").append(gson.toJson(user)).append(", ");
            jsonResponse.append("\"latestResult\": ").append(gson.toJson(latestResult)).append(", ");
            jsonResponse.append("\"allResults\": ").append(gson.toJson(allResults));
            jsonResponse.append("}");
            
            response.getWriter().write(jsonResponse.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}