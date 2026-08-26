
package com.mycompany.chatapp;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }
    public String getFirstName() { return firstName;}
    public String getLastName() { return lastName;}
    public String getUsername() { return username;}
    public String getCellPhoneNumber() { return cellPhoneNumber;}

    public boolean checkUserName() {
        if (username.contains("_") && username.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPasswordComplexity() {
        if (password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) { hasCapital = true; }
            if (Character.isDigit(c)) { hasNumber = true; }
            if (!Character.isLetterOrDigit(c)) { hasSpecial = true; }
        }
        if (hasCapital && hasNumber && hasSpecial) {
            return true;
        } else {
            return false;
        }
    }
    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber.matches("^\\+[0-9]{10,12}$")) {
            return true;
        } else {
            return false;
        }
    }
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
            return true;
        } else {
            return false;
        }
    }
    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}

public class Message {

    private String messageID;
    private int numMessagesSent;
    private String recipient;
    private String message;
    private String messageHash;

    private static int totalMessagesSent = 0;
    private static ArrayList<Message> sentMessages = new ArrayList<Message>();

    public Message(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.numMessagesSent = totalMessagesSent + 1;

        //random message ID using two random numbers joined together
        Random rand = new Random();
        int num1 = rand.nextInt(90000) + 10000;
        int num2 = rand.nextInt(90000) + 10000;
        this.messageID = num1 + "" + num2;

        this.messageHash = createMessageHash();
    }

