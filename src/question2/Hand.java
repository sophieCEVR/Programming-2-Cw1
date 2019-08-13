package question2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author jxy13mmu
 */
public final class Hand implements Serializable, Iterable<Card>  {
    static final long serialVersionUID = 102;
    private final LinkedList<Card> hand = new LinkedList();
    int[] cardCount = new int[13];
    private ArrayList<Integer> handValue = new ArrayList(); 
    
    /**
     * Constructs an empty hand
     */
    public Hand(){}
    
    /**
     * constructs a hand with a card array input
     * @param card
     */
    public Hand(Card[] card){
        hand.addAll(Arrays.asList(card));
        cardCounts();
        totalValue();
    }   
    
    /**
     * creates a hand using an existing hand
     * @param handIn
     */
    public Hand(Hand handIn){
        hand.addAll(handIn.hand);
        totalValue();
        cardCounts();
    }
    
    /**
     * counts how many cards are in the hand
     */
    public void cardCounts(){
        for(int y=0; y<13; y++){
            cardCount[y] = 0;
        }
        for(int i=0; i<(hand.size()); i++){
            Card.Rank curRank = (hand.get(i)).getRank();
            cardCount[curRank.ordinal()]++;
        }
    }
    
    /**
     * creates an arrayList which holds all possible hard and soft values 
     * check for aces and then extends the arraylist based on the amount of 
     * aces held.
     */
    public void totalValue(){
        handValue = new ArrayList();
        int aceCount = 0;
        int nonAce = 0;
        for(Card c:hand){
            if(c.getRank()==Card.Rank.ACE){
                aceCount++;
            }
            else{
                nonAce+=c.getValue();
            }
        }
        int temp = aceCount;
        for(int i=aceCount;i>=0;i--)
        {
            handValue.add(nonAce+temp);
            temp+=10;
        }
    }

    /**
     * Calls totalValue and then returns the handValue arrayList created
     * by the totalValue method.
     * @return
     */
    public ArrayList<Integer> getHandValue(){       
        totalValue();
        return handValue;
    }
    
    /**
     * adds one hand to the current hand whilst reserving the current hand
     * Calls totalValue and cardCounts to update these
     * @param handIn
     */
    public void addHand(Hand handIn){
        hand.addAll(handIn.hand);
        totalValue();
        cardCounts();
    }
    
    /**
     * adds a singular card into the current hand. 
     * Calls totalValue and cardCounts to update these
     * @param c
     */
    public void addCard(Card c){
        hand.add(c);
        totalValue();
        cardCounts();
    }
    
    /**
     * adds objects of supertype collection into the hand, with a specification
     * on card objects
     * Calls totalValue and cardCounts to update these
     * @param cCollection
     */
    public void addCollection(Collection<Card> cCollection){
        hand.addAll(cCollection);
        totalValue();
        cardCounts();
    }
    
    /**
     * removes the specified card from the hand 
     * Calls totalValue and cardCounts to update these
     * @param c
     * @return true or false if card found
     */
    public boolean removeCard(Card c){
        if(hand.remove(c)){
            totalValue();
            cardCounts();
            return true;
        } 
        return false;
    }
    
    /**
     * Takes in a hand and removes cards from both the hand the method is 
     * called on and the hand passed through the params
     * Calls totalValue and cardCounts to update these
     * @param handIn
     * @return boolean allpassed.
     */
    public boolean removeCardsInBoth(Hand handIn){
        boolean allpassed = true;
        for(int i=0; i<(handIn.size()); i++){
            if(hand.remove(handIn.get(i))==false){
                allpassed = false;
            }
        }
        cardCounts();
        totalValue();
        return allpassed;
    }
    
    /**
     * removes cards in hand at given int position.
     * Calls totalValue and cardCounts to update these
     * @param i
     * @return c, the card which was removed
     */
    public Card removeCardAt(int i){
        Card c = hand.get(i);
        hand.remove(i);
        totalValue();
        cardCounts();
        return c;
    }
    
    /**
     * finds the size() of the hand the method is called on
     * @return the int value of the hands size
     */
    public int size(){
        return hand.size();
    }
    
    /**
     * get the card at the given position i, this does not remove the card
     * from the hand
     * @param i
     * @return card c
     */
    public Card get(int i){
        Card c = hand.get(i);
        return c;
    }
    
    /**
     * count how many cards of the given suit s are in the hand
     * @param s
     * @return
     */
    public int countSuit(Card.Suit s){
        int suitCount=0;
        for(int i=0; i<(hand.size()); i++){
            Card c = hand.get(i);
            if(c.getSuit()==(s)){
                suitCount++;
            }
        }
        return suitCount; 
    }
    
    /**
     * counts the amount of carts in the hand with the given rank r
     * @param r
     * @return rankCount, an int containing the count
     */
    public int countRank(Card.Rank r){
        int rankCount=0;
        int handSize = hand.size();
        for(int i=0; i<handSize; i++){
            Card c = hand.get(i);
            if(c.getRank()==(r)){
                rankCount++;
            }
        }
        return rankCount; 
    }
    
    /**
     * returns true if the value of the hand is over the given int i,
     * else returns false
     * @param i
     * @return boolean true/false
     */
    public boolean isOver(int i){
        return handValue.get(0)>i;
    }
    
    /**
     * tests the hardest value of the hand.
     * If it is over the given int i, will return true, 
     * else will return false
     * @param i
     * @return boolean
     */
    public boolean isOverHard(int i){
        return handValue.get(handValue.size()-1)>i;
    }
    
    /**
     * reverses the current hand held
     * creates a empty hand, handRev, creates an empty linkedList, rev
     * adds all of the hand the method is called on to the empty linkedList rev
     * uses collections supertype method 'reverse' to reverse the linkedList rev
     * adds the reversed linked list to the empty hand handRev 
     * using addCollection
     * @return Hand handRev to give the reversed hand 
     */
    public Hand reverseHand(){
        Hand handRev = new Hand();
        LinkedList rev = new LinkedList();
        rev.addAll(hand);
        Collections.reverse(rev);
        handRev.addCollection(rev);
        return handRev;
    }

    /**
     * Method to sort the hand in ascending order
     * Sorts rank first then suit by their enum ordinal order
     */
    public void sortAscending(){
        Card.CompareAscending sort = new Card.CompareAscending();
        hand.sort(sort);
    }
    
    /**
     * Method to sort the hand in descending order
     * Sorts rank first then suit by their enum ordinal order
     */
    public void sortDescending(){
        Collections.sort(hand);//defaults to compareTo overriden
    }
    
    /**
     * returns a new iterator to iterate through cards in hand
     * @return
     */
    @Override
    public Iterator<Card> iterator(){
        Iterator<Card> it = hand.iterator();
        return it;
    }
    
    private class SortedIterator<Card> implements Iterator<Card>{
        int pos=0;
        @Override
        public boolean hasNext(){
            return pos<hand.size();
        }
        @Override
        public Card next() {
            return (Card) hand.get(pos++);
        }
    }
    
    /**
     * clears the hand so it contains nothing
     */
    public void emptyHand(){
        hand.clear();
    }
    
    /**
     * converts the hand into a string by iterating through the cards in the 
     * hand and returning the card toString
     * concatonates the toStrings of all the cards in the hand together into a
     * string variable called fullList
     * @return fullList
     */
    @Override
    public String toString(){
        String fullList = "";
        for(int y=0; y<(hand.size()); y++)
        { 
            String cardCurrent = hand.get(y).toString();
            fullList += cardCurrent + "\n";
        }
        return fullList;
    }
}
