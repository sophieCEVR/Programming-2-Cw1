
package question2;

import java.util.Scanner;

/**
 *
 * @author jxy13mmu
 */
public class HumanPlayer extends BasicPlayer {
    
    @Override
    public boolean hit() {
        boolean continueBool = true;
        while(true){
            //checks if player is bust or not
            if(!hand.isOver(21)){
                Scanner scan = new Scanner(System.in);
                //switch statement to ask human players choice
                System.out.println("Would you like to hit or stick?\nH-hit"
                        + "\nS-stick");
                int hitStick = scan.next().toLowerCase().charAt(0);
                switch(hitStick){  
                    case 'h':
                        return true; 
                    case 's':
                        return false; 
                    default:
                        System.out.println("Incorrect entry");
                }
            }
        }
    }
    
    @Override
    public int makeBet() {
        Scanner scan = new Scanner(System.in);  
        System.out.println("How much would you like to bet?\n");
        while(!scan.hasNextInt()){
            System.out.print("\nPlease enter an integer between [1] and ");
            if(currentBalance>500){
            System.out.print("[500]\n");
            }
            if(currentBalance<500){
                System.out.print("[" + currentBalance + "]\n");
            }
            scan = new Scanner(System.in);
        }
        betAmount = scan.nextInt();
        //checks if any balance existing and players min bet is possible
        if((currentBalance-betAmount)>-1){
            return betAmount;
        }
        else{
            betAmount=0; //sets to 0 if cannot afford
            System.out.println("You cannot afford this bet, please enter"
                    + " between 1 and " + getBalance());
            //reroutes to top of method until player enters a valid bet
            makeBet();
        }
        return betAmount; //will be 0 or 10 depending if player can afford
    }
}
