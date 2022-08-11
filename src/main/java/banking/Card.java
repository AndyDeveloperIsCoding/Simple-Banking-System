package banking;

import java.util.Random;

public class Card {

    public static String cardNumber;
    public static String pinNumber;
//    private int balance;

    public static void generateCard() {

        // Generate blank card
        Card card = new Card();

        // Generate initial (draft) card number:

        Random random = new Random();

        StringBuilder cardNumberGenerator = new StringBuilder();
        cardNumberGenerator.append("400000");
        for (int i = 0; i < 9; i++) {
            cardNumberGenerator.append(random.nextInt(10));
        }
        card.cardNumber = (String.valueOf(cardNumberGenerator));
        // System.out.println("Temp check- cardNumber is: " + Main.cardNumber); // Temp

        // Calculate the last digit of card number using Luhn algorithm:

        calculateLastDigit();

        cardNumberGenerator.append(String.valueOf(lastDigit));
        Card.cardNumber = (String.valueOf(cardNumberGenerator));

        // Display the full card number
        //  System.out.println(Main.cardNumber);

    }

    private static void calculateLastDigit() {

        lastDigit = 0;

        // 1. Add all characters to an Array

        int arraySize = Card.cardNumber.length(); // Array size will be the same for all following arrays
        int[] collection = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            collection[i] = Integer.parseInt(String.valueOf(Card.cardNumber.charAt(i)));
        }

        // System.out.println("See the array: " + Arrays.toString(collection)); // Temp

        // 2. Multiply odd digits by 2

        int[] collectionMultiplyByTwo = new int[arraySize];

        for (int j = 0; j < arraySize; j++) {
            if (j % 2 == 0) {
                collectionMultiplyByTwo[j] = collection[j] * 2;
            } else {
                collectionMultiplyByTwo[j] = collection[j];
            }
        }

        // System.out.println("Array with odd digits multiplied by 2: " + Arrays.toString(collectionMultiplyByTwo)); // Temp

        // 3. Subtract 9 to numbers over 9

        int[] collectionSubtractNine = new int[arraySize];

        for (int k = 0; k < arraySize; k++) {
            if (collectionMultiplyByTwo[k] > 9) {
                collectionSubtractNine[k] = collectionMultiplyByTwo[k] - 9;
            } else {
                collectionSubtractNine[k] = collectionMultiplyByTwo[k];
            }
        }

        // System.out.println("Array with 9 subtracted: " + Arrays.toString(collectionSubtractNine)); // Temp

        // 4. Check the sum of the array members

        int sum = 0;

        for (int l = 0; l < collectionSubtractNine.length; l++) {
            sum += collectionSubtractNine[l];
        }

        // System.out.println("Sum is: " + sum); // Temp

        // Calculate the last digit

        while (sum % 10 != 0) {
            sum++;
            lastDigit++;
        }

    }

    public static int lastDigit = 0;

    public static void generatePin() {
        Random random = new Random();

        StringBuilder pinGenerator = new StringBuilder();
        pinGenerator.append(random.nextInt(10));
        for (int i = 0; i < 3; i++) {
            pinGenerator.append(random.nextInt(10));
            Card.pinNumber = String.valueOf(pinGenerator);
        }
        System.out.println(Card.pinNumber);

    }

    public static boolean checkLuhnRules(String recipientCardNumber) {

        boolean luhnCheckPassed = false;

        // 1. Add all characters to an Array

        int arraySize = recipientCardNumber.length(); // Array size will be the same for all following arrays
        int[] collection = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            collection[i] = Integer.parseInt(String.valueOf(recipientCardNumber.charAt(i)));
        }

        // System.out.println("See the array: " + Arrays.toString(collection)); // Temp

        // 2. Multiply odd digits by 2

        int[] collectionMultiplyByTwo = new int[arraySize];

        for (int j = 0; j < arraySize; j++) {
            if (j % 2 == 0) {
                collectionMultiplyByTwo[j] = collection[j] * 2;
            } else {
                collectionMultiplyByTwo[j] = collection[j];
            }
        }

        // System.out.println("Array with odd digits multiplied by 2: " + Arrays.toString(collectionMultiplyByTwo)); // Temp

        // 3. Subtract 9 to numbers over 9

        int[] collectionSubtractNine = new int[arraySize];

        for (int k = 0; k < arraySize; k++) {
            if (collectionMultiplyByTwo[k] > 9) {
                collectionSubtractNine[k] = collectionMultiplyByTwo[k] - 9;
            } else {
                collectionSubtractNine[k] = collectionMultiplyByTwo[k];
            }
        }

        // System.out.println("Array with 9 subtracted: " + Arrays.toString(collectionSubtractNine)); // Temp

        // 4. Check the sum of the array members

        int sum = 0;

        for (int l = 0; l < collectionSubtractNine.length; l++) {
            sum += collectionSubtractNine[l];
        }

        // System.out.println("Sum is: " + sum); // Temp

        // Calculate the last digit

        if (sum % 10 == 0){
            luhnCheckPassed = true;
        }

        return luhnCheckPassed;
    }

}