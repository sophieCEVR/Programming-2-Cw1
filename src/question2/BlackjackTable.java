package question2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
/**
 *
 * @author jxy13mmu
 */
public final class BlackjackTable implements Serializable{
    static final long serialVersionUID = 69;
    
    Hand prevHand = new Hand();
    LinkedList<Card> cardCounting = new LinkedList();
    ArrayList<Player> players;
    int maxPlayers = 8;
    int minBet = 1;
    int maxBet = 500;
    Deck deck = new Deck();
    int gameType=0;
    Dealer dealer = new BlackjackDealer(deck); 
    int roundCount=1;
    int roundSkipper = 1;
    static char gameMenuChoice='s';
    static int innerChoice=1;
    
    public static void main(String [] args) {
        gameMenu();
    }
        
    /***Initial Startup menu allowing for new game, loading and quitting***/
    public static void gameMenu(){
        System.out.println("Welcome to Blackjack\nPlease enter your menu choice"
                + "\n[n] for New game\n[l] to load game\n[q] to quit the game");
        Scanner scan = new Scanner(System.in);
        boolean valid=false;
        while(!valid){
            BlackjackTable newTable;
            if(gameMenuChoice!='l'){
                gameMenuChoice = scan.next().toLowerCase().charAt(0);
            }
        switch(gameMenuChoice){  
                case 'n'://new game
                    newTable = new BlackjackTable();
                    valid=true;
                    return;
                case 'l'://load game
                    innerChoice=2;
                    newTable = readFromFile("savedGame.ser");
                    newTable.gameSetup();
                    valid=false;
                    return;
                case 'q'://quit 
                    valid=true;
                    return;
                default:
                    System.out.println("Please enter a correct menu choice"
                    + "\n[n] for New game\n[l] to load game\n[q] to quit the game");
            }
        }
    }
    
    /***Method continues the game, saves the game, allows for round skipping***/
    public void gameSetup(){
        Scanner scan = new Scanner(System.in);  
        boolean cont = true;
        char continueChoice;
        dealer.assignPlayers(players);
        while(cont){       
            if(roundCount==1){
                deck.shuffle();
                roundCount = playRound();
            }
            roundSkipper=1;
            while(!allBust() || cont){
                if(gameMenuChoice!='l' || innerChoice==1){
                System.out.println("Would you like to continue? [y] / [n]? ");
                continueChoice = scan.next().toLowerCase().charAt(0);
                }
                else{
                    continueChoice='y';
                }
                switch(continueChoice){  
                    case 'n'://continue
                        System.out.println("Would you like to save? [y] / [n]? ");
                        char saveChoice = scan.next().toLowerCase().charAt(0);
                        switch(saveChoice){  
                            case 'n'://don't save and exit
                                cont=false;
                                return;
                            case 'y'://quit    
                            writeToFile("savedGame.ser", this);
                        }
                        cont=false;
                        return;
                    case 'y'://quit
                        cont=true;
                        if (innerChoice==1 ){
                            if(gameType!=2){
                                int check=(-1);
                                System.out.println("How many rounds would you like to run?");
                                while(!scan.hasNextInt()){
                                    System.out.println("Please enter an integer between [0] and [10000]");
                                    scan = new Scanner(System.in);
                                }    
                                check = scan.nextInt();
                                while(check<0 || check>10000) {
                                        System.out.println("Please enter between [0] and [10000]"); 
                                        check = scan.nextInt();
                                    }
                                roundSkipper = check;
                            }
                        }
                        for(int i=0; i<roundSkipper; i++){
                            if(allBust()){
                                if(gameType!=2){
                                    System.out.println("Your players lost all their money on round " + (roundCount-1));
                                }
                                else{
                                    System.out.println("You lost all your money on round " + (roundCount-1) + "[GAME OVER]");
                                }
                                cont=false;
                                break;
                            }
                            roundCount = playRound();
                        }
                        innerChoice=1;
                        break;
                }
            }
        }
    }
    
