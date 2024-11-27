package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.objective.DispositionObjective;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.objective.SymbolObjective;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for generating graphical representations for various objects of the game,
 * such as cards, objectives and card placements
 */
public class Draw {
    private static final Map<Symbol, String> symbols = new HashMap<>(){
        {
            put(Symbol.FUNGI,  ANSI.RED + "F" +  ANSI.RESET);
            put(Symbol.INSECT,  ANSI.PURPLE + "I" +  ANSI.RESET);
            put(Symbol.ANIMAL,  ANSI.BLUE + "A" +  ANSI.RESET);
            put(Symbol.PLANT,  ANSI.GREEN + "P" +  ANSI.RESET);
            put(Symbol.INKWELL,  ANSI.GOLD + "W" +  ANSI.RESET);
            put(Symbol.MANUSCRIPT,  ANSI.GOLD + "M" +  ANSI.RESET);
            put(Symbol.QUILL,  ANSI.GOLD + "Q" +  ANSI.RESET);
            put(Symbol.EMPTY, " " +  ANSI.RESET);
        }
    };

    private static final Map<Symbol, String> bg_colors = new HashMap<>(){
        {
            put(Symbol.FUNGI,  ANSI.BG_LIGHT_RED);
            put(Symbol.INSECT,  ANSI.BG_LIGHT_PURPLE);
            put(Symbol.ANIMAL,  ANSI.BG_LIGHT_BLUE);
            put(Symbol.PLANT,  ANSI.BG_LIGHT_GREEN);
            put(Symbol.EMPTY, "");
        }
    };

