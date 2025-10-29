# Synapse
# Synapse - Mental Health Assessment Platform

A comprehensive web-based mental health assessment platform that evaluates cognitive and emotional health through standardized psychological tests, with AI-driven analysis and personalized recommendations.

## Features

- **User Registration & Management**: Secure login and signup system
- **Depression Screening**: PHQ-9 assessment
- **Anxiety Assessment**: GAD-7 scale
- **Stress Evaluation**: Perceived Stress Scale (PSS-10)
- **Personality Analysis**: Big Five personality traits
- **AI-Driven Evaluation**: Automated score calculation and interpretation
- **Personalized Recommendations**: Custom mental health guidance
- **Dashboard**: Track progress over time
- **Detailed Reports**: Comprehensive mental health reports

## Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or later**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Set JAVA_HOME environment variable

2. **Apache Tomcat 10.x**
   - Download from: https://tomcat.apache.org/download-10.cgi
   - Extract to a directory (e.g., C:\tomcat or /opt/tomcat)

3. **MySQL 8.x**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Note your root password during installation

4. **MySQL Connector/J (JDBC Driver)**
   - Download from: https://dev.mysql.com/downloads/connector/j/
   - Download the JAR file (mysql-connector-j-8.x.x.jar)

5. **Gson Library** (for JSON processing in servlets)
   - Download from: https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar

6. **IDE** (Optional but recommended)
   - Eclipse IDE for Java EE Developers, or
   - IntelliJ IDEA

## Step-by-Step Installation

### Step 1: Set Up MySQL Database

1. Open MySQL Command Line or MySQL Workbench
2. Login with your root credentials
3. Run the database setup script:

```sql
CREATE DATABASE synapse_db;
USE synapse_db;

-- Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    date_of_birth DATE,
    gender VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Test Sessions Table
CREATE TABLE test_sessions (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    session_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    test_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'incomplete',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Depression Test Responses
CREATE TABLE depression_responses (
    response_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id INT NOT NULL,
    question_number INT NOT NULL,
    response_value INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES test_sessions(session_id)
);

-- Anxiety Test Responses
CREATE TABLE anxiety_responses (
    response_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id INT NOT NULL,
    question_number INT NOT NULL,
    response_value INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES test_sessions(session_id)
);

-- Stress Test Responses
CREATE TABLE stress_responses (
    response_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id INT NOT NULL,
    question_number INT NOT NULL,
    response_value INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES test_sessions(session_id)
);

-- Personality Test Responses
CREATE TABLE personality_responses (
    response_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id INT NOT NULL,
    question_number INT NOT NULL,
    trait_category VARCHAR(50) NOT NULL,
    response_value INT NOT NULL,
    FOREIGN KEY (session_id) REFERENCES test_sessions(session_id)
);

-- Test Results Table
CREATE TABLE test_results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id INT NOT NULL,
    depression_score INT,
    depression_level VARCHAR(50),
    anxiety_score INT,
    anxiety_level VARCHAR(50),
    stress_score INT,
    stress_level VARCHAR(50),
    openness_score DECIMAL(5,2),
    conscientiousness_score DECIMAL(5,2),
    extraversion_score DECIMAL(5,2),
    agreeableness_score DECIMAL(5,2),
    neuroticism_score DECIMAL(5,2),
    overall_assessment TEXT,
    recommendations TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES test_sessions(session_id)
);
```

### Step 2: Create Project Directory Structure

Create the following directory structure:

```
Synapse/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── synapse/
│       │           ├── dao/
│       │           │   ├── DatabaseConnection.java
│       │           │   ├── UserDAO.java
│       │           │   └── TestDAO.java
│       │           ├── model/
│       │           │   ├── User.java
│       │           │   ├── TestSession.java
│       │           │   └── TestResult.java
│       │           ├── servlet/
│       │           │   ├── LoginServlet.java
│       │           │   ├── SignupServlet.java
│       │           │   ├── LogoutServlet.java
│       │           │   ├── TestServlet.java
│       │           │   ├── ResultServlet.java
│       │           │   └── DashboardServlet.java
│       │           └── util/
│       │               ├── PasswordUtil.java
│       │               └── AIAnalyzer.java
│       ├── resources/
│       │   └── db.properties
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml
│           │   └── lib/
│           │       ├── mysql-connector-j-8.x.x.jar
│           │       └── gson-2.10.1.jar
│           ├── css/
│           │   └── style.css
│           ├── js/
│           │   └── app.js
│           ├── index.html
│           ├── login.html
│           ├── signup.html
│           ├── dashboard.html
│           ├── test-depression.html
│           ├── test-anxiety.html
│           ├── test-stress.html
│           ├── test-personality.html
│           └── results.html
```

