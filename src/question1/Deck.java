package question1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author jxy13mmu
 */
public class Deck implements Serializable, Iterable<Card> {
    static final long serialVersionUID = 112;
    private final LinkedList<Card> deck = new LinkedList(); //linkedList chose for faster manipulation and no bit shifting required

    /**
     * Deck constructor, assigns card values in standard order
     */
    public Deck(){
        int x=0;
        int m=0;
            for(int y=0; y<52; y++)
            {                            
                if(x==13)
                {   
                    m++;
                    x=0;
                }
                x++;
                Card.Suit suitSelection = Card.Suit.values()[m];
                Card.Rank rankSelection = Card.Rank.values()[y%13];
                deck.add(new Card(rankSelection, suitSelection));
            }
        }   
    
    /**
     * toString converts the deck to a string
     * @return
     */
    @Override
    public String toString(){
        String fullList = "";
        int sizeOfDeck = deck.size();
        for(int y=0; y<sizeOfDeck; y++)
        { 
            String cardCurrent = deck.get(y).toString();
            fullList += cardCurrent + "\n";
        }
        return fullList;
    }
    
    /**
     * randomly shuffles the deck by grabbing a card at a random place
     * and putting it back into the deck at another random place.
     * Performs 52*3 times to ensure a full shuffle. 
     */
    public void shuffle(){
        for(int i=3; i>0; i--){
            for(int y=52; y>0; y--){
                Random randomNum = new Random();
                int randomSelector = randomNum.nextInt();
                //modulo 52 ensures in deck range
                randomSelector = randomSelector%52;
                //if statement ensures it is in the range 0-52 and not (-43)etc.
                if(randomSelector<0){
                    randomSelector=randomSelector*(-1);
                }
                Card cardInShuffle = deck.remove(randomSelector);
                int randomPlaceInDeck = randomNum.nextInt();
                randomPlaceInDeck = randomPlaceInDeck%52;
                if(randomPlaceInDeck<0){
                    randomPlaceInDeck=randomPlaceInDeck*(-1);
                }
                deck.add(randomPlaceInDeck, cardInShuffle);
            }
        }
    }
    
    /**
     * removes the card from the top of the deck
     * @return card
     */
    public Card deal(){
        Card topOfDeck = (Card) deck.remove(0);
        return topOfDeck;
    }
    
    /**
     * 
     * @return deck as linkedList
     */
    public LinkedList<Card> getDeck(){
        return deck;
    }
    
    /**
     *
     * @return deck size as int
     */
    public int size(){
        return deck.size();
    }
    
    /**
     *  removes all the cards currently in the deck
     * generates a new deck using same method as Deck() constructor
     */
    public void newDeck(){
        deck.removeAll(deck);
        int x=0;
        int m=0;
            for(int y=0; y<52; y++)
            {                            
                if(x==13)
                {   
                    m++;
                    x=0;
                }
                x++;    
                Card.Suit suitSelection = Card.Suit.values()[m];
                Card.Rank rankSelection = Card.Rank.values()[y%13];
                deck.add(new Card(rankSelection, suitSelection));
            }
    }

    /**
     * Iterates though the cards 
     * @return
     */
    @Override
    public Iterator<Card> iterator() {
        return new SecondCardIterator();
    }
    
    private class SecondCardIterator implements Iterator<Card>{
        int pos=(-2);
        @Override
        public boolean hasNext()
        {
            return (pos+2)<deck.size();
        }
        @Override
        public Card next() {
            return deck.get(pos+=2);
        }
    } 
            
    /**
     * Used on a singular card to add it to the deck.
     * @param c
     */
    public void addToDeck(Card c){
        deck.add(c);
    }   
    
    /**
     * clears the deck of all cards. 
     */
    public void emptyDeck(){
        deck.clear();
    }
    
    /*******************Serialisation******************/
    
    /**
     * saves the deck to the file deck.ser 
     * @param file
     * @param deck
     */
    public static void writeToFile(String file, Deck deck){

        try {
           FileOutputStream fileOut = new FileOutputStream(file);
           ObjectOutputStream out = new ObjectOutputStream(fileOut);
           out.writeObject(deck);
           out.close();
           fileOut.close();
           System.out.printf("Serialized data is saved in deck.ser\n");
        } catch (IOException i) {
           i.printStackTrace();
        }
    }
    
    /**
     * reads deck from file deck.ser
     * @param file
     * @return
     */
    public static Deck readFromFile(String file){
        Deck deck = null;
        try {
             FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn);
             deck = (Deck) in.readObject();
             in.close();
             fileIn.close();
        } 
        catch (IOException i) {
             i.printStackTrace();
             return deck;
        } 
        catch (ClassNotFoundException c) {
             System.out.println("table class not found");
             c.printStackTrace();
             return deck;
          }
        return deck;
    }

    
    public static void main(String[] args) {
        /******Output shows full deck of cards created******/
        Deck d1 = new Deck();
        String fullDeckString = d1.toString();
        System.out.print("\nFull deck\n" + fullDeckString);
        d1.shuffle();
        /******Output shows shuffled deck******/
        System.out.print( "\n\nShuffled Deck\n" + d1.toString());
        Card dealtCard = d1.deal();
        /******Output shows card just dealt******/
        System.out.print( "\n\nDealt Card\n" + dealtCard.toString());
        /******Output shows cards remaining in deck******/
        System.out.print( "\n\nRemaining Deck Card\n" + d1.toString());
        /******Output shows the size of the deck with card removed******/
        System.out.print( "\n\nRemaining Deck Size\n" + d1.size());
        /******Output shows new reinstantiated deck******/
        System.out.print( "\n\nreinstantiated deck\n" + d1.toString());   
        /******Output shows new iterated deck******/
        Deck d2 = new Deck();
        System.out.print( "\n\nIterated deck\n");
        for(Iterator id2 = d2.iterator(); id2.hasNext();){
            System.out.println(id2.next());
        }
        /******Serialisation******/
        Deck d3 = new Deck();
        d3.emptyDeck();
        for(Iterator id2 = d2.iterator(); id2.hasNext();){
            //adding to d3 for Serialisation ouputs
            d3.addToDeck((Card) id2.next());
        }
        System.out.print( "\n\nSerialisation\n");
        writeToFile("deck.ser", d3);
        System.out.print( "\nLoaded Iterated Deck \n");
        Deck d4 =readFromFile("deck.ser");
        System.out.print(d4.toString());
        
    }
}