    public static String getSymbol(Symbol s){
        return symbols.get(s);
    }
    /**
     * Returns the ANSI color corresponding to the given PlayerColor color
     * @param color PlayerColor enumeration
     * @return ANSI color code
     */
    public static String getColor(PlayerColor color){
        return switch (color){
            case RED ->  ANSI.RED;
            case GREEN ->  ANSI.GREEN;
            case BLUE ->  ANSI.BLUE;
            case YELLOW ->  ANSI.GOLD;
        };
    }
    /**
     * Returns the representation of a CardPlacement object
     * @param card CardPlacement object to represent
     * @return array of strings, each element is a line of the card placement representation
     */
    public static String[] cardPlacement(CardPlacement card) {
        String[] lines = new String[5];

        String bg_color = bg_colors.get(card.getKingdom());

        // first line of the card
        lines[0] = bg_color + "┌───";
        if (card.getCorner(CornerPosition.TOP_LEFT).IsPresent()){
            lines[0]  += "┬" + bg_color + "──────────";
        } else {
            lines[0]  += "─" + bg_color + "──────────";
        }

        if (card.getCorner(CornerPosition.TOP_RIGHT).IsPresent()){
            lines[0]  += "┬───┐";
        } else {
            lines[0]  += "────┐";
        }

        lines[0] +=  ANSI.RESET;

        // second line of the card
        lines[1] = bg_color + "│ " + (card.getCorner(CornerPosition.TOP_LEFT).IsPresent() ? symbols.get(card.getCorner(CornerPosition.TOP_LEFT).getSymbol()) + bg_color + " │" : "   ");
        lines[1] += bg_color + "          " + (card.getCorner(CornerPosition.TOP_RIGHT).IsPresent() ? "│ " + symbols.get(card.getCorner(CornerPosition.TOP_RIGHT).getSymbol()) + bg_color : "   ") + " │";
        lines[1] +=  ANSI.RESET;

        // third line of the card
        lines[2] = bg_color;
        if(card.getCorner(CornerPosition.TOP_LEFT).IsPresent() && card.getCorner(CornerPosition.BOTTOM_LEFT).IsPresent()){
            lines[2] += "├───┤";
        } else if(card.getCorner(CornerPosition.TOP_LEFT).IsPresent()){
            lines[2] += "├───┘";
        } else if (card.getCorner(CornerPosition.BOTTOM_LEFT).IsPresent()) {
            lines[2] += "├───┐";
        } else {
            lines[2] += "│    ";
        }
        lines[2] += bg_color;

        if(card.getKingdom() == Symbol.EMPTY){
            lines[2] += centerSymbols(((StarterCard) card.getCard()).getCenterSymbols(card.isFront()));
        } else if(!card.isFront()){
            lines[2] += "     " + symbols.get(card.getKingdom()) + bg_color + "    ";
        } else {
            lines[2] += "          ";
        }

        if(card.getCorner(CornerPosition.TOP_RIGHT).IsPresent() && card.getCorner(CornerPosition.BOTTOM_RIGHT).IsPresent()){
            lines[2] += "├───┤";
        } else if(card.getCorner(CornerPosition.TOP_RIGHT).IsPresent()){
            lines[2] += "└───┤";
        } else if (card.getCorner(CornerPosition.BOTTOM_RIGHT).IsPresent()) {
            lines[2] += "┌───┤";
        } else {
            lines[2] += "    │";
        }
        lines[2] +=  ANSI.RESET;

        // fourth line of the card
        lines[3] = bg_color + "│ " +
                (card.getCorner(CornerPosition.BOTTOM_LEFT).IsPresent() ? symbols.get(card.getCorner(CornerPosition.BOTTOM_LEFT).getSymbol()) + bg_color + " │" : "   ") +
                bg_color + getPosString(card) +
                (card.getCorner(CornerPosition.BOTTOM_RIGHT).IsPresent() ? "│ " + symbols.get(card.getCorner(CornerPosition.BOTTOM_RIGHT).getSymbol()) + bg_color : "   ") +
                " │" +  ANSI.RESET;

        // fifth line of the card
        lines[4] = bg_color + "└───";
        if (card.getCorner(CornerPosition.BOTTOM_LEFT).IsPresent()){
            lines[4] += "┴" + bg_color + "──────────";
        } else {
            lines[4] += "─" + bg_color + "──────────";
        }

        if (card.getCorner(CornerPosition.BOTTOM_RIGHT).IsPresent()){
            lines[4] += "┴───┘";
        } else {
            lines[4] += "────┘";
        }
        lines[4] +=  ANSI.RESET;

        return lines;
    }
    /**
     * @param placement CardPlacement object
     * @return position of a CardPlacement as a string
     */
    private static String getPosString(CardPlacement placement) {
        String posString;
        int x = placement.getPosition().x();
        int y = placement.getPosition().y();
        if(x < -9){
            if(y < -9){
                posString = "(" + x + ", " + y + ")";
            } else if(y < 0){
                posString = " (" + x + "," + y + ") ";
            } else if(y < 10){
                posString = " (" + x + ", " + y + ") ";
            } else {
                posString = " (" + x + "," + y + ") ";
            }
        } else if(x < 0 || x > 9){
            if(y < -9){
                posString = " (" + x + "," + y + ") ";
            } else if(y < 0){
                posString = " (" + x + ", " + y + ") ";
            } else if(y < 10){
                posString = "  (" + x + "," + y + ")  ";
            } else {
                posString = " (" + x + ", " + y + ") ";
            }
        } else {
            if(y < -9){
                posString = " (" + x + ", " + y + ") ";
            } else if(y < 0){
                posString = "  (" + x + "," + y + ")  ";
            } else if(y < 10){
                posString = "  (" + x + ", " + y + ")  ";
            } else {
                posString = " (" + x + ", " + y + ") ";
            }
        }
        return posString;
    }
    /**
     * Returns the representation of the back of a card with a given kingdom
     * @param kingdom Symbol object at the back of the card
     * @return array of strings, each element is a line of the card's representation
     */
    public static String[] backCard(Symbol kingdom){
        String[] lines = new String[5];

        String bg_color = bg_colors.get(kingdom);

        lines[0] = bg_color + "┌───┬──────────┬───┐" +  ANSI.RESET;
        lines[1] = bg_color + "│   │          │   │" +  ANSI.RESET;
        lines[2] = bg_color + "├───┤     " + symbols.get(kingdom) + bg_color + "    ├───┤" +  ANSI.RESET;
        lines[3] = bg_color + "│   │          │   │" +  ANSI.RESET;
        lines[4] = bg_color + "└───┴──────────┴───┘" +  ANSI.RESET;

        return lines;
    }
    /**
     * Returns the representation of the back of a PlayableCard object
     * @param card PlayableCard object to represent
     * @return array of strings, each element is a line of the card's representation
     */
    public static String[] backCard(PlayableCard card){
        return backCard(card.getKingdom());
    }
    /**
     * Returns the score representation of a given PlayableCard object and background color
     * @param card PlayableCard object whose score representation is required
     * @param bg_color background color for the score representation
     * @return score representation
     */
    private static String writeScore(PlayableCard card, String bg_color){
        if(card instanceof ResourceCard) {
            return " score: " + ((ResourceCard) card).getScore() + " ";
        } else if(card instanceof GoldCardInt){
            return " score: " + ((GoldCardInt) card).getScore() + " ";
        } else if(card instanceof GoldCardCorner){
            return "   ┐└│" + ((GoldCardCorner) card).getScorePerCorner() + "   ";
        } else if(card instanceof GoldCardObject){
            return "    " + symbols.get(((GoldCardObject) card).getSymbol()) + bg_color + "|" + ((GoldCardObject) card).getScorePerSymbol() + "   ";
        }

        return "          ";
    }

