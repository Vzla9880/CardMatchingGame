package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JFrame implements ActionListener {
    private final int FRAME_WIDTH = 800, FRAME_HEIGHT = 650;
    private final String BLUE_COLOR = "#9dc4ed", RED_COLOR="#f3dade", ORANGE_COLOR = "#f8e6d8", GREEN_COLOR="#d4efec";
    private final int PADDING_TOP = 30, PADDING_LEFT = 10, PADDING_RIGHT = 10, PADDING_BOTTOM = 40;
    private int numPlayers;
    private String difficulty;
    private JFrame mainFrame;
    private JPanel instructionsPanel, playerPanel, difficultyPanel;
    private JButton exitInstructionsBtn, onePlayerBtn, twoPlayerBtn, easyBtn, medBtn, hardBtn;
    private String[] playerNames;
    private EmptyBorder frameBorder;
    
    // Default no-argument constructor
    public void Board() {
    }

    public void init() {
        this.mainFrame = new JFrame("Card Matching Game By Memory Masters");
        this.mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.mainFrame.setLayout(new BorderLayout());
        this.mainFrame.getContentPane().setBackground(Color.decode(BLUE_COLOR));
        // Center the this.instructionsPanel on the screen
        this.mainFrame.setLocationRelativeTo(null);
        this.mainFrame.setVisible(true);
        //set the border for all Panels
        this.frameBorder = new EmptyBorder(30, 50, 30, 50);
        // create the different panel "Pages"
        this.instructionsPanel = getInstructionsPage();
        this.playerPanel = getPlayerPanel();
        this.difficultyPanel = getDifficultyPanel();
        updatePanel(null, this.instructionsPanel);
    }

    public void updatePanel(JPanel oldPanel, JPanel newPanel) {
            if (oldPanel != null) {
                this.mainFrame.remove(oldPanel);
            }
            this.mainFrame.add(newPanel);
            this.mainFrame.revalidate();
            this.mainFrame.repaint();
    }

    // instructions panel
    private JPanel getInstructionsPage() {
        JPanel instructions = new JPanel(new BorderLayout());
        instructions.setBorder(this.frameBorder);
        instructions.setBackground(Color.decode(BLUE_COLOR));
        // Add components to the frame
        // Header
        JLabel titleLabel = new JLabel("Welcome to the card matching game by Memory Masters!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBackground(Color.decode(ORANGE_COLOR));
        titleLabel.setOpaque(true);
        titleLabel.setBorder(new EmptyBorder(PADDING_TOP, PADDING_LEFT,0,PADDING_RIGHT));
        titleLabel.setForeground(Color.decode(BLUE_COLOR));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructions.add(titleLabel, BorderLayout.NORTH);
        // Instructions
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BorderLayout());
        instructionsPanel.add(getHeader("Instructions"), BorderLayout.NORTH);
        JTextArea bodyText = new JTextArea("The rules of the game are very simple. "
        		+ "You have to match all the pairs of identical cards in the shortest possible time (for single-player mode) or to find as many pairs as possible (for 2-player mode). "
        		+ "With each turn, you can only turn over two cards except. "
        		+ "\nIf you decide to choose the two-player mode, you can only turn over two cards each turn, except if you successfully match a pair. "
        		+ "In such a case, your turn continues until you fail to match a pair. "
        		+ "Then, it is the other player's turn. "
        		+ "\n\nIn the following, you can chose the player mode and difficulty level.");
        bodyText.setFont(new Font("Arial", Font.PLAIN, 18));
        bodyText.setBackground(Color.decode(ORANGE_COLOR));
        bodyText.setWrapStyleWord(true);
        bodyText.setLineWrap(true);
        bodyText.setEditable(false);
        bodyText.setBorder(new EmptyBorder(0, PADDING_LEFT + 50, PADDING_BOTTOM, PADDING_RIGHT + 50));
        bodyText.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsPanel.add(bodyText, BorderLayout.CENTER);
        instructions.add(instructionsPanel, BorderLayout.CENTER);
        // Play Button
        JPanel playButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        playButtonPanel.setBorder(new EmptyBorder(0,0,PADDING_BOTTOM,PADDING_RIGHT));
        playButtonPanel.setBackground(Color.decode(ORANGE_COLOR));
        this.exitInstructionsBtn = new JButton("Click here to play!");
        this.exitInstructionsBtn.addActionListener(this);
        this.exitInstructionsBtn.setPreferredSize(new Dimension(200, 50));
        this.exitInstructionsBtn.setBackground(Color.decode(GREEN_COLOR));
        playButtonPanel.add(this.exitInstructionsBtn);
        instructions.add(playButtonPanel, BorderLayout.SOUTH);
        return instructions;
    }
    // player choice panel
    private JPanel getPlayerPanel() {
        JPanel playerPage = new JPanel(new BorderLayout());
        playerPage.setBorder(this.frameBorder);
        playerPage.setBackground(Color.decode(BLUE_COLOR));
        // add title to the frame
        playerPage.add(getHeader("Please choose a game mode:"), BorderLayout.NORTH);
        // show buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.decode(ORANGE_COLOR));
        buttonPanel.setBorder(new EmptyBorder(50, 0, 0,0));
        this.onePlayerBtn = new JButton("<html>Click here for the<br/>SINGLE PLAYER options</html>");
        this.twoPlayerBtn = new JButton("<html>Click here for the<br/>TWO PLAYER options</html>");
        this.onePlayerBtn.setPreferredSize(new Dimension(300, 300));
        this.onePlayerBtn.setFont(new Font("Arial", Font.BOLD, 18));
        this.onePlayerBtn.setBackground(Color.decode(GREEN_COLOR));
        this.twoPlayerBtn.setPreferredSize(new Dimension(300, 300));
        this.twoPlayerBtn.setFont(new Font("Arial", Font.BOLD, 18));
        this.twoPlayerBtn.setBackground(Color.decode(GREEN_COLOR));
        // add action listeners to player buttons
        this.onePlayerBtn.addActionListener(this);
        this.twoPlayerBtn.addActionListener(this);
        buttonPanel.add(this.onePlayerBtn, BorderLayout.WEST);
        buttonPanel.add(this.twoPlayerBtn, BorderLayout.EAST);
        playerPage.add(buttonPanel, BorderLayout.CENTER);
        return playerPage;
    }

    // difficulty panel
    private JPanel getDifficultyPanel() {
        JPanel difficultyPage = new JPanel(new BorderLayout());
        difficultyPage.setBorder(this.frameBorder);
        difficultyPage.setBackground(Color.decode(BLUE_COLOR));
        difficultyPage.add(getHeader("Please choose the difficulty level:"), BorderLayout.NORTH);
        // button pannel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.decode(ORANGE_COLOR));
        buttonPanel.setBorder(new EmptyBorder(40, 0,0,0));
        this.easyBtn = new JButton("<html>Click here: <br/>EASY <br/> (8 cards)</html>)");
        this.medBtn = new JButton("<html>Click here: <br/>MEDIUM <br/> (16 cards)</html>)");
        this.hardBtn = new JButton("<html>Click here: <br/>HARD <br/> (38 cards)</html>)");
        this.easyBtn.setFont(new Font("Arial", Font.BOLD, 18));
        this.easyBtn.setBackground(Color.decode(GREEN_COLOR));
        this.medBtn.setFont(new Font("Arial", Font.BOLD, 18));
        this.medBtn.setBackground(Color.decode(GREEN_COLOR));
        this.hardBtn.setFont(new Font("Arial", Font.BOLD, 18));
        this.hardBtn.setBackground(Color.decode(GREEN_COLOR));
        this.easyBtn.setPreferredSize(new Dimension(200, 300));
        this.medBtn.setPreferredSize(new Dimension(200, 300));
        this.hardBtn.setPreferredSize(new Dimension(200, 300));
        // add action listeners to player buttons
        this.easyBtn.addActionListener(this);
        this.medBtn.addActionListener(this);
        this.hardBtn.addActionListener(this);
        buttonPanel.add(this.easyBtn);
        buttonPanel.add(this.medBtn);
        buttonPanel.add(this.hardBtn);
        difficultyPage.add(buttonPanel, BorderLayout.CENTER);
        return difficultyPage;
    }

    public JLabel getHeader(String headerName) {
        JLabel header = new JLabel(headerName);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setBackground(Color.decode(ORANGE_COLOR));
        header.setBorder(new EmptyBorder(PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT));
        header.setOpaque(true);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        return header;
    }

    // game setting methods
    public void handlePlayerSelection(int numPlayers) {
        this.numPlayers = numPlayers;
        this.playerNames = new String[this.numPlayers];
        for (int i = 0; i < this.numPlayers; i ++) {
            boolean flag = true;
            while (flag) {
            	//names can only be letters and numbers, use regular expression to ensure correct suer input
                String pattern = "^[a-zA-Z0-9]+$";
                String message;
                if (this.numPlayers == 1) {
                	message = "Enter player name";
                } else {
                	message = "Enter name for player " + (i + 1);
                }
                String name = JOptionPane.showInputDialog(null,message);
                if (name.matches(pattern)) {
                    this.playerNames[i] = name;
                    flag = false;
                } else {
                    JOptionPane.showMessageDialog(null,"Invalid name! Please use only letters or numbers.","Alert",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        updatePanel(this.playerPanel, this.difficultyPanel);
    }

    public void handleDifficultySelection(String difficulty) {
        this.difficulty = difficulty;
        // close the current JFrame
        this.mainFrame.dispose();
        // start the game
        Game game = new Game(this.numPlayers, this.difficulty, this.playerNames);
        game.init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.exitInstructionsBtn) {
            // execute the updatePanel method to update the GUI to the next panel
            updatePanel(this.instructionsPanel, this.playerPanel);
        }
        // Number of players
        if (e.getSource() == onePlayerBtn) {
           handlePlayerSelection(1);
        }
        if (e.getSource() == twoPlayerBtn) {
            handlePlayerSelection(2);
        }
        // game difficulty
        if (e.getSource() == easyBtn) {
           handleDifficultySelection("easy");
        }
        if (e.getSource() == medBtn) {
            handleDifficultySelection("medium");
        }
        if (e.getSource() == hardBtn) {
            handleDifficultySelection("hard");
        }
    }
}

