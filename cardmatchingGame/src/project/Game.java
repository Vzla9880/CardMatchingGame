package project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Game extends JFrame implements ActionListener {
    // Global constants
    private final String BLUE_COLOR = "#9dc4ed", RED_COLOR="#f3dade", ORANGE_COLOR = "#f8e6d8", GREEN_COLOR="#d4efec"; // Colors
    static final int FRAME_HEIGHT = 700; // height for the frame
    static final int NUM_CARDS_EASY = 4, NUM_CARDS_MED = 8, NUM_CARDS_HARD = 18; // number of cards based on difficulty
    static final int TIME_LIMIT = 60000; // time in milliseconds (1000 = 1 second)
    //create HashMap to get time limit based on difficulty
    static final Map<String, Integer> TIME_LIMITS = new HashMap<String, Integer>(){
        {
            put("easy", TIME_LIMIT);
            put("medium", TIME_LIMIT * 2);
            put("hard", TIME_LIMIT * 3);
        }
    };
    static final String[] IMAGES = {"fox","turtle","otter","raccoon","hippopotamus","boar","sloth","anaconda","badger","duck","ladybug","bear","cat","elephant","bird","koala","giraffe","monkey"};
    private int numberOfCards; // number of cards inputed
    private Card[] cards; // Array to store all the cards that will be used for the gameplay
    private Card currentCardFlipped; // the current card that is flipped up
    private Player player1, player2; // Players
    private Player winner; // The player who won the game, this will be used to write a record to the txt file
    private Player currentPlayer; // the current player
    private int numberOfMatches; // the total number of matches. The game ends when the number of matches equals the number of cards
    private Timer timer; // Timer (for single player only)
    private boolean isSinglePlayer; // only two game modes, use a boolean flag
    private String difficulty;
    // Fields for Timer
    private int timeRemaining;
    // LeaderBoard
    private LeaderBoard leaderBoard;
    private int imgSize = 96; // size of the card images
    // GUI fields
    private JLabel title, currentPlayerLabel, player1ScoreLabel, player2ScoreLabel, timerLabel;
    private JButton resetButton, exitButton;
    private JPanel cardsJPanel, gamePanel;

    public Game(int numberOfPlayers, String difficulty, String[] playerNames) {
        this.difficulty = difficulty;
        // determine the game mode
        this.isSinglePlayer = numberOfPlayers == 1;
        createPlayers(playerNames);
        // set the current player
        this.currentPlayer = this.player1;
        // initialize number of matches
        this.numberOfMatches = 0;
    }

    public void init() {
        // We have the number of players and difficulty
        this.numberOfCards = getCardsByDifficulty(this.difficulty);
        // Get Image size based on difficulty
        setImageSize();
        // create the cards based on the difficulty selected
        createCards(this.numberOfCards);
        // add elements to GUI
        createGUI();
        // If single player mode, start the timer
        if (this.isSinglePlayer) {
            initTimer();
        }
        // make our frame visible
        this.setVisible(true);
    }
    /** Setup Methods **/
    public void createPlayers(String[] playerNames) {
        // create player 1
        this.player1 = new Player(1, playerNames[0]);
        // create player 2 (optional)
        if (!this.isSinglePlayer) {
            this.player2 = new Player(2, playerNames[1]);
        }
    }

    private void initTimer() {
        this.timeRemaining = TIME_LIMITS.get(this.difficulty);
        this.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining -= 1000;
                timerLabel.setText("Time Remaining: \n" + String.valueOf(timeRemaining / 1000));
                if (timeRemaining <= 0) {
                    // end the game
                    endGame();
                }
            }
        });
        timer.start();
    }

    // Creates the cards that will be used for the game and shuffles them as well
    public void createCards(int numCards){
        Card[] uniqueCards = new Card[this.numberOfCards * 2];
        // Create unique cards based on the number specified
        // add unique cards to a list
        for (int i = 0; i < numCards; i++ ) {
            String imgName = IMAGES[i];
            Image image = loadImage(imgName); //load the image
            // use the loaded image as an imageIcon to resize the image
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(this.imgSize, this.imgSize, Image.SCALE_SMOOTH));
            Card newCard = new Card(i, IMAGES[i], imageIcon);
            // add newCard to array
            uniqueCards[i] = newCard;
        }
        // using the unique cards, add their duplicate counterparts to the array
        for (int i = this.numberOfCards; i < (this.numberOfCards * 2); i ++) {
            int oldCardID = i - this.numberOfCards;
            Card oldCard = uniqueCards[oldCardID];
            Card newCard = new Card(i, oldCard.getCardName(), oldCard.getCardImage());
            uniqueCards[i] = newCard;
        }
        // using unique cards, add and shuffle
        List<Card> cardTypes = Arrays.asList(uniqueCards);
        // Create an array of size specified
        this.cards = new Card[this.numberOfCards * 2];
        // Shuffle the list to randomize the card order
        Collections.shuffle(cardTypes);
        // Fill the array with the shuffled cards
        int index = 0;
        for (int i = 0; i < this.numberOfCards * 2; i++) {
            this.cards[i] = cardTypes.get(index);
            this.cards[i].addActionListener(this);
            index = (index + 1) % cardTypes.size();
        }
    }
    
    //get  number of cards by difficulty
    public int getCardsByDifficulty(String difficulty) {
        switch (difficulty) {
            case "easy":
                return NUM_CARDS_EASY;
            case "medium":
                return NUM_CARDS_MED;
            case "hard":
                return NUM_CARDS_HARD;
            default:
                return NUM_CARDS_MED;
        }
    }
    
    public void setImageSize() {
    	switch (this.difficulty) {
    	case "hard":
    		this.imgSize = 50;
    		break;
    	case "medium":
    		this.imgSize = 75;
    		break;
    	default:
    		this.imgSize = 96;
    		break;
    	}
    }

    /** GUI METHODS **/
    private void createGUI() {
        // set the frame for the application
        this.setSize(new Dimension(1100, FRAME_HEIGHT));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.decode(BLUE_COLOR));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.decode(BLUE_COLOR));
        this.gamePanel = new JPanel(new BorderLayout());
        this.gamePanel.setBackground(Color.decode(BLUE_COLOR));
        this.gamePanel.setBorder(new EmptyBorder(50,50,50,50));
        // init components
        gamePanel.add(getGameTitle(), BorderLayout.NORTH);
        this.cardsJPanel = getCardPanel();
        gamePanel.add(this.cardsJPanel, BorderLayout.CENTER);
        gamePanel.add(getPlayerPanel(), BorderLayout.EAST);
        this.add(gamePanel, BorderLayout.CENTER);
        //getContentPane().add(pizzaPanel, BorderLayout.CENTER);
    }

    // initialize title
    public JLabel getGameTitle(){
        title = new JLabel("The Card Matching Game", SwingConstants.CENTER);
        title.setBackground(Color.decode(ORANGE_COLOR));
        title.setOpaque(true);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        title.setHorizontalAlignment(JTextField.CENTER);
        return title;
    }

    public JPanel getCardPanel(){
        JPanel cardsPanel = new JPanel();
        int numRows = (int) Math.ceil(Math.sqrt(this.numberOfCards * 2));
        int numCols = (int) Math.ceil((this.numberOfCards * 2) / (double) numRows);
        cardsPanel.setLayout(new GridLayout(numRows, numCols));
        cardsPanel.setBackground(Color.decode(BLUE_COLOR));
        cardsPanel.setBorder(new EmptyBorder(10,10,10,10));
        // add buttons to the panel
        for (int i = 0; i < this.cards.length; i++) {
            Card currentCard = this.cards[i];
            JPanel cardWrapper = new JPanel(new BorderLayout());
            cardWrapper.setBorder(new EmptyBorder(5, 5, 5, 5));
            cardWrapper.setBackground(Color.decode(BLUE_COLOR));
            cardWrapper.add(currentCard);
            cardsPanel.add(cardWrapper);
        }
        return cardsPanel;
    }

    public JPanel getPlayerPanel() {
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setPreferredSize(new Dimension(260, FRAME_HEIGHT - 30));
        playerPanel.setBackground(Color.WHITE);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(25,30,30,30));
        // Show the current player
        this.currentPlayerLabel = new JLabel("<html> Current Player: \n" + this.player1.getPlayerName() + "</html>");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerPanel.add(currentPlayerLabel, BorderLayout.NORTH);
        // Create a panel for the player scores
        JPanel scoresPanel = new JPanel(new GridLayout(2, 1));
        scoresPanel.setBackground(Color.WHITE);
        // Show player 1 score
        this.player1ScoreLabel = new JLabel(this.player1.getPlayerName() + "'s Score: 0", SwingConstants.LEFT);
        player1ScoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoresPanel.add(player1ScoreLabel);
        if (this.isSinglePlayer) {
            this.timerLabel = new JLabel(String.valueOf(this.timeRemaining));
            this.timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
            scoresPanel.add(this.timerLabel);
        } else {
            // Show player 2 score
            this.player2ScoreLabel = new JLabel( this.player2.getPlayerName() + "'s Score: 0", SwingConstants.LEFT);
            player2ScoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            scoresPanel.add(player2ScoreLabel);
        }
        playerPanel.add(scoresPanel, BorderLayout.CENTER);
        // Add buttons to the panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        // Add a reset button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        resetButton.setBackground(Color.decode(GREEN_COLOR));
        buttonPanel.add(resetButton);
        // Add an exit button
        exitButton = new JButton("Exit");
        exitButton.setBackground(Color.decode(RED_COLOR));
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);
        playerPanel.add(buttonPanel, BorderLayout.SOUTH);
        return playerPanel;
    }

    /** MAIN LOGIC METHODS **/
    public void checkPlayerChange() {
        // method to handle the change of player
        // If single player mode, do nothing
        // only make changes if two player mode
        if (!this.isSinglePlayer) {
            // if current player is player 1, set to player 2
            if (this.currentPlayer.getPlayerId() == 1) {
                this.currentPlayer = this.player2;
            } else {
                // if current player is player 2, set to player 1
                this.currentPlayer = this.player1;
            }
            this.currentPlayerLabel.setText("<html> Current Player: \n" + this.currentPlayer.getPlayerName() + "</html>");
        }
    }

    public void updatePlayerScoreGUI() {
        if (this.isSinglePlayer) {
            // 1 player mode
            this.player1ScoreLabel.setText(this.player1.getPlayerName() + "'s Score: " + this.player1.getPlayerScore());
        } else {
            // 2 player mode
            if (this.currentPlayer.getPlayerId() == 1) {
                // update player one score label
                this.player1ScoreLabel.setText(this.player1.getPlayerName() + "'s Score: " + this.player1.getPlayerScore());
            } else {
                // update player two score label
                this.player2ScoreLabel.setText(this.player2.getPlayerName() + "'s Score: " + this.player2.getPlayerScore());
            }
        }
    }

    public void checkWinCondition() {
        if (this.numberOfMatches == this.numberOfCards) {
            // end the game by closing the frame
            if (this.isSinglePlayer) {
                // player 1 is always the winner by default
                this.winner = player1;
            } else {
                String result;
                // 2 players
                if (this.player1.getPlayerScore() == this.player2.getPlayerScore()) {
                    result = "TIE!";
                } else if (this.player1.getPlayerScore() > this.player2.getPlayerScore()) {
                    this.winner = this.player1;
                    result = this.winner.getPlayerName() + " Won!";
                } else {
                    this.winner = this.player2;
                    result = this.winner.getPlayerName() + " Won!";
                }
                JOptionPane.showMessageDialog(null,result);
            }
            endGame();
        }
    }

    // method to handle the end of a game, the goal is to retrieve the winner data and create a new instance of a Leadboard class
    private void endGame() {
        // Stop the timer if in single player mode
    	if (this.isSinglePlayer) {
    		 this.timer.stop();
    	}
        // prepare the data for the leaderBoard
        // determine the game mode
        String gameMode = this.isSinglePlayer ? "single" : "two";
        // If the game ended with no data to process just show the leader board
        //that is in case of a tie or if not all cards were matched
        if (this.winner != null) {
            int value;
            if (this.isSinglePlayer) { //value = time
                value = (TIME_LIMITS.get(this.difficulty) - this.timeRemaining) / 1000; // get the amount of time Player 1 took to finish
            } else {
                // two player mode
                value = this.winner.getPlayerScore(); //value = score/number of cards turned
            }
            this.leaderBoard = new LeaderBoard(gameMode, this.difficulty,this.winner.getPlayerName(), value);
        } else {
            this.leaderBoard = new LeaderBoard(gameMode, this.difficulty);
        }
        // close this GUI panel
        this.dispose();
        // show the leaderBoard
        this.leaderBoard.showGUI();
    }

    public void handleCardAction(Card card) {
        // check how many cards in the turn array
        // if the array is empty, add the current card to it
        if (this.currentCardFlipped == null) {
            this.currentCardFlipped = card;
            return;
        }
        // if the array is NOT empty, then compare the two cards
        String firstName = currentCardFlipped.getCardName();
        String secondName = card.getCardName();
        // if the cards MATCH, the checkPLayerChange() method will not be called, so the player that succesfully
        // matched a pair goes again
        if (firstName.equalsIgnoreCase(secondName)) {
            // cards remain face up
            this.currentCardFlipped.setTurnable(false);
            card.setTurnable(false);
            // current player get's one point
            int prevScore = this.currentPlayer.getPlayerScore();
            this.currentPlayer.setPlayerScore(prevScore + 1);
            // current player turn remains
            // update the number of matches and check if it matches the number of unique cards
            this.numberOfMatches ++;
            // update appropriate player GUI
            updatePlayerScoreGUI();
            // check if the game is over
            checkWinCondition();
        } else {
            // Cards do not match
            Card theCard =  this.currentCardFlipped;
            cardEventListener(false);
            // Flip both cards back after a delay, this is to allow the user see the card
            Timer cardFlipTimer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   theCard.flipCard();
                   card.flipCard();
                   cardEventListener(true);
                   checkPlayerChange();
                }
            });
            cardFlipTimer.setRepeats(false);
            cardFlipTimer.start();
        }
        // empty the current card turn array
        this.currentCardFlipped = null;
    }

    // method to enable or disable action listeners from the cards. This is to prevent players from making illegal card flips during the delay period of card flipping.
    private void cardEventListener(boolean action) {
        if (action) {
            // add action listeners to all cards
            for (Card card : this.cards) {
                card.addActionListener(this);
            }
        } else {
            // remove action listeners from card
            for (Card card : this.cards) {
                card.removeActionListener(this);
            }
        }
    }

    public void handleReset() {
        // If 1 player mode
        if (isSinglePlayer) {
            // reset the timer
            this.timeRemaining = TIME_LIMITS.get(this.difficulty);
            // score set to 0
            this.player1.setPlayerScore(0);
            // update the GUI
            this.timerLabel.setText("Time Remaining: \n" + String.valueOf(this.timeRemaining / 1000));
        } else {
            // IF 2 player mode
            // BOTH players score set to 0
            this.player1.setPlayerScore(0);
            this.player2.setPlayerScore(0);
        }
        updatePlayerScoreGUI();
        // All cards flipped upside down
        for (Card card : this.cards) {
            card.resetCard(); //turn cards upside down
        }
        // shuffle the cards
        // Convert array to list
        List<Card> list = Arrays.asList(this.cards);
        // Shuffle the list
        Collections.shuffle(list);
        // Convert list back to array
        list.toArray(this.cards);
        // redraw the card GUI
        this.gamePanel.remove(this.cardsJPanel); //removes existing cards panel...
        this.cardsJPanel = getCardPanel();
        this.gamePanel.add(this.cardsJPanel); //...and adds a new one
        this.gamePanel.revalidate();
        this.gamePanel.repaint();
        //
    }

    /** UTILITY METHODS **/
    // method to load the images onto the application
    public Image loadImage(String name) {
        String path = "assets/" + name + ".png";
        File file = new File(path);
        Image img = null;
        try { //loads the image
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // action will be performed based on the card selected
        // RESET
        if (e.getSource() == resetButton) {
            System.out.println("You want to reset the game");
            handleReset();
        } else if (e.getSource() == exitButton) {
            // QUIT
            System.exit(0);
        } else {
            for (Card card : this.cards) {
                if (e.getSource() == card) {
                    // handle the card action
                    if (!card.isFlipped() && card.turnable()) {
                        //only handle this logic if the card HAS NOT been yet flipped, use some property of the card
                        card.flipCard();
                        handleCardAction(card);
                    }

                }
            }
        }
    }
}

