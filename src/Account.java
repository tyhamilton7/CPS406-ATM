// CPS406 Group 2
//ATM Banking System

import java.text.DecimalFormat;

// This class defines an account, of which each user will get two

public class Account {

    DecimalFormat f = new DecimalFormat("##.00");
    private String id;
    private String name; //Chequing or Savings
    private double balance;

    public Account(){
        this.id = "";
        this.name = "";
        this.balance = 0;
    }

    public Account(String id, String name, double balance){
        this.id = id;
        this.name = name;
        this.balance = balance; //set to 0 on other side
    }

    //returns the account id
    public String getId(){
        return id;
    }
    //returns the account name
    public String getName(){
        return name;
    }
    //returns the account balance
    public double getBalance(){
        return balance;
    }
    
    //deposit
    public void deposit(double amount){
        balance += amount;
    }
    //withdraw
    public void withdraw(double amount){
        balance -= amount;
    }
    //equals
    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        Account otherAccount = (Account) other;
        return this.id.equals(otherAccount.id);
    }

    //prints the account info
    public void printBalance(){
        System.out.print("$" + f.format(balance));
    }
}