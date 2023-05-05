// CPS406 Group 2
//ATM Banking System

import java.util.ArrayList;
import java.util.Random;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ATM {

    Random rand = new Random();
    static DecimalFormat f = new DecimalFormat("##.00");
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("'DATE: 'yyyy/MM/dd ' TIME:' HH:mm:ss");

    public ArrayList<User> userList = new ArrayList<User>(); //list of all users
    private ArrayList<Transaction> transactionList = new ArrayList<Transaction>(); //list of all transactions
    public int[] systemBills = {200, 200, 200}; //5, 20, 50 FINAL

    private final int withdrawLimit = 500;
    private final int depositLimit = 500;

    //base values for generated numbers
    private String baseUId = "100000"; 
    private String baseTId = "001";
    private String baseAId = "32942943";

    public ATM(){
        String id = generateAccountId();
        userList.add(new User("Ty Hamilton", "1234", generateUserId(), "1234 1234 5678 5678", id, "00" + Integer.toString(Integer.parseInt(id + 1))));
        id = generateAccountId();
        userList.add(new User("Zain Bharwani", "5678", generateUserId(), "1212 2121 1212 2121", id, "00" + Integer.toString(Integer.parseInt(id + 1))));
        id = generateAccountId();
        userList.add(new User("Timo Kwok", "3434", generateUserId(), "1221 2112 2112 1221", id, "00" + Integer.toString(Integer.parseInt(id + 1))));
        id = generateAccountId();
        userList.add(new User("Sofia Pham", "2121", generateUserId(), "4321 4321 4321 4321", id, "00" + Integer.toString(Integer.parseInt(id + 1))));
        id = generateAccountId();
        userList.add(new User("Denesh Persaud", "4232", generateUserId(), "1010 0101 1010 0101", id, "00" + Integer.toString(Integer.parseInt(id + 1))));
        id = generateAccountId();
        userList.add(new User("Danny Rego", "4646", generateUserId(), "0909 9090 0909 9090", id, "00" + Integer.toString(Integer.parseInt(id + 1))));
        }

    public String generateUserId(){ //generates a new user id for each new user
        int tempId = Integer.parseInt(baseUId);
        tempId += rand.nextInt(100);
        return(Integer.toString(tempId));
    }
    public String generateTransactionId(){ //generates a transaction id for each transaction
        int tempId = Integer.parseInt(baseTId);
        tempId += 1;
        baseTId = Integer.toString(Integer.parseInt(baseTId) + 1);
        return("00" + Integer.toString(tempId));
    }
    public String generateAccountId(){ //generates 2 account ids for each new user, one for chequing and one for savings
        int tempId = Integer.parseInt(baseAId);
        tempId += rand.nextInt(20);
        return(Integer.toString(tempId));
    }

    //ADMIN ONLY METHODS
    //login - enter password (CANNOT ACCESS DIRECTLY) done
    //view cash avaliable in ATM done
    //add cash to atm done
    //view all users done
    //add user done
    //remove user done
    //view all transactions done
    public boolean sysAdminLogin(String password){
        SysAdmin admin = new SysAdmin();
        return (admin.verifyPassword(password));
    }

    public void viewCash(){
        System.out.print("\nThis ATM system currently contains:\n" + getBillRep(systemBills));
    }

    public void addCash(int billType, int billQuantity){
        systemBills[billType - 1] = systemBills[billType - 1] + billQuantity;
        System.out.print("\nBills have been added to the system.");
    }

    public void viewUsers(){ //done
        System.out.print("User Name:\tUser ID:\t\tCard Number:");
        for (User u : userList){
            u.print();
            delay(1);
        }
    }

    public void addUser(String userName, String cardNumber, String pinNumber){
        boolean valid = false;
        if (userName.length() < 2 || cardNumber.length() < 19 || pinNumber.length() != 4) {
        } else{
            valid = true;
        }
        if (valid){
            String accountId = generateAccountId();
            userList.add(new User(userName, pinNumber, generateUserId(), cardNumber, accountId, "00" + Integer.toString(Integer.parseInt(accountId + 1))));
            System.out.print("\nSuccessfully added user " + userName + " to the ATM system.");
            delay(2);
        } else{
            System.out.print("\nError creating user. Please enter valid information and try again.");
            delay(3);
        }
    }

    public void removeUser(String userID){
        User user = null;
        for (User u : userList){
            if (u.getId().equals(userID)) {
                user = u;
            }
        }
        if (Objects.nonNull(user)){
            userList.remove(user);
            System.out.print("\nUser " + user.getName() + " with ID " + user.getId() + " has been removed from the system.");
            delay(2);
        } else {
            System.out.print("\n" + userID + " is not a valid user id.");
            delay(2);
        }
    }

    public void viewTransactions(){
        if (!transactionList.isEmpty()){
            for (Transaction t : transactionList){
                t.printReceipt();
                delay(1);
            }
        } else {
            System.out.print("There have been no transactions at this machine.");
        }
    }

    //USER ONLY METHODS
    //login - enter card, enter pin (USER CANNOT ACCESS DIRECTLY) done
    //view balance done
    //deposit done
    //withdraw
    //transfer done
    //view all user transactions done
    //create withdraw combo (USER CANNOT ACCESS DIRECTLY)

    public User userLogin(String cardNumber, String pin) throws UserCardNotFoundException, UserPinIncorrectException { //done
        boolean exists = false;
        User user = null;
        for(User u : userList){ //checks each pin of currently registered users
            if(u.getCardNumber().equals(cardNumber)){
                exists = true;
                user = u; //if the pin exists sets the user object to that user
            }
        }
        try{
            if (exists == true){ //if the card number exists, check pin validity
                if (user.verifyPin(pin) == true){
                    return(user);
                }
                else {
                    throw new UserPinIncorrectException("The pin entered is incorrect for card " + cardNumber + ".");
                }
            } else{
                throw new UserCardNotFoundException("The card entered is invalid.");
                }
        } catch (UserCardNotFoundException | UserPinIncorrectException e){
            System.out.println(e.getMessage());
        }
        return user;
        }

    public void viewBalance(User thisUser, int account){ //done
        double chequingBalance = thisUser.getChequingAccount().getBalance();
        double savingsBalance = thisUser.getSavingsAccount().getBalance();

        if (account == 1){
            if (chequingBalance == 0.00){
                System.out.print("\n\n\nChequing Account Balance: $0.00");//view chequing account belonging to user
            } else {
                System.out.print("\n\n\nChequing Account Balance: $" + f.format(thisUser.getChequingAccount().getBalance()));//view chequing account belonging to user
            }            
        } else if (account == 2){
            if (savingsBalance == 0.00){
                System.out.print("\n\n\nSavings Account Balance: $0.00");//view chequing account belonging to user
            } else {
                System.out.print("\n\n\nSavings Account Balance: $" + f.format(savingsBalance));//view chequing account belonging to user
            }       
        } else if (account == 3){
            if (chequingBalance == 0.00 && savingsBalance == 0.00){
                System.out.print("\n\n\nChequing Account Balance: $0.00");//view chequing account belonging to user
                System.out.print("\nSavings Account Balance: $0.00");//view chequing account belonging to user
            } else {
                System.out.print("\n\n\nChequing Account Balance: $" + f.format(chequingBalance));//view both
                System.out.print("\nSavings Account Balance: $" + f.format(savingsBalance));      
            }
        }
        delay(1);
    }

    public void deposit(User thisUser, int account, double amount){ //done
        try{   
            if (amount > depositLimit){
                throw new DepositLimitExceeded("Amount exceeds deposit limit.");
            }
            if (account == 1){
                thisUser.getChequingAccount().deposit(amount); //deposit in chequing account belonging to user
            } else if (account == 2){
                thisUser.getSavingsAccount().deposit(amount); //deposit in savings account belonging to user
            }
            //generate new transaction object with the specifications
            Transaction t = new Transaction(generateTransactionId(), dtf.format(LocalDateTime.now()), amount, thisUser.getCardNumber(), "SAVINGS", "DEPOSIT", null, null, "APPROVED");
            transactionList.add(t); //add transaction to list of all transactions
            thisUser.addTransaction(t); //add transaction to list of user specific transactions
            System.out.println("\nAmount successfully deposited.");
            delay(2);

        } catch (DepositLimitExceeded e){
            System.out.println(e.getMessage());
            delay(2);
        }
    }

    public void withdraw(User thisUser, int account, double amount, int[] combination) {
        if (account == 1){
            thisUser.getChequingAccount().withdraw(amount);
            Transaction t = new Transaction(generateTransactionId(), dtf.format(LocalDateTime.now()), amount, thisUser.getCardNumber(), "CHEQUING", "WITHDRAW", null, "combination", "APPROVED");
            transactionList.add(t);
            thisUser.addTransaction(t);
            systemBills[0] -= combination[0];
            systemBills[1] -= combination[1];
            systemBills[2] -= combination[2];
        } else if (account == 2){
            thisUser.getSavingsAccount().withdraw(amount);
            Transaction t = new Transaction(generateTransactionId(), dtf.format(LocalDateTime.now()), amount, thisUser.getCardNumber(), "SAVINGS", "WITHDRAW", null, "combination", "APPROVED");
            transactionList.add(t);
            thisUser.addTransaction(t);
            systemBills[0] -= combination[0];
            systemBills[1] -= combination[1];
            systemBills[2] -= combination[2];
        }
    }

    public void withdraw(User thisUser, int account, double amount) throws InsufficientFundsException, MachineOutOfBillsException {
        //check balance validity
        Scanner sc = new Scanner(System.in);
        int choice;
        String valid = "APPROVED";
        try{
            if (amount > thisUser.getChequingAccount().getBalance() && account == 1){
                throw new InsufficientFundsException("\nWithdrawal exceeds funds in savings account.");
            }
            else if (amount > thisUser.getSavingsAccount().getBalance() && account == 2){
                throw new InsufficientFundsException("\nWithdrawal exceeds funds in savings account.");
            } else if (amount > systemBills[0] * 5){
                throw new MachineOutOfBillsException("\nThis ATM is out of bills.\nPlease contact a supervisor, and we are sorry for the inconvenience.");
            } else if (amount < 20 && amount % 5 != 0 || amount > withdrawLimit){
                throw new InvalidWithdrawlAmount("\nThis amount is not valid for withdrawl.");
            }

            ArrayList<int[]> billCombos = getBills((int)amount);
           
            System.out.print("\nPlease choose the desired bill combination:\n");
            do{
                for (int i = 0; i < billCombos.size(); i++){
                    System.out.print("\n\t(" + (i + 1) + ") " + getBillRep(billCombos.get(i)));
                }
                System.out.print("\n\t(" + (billCombos.size() + 1) + ") Cancel Transaction\n\nSelection: ");
                choice = sc.nextInt();
                if (choice != 1 && choice != 2 && choice != 3 || choice > billCombos.size()){
                    System.out.print("\n\tPlease enter a valid selection.");
                }
                if (choice == 4){
                    throw new WithdrawLimitExceeded();
                }
            } while (choice != 1 && choice != 2 && choice != 3 || choice > billCombos.size());

            if (account == 1){
                thisUser.getChequingAccount().withdraw(amount);
                Transaction t = new Transaction(generateTransactionId(), dtf.format(LocalDateTime.now()), amount, thisUser.getCardNumber(), "CHEQUING", "WITHDRAW", null, getBillRep(billCombos.get(choice - 1)), valid);
                transactionList.add(t);
                thisUser.addTransaction(t);
            } else if (account == 2){
                thisUser.getSavingsAccount().withdraw(amount);
                Transaction t = new Transaction(generateTransactionId(), dtf.format(LocalDateTime.now()), amount, thisUser.getCardNumber(), "SAVINGS", "WITHDRAW", null, getBillRep(billCombos.get(choice - 1)), valid);
                transactionList.add(t);
                thisUser.addTransaction(t);
            }
            System.out.print("\nThe withdrawal was successful.");
            delay(2);
        } catch (WithdrawLimitExceeded | InsufficientFundsException | InputMismatchException e){
            System.out.print(e.getMessage());
            System.out.println("\nCould not properly complete transaction. Please try again.");
            delay(3);
        }
    }

    public void transfer(User thisUser, int account, int destinationAccount, double amount) throws InsufficientFundsException {
        //CHECK IF AMOUNT IS VALID
        String valid = "APPROVED";
        Transaction t = null;
        try{
            //check balance validity
            if (amount <= 0 || account == destinationAccount || (account == 1 && amount > thisUser.getChequingAccount().getBalance()) || (account == 2 && amount > thisUser.getSavingsAccount().getBalance())){
                throw new InvalidTransferException("\n\nError completing transfer. \nPlease enter valid information and try again.");
            }
            if (account == 1 && destinationAccount == 2){
                thisUser.getChequingAccount().withdraw(amount); //withdraw from chequing account belonging to user
                thisUser.getSavingsAccount().deposit(amount); //deposit in savings account belonging to user
                t = new Transaction(generateTransactionId(), dtf.format(LocalDateTime.now()), amount, thisUser.getCardNumber(), "CHEQUING", "TRANSFER", "SAVINGS", null, valid);
            } else if (account == 2 && destinationAccount == 1){
                thisUser.getSavingsAccount().withdraw(amount); //withdraw from savings account belonging to user
                thisUser.getChequingAccount().deposit(amount); //deposit in chequing account belonging to user
                t = new Transaction(generateTransactionId(), dtf.format(LocalDateTime.now()), amount, thisUser.getCardNumber(), "SAVINGS", "TRANSFER", "CHEQUING", null, valid);
            }
            if (Objects.nonNull(t)){
                transactionList.add(t); //add transaction to list of all transactions
                thisUser.addTransaction(t); //add transaction to list of user specific transactions
            } else{
                System.out.print("\nThe transaction was not successful.\nPlease try again.\n");
                delay(3);
            }
        } catch (InvalidTransferException e){
            System.out.print(e.getMessage());
            delay(3);
        }
    }

    public void viewUserTransactions(User thisUser){
        ArrayList<Transaction> userTransactions = thisUser.getTransactions();
        if (!userTransactions.isEmpty()){
            for (Transaction t : userTransactions){
                t.printReceipt();
                delay(1);
            }
            System.out.print("\nAll transactions to date for this account have been printed.");
        } else{
            System.out.print("\nThere are no transactions recorded under this account.");
            delay(2);
        }
    }

    public ArrayList<int[]> getBills(int amount) {
        ArrayList<int[]> billCombos = new ArrayList<int[]>();
        // First combo: even split. If possible, get an even amount of all bills. If not, as close as possible.
        // Second combo: prefer 20's. If possible, use only $20 bills. If amount is not divisible by 20, use some 5 or 50. If not enough 20's, use all of them then 50's and 5's for the rest 
        int[] secondCombo = {0, 0, 0};
        if((amount / 20) <= systemBills[1]) { // add 20's
            secondCombo[1] = (amount / 20);
        } else { secondCombo[1] = systemBills[1];}
        if((amount - (20 * secondCombo[1])) > 0) { // add 50's
            if(((amount - (20 * secondCombo[1]))/ 50) <= systemBills[2]) {
                secondCombo[2] = ((amount - (20 * secondCombo[1])) / 50);
            } else { secondCombo[2] = systemBills[2];}
        }
        if((amount - (20 * secondCombo[1]) - (50 * secondCombo[2])) > 0) { // add 5's
            if(((amount - (20 * secondCombo[1]) - (50 * secondCombo[2])) / 5) <= systemBills[0]) {
                secondCombo[0] = ((amount - (20 * secondCombo[1]) - (50 * secondCombo[2])) / 5);
            } else { secondCombo[0] = systemBills[0];}
        }
        if((((secondCombo[0] * 5) + (secondCombo[1] * 20) + (secondCombo[2] * 50)) == amount) && !(billCombos.contains(secondCombo))) {
            billCombos.add(secondCombo);
        }
        // Third combo: least bills. Use the least amount of bills possible
        int[] thirdCombo = {0, 0, 0};
        if((amount / 50) <= systemBills[2]) { // add 50's
            thirdCombo[2] = (amount / 50);
        } else { thirdCombo[2] = systemBills[2];}
        if((amount - (50 * thirdCombo[2])) > 0) { // add 20's
            if(((amount - (50 * thirdCombo[2]))/ 20) <= systemBills[1]) {
                thirdCombo[1] = ((amount - (50 * thirdCombo[2])) / 20);
            } else { thirdCombo[1] = systemBills[1];}
        }
        if((amount - (50 * thirdCombo[2]) - (20 * thirdCombo[1])) > 0) { // add 5's
            if(((amount - (50 * thirdCombo[2]) - (20 * thirdCombo[1])) / 5) <= systemBills[0]) {
                thirdCombo[0] = ((amount - (50 * thirdCombo[2]) - (20 * thirdCombo[1])) / 5);
            } else { thirdCombo[0] = systemBills[0];}
        }  
        if((((thirdCombo[0] * 5) + (thirdCombo[1] * 20) + (thirdCombo[2] * 50)) == amount) && !(billCombos.contains(thirdCombo))) {
            billCombos.add(thirdCombo);
        } 
        return(billCombos);
    }

    public String getBillRep(int[] bills){
        String billStringRep = bills[0] + " X $5 bills : " + bills[1] + " X $20 bills : " + bills[2] + " X $50 bills";
        return billStringRep;
    }

    public void delay(int i){
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

}
//MAKE EXCEPTIONS CLASSES
//INSUFFICIENT FUNDS CLASS - thrown if user attempts to withdraw more than is available in account
class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(){}
    public InsufficientFundsException(String message){
        super(message);
    }
}
//INCORRECT SYSADMIN LOGIN CLASS - thrown if the sysadmin login is incorrect
class IncorrectPasswordException extends RuntimeException{ //not rly used rn
    public IncorrectPasswordException(){}
    public IncorrectPasswordException(String message){
        super(message);
    }
}
//USER CARD NOT FOUND CLASS - thrown if the card does not belong to a current user
class UserCardNotFoundException extends RuntimeException{
    public UserCardNotFoundException(){}
    public UserCardNotFoundException(String message){
        super(message);
    }
}
//INCORRECT PIN CLASS - thrown if pin is not correct after user enters card
class UserPinIncorrectException extends RuntimeException{
    public UserPinIncorrectException(){}
    public UserPinIncorrectException(String message){
        super(message);
    }
}
//OUT OF BILLS CLASS
class MachineOutOfBillsException extends RuntimeException{
    public MachineOutOfBillsException(){}
    public MachineOutOfBillsException(String message){
        super(message);
    }
}
//WITHDRAW LIMIT EXCEEDED CLASS - thrown when a withdrawal amount is too large
class WithdrawLimitExceeded extends RuntimeException{
    public WithdrawLimitExceeded(){}
    public WithdrawLimitExceeded(String message){
        super(message);
    }
}
//DEPOSIT LIMIT EXCEEDED CLASS - thrown when a deposit amount is too large
class DepositLimitExceeded extends RuntimeException{
    public DepositLimitExceeded(){}
    public DepositLimitExceeded(String message){
        super(message);
    }
}
class InvalidWithdrawlAmount extends RuntimeException{
    public InvalidWithdrawlAmount(){}
    public InvalidWithdrawlAmount(String message){
        super(message);
    }
}
class InvalidTransferException extends RuntimeException{
    public InvalidTransferException(){}
    public InvalidTransferException(String message){
        super(message);
    }
}