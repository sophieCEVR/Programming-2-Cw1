
package question2;

import java.util.ArrayList;

/**
 *
 * @author jxy13mmu
 */
public class IntermediatePlayer extends BasicPlayer {
    
    int dealerCardValue;
    
    @Override
    public boolean hit() {
        ArrayList<Integer> handValues = hand.getHandValue();
        //If no Aces & dealers card is >= 7 & handValue is less than 17, hit 
        if(handValues.size()==1 && dealerCardValue>=7){
            return !hand.isOver(17);
        }
        //If no Aces & dealers card is < 7 & handValue is less than 12, hit 
        else if(handValues.size()==1 && dealerCardValue<7){//excludes Ace 
            return !hand.isOver(12);
        }
        //if handValues.size>1 then the player has an ace
        else if(handValues.size()>1){
            //w Ace the player should stick on soft total of 9 or 10
            if(handValues.get(0) == 9 || handValues.get(0) == 10){
                return false;
            }
            //they should hit if soft total is lower than 9/10
            else if(handValues.get(0) <= 8){
                return true;
            }
        }
        //returns true when hand is not over 17, false otherwise
        return !hand.isOver(17); 
    }

    @Override
    public void viewDealerCard(Card c) {//Dealer shows his card on the table 
        dealerCardValue = c.getValue();
    }
}
