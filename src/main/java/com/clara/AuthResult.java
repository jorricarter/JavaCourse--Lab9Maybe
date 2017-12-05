package com.clara;

/**
 * Created by clara on 11/14/17.
 */
public class AuthResult {
    
    AuthResult(String username, Exception error) {
        this.error = error;
        this.username = username;
    }
    
    Exception error;
    String username;
}
