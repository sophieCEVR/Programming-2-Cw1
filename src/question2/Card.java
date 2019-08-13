package question2;

/**
 * @Student Number 100021268
 * @author jxy13mmu
 */

import java.io.Serializable; 
import java.util.Comparator;

public class Card implements Serializable, Comparable<Card>{
    
    static final long serialVersionUID = 111;

    private final Rank r;
    private final Suit s;
    
    /**
     * Card object constructor 
     * @param r 
     * @param s
     */
    public Card(Rank r, Suit s){
        this.r = r;
        this.s = s;
    } 

    /**
     * Comparator which compares the rank of a passed card against the 
     * current card (for implementation as cardOne.compareTo(cardTwo) 
     * @param c
     * @return
     */
    @Override
    public int compareTo(Card c) {
        int result = (c.getRank().ordinal())-(r.ordinal());
        return result; 
    }
    
    /**
     * Comparator which compares the suit of a passed card against the 
     * current card (for implementation as cardOne.compareTo(cardTwo) 
     * @param c
     * @return
     */
    public int compareToSuit(Card c){
         int result = (s.ordinal())-(c.getSuit().ordinal());
         return result;
    }
    
    /**
     * Suit Enums
     */
    public enum Suit{Clubs,Diamonds,Hearts,Spades};

    /**
     * enums for Rank, brackets contain the values of the cards
     */
    public enum Rank{TWO(2),THREE(3),FOUR(4),FIVE(5),SIX(6),SEVEN(7),EIGHT(8),
    NINE(9),TEN(10),JACK(10),QUEEN(10),KING(10),ACE(11);
        private final int val;
        //int r
        Rank(int x){
            val=x;
        }
        public int getValue(){
            return val;
        }        
    } 
    
    /**
     *
     * @return value of rank 
     */
    public int getValue(){
        return r.getValue();
    }
    
    /**
     * gets the previous cards values
     * @return
     */
    public Rank getPrevious(){
        int currentIndex=r.ordinal();
        if(currentIndex==0){ //condition when card is TWO as ordinal gives 0
            currentIndex=13; //moves to 13 so next statement will return ACE
        }
        Rank previous = Rank.values()[currentIndex-1];
        return previous;
    }
    
    /**
     * 
     * @return rank
     */
    public Rank getRank(){
        return r;
    }
    
    /**
     * 
     * @return suit
     */
    public Suit getSuit(){
        Suit currentSuit=Suit.values()[s.ordinal()];
        return currentSuit;
    }
    
    /**
     * 
     * @param rankInput
     * @param suitInput
     * @return
     */
    public static Card createCard(Rank rankInput, Suit suitInput){
        Card card = new Card(rankInput, suitInput);
        return card;
    }
    
    /**
     * Takes two cards as an input and sums the values of these together,
     * for example 7 of hearts plus 9 of diamonds would return int 16
     * @param cardOne
     * @param cardTwo
     * @return
     */
    public static int sum(Card cardOne, Card cardTwo){
        int summedCards = cardOne.getValue()+cardTwo.getValue();
        return summedCards;
    }
    
    /**
     * runs sum method on two cards (used in firstCardsDealt method in 
     * BlackjackDealer) to check if they match the conditions of a blackjack
     * @param cardOne
     * @param cardTwo
     * @return
     */
    public static boolean isBlackjack(Card cardOne, Card cardTwo){
        return sum(cardOne,cardTwo) == 21;
    }
    
    /**
     * class which contains the compare function, takes two cards as an input
     * then returns the difference
     */
    public static class CompareAscending implements Comparator<Card>{
        @Override
        public int compare(Card c, Card t) {
            int g = t.compareTo(c);
            if(g==0){
                return c.compareToSuit(t);
            }
            return g;
        }
    } 
    
    /**
     * compares two cards and 'sorts' them into the order given with the 
     * enum instantiation
     */
    public static class CompareSuit implements Comparator<Card>{
        @Override
        public int compare(Card c, Card t) {
            c.compareTo(t);
            return c.compareToSuit(t);
        }
    }
    
    /**
     * converts rank and suit into printable string data types
     * @return
     */
    @Override
    public String toString()
    { return r + " of " + s; }
}
