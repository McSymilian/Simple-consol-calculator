package calculator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String commend;

    private static void shorten(CommandHandler com) {
        for (int i = 0; i < 4; i++) {
            commend = com.delete("\\+\\+", "+");
            commend = com.delete("--", "+");
            commend = com.delete("(-\\+)|(\\+-)", "-");
            commend = com.delete(" +", " ");
            commend = com.delete(" +", " ");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        while (true){
            commend = scanner.nextLine();
            commend = commend.trim();
            CommandHandler commandHandler = new CommandHandler(commend);

            if (commandHandler.close()) break;

            Counter counter = new Counter();
            if (commend.matches(".+=.+")) {
                counter.addNewVariable(commend);
                continue;
            }
            if (commandHandler.operation()) continue;

            shorten(commandHandler);
            BracketsChecker bracketsChecker = new BracketsChecker(commend);

            Pattern possible = Pattern.compile("(//+)|(\\*\\*+)|(\\^\\^+)");
            Matcher conditions = possible.matcher(commend);
            if (bracketsChecker.check() && !conditions.find() && !commend.matches(".+[-+*/^] ?[)\\]}]? ?")) {
                try {
                    System.out.println(counter.count(commend));
                }
                catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
            else System.out.println("Invalid expression");
        }
    }
}
