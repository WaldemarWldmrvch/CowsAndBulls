import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner numbers = new Scanner(System.in);
        System.out.println("Please, enter the secret code`s length:");
        int numbersInCode = 0;
        int summaryLength = 0;
        try {
            numbersInCode = numbers.nextInt();
            if (numbersInCode < 1) {
                System.out.println("Error.");
                return;
            }
            System.out.println("Input the number of possible symbols in the code:");
            summaryLength = numbers.nextInt();
        } catch (Exception e) {
            System.out.println("Error.");
            System.exit(0);
        }
        int stars = 0;
        char firstLetter = 'a';
        char lastLetter = (char) (firstLetter + summaryLength - 11);
        if (summaryLength > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        } else if (summaryLength < numbersInCode) {
            System.out.println("Error: it's not possible to generate a code with a length of " + numbersInCode
                    + " with " + summaryLength + " unique symbols.");
        } else {
            System.out.print("The secret is prepared: ");
            while (stars < numbersInCode) {
                System.out.print("*");
                stars++;
            }
            if (summaryLength <= 10) {
                System.out.print(" " + "(" + 0 + "-" + (summaryLength - 1) + ").");
            } else if (summaryLength > 11) {
                System.out.println(" " + "(" + 0 + "-" + 9 + ", " + firstLetter + "-" + lastLetter + ").");
            } else if (summaryLength == 11) {
                System.out.println(" " + "(" + 0 + "-" + 9 + ", " + firstLetter + ").");
            }
            System.out.println();
            System.out.println("Okay, let's start a game!");
            GameEngine(numbersInCode, Randomizer(numbersInCode, summaryLength));
        }
    }


    public static String Randomizer(int codeLength, int totalElements) { // Отлаженный метод, больше не трогать!
        Random random = new Random();
        String finalCode = "";
        ArrayList<Character> naturalDigits = new ArrayList<>();
        ArrayList<Character> naturalDigitsWithout0 = new ArrayList<>();
        ArrayList<Character> letters = new ArrayList<>();
        ArrayList<Character> digitsAndLetterWithout0 = new ArrayList<>();
        ArrayList<Character> digitsAndLetters = new ArrayList<>();
        ArrayList<Character> userCode = new ArrayList<>();
        Collections.addAll(naturalDigits, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(naturalDigitsWithout0, '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(letters, 'a', 'b', 'c', 'd', 'e', 'f' , 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
        digitsAndLetters.addAll(naturalDigits);
        digitsAndLetters.addAll(letters);
        digitsAndLetterWithout0.addAll(naturalDigitsWithout0);
        digitsAndLetterWithout0.addAll(letters);
        int countOfNumbers = 0;

        if (codeLength <= 10) { // в случае, когда требуются только натуральные числа только
            userCode.add(naturalDigitsWithout0.get(random.nextInt(naturalDigitsWithout0.size()))); // добавление первой цифры, которая не будет 0
            naturalDigits.remove(userCode.get(0)); // удаление этой цифры из массива для первой буквы, чтобы она не повторялась в коде
            finalCode += userCode.get(0);
        } else { // для случая, когда помимо цифр нужны еще и буквы
            userCode.add(digitsAndLetterWithout0.get(random.nextInt(digitsAndLetterWithout0.size()))); // добавление первой цифры или буквы, которая не будет 0
            digitsAndLetters.remove(userCode.get(0)); // удаление этой цифры или буквы из массива (полного) для первой буквы, чтобы она не повторялась в коде
            finalCode += userCode.get(0);
        }
        for (int i = 1; i < codeLength; i++) {
            if (codeLength <= 10) {
                userCode.add(naturalDigits.get(random.nextInt(naturalDigits.size())));
                naturalDigits.remove(userCode.get(i));
                countOfNumbers++;
                finalCode += userCode.get(i);
            } else {
                userCode.add(digitsAndLetters.get(random.nextInt(totalElements)));
                digitsAndLetters.remove(userCode.get(i));
                countOfNumbers++;
                finalCode += userCode.get(i);
            }
        }
        return finalCode;
    }

    public static void GameEngine(int numbersInCode, String finalCode) {
        Scanner scanner = new Scanner(System.in);
        int cows = 0, bulls = 0, step = 1;
        // Предлагают заменять угаданных быков пробелами, для этого нужен тип StringBuilder (первый цикл). Во втором цикле игнорируются пробелы, и считаются коровы.
        while (bulls != numbersInCode) {
            StringBuilder secretCode = new StringBuilder(finalCode);
            System.out.println("Turn " + step + ":");
            String userInput = scanner.next();
            StringBuilder userCode = new StringBuilder(userInput);
            for (int i = 0; i < numbersInCode; i++) {
                if (userCode.charAt(i) == secretCode.charAt(i)) {
                    bulls++;
                    cows--;
                    secretCode.setCharAt(i, ' ');
                    userCode.setCharAt(i, ' ');
                }
            }
            for (int i = 0; i < userInput.length(); ++i) {
                if (userInput.charAt(i) == ' ')
                    continue;
                int countLeft = countBetween(userInput.charAt(i), userInput, 0, i);
                int countInComputerNumber = countBetween(
                        userInput.charAt(i), finalCode, 0, finalCode.length());
                if (countInComputerNumber != 0
                        && countLeft < countInComputerNumber)
                    cows++;
            }
            if (cows == 0 && bulls == 0) {              // переписать эту часть с помощью регулярных выражений!
                System.out.println("Grade: None.");
            } else if (bulls == numbersInCode) {
                System.out.println("Grade: " + bulls + " bull(s).");
                System.out.println("Congratulations! You guessed the secret code.");
                return;
            } else if (cows > 0 && bulls > 0) {
                System.out.println("Grade: " + bulls + " bull(s)" + " and " + cows + " cow(s).");
            } else if (bulls > 0) {
                System.out.println("Grade: " + bulls + " bull(s).");
            } else if (cows > 0) {
                System.out.println("Grade: " + cows + " cow(s).");
            }
            step++;
            cows = 0;
            bulls = 0;

        }
    }
    public static int countBetween(char what, String where, int from, int to) {
        int count = 0;
        for (int i = from; i < to; ++i) {
            if (where.charAt(i) == what) {
                count++;
            }
        }
        return count;
    }
}







