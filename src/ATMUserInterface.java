// CPS406 Group 2
//ATM Banking System

import java.util.Scanner;
import java.util.Objects;
import java.util.InputMismatchException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public class ATMUserInterface {

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("'DATE: 'yyyy/MM/dd ' TIME:' HH:mm");

    //main (give option for sysAdmin access or user access) - send user to sysAdmin or user overall loop after login is confirmed
    public static void main(String[] args) throws IOException, InterruptedException { //done
        ATM TMUatm = new ATM(); //create our ATM object
        Scanner sc = new Scanner(System.in);
        int selection = 0;
        do{
            try {
                sc = new Scanner(System.in);
                clearScreen();
                printHeader(); 
                System.out.print("\n\n\t\t\t**WELCOME TO THE OFFICIAL TORONTO METROPOLITAN UNIVERSITY ATM**\n");
                delay(1);
                System.out.print("\n\nPlease select:\n\t(1) User Access\n\t(2) Admin Access\n\t(3) End Session\n\nSelection: ");
                selection = sc.nextInt();
                
        
                switch (selection) {
                    case 1: User user=userVerify(TMUatm); if(Objects.nonNull(user)){userATM(TMUatm,user);}; break; //if login is correct sends atm and user object to userATM
                    case 2: SysAdmin admin = adminVerify(TMUatm); if(Objects.nonNull(admin)){sysAdminAtm(TMUatm,admin);}; break; //if login is correct sends atm and admin object to sysAdminATM
                    case 3: System.out.print("\nThank you for using TMU ATM."); delay(1); sc.nextLine(); System.out.print("\nGoodbye!"); System.out.print("\n\nPress enter to continue."); sc.nextLine(); clearScreen();break;//exit
                    default: System.out.print("\nPlease enter a valid selection."); delay(2); System.out.print("\n\nPress enter to continue."); sc.nextLine(); clearScreen(); //none of the above
                }

            } catch (InterruptedException | InputMismatchException e) {
                if (e instanceof InterruptedException){
                    e.printStackTrace();
                }
                System.out.print("\nUnable to read input.");
                waitForInput();
                delay(1);
            }

        } while(selection != 3);
        
        sc.close();

    }

    public static User userVerify(ATM atm) throws IOException, InterruptedException{ //finished
        Scanner sc = new Scanner(System.in);
        User user = null;
        String cardNumber;
        String pin;
        int strikes = 2; //amount of attempts user has to login correctly
        do {
            clearScreen();
            printHeader();
            System.out.print("\n\n\t\t\t\t\t\t    *USER LOGIN*\n\nEnter 'exit' at any time to return to main menu.");
            System.out.print("\n\nEnter card number: ");
            cardNumber = sc.nextLine();
            System.out.print("Enter 4-digit pin number: ");
            pin = sc.nextLine();
            if(!cardNumber.equalsIgnoreCase("exit") && !pin.equalsIgnoreCase("exit")){
                user = atm.userLogin(cardNumber, pin); //check if entered info is correct
            } else {
                System.out.print("\nReturning to main menu.");
                strikes = 0;
                delay(2);
            }

            if (!Objects.nonNull(user) && !cardNumber.equalsIgnoreCase("exit") && !pin.equalsIgnoreCase("exit")){
                System.out.print(strikes + " attempts remaining until auto-exit.\n\n");
                strikes--; //decrement strikes
                delay(2);
            }
            clearScreen();
        } while (strikes >= 0 && !Objects.nonNull(user) && !cardNumber.equalsIgnoreCase("exit") && !pin.equalsIgnoreCase("exit")); //runs until user is out of strikes or user != null

        return(user);
    }

    public static SysAdmin adminVerify(ATM atm) throws IOException, InterruptedException{
        Scanner sc = new Scanner(System.in);
        SysAdmin admin = null;
        int strikes = 2;
        String password;

        do {
            clearScreen();
            printHeader();
            System.out.print("\n\n\t\t\t\t\t\t    *ADMIN LOGIN*\n\nEnter 'exit' to return to main menu.");
            System.out.print("\n\nEnter admin password: ");
            password = sc.nextLine();
            if (!atm.sysAdminLogin(password)){
                System.out.print(strikes + " attempts remaining until auto-exit.");
                strikes --;
                delay(2);
            } else if (password.equalsIgnoreCase("exit")){
                System.out.print("\nReturning to main menu.");
                strikes = 0;
                delay(2);
            } else{
                admin = new SysAdmin();
            }
        } while (strikes >= 0 && !Objects.nonNull(admin) && !password.equalsIgnoreCase("exit"));

        return(admin);
    }

    //sysAdmin loop (loop until exit condition met) - addCash, viewTransactions, addUser
    public static void sysAdminAtm(ATM atm, SysAdmin admin) throws IOException, InterruptedException{
        int selection = 0;
        Scanner sc = new Scanner(System.in);
        do {
            try {        
                clearScreen();
                printHeader();
                System.out.print("\n\n\t\t\t\t\t*WELCOME, SYSTEM ADMINISTRATOR.*\n\n\t(1) View Cash in ATM\n\t(2) Add Cash to ATM\n\t(3) View Users\n\t(4) Add User\n\t(5) Remove User\n\t(6) View all Transactions\n\t(7) Log Out\n\nSelection: ");
                selection = sc.nextInt();
                clearScreen();

                switch (selection) {
                    case 1: printHeader(); System.out.print("\n\n\t\t\t\t\t\t*VIEW CASH IN ATM*\n\n"); atm.viewCash(); waitForInput(); break;
                    case 2: 
                    int billQuantity = -1;
                    int billType = -1;
                    do{
                        printHeader(); System.out.print("\n\n\t\t\t\t\t\t*ADD CASH TO ATM*\n\n");
                        System.out.print("\nEnter an appropriate bill type.\n\t(1) $5 Bills\n\t(2) $20 Bills\n\t(3) $50 Bills\n\nBill Type: ");
                        billType = sc.nextInt();
                        System.out.print("\nEnter an appropriate bill quantity: ");
                        billQuantity = sc.nextInt();
                        if (billType != 1 && billType != 2 && billType != 3 || billQuantity < 0 || billQuantity > 1000){
                            System.out.print("Bill type/quantity invalid. Please try again.\n");
                            delay(2);
                            clearScreen();
                        }
                    } while (billType != 1 && billType != 2 && billType != 3 || billQuantity < 0 || billQuantity > 1000);
                    atm.addCash(billType, billQuantity); waitForInput(); break;
                    case 3: printHeader(); System.out.print("\n\n\t\t\t\t\t\t*VIEW REGISTERED USERS*\n\n"); atm.viewUsers(); waitForInput(); break;
                    case 4:
                    String userName, cardNumber, pinNumber;
                    do{
                        clearScreen(); printHeader(); sc.nextLine(); System.out.print("\n\n\t\t\t\t\t\t*ADD USER*\n\n");
                        System.out.print("\nEnter the new user's name: ");
                        userName = sc.nextLine();
                        System.out.print("\nEnter their cardNumber (12 digits 0-9, include spaces): ");
                        cardNumber = sc.nextLine();
                        System.out.print("\nEnter their pin number (4 digit numeric code): ");
                        pinNumber = sc.next();
                        if (userName.length() < 1 || cardNumber.length() < 1 || pinNumber.length() < 1){
                            System.out.print("Bill type/quantity invalid. Please try again.\n");
                            delay(2);
                            clearScreen();
                        }
                    } while (userName.length() < 1 || cardNumber.length() < 1 || pinNumber.length() < 1);
                    atm.addUser(userName, cardNumber, pinNumber);
                    waitForInput(); break;
                    case 5:
                    sc.nextLine();
                    String targetId;
                    do{
                        clearScreen();
                        printHeader();
                        System.out.print("\n\n\t\t\t\t\t\t\t*REMOVE USER*\n\n");
                        atm.viewUsers();
                        System.out.print("\n\nEnter the user's ID: ");
                        targetId = sc.nextLine();
                    } while (targetId.length() < 1);
                    atm.removeUser(targetId);
                    waitForInput(); break;
                    case 6: System.out.print("\n\n\t\t\t\t\t*TRANSACTIONS*\n\n"); atm.viewTransactions(); waitForInput();; break;
                    case 7: System.out.print("\n\nLogging out. Goodbye!"); waitForInput();; break;
                    default: System.out.print("\n\nPlease enter a valid selection."); delay(2);
                }

                } catch (InputMismatchException e){
                    System.out.print("Please enter the appropriate input and try again.");
                    delay(1);
                    waitForInput();
                    sc.nextLine();
                }

        } while (selection != 7);    
    }

    //user loop (create a user object so all user methods can be accessed directly loop until exit condition met) - deposit, withdraw, transfer, etc.
    public static void userATM(ATM atm, User user) throws IOException, InterruptedException{
        Scanner sc = new Scanner(System.in);
        int selection = 0;
        do{
            try {
                clearScreen();
                printHeader();
                System.out.print("\n\n\t\t\t\t\t\t*USER OPERATIONS*\n\nWelcome " + user.getName() + ".\n\n\t(1) View Balance\n\t(2) Deposit Funds\n\t(3) Withdraw Funds\n\t(4) Transfer Funds\n\t(5) View Transactions\n\t(6) Log Out\n\nSelection: ");
                selection = sc.nextInt();
                clearScreen();
                printHeader();

                switch (selection) {
                    case 1:
                        int selection2;
                        do {
                            clearScreen();
                            printHeader();
                            System.out.print("\n\n\t\t\t\t\t\t*VIEW BALANCE*\n\n\t(1) View Chequing Balance\n\t(2) View Savings Balance\n\t(3) View Both Balances\n\nSelection: ");
                            selection2 = sc.nextInt();
                            if(selection2 != 1 && selection2 != 2 && selection2 != 3){
                                System.out.print("\nPlease enter a valid selection.");
                                delay(1);
                            }
                            clearScreen();
                        } while(selection2 != 1 && selection2 != 2 && selection2 != 3);
                        printHeader();
                        atm.viewBalance(user, selection2);
                        waitForInput();break; //view balance
                    case 2: //deposit funds
                    
                        double amount;
                        int account;
                        do{
                            clearScreen();
                            printHeader();
                            System.out.print("\n\n\t\t\t\t\t\t\t*DEPOSIT*\n\n");
                            atm.viewBalance(user, 3);
                            System.out.print("\n\nEnter amount to deposit: ");
                            amount = sc.nextDouble();
                            System.out.print("\nEnter account to deposit to:\n(1) Chequing\n(2) Savings\n\nSelection: ");
                            account = sc.nextInt();
                            if(account != 1 && account != 2 || amount <= 0){
                                System.out.print("\nPlease enter appropriate input.");
                                delay(1);
                            }
                        } while (account != 1 && account != 2 || amount <= 0);
                        atm.deposit(user, account, amount);
                        delay(1);
                        waitForInput(); break;


                    case 3: //withdraw
                    
                        double amount2;
                        int account2;
                        do{
                            clearScreen();
                            printHeader();
                            System.out.print("\n\n\t\t\t\t\t\t\t*WITHDRAW*\n\n");
                            atm.viewBalance(user, 3);
                            System.out.print("\n\nEnter amount to withdraw: ");
                            amount2 = sc.nextDouble();
                            System.out.print("\nEnter account to withdraw from:\n(1) Chequing\n(2) Savings\n\nSelection: ");
                            account2 = sc.nextInt();
                            if(account2 != 1 && account2 != 2 || amount2 <= 0){
                                System.out.print("\nPlease enter appropriate input.");
                                delay(1);
                            }
                        } while (account2 != 1 && account2 != 2 || amount2 <= 0);
                        atm.withdraw(user, account2, amount2);
                        delay(1);
                        waitForInput(); break;

                    case 4: //transfer funds
                    
                        double amount3;
                        int account3;
                        int targetAccount;
                        do{
                            clearScreen();
                            printHeader();
                            System.out.print("\n\n\t\t\t\t\t\t\t*TRANSFER*\n\n");
                            atm.viewBalance(user, 3);
                            System.out.print("\n\nEnter amount to transfer: ");
                            amount3 = sc.nextDouble();
                            System.out.print("\nEnter source account:\n(1) Chequing\n(2) Savings\n\nSelection: ");
                            account3 = sc.nextInt();
                            System.out.print("\nEnter destination account:\n(1) Chequing\n(2) Savings\n\nSelection: ");
                            targetAccount = sc.nextInt();
                            if(account3 != 1 && account3 != 2 && targetAccount != 1 && targetAccount != 2 || amount3 <= 0){
                                System.out.print("\nPlease enter appropriate input.");
                                delay(1);
                            }
                        } while (account3 != 1 && account3 != 2 && targetAccount != 1 && targetAccount != 2 || amount3 <= 0);
                        atm.transfer(user, account3, targetAccount, amount3);
                        waitForInput(); break;

                    case 5: System.out.print("\n\n\t\t\t\t\t\t\t*TRANSACTIONS*\n\n"); atm.viewUserTransactions(user); waitForInput(); break;
                    case 6: System.out.print("\n\nLogging out. Goodbye!"); waitForInput(); break;
                    default: System.out.print("\n\nPlease enter a valid selection."); waitForInput(); //none of the above
                }

        } catch (InputMismatchException ex){
            System.out.print("Please enter the appropriate input and try again.");
            delay(1);
            waitForInput();
            sc.nextLine();
        }

        } while (selection != 6);

    }

    public static void delay(int i){
        try{
            switch (i){
                case 1: Thread.sleep(500); break;
                case 2: Thread.sleep(1000); break;
                case 3: Thread.sleep(2000); break;
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void clearScreen() throws IOException, InterruptedException {  //clears screen
        //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.print("\033\143");
    }

    public static void waitForInput(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\n\nPress enter to continue."); sc.nextLine();
    }
    
    public static void printHeader(){
        System.out.println("\n\n\t\t\t\t\t" + dtf.format(LocalDateTime.now()));
        System.out.print("\n\t\t\t_|_|_|_|_| _|      _|  _|    _|        _|_|    _|_|_|_|_|  _|      _|  \n\t\t\t   _|      _|_|  _|_|  _|    _|      _|    _|      _|      _|_|  _|_|  \n\t\t\t   _|      _|  _|  _|  _|    _|      _|_|_|_|      _|      _|  _|  _|  \n\t\t\t   _|      _|      _|  _|    _|      _|    _|      _|      _|      _|  \n\t\t\t   _|      _|      _|    _|_|        _|    _|      _|      _|      _|");  
    }

}