
package question2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jxy13mmu
 */
public class BasicPlayer implements Player, Serializable {
    static final long serialVersionUID = 68;
//write to file
    //read from file
    Hand hand = new Hand();
    int currentBalance=200;
    int betAmount=10;
    int totalHandValue;

    @Override
    
    public Hand newHand() {
        Hand prevHand = new Hand(hand);//assign current hand into a new hand
        hand.emptyHand();
        return prevHand; //returns the previous hand
    }
    
    @Override
    public int makeBet() {
        //checks if any balance existing and players min bet is possible
        if((currentBalance-betAmount)>-1){
            return betAmount;
        }
        else{
            betAmount=0; //sets to 0 if cannot afford
        }
        return betAmount; //will be 0 or 10 depending if player can afford
    }
    
    @Override
    public int getBet() {
        return betAmount; //returns 0 or 10 for BasicPlayer
    }
    
    @Override
    public int getBalance() {
        return currentBalance;//returns the players current balance.
    }

    @Override
    public boolean hit() {
        return !hand.isOver(16); //only factor for basic player to hit
    }

    @Override
    public void takeCard(Card c) {
        hand.addCard(c);//adds c to players current hand
    }
    
    //p value is created in BlackjackDealer settleBets() method
    @Override
    public boolean settleBet(int p) {
        //if p is <0 then the player lost
        if(p<0){
            currentBalance-=betAmount;
            return (getBalance()>0);
        }
        //if p is >0 player won
        else if (p>0){
            //adds an extra betAmount if player got a blackjack
            if(blackjack()){
               currentBalance+=betAmount;
            }
            currentBalance+=betAmount;
        }
        return (getBalance()>0);
    }

    @Override
    public int getHandTotal() {
        int totalScore=(0);
        ArrayList<Integer> handValues = hand.getHandValue();
        for(int i=0;i<handValues.size();i++){
            //only valid scores will be added to totalScore
            if((handValues.get(i))<=21){ 
                totalScore=handValues.get(i);
            }
        }
        return totalScore;//this will be zero if the hand is bust
    }

    @Override
    public boolean blackjack() {
        return (hand.size()==2) && (getHandTotal()==21);
    }

    @Override
    public boolean isBust() {
        return (getHandTotal()==0); //getHandTotal will ALWAYS return 0 if bust
    }
 
    @Override
    public Hand getHand() {
        return hand; 
    }

    @Override
    public void viewDealerCard(Card c) {
        //not used by basicPlayer
    }

    @Override
    public void viewCards(List<Card> cards) {
        //not used by basicPlayer
    }

    @Override
    public void newDeck() {
        hand.emptyHand();
    }
}
