package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {
    private String command;
    CommandHandler(String command) {
        this.command = command;
    }

    String delete (String regex, String replacement) {
        Pattern symbols = Pattern.compile(regex);
        Matcher symbolsMatch = symbols.matcher(command);

        while (symbolsMatch.find()) {
            command = command.replaceAll(regex, replacement);
        }
        return command;
    }

    boolean close() {
        if (command.equals("/exit")) {
            System.out.println("Bye!");
            return true;
        }
        return  false;
        //else return command.equals("/exit");
    }

    boolean operation () {
        if (command.equals("/help")) {
            System.out.println("The program calculates the sum of numbers");
        }
        else if (command.matches("/.*")) {
            System.out.println("Unknown command");
        }
        else if (command.equals("")) {
            return true;
        }
        else if (command.matches("\\d+1 2[+*/-]")) {
            System.out.println("Invalid expression");
        }
        else {
            return false;
        }
        return true;
    }
}
