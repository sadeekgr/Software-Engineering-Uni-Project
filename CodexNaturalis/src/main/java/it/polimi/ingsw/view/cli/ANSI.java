package it.polimi.ingsw.view.cli;

/**
 * ANSI escape codes for text and background colors.
 * This class provides constants for various ANSI escape codes that can be used
 * to format text and background colors in the console.
 * <p>
 * The class is not meant to be instantiated.
 * </p>
 */
public class ANSI {
    /**
     * ANSI escape code for gold text.
     */
    public static final String GOLD = "\u001B[33m";

    /**
     * ANSI escape code for dark green text.
     */
    public static final String GREEN = "\u001B[32m";

    /**
     * ANSI escape code for red text.
     */
    public static final String RED = "\u001B[31m";

    /**
     * ANSI escape code for purple text.
     */
    public static final String PURPLE = "\u001B[35m";

    /**
     * ANSI escape code for blue text.
     */
    public static final String BLUE = "\u001B[34m";

    /**
     * ANSI escape code for a light green background.
     */
    public static final String BG_LIGHT_GREEN = "\u001B[102m";

    /**
     * ANSI escape code for a light red background.
     */
    public static final String BG_LIGHT_RED = "\u001B[101m";

    /**
     * ANSI escape code for a light purple background.
     */
    public static final String BG_LIGHT_PURPLE = "\u001B[105m";

    /**
     * ANSI escape code for a light blue background.
     */
    public static final String BG_LIGHT_BLUE = "\u001B[104m";

    /**
     * ANSI escape code for a yellow background.
     */
    public static final String HIGHLIGHT = "\033[43m";

    /**
     * ANSI escape code to reset text formatting.
     */
    public static final String RESET = "\u001B[0m";

    /**
     * ANSI escape code to clear the screen.
     */
    public static final String CLEAR_SCREEN = "\033[2J";

    /**
     * ANSI escape code to move the cursor to the start of the screen.
     */
    public static final String MOVE_CURSOR_TO_START = "\033[H";

    /**
     * Private constructor to prevent instantiation.
     */
    private ANSI(){}

    /**
     * Removes ANSI escape codes from the given string.
     *
     * @param str the string from which to remove ANSI escape codes.
     * @return a new string without ANSI escape codes.
     */
    public static String removeANSI(String str) {
        String strNoAnsi = str.replace(RESET, "");
        strNoAnsi = strNoAnsi.replace(GOLD, "");
        strNoAnsi = strNoAnsi.replace(GREEN, "");
        strNoAnsi = strNoAnsi.replace(RED, "");
        strNoAnsi = strNoAnsi.replace(PURPLE, "");
        strNoAnsi = strNoAnsi.replace(BLUE, "");
        strNoAnsi = strNoAnsi.replace(BG_LIGHT_GREEN, "");
        strNoAnsi = strNoAnsi.replace(BG_LIGHT_RED, "");
        strNoAnsi = strNoAnsi.replace(BG_LIGHT_PURPLE, "");
        strNoAnsi = strNoAnsi.replace(BG_LIGHT_BLUE, "");
        return strNoAnsi;
    }

    /**
     * Calculates the length of the string excluding ANSI escape codes.
     *
     * @param str the string whose length is to be calculated.
     * @return the length of the string without ANSI escape codes.
     */
    public static int strLenSkipANSI(String str){ return removeANSI(str).length(); }

    /**
     * Inserts a string into another string at the specified position, considering ANSI escape codes.
     *
     * @param str the original string.
     * @param in the string to be inserted.
     * @param pos the position at which to insert the string.
     * @return a new string with the inserted string.
     */
    public static String insertSkipANSI(String str, String in, int pos){
        int inLen = strLenSkipANSI(in);
        if(inLen < pos){
            return in + " ".repeat(pos - inLen) + str;
        } else if (inLen > pos) {
            if (inLen < pos + strLenSkipANSI(str)){
                return substringSkipANSI(in, 0, pos) + str;
            } else {
                return substringSkipANSI(in, 0, pos) + str + substringSkipANSI(in,pos + strLenSkipANSI(str));
            }
        } else {
            return in + str;
        }
    }

    /**
     * Returns a substring of the given string starting from the specified position, considering ANSI escape codes.
     *
     * @param str the original string.
     * @param start the starting position of the substring.
     * @return a substring of the original string.
     */
    public static String substringSkipANSI(String str, int start){
        if(start == 0){
            return str;
        }

        for(int i = str.length(); i >= start; i--){
            if(strLenSkipANSI(str.substring(0, i)) == start){
                // Check if there are ANSI before and take that too
                if(i > start && strLenSkipANSI(str.substring(0, i - 1)) > start){
                    int j = i - 1;
                    while(strLenSkipANSI(str.substring(0, j)) > start){ j--; }
                    return str.substring(j);
                }
                return str.substring(i);
            }
        }
        return "";
    }

    /**
     * Returns a substring of the given string between the specified start and end positions, considering ANSI escape codes.
     *
     * @param str the original string.
     * @param start the starting position of the substring.
     * @param end the ending position of the substring.
     * @return a substring of the original string.
     */
    public static String substringSkipANSI(String str, int start, int end){
        String partial = null;
        for(int i = str.length(); i >= end; i--){
            if(strLenSkipANSI(str.substring(0, i)) == end){
                partial = str.substring(0, i);
                break;
            }
        }

        if(partial == null)
            return "";

        return substringSkipANSI(partial, start);
    }

    /**
     * Pads the given string to the specified length with spaces, considering ANSI escape codes.
     *
     * @param str the original string.
     * @param len the desired length of the string after padding.
     * @return a new string padded with spaces to the desired length.
     */
    public static String padSkipANSI(String str, int len){
        int strLen = strLenSkipANSI(str);
        if(strLen < len)
            return str + " ".repeat(len - strLen);
        return str;
    }
}