### Step 3: Configure Database Connection

Create `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/synapse_db
db.user=root
db.password=YOUR_MYSQL_PASSWORD
```

**Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password.**

### Step 4: Copy All Java Files

Copy all the Java files provided in the artifacts to their respective directories as shown in the structure above.

### Step 5: Copy Frontend Files

Copy all HTML, CSS, and JavaScript files to the webapp directory.

### Step 6: Add Required JAR Files

1. Copy `mysql-connector-j-8.x.x.jar` to `src/main/webapp/WEB-INF/lib/`
2. Copy `gson-2.10.1.jar` to `src/main/webapp/WEB-INF/lib/`

### Step 7: Compile Java Files

#### Option A: Using Command Line

Navigate to your project root directory and run:

```bash
# Windows
javac -cp ".;src/main/webapp/WEB-INF/lib/*" -d build/classes src/main/java/com/synapse/*/*.java

# Linux/Mac
javac -cp ".:src/main/webapp/WEB-INF/lib/*" -d build/classes src/main/java/com/synapse/*/*.java
```

Then copy compiled classes to:
```
src/main/webapp/WEB-INF/classes/
```

#### Option B: Using Eclipse IDE

1. Open Eclipse
2. File → New → Dynamic Web Project
3. Project name: Synapse
4. Target runtime: Apache Tomcat 10.x
5. Click Finish
6. Copy all source files to src folder
7. Copy all webapp files to WebContent folder
8. Right-click project → Build Path → Configure Build Path
9. Add External JARs: mysql-connector and gson
10. Right-click project → Run As → Run on Server

#### Option C: Using IntelliJ IDEA

1. Open IntelliJ IDEA
2. File → New → Project from Existing Sources
3. Select your Synapse directory
4. Choose "Import project from external model" → Maven/Gradle (or just Java)
5. Add Tomcat configuration: Run → Edit Configurations → + → Tomcat Server → Local
6. Configure Tomcat home directory
7. Add mysql-connector and gson JARs to libraries
8. Run the project

### Step 8: Create WAR File (for deployment)

1. Navigate to your project directory
2. Create a WAR file structure:

```bash
cd src/main/webapp
jar -cvf Synapse.war *
```

3. Copy `Synapse.war` to Tomcat's `webapps` directory

### Step 9: Configure Tomcat

1. **Set Tomcat Port** (if 8080 is already in use):
   - Open `TOMCAT_HOME/conf/server.xml`
   - Find `<Connector port="8080"` and change to another port (e.g., 8081)

2. **Start Tomcat**:
   - Windows: Run `TOMCAT_HOME/bin/startup.bat`
   - Linux/Mac: Run `TOMCAT_HOME/bin/startup.sh`

3. **Verify Tomcat is running**:
   - Open browser and go to `http://localhost:8080`
   - You should see Tomcat welcome page

### Step 10: Deploy Application

#### Method 1: Manual Deployment
1. Copy the entire `Synapse` folder (from src/main/webapp) to `TOMCAT_HOME/webapps/`
2. Restart Tomcat

#### Method 2: WAR Deployment
1. Copy `Synapse.war` to `TOMCAT_HOME/webapps/`
2. Tomcat will automatically extract it
3. Restart Tomcat

#### Method 3: IDE Deployment
- If using Eclipse/IntelliJ, simply click "Run" and select Tomcat server

### Step 11: Access the Application

1. Open your web browser
2. Navigate to: `http://localhost:8080/Synapse/`
3. You should see the Synapse home page

## Application URLs

- **Home Page**: http://localhost:8080/Synapse/
- **Sign Up**: http://localhost:8080/Synapse/signup.html
- **Login**: http://localhost:8080/Synapse/login.html
- **Dashboard**: http://localhost:8080/Synapse/dashboard.html (requires login)

## Usage Guide

### 1. Create an Account
- Click "Sign Up" on the home page
- Fill in the registration form
- Submit to create your account

### 2. Login
- Use your username and password to login
- You'll be redirected to the dashboard

### 3. Take Tests
- From the dashboard, select any test to begin:
  - **Depression Test**: 9 questions (PHQ-9)
  - **Anxiety Test**: 7 questions (GAD-7)
  - **Stress Test**: 10 questions (PSS)
  - **Personality Test**: 50 questions (Big Five)

### 4. Complete All Tests
- Tests should be completed in sequence
- After completing all tests, the system generates comprehensive analysis

### 5. View Results
- Access your results from the dashboard
- View detailed scores, personality profile, and personalized recommendations
- Print your report for reference

## Troubleshooting

### Issue: Cannot connect to database
**Solution**: 
- Verify MySQL is running: `mysql -u root -p`
- Check `db.properties` has correct credentials
- Ensure database `synapse_db` exists

