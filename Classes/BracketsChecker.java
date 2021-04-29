package calculator;

import java.util.Stack;

public class BracketsChecker {
    private final String line;

    BracketsChecker(String line) {
        this.line = line.replaceAll("[\\w +*/^-]+", "");
    }
    static boolean isStart(char CHAR) {
        return CHAR == '{' || CHAR == '[' || CHAR == '(';
    }
    static boolean isEnd(char CHAR) {
        return CHAR == '}' || CHAR == ']' || CHAR == ')';
    }

    static boolean isEnd(char first, char second) {
        switch (first) {
            case '{' :
                return second == '}';
            case '[' :
                return second == ']';
            case '(' :
                return second == ')';
            default:
                return false;
        }
    }

    public boolean check() {
        // put your code here

        Stack<Character> brackets = new Stack<>();
        switch (line.length()) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                char[] fragmented = line.toCharArray();

                if (isStart(fragmented[0])) {
                    brackets.push(fragmented[0]);
                    for (int i = 1; i < fragmented.length; i++) {
                        if (isStart(fragmented[i])) brackets.push(fragmented[i]);
                        else if (!brackets.isEmpty() && isEnd(brackets.lastElement(), fragmented[i])) brackets.pop();
                        else brackets.push(fragmented[i]);
                    }
                    return brackets.empty();
                }
                else return false;
        }

    }
}
