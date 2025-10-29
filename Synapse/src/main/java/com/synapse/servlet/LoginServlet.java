package com.synapse.servlet;

import com.synapse.dao.UserDAO;
import com.synapse.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // Validate inputs
            if (username == null || password == null || 
                username.trim().isEmpty() || password.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Username and password are required\"}");
                return;
            }
            
            // Authenticate user
            User user = userDAO.authenticateUser(username, password);
            
            if (user != null) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getFullName());
                session.setMaxInactiveInterval(3600); // 1 hour
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true, \"message\": \"Login successful\", " +
                        "\"userId\": " + user.getUserId() + ", " +
                        "\"username\": \"" + user.getUsername() + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid username or password\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("userId") != null) {
            response.getWriter().write("{\"loggedIn\": true, " +
                    "\"userId\": " + session.getAttribute("userId") + ", " +
                    "\"username\": \"" + session.getAttribute("username") + "\"}");
        } else {
            response.getWriter().write("{\"loggedIn\": false}");
        }
    }
}