### Issue: 404 Error when accessing application
**Solution**:
- Verify Tomcat is running
- Check application deployed correctly in `webapps` folder
- Check context path in URL: `http://localhost:8080/Synapse/`

### Issue: ClassNotFoundException for JDBC driver
**Solution**:
- Ensure `mysql-connector-j-8.x.x.jar` is in `WEB-INF/lib/`
- Restart Tomcat after adding JAR files

### Issue: Compilation errors
**Solution**:
- Verify JDK version is 17 or later: `java -version`
- Check all Java files are in correct packages
- Ensure all required JAR files are in classpath

### Issue: Login not working
**Solution**:
- Check browser console for errors (F12)
- Verify servlet mappings in `web.xml`
- Check Tomcat logs: `TOMCAT_HOME/logs/catalina.out`

### Issue: Tests not submitting
**Solution**:
- Ensure all questions are answered
- Check network tab in browser developer tools
- Verify servlets are mapped correctly

## File Checklist

Ensure you have all these files in the correct locations:

### Java Files (src/main/java/com/synapse/)
- ✓ dao/DatabaseConnection.java
- ✓ dao/UserDAO.java
- ✓ dao/TestDAO.java
- ✓ model/User.java
- ✓ model/TestSession.java
- ✓ model/TestResult.java
- ✓ servlet/LoginServlet.java
- ✓ servlet/SignupServlet.java
- ✓ servlet/LogoutServlet.java
- ✓ servlet/TestServlet.java
- ✓ servlet/ResultServlet.java
- ✓ servlet/DashboardServlet.java
- ✓ util/PasswordUtil.java
- ✓ util/AIAnalyzer.java

### Configuration Files
- ✓ src/main/resources/db.properties
- ✓ src/main/webapp/WEB-INF/web.xml

### Frontend Files (src/main/webapp/)
- ✓ index.html
- ✓ login.html
- ✓ signup.html
- ✓ dashboard.html
- ✓ test-depression.html
- ✓ test-anxiety.html
- ✓ test-stress.html
- ✓ test-personality.html
- ✓ results.html
- ✓ css/style.css
- ✓ js/app.js

### Library Files (src/main/webapp/WEB-INF/lib/)
- ✓ mysql-connector-j-8.x.x.jar
- ✓ gson-2.10.1.jar

## Database Tables Verification

Run this query to verify all tables are created:

```sql
USE synapse_db;
SHOW TABLES;
```

You should see:
- users
- test_sessions
- depression_responses
- anxiety_responses
- stress_responses
- personality_responses
- test_results

## Testing the Application

### Test User Creation
1. Go to signup page
2. Create a test account:
   - Username: testuser
   - Email: test@example.com
   - Password: test123

### Test Complete Flow
1. Login with test account
2. Take all four tests in sequence
3. View results on dashboard
4. Check database to verify data storage:

```sql
SELECT * FROM users WHERE username = 'testuser';
SELECT * FROM test_sessions WHERE user_id = 1;
SELECT * FROM test_results WHERE session_id = 1;
```

## Security Notes

⚠️ **Important**: This is a development version. For production deployment:

1. **Use HTTPS**: Configure SSL/TLS certificates
2. **Stronger Password Hashing**: Implement bcrypt or Argon2
3. **SQL Injection Prevention**: Use PreparedStatements (already implemented)
4. **Session Management**: Configure secure session cookies
5. **Input Validation**: Add server-side validation for all inputs
6. **CORS**: Configure Cross-Origin Resource Sharing properly
7. **Rate Limiting**: Implement to prevent abuse

## Architecture Overview

```
Frontend (HTML/CSS/JS)
        ↓
    Servlets
        ↓
      DAO Layer
        ↓
   JDBC Connection
        ↓
   MySQL Database
```

## Technology Stack

- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Backend**: Java Servlets
- **Server**: Apache Tomcat 10.x
- **Database**: MySQL 8.x
- **JDBC Driver**: MySQL Connector/J
- **JSON Processing**: Gson

## Support & Contribution

For issues or questions:
1. Check the troubleshooting section
2. Review Tomcat logs in `TOMCAT_HOME/logs/`
3. Check browser console for frontend errors
4. Verify MySQL logs for database issues

## License

This project is for educational purposes.

## Acknowledgments

- PHQ-9 Depression Scale
- GAD-7 Anxiety Scale
- Perceived Stress Scale (PSS)
- Big Five Personality Traits Model

---

**Note**: This application is for informational and educational purposes only. It is not a substitute for professional medical advice, diagnosis, or treatment. Always seek the advice of qualified health providers with any questions regarding mental health conditions.









