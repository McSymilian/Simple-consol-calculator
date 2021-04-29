package calculator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Counter {
    private static final Map<String, String> map= new HashMap<>();

    void addNewVariable (String line) {
        String[] nameAndValue = line.split("\\s*=\\s*");

        if (nameAndValue[0].matches(".*\\d+.*") || nameAndValue.length > 2) {
            System.out.println("Invalid assignment");
        }
        else {
            try {
                if (map.containsKey(nameAndValue[1])) {
                    map.put(nameAndValue[0], map.get(nameAndValue[1]));
                } else if (nameAndValue[1].matches(".+\\s+[+^*/-]\\s+.+")) {
                    map.put(nameAndValue[0], count(nameAndValue[1]));
                } else if (nameAndValue[1].matches(" *\\d+ *")){
                    map.put(nameAndValue[0], nameAndValue[1]);
                }
                else {
                    throw new RuntimeException();
                }
            } catch (RuntimeException e) {
                System.out.println("Invalid assignment");
            }
        }
    }

    String valueOfValue(String s) {
        if (s.matches(" *\\d+ *")) return s;
        if (map.containsKey(s)) return map.get(s);
        throw new RuntimeException("Unknown variable");

    }

    String sTrim(String s) {
        s = s.replaceAll("\\++ *-+", "- ");
        s = s.replaceAll("-+ *\\++", "- ");
        s = s.replaceAll("-+ *-+", "+ ");
        s = s.replaceAll("--+", "- ");
        s = s.replaceAll("\\+\\++", "- ");
        s = s.replaceAll("\\s+", "");

        return s;
    }

    BigInteger smallCount(String s) {
        //System.out.println("Small count for: " + s);
        s = sTrim(s);

        ArrayList<String> elem = chop(s);
   //     System.out.println("Small count array: " + elem);
        boolean b = false;
        if ("-".equals(elem.get(0))) {
            elem.set(2, easySwitch(elem.get(2)));
            elem.remove(0);
            b = true;
        }

        BigInteger one = new BigInteger(elem.get(0));
        BigInteger two = new BigInteger(elem.get(2));

        if (elem.get(1).matches(".?\\^.?")) {
            return one.pow(Integer.parseInt(valueOfValue(elem.get(2))));
        } else if (elem.get(1).matches(".?\\*.?")) {
            return one.multiply(two);
        } else if (elem.get(1).matches(".?/.?")) {
            return one.divide(two);
        } else if (elem.get(1).matches(".?\\+.?")) {
            if (b) return one.add(two).negate();
            return one.add(two);
        } else if (elem.get(1).matches(".?-.?")) {
            if (b) return one.subtract(two).negate();
            return one.subtract(two);
        }

        return one;
    }
    ArrayList<String> chop(String s) {
        ArrayList<String> arrayList = new ArrayList<>();
   //     System.out.println("Chop: " + s);
        int tmp = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '^' || s.charAt(i) == '*' || s.charAt(i) == '/' || s.charAt(i) == '+' || s.charAt(i) == '-') {
                if (i > 0) {
           //        System.out.println("Dodano do listy: " + s.substring(tmp,i));
                    try {
                        arrayList.add(valueOfValue(s.substring(tmp, i)));
                    }
                    catch (RuntimeException e) {
                        System.out.println("Invalid assignment");
                    }
                }
                tmp = i + 1;
            //    System.out.println("Dodano do listy: " + s.substring(i, tmp));
                arrayList.add(s.substring(i, tmp));
            }
        }
     //   System.out.println("Dodano do listy: " + s.substring(tmp));
        try {
            arrayList.add(valueOfValue(s.substring(tmp)));
        }
        catch (RuntimeException e) {
            System.out.println("Invalid assignment");
        }
        return arrayList;
    }

    String count(String s) {

        s = s.trim();
        s = sTrim(s);
        if(s.matches("[a-zA-z]+")) return valueOfValue(s);
        while (s.matches(".*[(){}\\[\\]].*")) {
            char open = 0;
            boolean sw = true;
            int start = 0;
            for (int i = 0, counter = 0; i < s.length(); i++) {
                if (BracketsChecker.isStart(s.charAt(i)) && sw) {
                    open = s.charAt(i);
                    sw = false;
                    start = i;
                }
                else if (BracketsChecker.isStart(s.charAt(i)) && !sw) {
                    counter++;
                }
                else if (BracketsChecker.isEnd(open, s.charAt(i)) && counter == 0) {
                    if (start > 0 && i < s.length() - 1) {
                    //    System.out.println("Opcja 1");
                        s = s.substring(0, start) + count(s.substring(start + 1, i)) + s.substring(i + 1);
                    }
                    else if (start > 0) {
                     //   System.out.println("Opcja 2");
                        s = s.substring(0, start) + count(s.substring(start + 1, i));
                    }
                    else if (start == 0 && i == s.length() - 1) {
                        s = count(s.substring(1, i));
                    }
                    else {
                    //    System.out.println("Opcja 3");
                        s = count(s.substring(1, i)) + s.substring(i + 1);
                    }
                }
                else if (BracketsChecker.isEnd(s.charAt(i))) {
                    counter--;
                }
            }
        }

        s = s.trim();
        ArrayList<String> elem = chop(s);

  //      System.out.println(elem);
        for (int i = 0; i < elem.size(); i++) {
            if (elem.get(i).equals("^")) {
                elem.set(i - 1, smallCount(elem.get(i - 1) + elem.get(i) + elem.get(i + 1)).toString());
                elem.remove(i);
                elem.remove(i);
            }
        }

        for (int i = 0; i < elem.size(); i++) {
            if (elem.get(i).equals("*") || elem.get(i).equals("/")) {
                elem.set(i - 1, smallCount(elem.get(i - 1) + elem.get(i) + elem.get(i + 1)).toString());
                elem.remove(i);
                elem.remove(i);
            }
        }
        while (elem.size() > 2) {
            if ("-".equals(elem.get(0))) {
                elem.set(1, smallCount(elem.get(1) + easySwitch(elem.get(2)) + elem.get(3)).toString());
                elem.remove(2);
                elem.remove(2);
            }
            else {
                elem.set(0, smallCount(elem.get(0) + elem.get(1) + elem.get(2)).toString());
                elem.remove(1);
                elem.remove(1);
            }
        }

      //  System.out.println(elem);
        StringBuilder eq1 = new StringBuilder();

        for (String s1: elem) {
            eq1.append(s1);
        }
        return String.valueOf(eq1);

    }
    String easySwitch(String s) {
        switch (s){
            case "+":
                return "-";
            case "-":
                return "+";
            default:
                return s;
        }
    }
}
