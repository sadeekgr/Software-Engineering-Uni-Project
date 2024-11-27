package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;

import java.util.Arrays;
import java.util.List;

/**
 * The FieldWindow class represents a window that displays a player's field,
 * including card placements and symbol counts.
 */
public class FieldWindow {
    protected final String[] lines;
    protected final int height;
    protected final int width;
    protected PlayerField field;

    /**
     * Constructs a FieldWindow with the specified height and width.
     *
     * @param height the number of lines the field window can display
     * @param width the maximum number of characters per line
     */
    public FieldWindow(int height, int width) {
        lines = new String[height];
        Arrays.fill(lines, "");
        this.height = height;
        this.width = width;
    }

    /**
     * Gets the lines of the field window.
     *
     * @return the lines of the field window
     */
    public String[] getLines(){ return lines; }

    /**
     * Draws the player's field, including card placements and symbol counts.
     *
     * @param field the player's field to draw
     */
    public void draw(PlayerField field){
        this.field = field;
        Arrays.fill(lines, "");

        // Center the field
        List<Position> pos = field.getCards().stream()
                .map(CardPlacement::getPosition)
                .toList();
        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;
        for(Position p : pos){
            if(p.x() < left){
                left = p.x();
            } else if (p.x() > right) {
                right = p.x();
            }

            if(p.y() < bottom){
                bottom = p.y();
            } else if (p.y() > top) {
                top = p.y();
            }
        }

        double centerX = left + (right - left) / 2.;
        double centerY = bottom + (top - bottom) / 2.;

        for(CardPlacement card : field.getCards()){
            String[] cardLines = Draw.cardPlacement(card);

            // draw card
            int i = (int) (height / 2. - 2 * (card.getPosition().y() - centerY));

            if(i >= 0 && i < height) {
                double relX = card.getPosition().x() - centerX;
                int startingPos = (int) ((width / 2.0 + relX * 15) - 7.5);

                if (i > 1) {
                    lines[i - 2] = ANSI.insertSkipANSI(cardLines[0], lines[i-2], startingPos);
                }

                if (i > 0) {
                    lines[i - 1] = ANSI.insertSkipANSI(cardLines[1], lines[i-1], startingPos);
                }

                lines[i] = ANSI.insertSkipANSI(cardLines[2], lines[i], startingPos);

                if (i < height - 1) {
                    lines[i + 1] = ANSI.insertSkipANSI(cardLines[3], lines[i+1], startingPos);
                }

                if (i < height - 2) {
                    lines[i + 2] = ANSI.insertSkipANSI(cardLines[4], lines[i+2], startingPos);
                }
            }
        }

        // Add the symbol pool
        lines[lines.length - 2] = ANSI.insertSkipANSI("┌─────┬─────┬─────┬─────┬─────┬─────┬─────", lines[lines.length - 2], width - 42);
        lines[lines.length - 1] = ANSI.insertSkipANSI(
                "│" + formatNum(field.getSymbolNum(Symbol.FUNGI)) + Draw.getSymbol(Symbol.FUNGI)
                        + " │" + formatNum(field.getSymbolNum(Symbol.PLANT)) + Draw.getSymbol(Symbol.PLANT)
                        + " │" + formatNum(field.getSymbolNum(Symbol.ANIMAL)) + Draw.getSymbol(Symbol.ANIMAL)
                        + " │" + formatNum(field.getSymbolNum(Symbol.INSECT)) + Draw.getSymbol(Symbol.INSECT)
                        + " │" + formatNum(field.getSymbolNum(Symbol.INKWELL)) + Draw.getSymbol(Symbol.INKWELL)
                        + " │" + formatNum(field.getSymbolNum(Symbol.MANUSCRIPT)) + Draw.getSymbol(Symbol.MANUSCRIPT)
                        + " │" + formatNum(field.getSymbolNum(Symbol.QUILL)) + Draw.getSymbol(Symbol.QUILL) + " ",
                lines[lines.length - 1], width - 42);
    }

    /**
     * Updates the field window by redrawing the player's field.
     */
    public void update(){
        draw(field);
    }

    /**
     * Formats a number for display in the symbol pool.
     *
     * @param i the number to format
     * @return the formatted number as a string
     */
    private String formatNum(int i){
        if(i < 10){
            return " " + i + " ";
        } else if(i < 100){
            return i + " ";
        } else {
            return "" + i;
        }
    }
}