    /**
     * Returns the representation of an empty card
     * @return array of strings, each element is a line of the empty card representation
     */
    public static String[] emptyCard(){
        String[] lines = new String[5];
        lines[0] = " ".repeat(20);
        lines[1] = " ".repeat(20);
        lines[2] = " ".repeat(20);
        lines[3] = " ".repeat(20);
        lines[4] = " ".repeat(20);
        return lines;
    }

    /**
     * Returns the representation of an empty objective
     * @return array of strings, each element is a line of the empty objective representation
     */
    public static String[] emptyObjective(){
        String[] lines = new String[6];
        lines[0] = " ".repeat(19);
        lines[1] = " ".repeat(19);
        lines[2] = "                   ";
        lines[3] = " ".repeat(19);
        lines[4] = " ".repeat(19);
        lines[5] = " ".repeat(19);
        return lines;
    }

    /**
     * Returns the representation of the front of a PlayableCard object
     * @param card PlayableCard object to represent
     * @return array of strings, each element is a line of the card's representation. Returns an empty card representation if card is null
     */
    public static String[] frontCard(PlayableCard card){
        if(card == null){
            return emptyCard();
        }

        String[] lines = new String[5];

        String bg_color = bg_colors.get(card.getKingdom());

        // first line of the card
        lines[0] = bg_color + "┌───";
        if (card.getCorner(CornerPosition.TOP_LEFT, true).IsPresent()){
            lines[0]  += "┬──────────";
        } else {
            lines[0]  += "───────────";
        }

        if (card.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent()){
            lines[0]  += "┬───┐";
        } else {
            lines[0]  += "────┐";
        }
        lines[0] +=  ANSI.RESET;

        // second line of the card
        lines[1] = bg_color + "│ " + (card.getCorner(CornerPosition.TOP_LEFT, true).IsPresent() ? symbols.get(card.getCorner(CornerPosition.TOP_LEFT, true).getSymbol()) + bg_color + " │" : "   ");

        lines[1] += writeScore(card, bg_color);
        lines[1] += (card.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent() ? "│ " + symbols.get(card.getCorner(CornerPosition.TOP_RIGHT, true).getSymbol()) + bg_color : "   ") + " │";
        lines[1] +=  ANSI.RESET;

        // third line of the card
        lines[2] = bg_color;
        if(card.getCorner(CornerPosition.TOP_LEFT, true).IsPresent() && card.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent()){
            lines[2] += "├───┤          ";
        } else if(card.getCorner(CornerPosition.TOP_LEFT, true).IsPresent()){
            lines[2] += "├───┘          ";
        } else if (card.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent()) {
            lines[2] += "├───┐          ";
        } else {
            lines[2] += "│              ";
        }

        if(card.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent() && card.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent()){
            lines[2] += "├───┤";
        } else if(card.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent()){
            lines[2] += "└───┤";
        } else if (card.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent()) {
            lines[2] += "┌───┤";
        } else {
            lines[2] += "    │";
        }
        lines[2] +=  ANSI.RESET;

        // fourth line of the card
        lines[3] = bg_color + "│ " + (card.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent() ? symbols.get(card.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol()) + bg_color + " │" : "   ");

        StringBuilder reqs = new StringBuilder();
        Map<Symbol, Integer> reqMap = card.getRequirements(true);
        int count = 0;
        for(Symbol s : reqMap.keySet()){
            for(int i = 0; i < reqMap.get(s); i++){
                reqs.append(symbols.get(s)).append(bg_color);
                count++;
            }
        }

        for(int i = 0; i < 10 - count; i++){
            if(i % 2 == 0){
                reqs = new StringBuilder(" " + reqs);
            } else {
                reqs = new StringBuilder(reqs + " ");
            }
        }

        lines[3] += reqs + (card.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent() ? "│ " + symbols.get(card.getCorner(CornerPosition.BOTTOM_RIGHT, true).getSymbol()) + bg_color : "   ") + " │";
        lines[3] +=  ANSI.RESET;

        // fifth line of the card
        lines[4] = bg_color + "└───";
        if (card.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent()){
            lines[4] += "┴──────────";
        } else {
            lines[4] += "───────────";
        }

        if (card.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent()){
            lines[4] += "┴───┘";
        } else {
            lines[4] += "────┘";
        }
        lines[4] +=  ANSI.RESET;

        return lines;
    }
    /**
     * Returns the representation of a given side of a StarterCard object
     * @param card StarterCard object to represent
     * @param side true for the front of the card, false for the back
     * @return array of strings, each element is a line of the card's representation
     */
    public static String[] sideStarter(StarterCard card, boolean side){
        String[] lines = new String[5];

        // first line of the card
        lines[0] = "┌───";
        if (card.getCorner(CornerPosition.TOP_LEFT, side).IsPresent()){
            lines[0]  += "┬──────────";
        } else {
            lines[0]  += "───────────";
        }

        if (card.getCorner(CornerPosition.TOP_RIGHT, side).IsPresent()){
            lines[0]  += "┬───┐";
        } else {
            lines[0]  += "────┐";
        }

        // second line of the card
        lines[1] = "│ " + (card.getCorner(CornerPosition.TOP_LEFT, side).IsPresent() ? symbols.get(card.getCorner(CornerPosition.TOP_LEFT,side).getSymbol()) + " │" : "   ");

        lines[1] += "          ";

        lines[1] += (card.getCorner(CornerPosition.TOP_RIGHT, side).IsPresent() ? "│ " + symbols.get(card.getCorner(CornerPosition.TOP_RIGHT, side).getSymbol()) : "   ") + " │";

        // third line of the card
        if(card.getCorner(CornerPosition.TOP_LEFT, side).IsPresent() && card.getCorner(CornerPosition.BOTTOM_LEFT, side).IsPresent()){
            lines[2] = "├───┤";
        } else if(card.getCorner(CornerPosition.TOP_LEFT, side).IsPresent()){
            lines[2] = "├───┘";
        } else if (card.getCorner(CornerPosition.BOTTOM_LEFT, side).IsPresent()) {
            lines[2] = "├───┐";
        } else {
            lines[2] = "│    ";
        }

        lines[2] += centerSymbols(card.getCenterSymbols(side));

        if(card.getCorner(CornerPosition.TOP_RIGHT, side).IsPresent() && card.getCorner(CornerPosition.BOTTOM_RIGHT, side).IsPresent()){
            lines[2] += "├───┤";
        } else if(card.getCorner(CornerPosition.TOP_RIGHT, side).IsPresent()){
            lines[2] += "└───┤";
        } else if (card.getCorner(CornerPosition.BOTTOM_RIGHT, side).IsPresent()) {
            lines[2] += "┌───┤";
        } else {
            lines[2] += "    │";
        }

        // fourth line of the card
        lines[3] = "│ " + (card.getCorner(CornerPosition.BOTTOM_LEFT, side).IsPresent() ? symbols.get(card.getCorner(CornerPosition.BOTTOM_LEFT, side).getSymbol()) + " │" : "   ") +
                "          " + (card.getCorner(CornerPosition.BOTTOM_RIGHT, side).IsPresent() ? "│ " + symbols.get(card.getCorner(CornerPosition.BOTTOM_RIGHT, side).getSymbol()) : "   ") + " │";

        // fifth line of the card
        lines[4] = "└───";
        if (card.getCorner(CornerPosition.BOTTOM_LEFT, side).IsPresent()){
            lines[4] += "┴──────────";
        } else {
            lines[4] += "───────────";
        }

        if (card.getCorner(CornerPosition.BOTTOM_RIGHT, side).IsPresent()){
            lines[4] += "┴───┘";
        } else {
            lines[4] += "────┘";
        }

        return lines;
    }