    /*********Method to chose game type and generate the players for this******/
    public void createPlayers(){
        System.out.println("Please select a game type"
                + "\n[b] - basic game\n[h] - human game\n[i] - intermediate game"
                + "\n[a] - advanced game");
        Scanner scan = new Scanner(System.in);
        boolean valid=false;
        while(!valid){
            char input = scan.next().toLowerCase().charAt(0);
            switch(input){  
                case 'b'://basic game
                    basicGame();
                    valid=true;
                    return;
                case 'h'://human game
                    humanGame();
                    valid=true;
                    return;
                case 'i'://Intermediate game
                    IntermediateGame();
                    valid=true;
                    return;
                case 'a'://Advanced game
                    AdvancedGame();
                    valid=true;
                    return;
                default://incorrect entry 
                    System.out.print("Please enter a correct menu choice\n");
                    valid=false;
            }
        }
    }
    
    /*******Used to check if all players have lost all their money************/
    public boolean allBust(){
        int bust=0;
        if(gameType==2){
            int pID=1;
            for(Player p: players){
                if(pID==2){
                    if(p.getBalance()==0){
                        return true;
                    }
                }
                pID++;
            }
        }
        for(Player p: players){
            if(p.getBalance()<=0){
                bust++;
            }
        }
        return players.size()==bust;
    }
    
    
    public BlackjackTable() {
        Scanner scan = new Scanner(System.in);  
        boolean newGame=true;
        createPlayers();
        gameSetup();
        while(newGame){
            System.out.println("Would you like to start a new game ?  [y] / [n]");
            char inNew = scan.next().toLowerCase().charAt(0);
            switch(inNew){
                case'y'://create a
                    roundCount=0;
                    deck.newDeck();
                    deck.shuffle();
                    dealer = new BlackjackDealer(deck);
                    BlackjackTable newTable = new BlackjackTable();
                    return;
                case'n':
                    newGame=false;
                    return;
                default: 
                    System.out.println("Please enter a correct choice");
            }
        }
    }
    
    
    /************************Method for each round************************/

    public int playRound(){
        if(innerChoice==2){
            printLastRound();
        }
        if(deck.size()<13){
            deckRenew();
            return roundCount;
        }
        System.out.println("/*************************************************************/"); 
        System.out.println("/                            ROUND " + roundCount); 
        System.out.println("/*************************************************************/"); 

        /************Player balance displays*************/
        System.out.println("/**********************Player balances************************/"); 
        int i=1;
        for(Player p:players){
            prevHand = new Hand();
            System.out.println("                       Player " + i + " balance: " + p.getBalance());
            prevHand.addHand(p.newHand());
            if(i==1){
                prevHand.addHand(dealer.getHand());
                dealer.emptyHand();
            }
            i++;
            /***********CARD COUNTING SECTION*******************/
            if(gameType==4){
                for(Card c : prevHand){
                    cardCounting.add(c);
                }
            }
        }
        for(Player p:players){ 
            p.viewCards(cardCounting);
        }
        cardCounting.clear();
        
        /***********************Bet taking*********************/
        for (Iterator<Player> it = players.iterator(); it.hasNext();) {
            //iterates through players
            Player p = it.next();
            int betMinCheck=(-1);
            int betMaxCheck=(900);
            int runNo=1;
            //while bet is within both min and max, 2 variables needed for this
            while(betMinCheck<minBet || betMaxCheck>maxBet){
                if(runNo!=1){
                    System.out.println("Please enter a valid bet between [1] and [500]");
                }
                //if p balance==0 player is bust, so set values to move on loop
                if(p.getBalance()==0){
                    betMinCheck=1;
                    betMaxCheck=1;
                }
                //bets are taken here
                dealer.takeBets(p); 
                //two loops below are to allow for human game else errors 
                //are caused in different game types
                if(p.getBalance()!=0 && p.getBet()>0){
                    betMaxCheck=p.getBet();
                    betMinCheck=1;
                }
                if(p.getBalance()!=0 && p.getBet()<0){
                    betMaxCheck=1;
                    betMinCheck=p.getBet();
                }
                runNo++;
            }
        }
        
        /************Player Bet displays*************/
        System.out.println("\n/************************Player bets**************************/"); 
        i=1;
        for(Player p: players){
            if(p.getBalance()>0){
                System.out.println("                       Player " + i + " bet : " + p.getBet());
            }
        i++;
        }
        
        /***********************Game play***********************/
        dealer.dealFirstCards();
        i=1;
        for(Player p: players){
            if(p.getBalance()>0){
                if(deck.size()<13){ 
                    deckRenew();
                    return roundCount;
                }
                else{
                    System.out.println("\n/**************************Player " + i + "**************************/");
                    dealer.play(p);                 
                }             
            }
        i++;
        }
        int dealersTotal = dealer.playDealer();
        if(dealersTotal>0){
            System.out.println("Dealer Total: " + dealersTotal);
        }
        else{
            System.out.println("Dealer went bust, everybody (with less than 22) wins!");
        }
        dealer.settleBets();
        return roundCount+1;
    }
    
