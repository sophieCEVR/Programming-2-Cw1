package question2;

import java.util.List;

/**
 *
 * @author jxy13mmu
 */
public class AdvancedPlayer extends IntermediatePlayer{
    int cardCount=0;
    
    @Override
    public int makeBet() {//overwrites basicPlayers betting strategy
        int basicBet=10;
        betAmount=0;
       //checks if any balance existing and players min bet is possible
        if((currentBalance-basicBet)>0){
            if(cardCount<=0){
                 betAmount=basicBet;
            }
            //checks if they can afford the max bet with good odds
            else if((currentBalance-(basicBet*cardCount))>0){
                betAmount=(basicBet*cardCount);
            }
            else if((currentBalance-(basicBet*cardCount))<0){
                betAmount = currentBalance; 
            }
        }
        else{
            betAmount = currentBalance; //else bets remaining balance
        }
        return betAmount;
    }
    
    @Override
    public void viewCards(List<Card> cards) {
        //card counting algorithm
        for(Card c:cards){
            int r=0;
            r = c.getValue();
            //minuses 1 from cardCount int if card value is 10 or 11
            if(r==10 || r==11){
                cardCount--;
            }
            //adds 1 from cardCount int if card value is >1 or <7
            else if(r>1 && r<7){
                cardCount++;
            }
        }
    }
    
    @Override
    public void newDeck() {
        //resets the cardCount int when a newDeck is made
        cardCount=0;
        //tells player their hand is now empty
        hand.emptyHand();
    }
    
}
