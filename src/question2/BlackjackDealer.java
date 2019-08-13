package question2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jxy13mmu
 */
public class BlackjackDealer implements Dealer, Serializable{
    static final long serialVersionUID = 70;
    
    Hand dealerHand = new Hand();
    Deck deck;
    int dealerTotal;
    List<Player> players;
    
    /**
     * Dealer constructor
     * Creates the dealer with the given deck
     * @param deck
     */
    public BlackjackDealer(Deck deck){
        this.deck = deck;
    }
    
    /**
     * assigns the passed players into list players of type Player
     * Is used in the creation of different game types
     * @param p
     */
    @Override
    public void assignPlayers(List<Player> p) {
        this.players=p;//moves list into player list for handling
    }

    @Override
    public void takeBets(Player p) {
            p.makeBet(); //takes bet from each player
        
    }

    @Override
    public void dealFirstCards() {
        for(Player p: players){//loop deals the first two cards for each player
            p.takeCard(deck.deal());
            p.takeCard(deck.deal());
        }
        dealerHand.addCard(deck.deal()); //deals dealers first card
        for(Player p: players){
            p.viewDealerCard(dealerHand.get(0));//shows dealers card to players
        }
    }

    @Override
    public int play(Player p) {
        //prints the hand at start of hand play. This will just show the 
        //two first dealt cards
        System.out.println("First dealt hand: \n" + p.getHand() + "Hand Total: "
                + p.getHandTotal() + "\n");
        //while the player chooses to hit, isnt bust and does not have blackjack
        while(!p.isBust() && !p.blackjack()){
            if(p.hit()){
                System.out.println("Player chose to hit\n");
                p.takeCard(deck.deal());
                System.out.println("Hand: \n" + p.getHand());
                //if hand total==0, hand is bust
                if(p.getHandTotal()!=0){
                    System.out.println("Hand Total: " + p.getHandTotal() + "\n");
                }
                //Stick choice. If !isBust is not incl, will print even when bust
                if(!p.isBust()){
                }
            }
            else{
                System.out.println("\nPlayer chose to stick\nFinal Hand total: " + 
                        p.getHandTotal());
                return p.getHandTotal();
            }
        }  
        //print out when the player has a blackjack
        if(p.blackjack()){
            System.out.println("Player has a blackjack");
        }
        //print out for when player has gone bust
        else if(p.isBust()){
            System.out.println("Player is bust");
        }
        else{
            System.out.println("Player chose to stick with first deal");
        }
        return p.getHandTotal();
    }
    
    
    //conditions for dealer to hit/stick
    @Override
    public int playDealer() {
        //displays first dealt card 
        System.out.println(
           "\n/*************************Dealer Hand*************************/\n"
                   + "First dealt hand: \n" + dealerHand.toString() + 
                   "Hand Total: " + scoreHand(dealerHand) + "\n");
        //dealer will ask for deal when his score is below 17
        while (!dealerHand.isOverHard(16)){ 
            dealerHand.addCard(deck.deal());
            System.out.println( "Dealer chose to hit\n" + "\nHand: \n" + 
                    dealerHand.toString());
            //doesn't print the total in the case the dealer has gone bust
            if(!dealerHand.isOverHard(21)){
                System.out.println("Hand Total: " + scoreHand(dealerHand) + "\n");
            }
        }
        //if the dealer did not go bust during play then he chose to stick
        if(!dealerHand.isOverHard(21)){
            System.out.println("Dealer chose to stick\n");
        }
        return scoreHand(dealerHand);
    }
    
    @Override
    public void emptyHand() {
        dealerHand.emptyHand();
    }
    
    @Override
    public Hand getHand(){
        return dealerHand;
    }
    
    @Override
    public int scoreHand(Hand h) {
        int totalScore=0;
        //if the soft total is over 21 return totalScore as 0
        if(h.isOver(21)){
            return totalScore;
        }
        else{
            ArrayList<Integer> handValue = h.getHandValue();
            for(int i=0;i<handValue.size();i++){
                //only none bust scores will be added to totalScore
                if((handValue.get(i))<=21){ 
                    totalScore=handValue.get(i);
                }
            }
        }
        return totalScore;
    }
 
    @Override
    public void settleBets() {
        //runs through the players on current game
        System.out.println("\nFinal Scores");
        int i=1;
        for(Player p: players){
            //checks if player is bust
            if(p.getBalance()>0){
                if(p.isBust()){
                    System.out.println("Player " + i + " [LOST]");
                    p.settleBet(-1);
                }
                //checks if players score is greater than dealers score
                else if((scoreHand(p.getHand())>scoreHand(dealerHand))){
                    if(p.blackjack()){
                        System.out.println("Player " + i + " [BLACKJACK] - [DOUBLE WIN]");
                        p.settleBet(1);
                    }
                    else{
                        System.out.println("Player " + i + " [WON]");
                        p.settleBet(1);
                    }
                }
                //checks if players score is equal to dealers score
                else if(scoreHand(p.getHand())==(scoreHand(dealerHand))){
                    System.out.println("Player " + i + " [DRAW]");
                    p.settleBet(0);
                }
                //else the player will have lost, so inputs -1 to settlebet
                else{
                    System.out.println("Player " + i + " [LOST]");
                    p.settleBet(-1);
                }
                i++;
            }
        }
    }
    
}