    /**************Checks for @least 13 cards in deck on each hand*********/
    public void deckRenew(){
        if(deck.size()<13){ 
            cardCounting.clear();
            prevHand = new Hand();
            for(Player s: players){
                s.newDeck();
            }
            deck.newDeck();
            deck.shuffle();
            System.out.println("/******************NOT ENOUGH CARDS IN DECK,******************/"); 
            System.out.println("/************ROUND RESTARTED WITH NEW SHUFFLED DECK***********/\n\n"); 
            roundSkipper++;
            }
    }
    
    /***Returns the deck***/
    public Deck getDeck(){
        return deck;
    }
    
    
    /**************************Game Types******************************/
    
    //game with 4 basic players
    public void basicGame(){
        gameType=1;
        Player p1 = new BasicPlayer(); 
        Player p2 = new BasicPlayer(); 
        Player p3 = new BasicPlayer(); 
        Player p4 = new BasicPlayer(); 
        players = new ArrayList();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        System.out.println("All players are basic players");
    }
    
    //human vs. basic player 
    public void humanGame(){
        gameType=2;
        Player p1 = new BasicPlayer(); 
        Player p2 = new HumanPlayer(); 
        players = new ArrayList();
        players.add(p1);
        players.add(p2);
        System.out.println("Player 1 is basic player, player 2 is human(you!)");
    }
    
    //same as basicPlayers but with intermediatePlayers       
    public void IntermediateGame(){
        gameType=3;
        Player p1 = new IntermediatePlayer(); 
        Player p2 = new IntermediatePlayer(); 
        Player p3 = new IntermediatePlayer(); 
        Player p4 = new IntermediatePlayer(); 
        players = new ArrayList();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        System.out.println("All players are intermediate players");
    }
    
    //basic, intermediate and advanced player
    public void AdvancedGame(){
        gameType=4;
        Player p1 = new BasicPlayer(); 
        Player p2 = new IntermediatePlayer(); 
        Player p3 = new AdvancedPlayer(); 
        players = new ArrayList();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        System.out.println("Player 1: Basic Player, Player 2: Intermediate "
                + "player, Player 3: Advanced Player");
    }
    
    /**********************Serialisation stuff*************************/
    
    public static void writeToFile(String file, BlackjackTable theTable){

        try {
           FileOutputStream fileOut =
           new FileOutputStream(file);
           ObjectOutputStream out = new ObjectOutputStream(fileOut);
           out.writeObject(theTable);
           out.close();
           fileOut.close();
           System.out.printf("Serialized data is saved in savedGame.ser\n");
        } catch (IOException i) {
           i.printStackTrace();
        }
    }
    
    public static BlackjackTable readFromFile(String file){
        BlackjackTable theTable = null;
        try {
             FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn);
             theTable = (BlackjackTable) in.readObject();
             in.close();
             fileIn.close();
        } 
        catch (IOException i) {
             i.printStackTrace();
             return theTable;
        } 
        catch (ClassNotFoundException c) {
             System.out.println("table class not found");
             c.printStackTrace();
             return theTable;
          }
        return theTable;
    }
    
    public void printLastRound(){
        System.out.println("STATS FROM LAST ROUND");
        int i=1;
        for(Player p:players){
            System.out.println("/**********Player " + i + "**********/"); 
            System.out.println("Bet : " + p.getBet());
            System.out.println("Hand : \n" + p.getHand());
            System.out.println("Hand total : " + p.getHandTotal());
            System.out.println("Balance balance : " + p.getBalance() + "\n");
            i++;
        }
        System.out.println("/**********Dealer hand**********/\n" + dealer.getHand());
    }
}
