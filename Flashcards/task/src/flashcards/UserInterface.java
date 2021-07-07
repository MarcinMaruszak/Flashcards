package flashcards;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final SetOfCards setOfCards;
    private StringBuilder log;
    private String importFile;
    private String exportFile;

    public UserInterface(Scanner scanner, String importFile, String exportFile) {
        this.scanner = scanner;
        this.setOfCards = new SetOfCards();
        this.log = new StringBuilder();
        this.importFile = importFile;
        this.exportFile = exportFile;
    }

    public void start() {
        importOnStart();
        label:
        while (true) {
            printAndLog("\nInput the action " +
                    "(add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String input = getInputAndLog();
            switch (input) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    loadFromFile();
                    break;
                case "export":
                    saveToFile();
                    break;
                case "ask":
                    askUser();
                    break;
                case "log":
                    saveLog();
                    break;
                case "hardest card":
                    findHardestCards();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                case "exit":
                    exit();
                    break label;
                default:
                    printAndLog("incorrect input");
            }
        }
    }

    private void importOnStart() {
        if(!importFile.isEmpty()){
            try {
                int x =setOfCards.loadFromFile(importFile);
                if (x >= 0) {
                    printAndLog(x + " cards have been loaded.");
                }
            } catch (IOException e) {
                printAndLog("File not found.");
            }
        }
    }

    private void exit() {
        if(!exportFile.isEmpty()){
            setOfCards.saveToFile(exportFile);
            printAndLog(setOfCards.getCards().size() + " cards have been saved.");
        }
        printAndLog("Bye bye!");
    }

    private void resetStats() {
        setOfCards.resetMistakes();
        printAndLog("Card statistics have been reset.");
    }

    private void findHardestCards() {
        List<FlashCard> hardest = setOfCards.findHardestCards();
        if (!hardest.isEmpty()) {
            StringBuilder output = new StringBuilder("The hardest ");
            output.append(hardest.size() > 1 ? "cards are" : "card is");
            for (int i = 0; i < hardest.size(); i++) {
                output.append(" \"").append(hardest.get(i).getTerm()).append("\"");
                if (i == hardest.size() - 1) {
                    output.append(".");
                } else {
                    output.append(",");
                }
            }
            output.append(" You have ").append(hardest.get(0).getMistakes()).append(" errors answering ");
            output.append(hardest.size() > 1 ? "them." : "it.");
            printAndLog(output.toString());
        } else {
            printAndLog("There are no cards with errors.");
        }
    }

    private void saveLog() {
        printAndLog("File name:");
        String file = getInputAndLog();
        try (FileWriter writer = new FileWriter(file)) {
            printAndLog("The log has been saved.");
            writer.write(log.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printAndLog(String text) {
        System.out.println(text);
        log.append(LocalDateTime.now()).append(": ").append(text).append("\n");
    }

    private String getInputAndLog() {
        String input = scanner.nextLine();
        log.append(LocalDateTime.now()).append(": ").append(input).append("\n");
        return input;
    }


    private void loadFromFile() {
        printAndLog("File name:");
        int x;
        try {
            x = setOfCards.loadFromFile(getInputAndLog());
            if (x >= 0) {
                printAndLog(x + " cards have been loaded.");
            }
        } catch (IOException e) {
            printAndLog("File not found.");
        }

    }

    private void saveToFile() {
        printAndLog("File name:");
        setOfCards.saveToFile(getInputAndLog());
        printAndLog(setOfCards.getCards().size() + " cards have been saved.");
    }


    private void removeCard() {
        printAndLog("Which card?");
        String term = getInputAndLog();
        if (setOfCards.containsKey(term)) {
            setOfCards.remove(term);
            printAndLog("The card has been removed.");
        } else {
            printAndLog("Can't remove \"" + term + "\": there is no such card.");
        }
    }

    private void askUser() {
        printAndLog("How many times to ask?");
        int x = Integer.parseInt(getInputAndLog());

        List<FlashCard> randomTerms = setOfCards.getRandomTerms(x);
        for (FlashCard card : randomTerms) {
            printAndLog("Print the definition of \"" + card.getTerm() + "\":");
            String answer = getInputAndLog();
            String correctAnswer = card.getDefinition();
            if (answer.equals(correctAnswer)) {
                printAndLog("Correct!");
            } else {
                card.increaseMistakeCount();
                if (setOfCards.containsDefinition(answer)) {
                    printAndLog("Wrong. The right answer is \"" + correctAnswer + "\", " +
                            "but your definition is correct for \"" + setOfCards.getTermByDef(answer) + "\".");
                } else {
                    printAndLog("Wrong. The right answer is \"" + correctAnswer + "\".");
                }
            }
        }
    }

    private void addCard() {
        String term = getTerm();
        if (!term.isEmpty()) {
            String def = getDefinition();
            if (!def.isEmpty()) {
                setOfCards.addCard(term, def);
                printAndLog("The pair (\"" + term + "\":\"" + def + "\") has been added.");
            }
        }
    }

    private String getTerm() {
        printAndLog("The card:");

        String term = getInputAndLog();
        if (!setOfCards.getCards().contains(new FlashCard(term))) {
            return term;
        }
        printAndLog("The card \"" + term + "\" already exists.");
        return "";
    }

    private String getDefinition() {
        printAndLog("The definition of the card:");

        String def = getInputAndLog();
        if (!setOfCards.containsDefinition(def)) {
            return def;
        }
        printAndLog("The definition \"" + def + "\" already exists. Try again:");
        return "";
    }
}
