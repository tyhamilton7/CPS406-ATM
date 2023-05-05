// CPS406 Group 2
//ATM Banking System

// This class defines an ATM system admin, a class with special privileges and abilities over users

public class SysAdmin {

    private String password = "1234";

    public boolean verifyPassword(String enteredPassword){
        return (enteredPassword.equalsIgnoreCase(password));
    }

    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        return true;
    }

    // not sure what properties the sysAdmin has, maybe just a login? check w zain
    

}
