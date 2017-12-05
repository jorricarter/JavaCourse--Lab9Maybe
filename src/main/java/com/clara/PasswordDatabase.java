package com.clara;


import java.sql.*;

public class PasswordDatabase {

    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/users";

    private static final String USER = "jorri";        // TODO change to your own username
    private static final String PASS = "pszih.";      //TODO change to your own password, or read from an enviroment variable

    private static final String PASSWORD_TABLE = "passwords";
    private static final String USERNAME_COL = "username";
    private static final String LOGIN_COL = "login";
    private static final String PASSWORD_COL = "password";

    public PasswordDatabase()  {

        try {
            String Driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(Driver);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No database drivers found. Quitting");
            System.exit(-1);
        }
    }

    public AuthResult authenticateUser(String login, String password) {

        //Note the allowMultiQueries=true parameter. This is needed to allow more than one query in an executeQuery statement
        //Can be very useful... but also permits abuse.

        try ( Connection connection =
                      DriverManager.getConnection(DB_CONNECTION_URL + "?allowMultiQueries=true", USER, PASS);
                            Statement statement = connection.createStatement()  ) {

            // Don't do this! Don't concatenate strings to create SQL statements!


            String authSQL = "SELECT * FROM passwords WHERE " + LOGIN_COL + " = ? AND " + PASSWORD_COL + "= ?";
            PreparedStatement psInsert = connection.prepareStatement(prepStatInsertSQL);
            psInsert.setString(1, login);
            psInsert.setString(2, password);


            //String authSQL = "SELECT * FROM " + PASSWORD_TABLE + " WHERE " + LOGIN_COL + " = '" + login + "' AND " + PASSWORD_COL + " = '" + password + "'";
            System.out.println(authSQL);   //just for testing!



            ResultSet rs = statement.executeQuery(authSQL);
            //If login is in the database, and password is the password for that account, then user is authenticated.

            String username;
    
            if (rs.next()) {
                username = rs.getString(USERNAME_COL);   //Return the user's name
            } else {
                //No results
                username = null;   //Use null to indicate user with this password not found - user not authenticated
            }
    
            rs.close();
            statement.close();
            connection.close();

            return new AuthResult(username, null);   // Return username and error - no error, so second parameter is null.

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new AuthResult(null, sqle);  // Null for no user, but provide DB error.
        }
    }

}
