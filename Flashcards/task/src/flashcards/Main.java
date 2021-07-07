package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String importFile ="";
        String exportFile = "";
        if(args.length>1){
            if(args[0].equals("-import")) {
                importFile = args[1];
            }else if(args[0].equals("-export")){
                exportFile = args[1];
            }
            if(args.length>3){
                if(args[2].equals("-import")) {
                    importFile = args[3];
                }else if(args[2].equals("-export")){
                    exportFile = args[3];
                }
            }
        }
        new UserInterface(new Scanner(System.in), importFile, exportFile).start();
    }
}
