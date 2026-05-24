package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    //test 1 - message under 250 chars should pass
    @Test
    public void testMessageUnder250() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.validateMessage());
    }

    //test 2 - message over 250 chars should fail
    @Test
    public void testMessageOver250() {
        String longMsg = "";
        int i = 0;
        while (i < 260) {
            longMsg = longMsg + "a";
            i++;
        }
        Message msg = new Message("+27718693002", longMsg);
        assertTrue(msg.validateMessage().startsWith("Message exceeds 250 characters by"));
    }

    //test 3 - check excess count is 10
    @Test
    public void testMessageExcessCount() {
        String longMsg = "";
        int i = 0;
        while (i < 260) {
            longMsg = longMsg + "a";
            i++;
        }
        Message msg = new Message("+27718693002", longMsg);
        assertTrue(msg.validateMessage().contains("10"));
    }

    //test 4 - valid cell number
    @Test
    public void testValidCellNumber() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    //test 5 - no international code
    @Test
    public void testNoInternationalCode() {
        Message msg = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    //test 6 - number too long
    @Test
    public void testNumberTooLong() {
        Message msg = new Message("+277186930021234567", "Hello");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    //test 7 - hash must have 3 parts
    @Test
    public void testHashHas3Parts() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hash = msg.getMessageHash();
        String[] parts = hash.split(":");
        assertEquals(3, parts.length);
    }

    //test 8 - last part of hash must be HITONIGHT
    @Test
    public void testHashLastPart() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hash = msg.getMessageHash();
        String[] parts = hash.split(":");
        assertEquals("HITONIGHT", parts[2]);
    }

    //test 9 - loop through messages and check all hashes correct format
    @Test
    public void testHashLoop() {
        String[] numbers = {"+27718693002", "+27100000001", "+27100000002"};
        String[] msgs = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hello world",
            "Testing hash generation here"
        };
        int j = 0;
        while (j < 3) {
            Message msg = new Message(numbers[j], msgs[j]);
            String hash = msg.getMessageHash();
            assertEquals(3, hash.split(":").length);
            j++;
        }
    }

    //test 10 - message ID must be 10 or less characters
    @Test
    public void testMessageIDLength() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(msg.checkMessageID());
    }

    //test 11 - message ID must be exactly 10 digits
    @Test
    public void testMessageIDExact() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals(10, msg.getMessageID().length());
    }

    //test 12 - send option
    @Test
    public void testSendOption() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", msg.SentMessage(1));
    }

    //test 13 - disregard option
    @Test
    public void testDisregardOption() {
        Message msg = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", msg.SentMessage(2));
    }

    //test 14 - store option
    @Test
    public void testStoreOption() {
        Message msg = new Message("+27718693002", "Test store message");
        assertEquals("Message successfully stored.", msg.SentMessage(3));
    }
}