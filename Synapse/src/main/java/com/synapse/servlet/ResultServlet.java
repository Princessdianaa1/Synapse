package com.synapse.servlet;

import com.synapse.dao.TestDAO;
import com.synapse.model.TestResult;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/result")
public class ResultServlet extends HttpServlet {
    private TestDAO testDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        testDAO = new TestDAO();
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
            
            // Get latest result
            TestResult result = testDAO.getLatestResult(userId);
            
            if (result != null) {
                response.getWriter().write("{\"success\": true, \"result\": " + gson.toJson(result) + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"No results found\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}