package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class LeaderBoard extends JFrame implements ActionListener {
    static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 650;
    static final int MAX_ENTRIES = 10;
    private final String BLUE_COLOR = "#9dc4ed", RED_COLOR="#f3dade", ORANGE_COLOR = "#f8e6d8", GREEN_COLOR="#d4efec";
    static final String SINGLE_PLAYER_DATA = "data/singlePlayer.txt";
    static final String TWO_PLAYER_DATA = "data/twoPlayer.txt";  
    private String gameMode, difficulty, playerName;
    private int value;
    private List<Entry> entries;
    private JPanel leaderJPanel;
    private JButton exitBtn;

    // constructor for single player
    public LeaderBoard(String gameMode, String difficulty) {
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        handleFile();
        readFile();
    }
    
    // constructor for two player mode    
    public LeaderBoard(String gameMode, String difficulty, String playerName, int value) {
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.playerName = playerName;
        this.value = value;
        handleFile();
        readFile();
    }
 
    // process the data based on the game mode selected
    private void handleFile() {
        String filePath;
        if (this.gameMode.equals("single")) {
            filePath = SINGLE_PLAYER_DATA;
        } else {
            filePath = TWO_PLAYER_DATA;
        }
        // If we have player we have a winner and data to write. Only case where data is NOT written to a file is if there is a tie
        if (this.playerName != null) {
            // adds the data to the appropriate text file in the correct format
            Entry newEntry = new Entry(this.playerName, this.value, this.difficulty);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(newEntry.toString());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void readFile() {
        String filePath;
        if (this.gameMode.equals("single")) {
            filePath = SINGLE_PLAYER_DATA;
        } else {
            filePath = TWO_PLAYER_DATA;
        }
        this.entries = new ArrayList<>();
        // retrieves the data
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\s+"); // Split by whitespace
                if (parts.length > 0) {
                    String name = parts[0];
                    int value = Integer.parseInt(parts[1]);
                    String entryDifficulty = parts[2];
                    if (entryDifficulty.equals(this.difficulty)) {
                        entries.add(new Entry(name, value));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (this.gameMode.equalsIgnoreCase("single")) {
            Collections.sort(this.entries);
        } else {
            Collections.sort(this.entries, Collections.reverseOrder());
        }
    }
    
    // create the GUI
    public void showGUI() {
        //this = new JFrame();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.decode(BLUE_COLOR));
        this.leaderJPanel = new JPanel(new BorderLayout());
        this.leaderJPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        this.leaderJPanel.setBackground(Color.decode(BLUE_COLOR));
        // Add title to frame
        JLabel titleLabel = new JLabel("Leadership Board");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 50));
        titleLabel.setBackground(Color.decode(ORANGE_COLOR));
        titleLabel.setOpaque(true);
        this.leaderJPanel.add(titleLabel, BorderLayout.NORTH);
        // center leadership frame
        JPanel centerJPanel = new JPanel(new BorderLayout());
        // Add subtitle to frame
        JLabel subtitleLabel = new JLabel(this.gameMode.toUpperCase() + " Player Mode - " + this.difficulty.toUpperCase() + " Difficulty");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        subtitleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        subtitleLabel.setBackground(Color.decode(ORANGE_COLOR));
        centerJPanel.add(subtitleLabel, BorderLayout.NORTH);
        // show the top 10 entries
        String columnName = this.gameMode.equals("single") ? "Total Duration (Seconds)" : "Number of matches";
        // Create column names
        String[] columnNames = {"Player Name", columnName};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        model.addRow(columnNames);
        // Add data to the table model, showing only the max number of entries
        int limit = Math.min(MAX_ENTRIES, entries.size());
        for (int i = 0; i < limit; i ++) {
            Entry entry = entries.get(i);
            model.addRow(new Object[]{entry.getName(), entry.getValue()});
        }
        // Create a JTable with the table model
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setRowHeight(28);
        //Make the first row (header) bold
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == 0) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
                return c;
            }
        });
        centerJPanel.add(table, BorderLayout.CENTER);
        this.leaderJPanel.add(centerJPanel, BorderLayout.CENTER);
        // add button to exit the game
        this.exitBtn = new JButton("Exit");
        this.exitBtn.setBackground(Color.decode(RED_COLOR));
        this.exitBtn.setBorder(new EmptyBorder(10,30,15,30));
        this.exitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        exitBtn.addActionListener(this);
        this.leaderJPanel.add(exitBtn, BorderLayout.SOUTH);
        this.add(this.leaderJPanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.exitBtn) {
            System.exit(0);
        }
    }
}

