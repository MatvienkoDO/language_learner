package code;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Solution {
    private static String PATH2DICTIONARY = "src/res/dictionary.xlsx";
    private static TypeOfQuestions type = TypeOfQuestions.Random;

    public static void main(String[] args) throws Exception {
        File dictionaryFile = new File(PATH2DICTIONARY);
        if (!dictionaryFile.exists())
            throw new Exception("dictionary is not found");

        Scanner in = new Scanner(System.in);
        if (args.length > 0) {
            setTypeOfQuestion(args[0]);
        } else {
            System.out.println("Select type of questions:");
            System.out.println("Type '0' for Random (standart mode)");
            System.out.println("Type '1' for 'English word is question, Russian - answer'");
            System.out.println("Type '2' for 'Russian - question, English - answer'");
            setTypeOfQuestion(in.next());
        }

        XSSFSheet sheet = new XSSFWorkbook(new FileInputStream(dictionaryFile)).getSheetAt(0);
        int rowsNumber = sheet.getLastRowNum() + 1;

        Random random = new Random();
        String answer = "";
        String engWord;
        String rusWord;
        String question;
        String rightAnswer;
        List<Mistake> mistakes = new ArrayList<>();
        System.out.println("Type '/stop' for stop testing");
        while (!answer.equalsIgnoreCase("/stop")) {
            XSSFRow row = sheet.getRow(random.nextInt(rowsNumber));
            if (row.getCell(0).getStringCellValue().charAt(0) == 'а') {
                engWord = row.getCell(2).getStringCellValue();
                rusWord = row.getCell(3).getStringCellValue();
            } else {
                rusWord = row.getCell(2).getStringCellValue();
                engWord = row.getCell(3).getStringCellValue();
            }

            if ((random.nextBoolean() && type == TypeOfQuestions.Random) || type == TypeOfQuestions.EngRus) {
                question = engWord;
                rightAnswer = rusWord;
            } else {
                question = rusWord;
                rightAnswer = engWord;
            }

            System.out.println(question);
            answer = in.next();

            System.out.println("Right answer is '" + rightAnswer + "'");
            if (checkString(answer, rightAnswer)) { //answer is right
                System.out.println("Right");
            } else { //mistake is done
                mistakes.add(new Mistake(answer, rightAnswer));
                System.out.println("Mistake");
            }
            System.out.println();
        }
    }

    private static boolean checkString(String answer_, String right_) {
        if(answer_.equalsIgnoreCase(right_))
            return true;
        String answer = answer_.toLowerCase();
        String right = right_.toLowerCase();
        int length = Integer.max(answer.length(), right.length()) / 2;
        try {
            for (int i = 0; i < length; i++) {
                if (answer.contains(right.substring(i, i + length)))
                    return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private static void setTypeOfQuestion(String value) {
        switch (Integer.parseInt(value)) {
            case 1:
                type = TypeOfQuestions.EngRus;
                break;
            case 2:
                type = TypeOfQuestions.RusEng;
                break;
            default:
                type = TypeOfQuestions.Random;
        }
    }
}
