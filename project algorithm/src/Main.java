import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner scan = new Scanner(System.in);
        System.out.println("would you like play with computer");
        String answer = scan.nextLine();
        boolean valid = false ;
        while (!valid){
       if (answer.equals("no")  || answer.equals("yes") ){
           valid = true ;
           break;
       }
            System.out.println("please enter again");
            answer = scan.nextLine();

    }
     switch (answer){
         case "yes" : board.PlayGameWithAlgorithm();
         case "no"  : board.PlayGame();
     }
    }
}