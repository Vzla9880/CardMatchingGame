package project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Card extends JButton {
    private static final String BACK_COLOR = "#f8e6d8";

    private int cardID;
    private String cardName;
    private boolean isFlipped = false;
    private boolean turnable = true;
    private ImageIcon cardImage, cardBack;


    public Card(int cardID, String cardName, ImageIcon image) {
        this.cardID = cardID;
        this.cardName = cardName;
        this.isFlipped = false;
        this.turnable = true;
        this.cardImage = image;
        this.setBackground(Color.decode(BACK_COLOR));
        setIcon(null);
    }

    public void flipCard() {
        if (!isFlipped) {
            setIcon(this.cardImage);
            this.setBackground(Color.WHITE);
        } else {
            setIcon(null);
            this.setBackground(Color.decode(BACK_COLOR));
        }
        isFlipped = !isFlipped;
    }

    public void resetCard() {
        setIcon(null);
        this.setBackground(Color.decode(BACK_COLOR));
        this.isFlipped = false;
        this.turnable = true;
    }

    public void setCardNumber(int cardID) {
        this.cardID = cardID;
    }
    public int getCardNumber() {
        return cardID;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public boolean turnable() {
        return turnable;
    }

    public void setTurnable(boolean turns) {
        this.turnable = turns;
    }

    public ImageIcon getCardImage() {
        return cardImage;
    }

    public ImageIcon getCardBack() {
        return cardBack;
    }

    public void setCardBack(ImageIcon cardBack) {
        this.cardBack = cardBack;
    }

    public void setCardImage(ImageIcon cardImage) {
        this.cardImage = cardImage;
    }

    public String getCardName() {
        return this.cardName;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardID=" + cardID +
                ", cardName='" + cardName + '\'' +
                ", isFlipped=" + isFlipped +
                ", turnable=" + turnable +
                ", cardImage=" + cardImage +
                ", cardBack=" + cardBack +
                '}';
    }
}

