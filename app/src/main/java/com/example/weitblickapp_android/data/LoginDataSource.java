package com.example.weitblickapp_android.data;

import com.example.weitblickapp_android.data.model.LoggedInUser;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser user =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            if(username.equals("admin") && password.equals("123456")){
                return new Result.Success<>(user);
            }

            else {
                return new Result.Error(new IOException("Error logging in"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