    public boolean checkMessageID() {
        if (messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    public String checkRecipientCell() {
        if (recipient.startsWith("+") && recipient.length() <= 13) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {
    
        String first2 = messageID.substring(0, 2);
        String[] words = message.split(" ");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String cleanWord = "";
        int x = 0;
        while (x < lastWord.length()) {
            char letter = lastWord.charAt(x);
            if (Character.isLetterOrDigit(letter)) {
                cleanWord = cleanWord + letter;
            }
            x++;
        }

        String hash = first2 + ":" + numMessagesSent + ":" + firstWord + cleanWord;
        hash = hash.toUpperCase();

        return hash;
    }

    public String SentMessage(int choice) {
        if (choice == 1) {
            totalMessagesSent++;
            numMessagesSent = totalMessagesSent;
            sentMessages.add(this);
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            sentMessages.add(this);
            return "Message successfully stored.";
        } else {
            return "Invalid choice.";
        }
    }

    public static String printMessages() {
        if (sentMessages.size() == 0) {
            return "No messages sent yet.";
        }

        String result = "";
        int i = 0;
        while (i < sentMessages.size()) {
            Message m = sentMessages.get(i);
            result = result + "Message ID: " + m.messageID + "\n";
            result = result + "Message Hash: " + m.messageHash + "\n";
            result = result + "Recipient: " + m.recipient + "\n";
            result = result + "Message: " + m.message + "\n";
            i++;
        }
        return result;
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public String storeMessage() {
        String json = "{\"messageID\":\"" + messageID + "\","
                + "\"numMessagesSent\":" + numMessagesSent + ","
                + "\"recipient\":\"" + recipient + "\","
                + "\"message\":\"" + message + "\","
                + "\"messageHash\":\"" + messageHash + "\"}";
        try {
            FileWriter fw = new FileWriter("messages.json", true);
            fw.write(json + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Could not save to file.");
        }
        return "Message successfully stored.";
    }

    public String validateMessage() {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public int getNumMessagesSent() { return numMessagesSent; }

    public static void sendMessage(Scanner input) {

        //Ask a number until they give a valid one
        String cellNum = "";
        boolean numOk = false;
        while (numOk == false) {
            System.out.print("Enter recipient cell number (With International Code +27: ");
            cellNum = input.nextLine();

            Message temp = new Message(cellNum, "test");
            String check = temp.checkRecipientCell();
            System.out.println(check);

            if (check.equals("Cell phone number successfully captured.")) {
                numOk = true;
            }
        }

        //keep asking for a message until its under 250 characters
        String msg = "";
        boolean msgOk = false;
        while (msgOk == false) {
            System.out.print("Enter your message (max 250 characters): ");
            msg = input.nextLine();

            if (msg.length() > 250) {
                int over = msg.length() - 250;
                System.out.println("Please enter a message of less than 250 characters.");
                System.out.println("Your message is " + over + " characters too long.");
            } else {
                msgOk = true;
            }
        }

        System.out.println("Message sent");

        Message newMsg = new Message(cellNum, msg);

        System.out.println("Message ID: " + newMsg.getMessageID());
        System.out.println("Message Hash: " + newMsg.getMessageHash());
        System.out.println("Recipient: " + newMsg.getRecipient());
        System.out.println("Message: " + newMsg.getMessage());

        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Choose: ");
        int pick = Integer.parseInt(input.nextLine());

        String res = newMsg.SentMessage(pick);

        if (pick == 3) {
            newMsg.storeMessage();
        }

        System.out.println(res);
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        //REGISTRATION - User must enter information
        System.out.println(" REGISTER ");

        System.out.print("Enter First Name: ");
        String fName = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lName = input.nextLine();

        System.out.print("Enter Username (must have _ and max 5 chars: ");
        String user = input.nextLine();
        Login tempUsername = new Login(fName, lName, user, "", "");
        if (tempUsername.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        System.out.print("Enter Password (8+ chars, 1 capital, 1 number, 1 special: ");
        String pass = input.nextLine();
        Login tempPassword = new Login(fName, lName, user, pass, "");
        if (tempPassword.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number and a special character.");
        }

        System.out.print("Enter CellPhone Number (With international code +27): ");
        String phone = input.nextLine();
        Login registeredUser = new Login(fName, lName, user, pass, phone);
        if (registeredUser.checkCellPhoneNumber()) {
            System.out.println("Cellphone number successfully added.");
        } else {
            System.out.println("Cellphone number incorrectly formatted or does not contain international code.");
        }

        //LOGIN - Enter the Registered Information
        System.out.println(" LOGIN ");

        System.out.print("Enter First Name: ");
        String enteredFirstName = input.nextLine();

        if (enteredFirstName.equals(registeredUser.getFirstName())) {
            System.out.println("First name Match.");
        } else {
            System.out.println("First name does not match.");
        }

        System.out.print("Enter Last Name: ");
        String enteredLastName = input.nextLine();

        if (enteredLastName.equals(registeredUser.getLastName())) {
            System.out.println("Last name Match.");
        } else {
            System.out.println("Last name does not match.");
        }

        System.out.print("Enter Username: ");
        String enteredUser = input.nextLine();

        if (enteredUser.equals(registeredUser.getUsername())) {
            System.out.println("Username Match.");
        } else {
            System.out.println("Username does not match.");
        }

        System.out.print("Enter Password: ");
        String enteredPass = input.nextLine();

        if (registeredUser.loginUser(enteredUser, enteredPass)) {
            System.out.println("Password Match.");
        } else {
            System.out.println("Password does not match.");
        }

        System.out.print("Enter CellPhone Number: ");
        String enteredPhone = input.nextLine();

        if (enteredPhone.equals(registeredUser.getCellPhoneNumber())) {
            System.out.println("CellPhone number Match.");
        } else {
            System.out.println("CellPhone number does not match.");
        }

        //LOGIN STATUS
        System.out.println(" LOGIN STATUS ");

        boolean namesMatch = false;
        boolean usernameMatch = false;
        boolean passwordMatch = false;
        boolean cellphoneMatch = false;

        if (enteredFirstName.equals(registeredUser.getFirstName()) && enteredLastName.equals(registeredUser.getLastName())) {
            namesMatch = true;
        }
        if (enteredUser.equals(registeredUser.getUsername())) {
            usernameMatch = true;
        }
        if (registeredUser.loginUser(enteredUser, enteredPass)) {
            passwordMatch = true;
        }
        if (enteredPhone.equals(registeredUser.getCellPhoneNumber())) {
            cellphoneMatch = true;
        }
        if (namesMatch && usernameMatch && passwordMatch && cellphoneMatch) {
        } else {
            System.out.println("Username or password incorrect, please try again.");
            input.close();
            return;
        }

        //QUICKCHAT - Actual Application
        System.out.println("Welcome to QuickChat.");

        System.out.print("How many messages would you like to send? ");
        int numMessages = Integer.parseInt(input.nextLine());

        int count = 0;
        boolean running = true;

        while (running == true) {
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                if (count >= numMessages) {
                    System.out.println("You have reached your message limit.");
                } else {
                    sendMessage(input);
                    count++;
                }
            } else if (choice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (choice.equals("3")) {
                running = false;
            } else {
                System.out.println("Please enter 1, 2 or 3.");
            }
        }

        System.out.println("Total messages sent: " + returnTotalMessages());
        System.out.println(printMessages());
        System.out.println("Goodbye!");

        input.close();
    }
}
