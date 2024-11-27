package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.field.CardPlacement;

import java.util.*;

/**
 * Represents an objective related to the disposition of cards on the player's field.
 * This objective checks for specific patterns of card placements.
 */
public class DispositionObjective extends Objective{
    // Array to store pattern positions
    private final Position[] patternPosition;
    // Array to store pattern kingdoms
    private final Symbol[] patternKingdom;
    
    public Position[] getPatternPosition() {
        return Arrays.copyOf(patternPosition, 2);
    }

    public Symbol[] getPatternKingdom() {
        return Arrays.copyOf(patternKingdom, 3);
    }

    /**
     * Constructs a DispositionObjective with the specified pattern positions, pattern kingdoms, score, and identifier.
     *
     * @param p     The array of pattern positions.
     * @param s     The array of pattern kingdoms.
     * @param score The score associated with achieving this objective.
     * @param id    Identifier for the objective.
     */
    public DispositionObjective(Position[] p, Symbol[] s, int score, String id) {
        super(score, id);
        patternPosition = p;
        patternKingdom = s;
    }

    /**
     * Calculates the difference between the positions of two card placements.
     *
     * @param c1 the first card placement
     * @param c2 the second card placement
     * @return the position representing the difference between the positions of the two card placements
     */
    private Position difference(CardPlacement c1, CardPlacement c2) {
        return c1.getPosition().distance(c2.getPosition());
    }

    /**
     * Finds all occurrences of the specified card pattern within the given list of card placements.
     *
     * @param cards the list of card placements to search for occurrences
     * @return an ArrayList containing arrays representing occurrences of the pattern
     */
    private ArrayList<int[]> findAllOccurrences(ArrayList<CardPlacement> cards){
        ArrayList<int[]> occurrences = new ArrayList<>();

        int c1_index = 0;
        for(CardPlacement c1: cards){
            if(c1.getKingdom() == patternKingdom[0]){

                int c2_index = 0;
                for(CardPlacement c2: cards){
                    if (c2.getKingdom() == patternKingdom[1] && difference(c1, c2).equals(patternPosition[0])) {

                        int c3_index = 0;
                        for (CardPlacement c3 : cards) {
                            if (c3.getKingdom() == patternKingdom[2] && difference(c1, c3).equals(patternPosition[1])) {
                                occurrences.add(new int[]{c1_index, c2_index, c3_index});
                            }

                            c3_index += 1;
                        }
                    }

                    c2_index += 1;
                }
            }

            c1_index += 1;
        }

        return occurrences;
    }

    /**
     * Counts the number of occurrences of the specified card pattern without allowing the same card to be used in multiple occurrences.
     *
     * @param arrays the list of arrays representing occurrences of the pattern
     * @return the count of occurrences without repetition
     */
    private int countWithoutRepetition(ArrayList<int[]> arrays){
        //Count frequency of each number
        Map<Integer, Integer> freq = new HashMap<>();

        for (int[] arr : arrays){
            for(int n : arr){
                if(freq.containsKey(n)){
                    freq.put(n, freq.get(n) + 1);
                }
                else{
                    freq.put(n, 1);
                }
            }
        }

        int res = 0;
        while (!arrays.isEmpty()) {
            //Find the element with minimum number frequency
            int[] chosen = arrays.stream().min((a, b) -> {
                int a_sum = 0;
                for (int n : a) {
                    a_sum += freq.get(n);
                }

                int b_sum = 0;
                for (int n : b) {
                    b_sum += freq.get(n);
                }

                return Integer.compare(a_sum, b_sum);
            }).get();

            res += 1;

            //Remove all the array that have any number in common with the chosen array
            // Collect arrays to be removed
            List<int[]> arraysToRemove = new ArrayList<>();
            for (int[] arr : arrays) {
                for (int n : chosen) {
                    if (arr[0] == n || arr[1] == n || arr[2] == n) {
                        for (int k : arr) {
                            freq.put(n, freq.get(k) - 1);
                        }
                        arraysToRemove.add(arr);
                        break; // No need to continue checking if any number is found
                    }
                }
            }

            // Remove collected arrays
            for (int[] arr : arraysToRemove) {
                arrays.remove(arr);
            }
        }

        return res;
    }

    /**
     * Calculates the total score achievable for this objective based on the provided player field.
     * This method overrides the abstract method in the superclass.
     *
     * @param field the player field on which to calculate the score
     * @return the total score achievable for this objective based on the provided player field
     */
    @Override
    public  int calculateObjectiveCompletionTimes(PlayerField field) {
        ArrayList<int[]> occurrences = findAllOccurrences(field.getCards());

        return countWithoutRepetition(occurrences);
    }
}
