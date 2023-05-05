
 // CPS406 Group 2
//ATM Banking System

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class Transaction {

    static DecimalFormat f = new DecimalFormat("##.00");
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("'DATE: 'yyyy/MM/dd ' TIME:' HH:mm:ss");

    private String transactionId;
    private String timeStamp;
    private double amount;
    private String cardNumber;
    private String accountName;
    private String transactionType;
    private String tDestination;
    private String billsGiven;//decide on format ex. "1x 20, 3x 50", something like that
    private String approved;


    public Transaction(String transactionId, String timeStamp, double amount, String cardNumber, String accountName, String transactionType, String tDestination, String billsGiven, String approved){
        this.transactionId = transactionId;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.accountName = accountName;
        this.transactionType = transactionType;
        this.tDestination = tDestination;
        this.billsGiven = billsGiven;
        this.approved = approved;
    }

    public String getTransactionId(){
        return transactionId;
    }

    public String getTimeStamp(){
        return timeStamp;
    }

    public double getAmount(){
        return amount;
    }

    public String cardNumber(){
        return cardNumber;
    }

    public String getAccountName(){
        return accountName;
    }

    public String getTransactionType(){
        return transactionType;
    }

    public String getTDestination(){
        return tDestination;
    }

    public String getBillsGiven(){
        return billsGiven;
    }

    public String getApproved(){
        return approved;
    }

    // 3 different reciept types, deposit, withdrawal (shows bills give) and transfer (shows chequing->savings and vice versa)
    public void printReceipt(){
        System.out.print
        ("\n\nTRANSACTION TYPE:\t\t\t" + transactionType + 
        "\nDATE & TIME:\t\t\t\t" + timeStamp + 
        "\nCARD NUMBER:\t\t\t\tXXXX XXXX XXXX " + cardNumber.substring(cardNumber.length() - 4));
        if (transactionType.equals("Deposit") || transactionType.equals("Withdraw")){
            System.out.print("\nACCOUNT:\t\t\t\t" + accountName);
        }
        else if (transactionType.equals("Transfer")) {
            System.out.print
            ("\nFROM:\t\t\t\t\t" + accountName + 
            "\nTO:\t\t\t\t\t" + tDestination);
        }
        System.out.print
        ("\n---------------------------------------------------------------------------" + 
        "\nREQUESTED AMOUNT:\t\t\t$" + f.format(amount));
        if (transactionType.equals("Withdraw")){
            System.out.print("\nBILLS GIVEN:\t\t\t\t" + billsGiven);
        }
        System.out.print("\nTRANSACTION STATUS:\t\t\t" + approved +
        "\nID:\t" + transactionId + "\n");
    }

    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        Transaction otherTransaction = (Transaction) other;
        return transactionId.equals(otherTransaction.getTransactionId());
    }


}