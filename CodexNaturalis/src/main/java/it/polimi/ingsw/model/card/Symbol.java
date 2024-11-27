package it.polimi.ingsw.model.card;

/**
 * Represents various types of symbols, including both kingdom types and object types.
 */
public enum Symbol {
    EMPTY, FUNGI, INSECT, PLANT, ANIMAL, QUILL, INKWELL, MANUSCRIPT;

    /**
     * @return true if the symbol is kingdom type, otherwise false
     */
    public boolean isKingdom() {
        return this == Symbol.FUNGI || this == Symbol.INSECT || this == Symbol.PLANT || this == Symbol.ANIMAL;
    }

    /**
     * @return true if the symbol is object type, otherwise false
     */
    public boolean isObject() {
        return this == Symbol.QUILL || this == Symbol.MANUSCRIPT || this == Symbol.INKWELL;
    }
}
