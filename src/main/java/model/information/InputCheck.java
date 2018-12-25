package model.information;

import javafx.scene.control.Label;

/**
 * Makes sure that the information given by the user is valid
 */
public class InputCheck {
    public static boolean checkUsername(Label errorLabel, String username){
        if(username.isBlank()){
            errorLabel.setText("Please enter a username");
        }else if(username.contains(", ")){
            // ", " is the regex for sending usernames between computers
            errorLabel.setText("Cannot have \", \"");
            return false;
        }
        return true;
    }
}