    /**
     * Returns the representation of the center symbols
     * @param centerSym list of symbols to represent
     * @return string representing the center symbols
     */
    private static String centerSymbols(ArrayList<Symbol> centerSym) {
        return switch (centerSym.size()) {
            case 0 -> "          ";
            case 1 -> "     " + symbols.get(centerSym.getFirst()) + "    ";
            case 2 -> "    " + symbols.get(centerSym.getFirst()) + symbols.get(centerSym.get(1)) + "    ";
            case 3 ->
                    "    " + symbols.get(centerSym.getFirst()) + symbols.get(centerSym.get(1)) + symbols.get(centerSym.get(2)) + "   ";
            default -> null;
        };
    }
    /**
     * Returns the representation of both sides of a StarterCard object
     * @param card StarterCard object to represent
     * @return array of strings, each element is a line of the card's representation
     */
    public static String[] starter(StarterCard card){
        String[] front = sideStarter(card, true);
        String[] back = sideStarter(card, false);

        for(int i = 0; i < 5; i++) {
            front[i] += "    " + back[i];
        }

        return front;
    }
    /**
     * Returns the representation of an objective (of type symbol or disposition)
     * @param o Objective object to represent
     * @return array of strings, each element is a line of the objective's representation
     */
    public static String[] objective(Objective o){
        if(o instanceof SymbolObjective){
            return symbolObjective((SymbolObjective) o);
        }

        DispositionObjective obj = (DispositionObjective) o;
        Position[] pos = obj.getPatternPosition();
        Symbol[] symbols = obj.getPatternKingdom();

        int score = obj.getScore();
        Symbol first, second, third;

        if (pos[0].x() == 0) {
            if (pos[1].x() < 0){ // left
                if (pos[1].y() < 0){ // bottom
                    third = symbols[2];
                    if (pos[0].y() < 0){ // 0,0 top most
                        first = symbols[0];
                        second = symbols[1];
                    } else { // 0,0 center
                        first = symbols[1];
                        second = symbols[0];
                    }
                    return dispositionObjectiveBottomLeft(score, first, second, third);
                } else { // top
                    first = symbols[2];
                    if (pos[0].y() < 0){ // 0,0 center
                        second = symbols[0];
                        third = symbols[1];
                    } else { // 0,0 bottom most
                        second = symbols[1];
                        third = symbols[0];
                    }
                    return dispositionObjectiveTopLeft(score, first, second, third);
                }
            } else { //right
                if (pos[1].y() < 0){ // bottom
                    third = symbols[2];
                    if (pos[0].y() < 0){ // 0,0 top most
                        first = symbols[0];
                        second = symbols[1];
                    } else { // 0,0 center
                        first = symbols[1];
                        second = symbols[0];
                    }
                    return dispositionObjectiveBottomRight(score, first, second, third);
                } else { // top
                    first = symbols[2];
                    if (pos[0].y() < 0){ // 0,0 center
                        second = symbols[0];
                        third = symbols[1];
                    } else { // 0,0 bottom most
                        second = symbols[1];
                        third = symbols[0];
                    }
                    return dispositionObjectiveTopRight(score, first, second, third);
                }
            }
        } else if (pos[1].x() == 0) {
            if (pos[0].x() < 0){ // left
                if (pos[0].y() < 0){ // bottom
                    third = symbols[1];
                    if (pos[1].y() < 0){ // 0,0 top most
                        first = symbols[0];
                        second = symbols[2];
                    } else { // 0,0 center
                        first = symbols[2];
                        second = symbols[0];
                    }
                    return dispositionObjectiveBottomLeft(score, first, second, third);
                } else { // top
                    first = symbols[2];
                    if (pos[1].y() < 0){ // 0,0 center
                        second = symbols[0];
                        third = symbols[2];
                    } else { // 0,0 bottom most
                        second = symbols[2];
                        third = symbols[0];
                    }
                    return dispositionObjectiveTopLeft(score, first, second, third);
                }
            } else { //right
                if (pos[0].y() < 0){ // bottom
                    third = symbols[1];
                    if (pos[1].y() < 0){ // 0,0 top most
                        first = symbols[0];
                        second = symbols[2];
                    } else { // 0,0 center
                        first = symbols[2];
                        second = symbols[0];
                    }
                    return dispositionObjectiveBottomRight(score, first, second, third);
                } else { // top
                    first = symbols[2];
                    if (pos[1].y() < 0){ // 0,0 center
                        second = symbols[0];
                        third = symbols[1];
                    } else { // 0,0 bottom most
                        second = symbols[1];
                        third = symbols[0];
                    }
                    return dispositionObjectiveTopRight(score, first, second, third);
                }
            }
        } else if (pos[0].x() == pos[1].x()) {
            if (pos[1].x() > 0){ // left
                if (pos[1].y() > 0){ // bottom
                    third = symbols[0];
                    if (pos[0].y() > pos[1].y()){ // pos 0 top most
                        first = symbols[1];
                        second = symbols[2];
                    } else { // pos 1 top most
                        first = symbols[2];
                        second = symbols[1];
                    }
                    return dispositionObjectiveBottomLeft(score, first, second, third);
                } else { // top
                    first = symbols[0];
                    if (pos[0].y() > pos[1].y()){ // pos 1 bottom most
                        second = symbols[1];
                        third = symbols[2];
                    } else { // pos 0 bottom most
                        second = symbols[2];
                        third = symbols[1];
                    }
                    return dispositionObjectiveTopLeft(score, first, second, third);
                }
            } else { //right
                if (pos[1].y() > 0){ // bottom
                    third = symbols[0];
                    if (pos[0].y() > pos[1].y()){ // pos 0 top most
                        first = symbols[1];
                        second = symbols[2];
                    } else { // pos 1 top most
                        first = symbols[2];
                        second = symbols[1];
                    }
                    return dispositionObjectiveBottomRight(score, first, second, third);
                } else { // top
                    first = symbols[0];
                    if (pos[0].y() > pos[1].y()){ // pos 1 bottom most
                        second = symbols[1];
                        third = symbols[2];
                    } else { // pos 0 bottom most
                        second = symbols[2];
                        third = symbols[1];
                    }
                    return dispositionObjectiveTopRight(score, first, second, third);
                }
            }
        } else {
            int lIndex, cIndex, rIndex;
            if(0 > pos[0].x()){
                if (0 > pos[1].x()){
                    rIndex = 0;
                    if (pos[0].x() > pos[1].x()) {
                        cIndex = 1;
                        lIndex = 2;
                    } else {
                        cIndex = 2;
                        lIndex = 1;
                    }
                } else {
                    rIndex = 2;
                    cIndex = 0;
                    lIndex = 1;
                }
            } else {
                if (0 > pos[1].x()){
                    rIndex = 1;
                    cIndex = 0;
                    lIndex = 2;
                } else {
                    lIndex = 0;
                    if (pos[0].x() > pos[1].x()) {
                        cIndex = 2;
                        rIndex = 1;
                    } else {
                        cIndex = 1;
                        rIndex = 2;
                    }
                }
            }

            if(pos[0].x() < 0){
                if (pos[0].y() < 0){
                    return dispositionObjectiveLeftRight(obj.getScore(), symbols[lIndex], symbols[cIndex], symbols[rIndex]);
                } else {
                    return dispositionObjectiveRightLeft(obj.getScore(), symbols[rIndex], symbols[cIndex], symbols[lIndex]);
                }
            } else {
                if (pos[0].y() > 0){
                    return dispositionObjectiveLeftRight(obj.getScore(), symbols[lIndex], symbols[cIndex], symbols[rIndex]);
                } else {
                    return dispositionObjectiveRightLeft(obj.getScore(), symbols[rIndex], symbols[cIndex], symbols[lIndex]);
                }
            }
        }
    }
    /**
     * Returns the representation of a disposition objective with a specific structure
     * @param score score of the objective
     * @param first first symbol
     * @param second second symbol
     * @param third third symbol
     * @return array of strings, each element is a line of the disposition objective's representation
     */
    private static String[] dispositionObjectiveTopLeft(int score, Symbol first, Symbol second, Symbol third) {
        String[] lines = new String[6];

        lines[0] = "┌──────┬────┬─────┐";
        lines[1] = "│      │ " + bg_colors.get(first) + "  " +  ANSI.RESET + " ├────┐│";
        lines[2] = "│      └────┤ " + bg_colors.get(second) + "  " +  ANSI.RESET + " ││";
        lines[3] = "│ score : "+ score + " ├────┤│";
        lines[4] = "│           │ " + bg_colors.get(third) + "  " +  ANSI.RESET + " ││";
        lines[5] = "└───────────┴────┴┘";

        return lines;
    }
    /**
     * Returns the representation of a disposition objective with a specific structure
     * @param score score of the objective
     * @param first first symbol
     * @param second second symbol
     * @param third third symbol
     * @return array of strings, each element is a line of the disposition objective's representation
     */
    private static String[] dispositionObjectiveBottomLeft(int score, Symbol first, Symbol second, Symbol third) {
        String[] lines = new String[6];

        lines[0] = "┌───────────┬────┬┐";
        lines[1] = "│           │ " + bg_colors.get(first) + "  " +  ANSI.RESET + " ││";
        lines[2] = "│ score : "+ score + " ├────┤│";
        lines[3] = "│      ┌────┤ " + bg_colors.get(second) + "  " +  ANSI.RESET + " ││";
        lines[4] = "│      │ " + bg_colors.get(third) + "  " +  ANSI.RESET + " ├────┘│";
        lines[5] = "└──────┴────┴─────┘";

        return lines;
    }
    /**
     * Returns the representation of a disposition objective with a specific structure
     * @param score score of the objective
     * @param first first symbol
     * @param second second symbol
     * @param third third symbol
     * @return array of strings, each element is a line of the disposition objective's representation
     */
    private static String[] dispositionObjectiveTopRight(int score, Symbol first, Symbol second, Symbol third) {
        String[] lines = new String[6];
        lines[0] = "┌─────┬────┬──────┐";
        lines[1] = "│┌────┤ " + bg_colors.get(first) + "  " +  ANSI.RESET + " │      │";
        lines[2] = "││ " + bg_colors.get(second) + "  " +  ANSI.RESET + " ├────┘      │";
        lines[3] = "│├────┤ score : "+ score + " │";
        lines[4] = "││ " + bg_colors.get(third) + "  " +  ANSI.RESET + " │           │";
        lines[5] = "└┴────┴───────────┘";
        return lines;
    }
    /**
     * Returns the representation of a disposition objective with a specific structure
     * @param score score of the objective
     * @param first first symbol
     * @param second second symbol
     * @param third third symbol
     * @return array of strings, each element is a line of the disposition objective's representation
     */
    private static String[] dispositionObjectiveBottomRight(int score, Symbol first, Symbol second, Symbol third) {
        String[] lines = new String[6];
        lines[0] = "┌┬────┬───────────┐";
        lines[1] = "││ " + bg_colors.get(first) + "  " +  ANSI.RESET + " │           │";
        lines[2] = "│├────┤ score : "+ score + " │";
        lines[3] = "││ " + bg_colors.get(second) + "  " +  ANSI.RESET + " ├────┐      │";
        lines[4] = "│└────┤ " + bg_colors.get(third) + "  " +  ANSI.RESET + " │      │";
        lines[5] = "└─────┴────┴──────┘";
        return lines;
    }
    /**
     * Returns the representation of a disposition objective with a specific structure
     * @param score score of the objective
     * @param first first symbol
     * @param second second symbol
     * @param third third symbol
     * @return array of strings, each element is a line of the disposition objective's representation
     */
    private static String[] dispositionObjectiveRightLeft(int score, Symbol first, Symbol second, Symbol third) {
        String[] lines = new String[6];
        lines[0] = "┌─────────────────┐";
        lines[1] = "│┌────┐ score : "+ score + " │";
        lines[2] = "││ " + bg_colors.get(first) + "  " +  ANSI.RESET + " ├────┐      │";
        lines[3] = "│└────┤ " + bg_colors.get(second) + "  " +  ANSI.RESET + " ├────┐ │";
        lines[4] = "│     └────┤ " + bg_colors.get(third) + "  " +  ANSI.RESET + " │ │";
        lines[5] = "└──────────┴────┴─┘";
        return lines;
    }
    /**
     * Returns the representation of a disposition objective with a specific structure
     * @param score score of the objective
     * @param first first symbol
     * @param second second symbol
     * @param third third symbol
     * @return array of strings, each element is a line of the disposition objective's representation
     */
    private static String[] dispositionObjectiveLeftRight(int score, Symbol first, Symbol second, Symbol third) {
        String[] lines = new String[6];
        lines[0] = "┌──────────┬────┬─┐";
        lines[1] = "│     ┌────┤ " + bg_colors.get(first) + "  " +  ANSI.RESET + " │ │";
        lines[2] = "│┌────┤ " + bg_colors.get(second) + "  " +  ANSI.RESET + " ├────┘ │";
        lines[3] = "││ " + bg_colors.get(third) + "  " +  ANSI.RESET + " ├────┘      │";
        lines[4] = "│└────┘ score : "+ score + " │";
        lines[5] = "└─────────────────┘";
        return lines;
    }

    /**
     * Returns the representation of both sides of a SymbolObjective object
     * @param o The SymbolObjective object to represent
     * @return array of strings, each element is a line of the symbol objective's representation
     */
    private static String[] symbolObjective(SymbolObjective o){
        String[] lines = new String[6];

        lines[0] = "┌─────────────────┐";
        lines[1] = "│    score: " + o.getScore() + "     │";
        lines[2] = "│                 │";

        Map<Symbol, Integer> symbolList = o.getSymbols();

        int count = 0;
        StringBuilder s = new StringBuilder();
        for (Symbol symbol : symbolList.keySet()){
            for(int i = 0; i < symbolList.get(symbol); i++){
                s.append(symbols.get(symbol));
                count++;
            }
        }

        String req = switch (count){
            case 1 -> "  " + s + "   ";
            case 2 -> "  " + s + "  ";
            case 3 -> " " + s + "  ";
            case 4 -> " " + s + " ";
            default -> "      ";
        };


        lines[3] = "│  req: " + req + "    │";
        lines[4] = "│                 │";
        lines[5] = "└─────────────────┘";

        return lines;
    }
}
