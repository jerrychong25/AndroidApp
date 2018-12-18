package king.upihc;

/**
 * Created by user on 9/4/2017.
 */
public class UserDetails {

    String UserName;
    String UserChildName;
    String UserEmail;
    String RehabilitationDetails;

    public UserDetails(String InputUserName, String InputUserChildName, String InputUserEmail, String InputRehabilitationDetails) {
        UserName = InputUserName;
        UserChildName = InputUserChildName;
        UserEmail = InputUserEmail;
        RehabilitationDetails = InputRehabilitationDetails;
    }
}
