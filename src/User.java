// CPS406 Group 2
//ATM Banking System

import java.util.ArrayList;
import java.util.Objects;

// This class defines an ATM user, keeping track of their name, password, and account id, and accounts

public class User implements Comparable<User>{

    private String name;
    private String pin;
    private String id;
    private String cardNumber;
    //generate id from customer id for savings and id+1 for chequeing? ask zain
    private Account chequingAccount = new Account();
    private Account savingsAccount = new Account();
    private ArrayList<Transaction> userTransactions = new ArrayList<Transaction>();

    public User(String name, String pin, String id, String cardNumber, String chequingId, String savingsId){
        this.name = name;
        this.pin = pin; //4 char long like irl?
        this.id = id;
        this.cardNumber = cardNumber;
        chequingAccount = new Account(chequingId, "Chequing", 0.00);
        savingsAccount = new Account(savingsId, "Savings", 0.00);
    }

    //returns the name
    public String getName(){
        return name;
    }
    //returns the password
    public String getPin(){
        return pin;
    }
    //returns the userId
    public String getId(){
        return id;
    }
    //returns the user's card number
    public String getCardNumber(){
        return cardNumber;
    }
    //returns the user's chequing account
    public Account getChequingAccount(){
        return chequingAccount;
    }
    //returns the user's savings account
    public Account getSavingsAccount(){
        return savingsAccount;
    }
    //returns the arraylist of a users transactions
    public ArrayList<Transaction> getTransactions(){
        return userTransactions;
    }
    //adds a passed transaction to the users list
    public void addTransaction(Transaction t){
        userTransactions.add(t);
    }
    //checks if the password entered is valid
    public boolean verifyPin(String enteredPin){
        return (enteredPin.equalsIgnoreCase(pin));
    }
    //compares the two names
    public int compareTo(User otherUser){
        String otherName = otherUser.getName();
        return name.compareTo(otherName);
    }
    //checks if two users are equal to each other
    public boolean equals(Object other){
        if (!Objects.nonNull(other)){
            return false;
        }
        User otherUser = (User) other;
        return this.id.equals(otherUser.id);
    }

    public void print(){
        System.out.print("\n" + name + "\t" + id + "\t\t" + cardNumber);
    }
}
