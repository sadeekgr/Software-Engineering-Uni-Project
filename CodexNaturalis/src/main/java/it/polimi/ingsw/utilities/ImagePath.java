package it.polimi.ingsw.utilities;

import it.polimi.ingsw.model.card.Symbol;
import javafx.scene.image.Image;

import java.io.InputStream;

/**
 * Utility class for generating Image objects based on specified paths.
 */
public class ImagePath {

    /**
     * Generates an Image object for a card either front or back based on side and id.
     *
     * @param side whether to generate the front or back side of the card
     * @param id   the identifier of the card
     * @return Image object corresponding to the specified card image, or null if not found
     */
    public static Image generate(boolean side, String id){
        InputStream image = ImagePath.class.getResourceAsStream("/images/cards/" +
                (side ? "front" : "back") +
                "/" + id + ".png");
        return image == null ? null : new Image(image);
    }

    /**
     * Generates an Image object for the back side of a card based on the kingdom symbol.
     *
     * @param kingdom the kingdom symbol representing the type of card back
     * @return Image object corresponding to the specified card back image, or null if not found
     */
    public static Image generateBackResource(Symbol kingdom){
        String s = "";
        if(kingdom == Symbol.FUNGI){
            s = "/images/cards/back/001.png";
        } else if (kingdom == Symbol.PLANT) {
            s = "/images/cards/back/011.png";
        } else if(kingdom == Symbol.ANIMAL){
            s = "/images/cards/back/021.png";
        } else if(kingdom == Symbol.INSECT){
            s = "/images/cards/back/031.png";
        }
        InputStream image = ImagePath.class.getResourceAsStream(s);
        return image == null ? null : new Image(image);
    }

    /**
     * Generates an Image object for the gold version of the back side of a card based on the kingdom symbol.
     *
     * @param kingdom the kingdom symbol representing the type of card back
     * @return Image object corresponding to the specified gold card back image, or null if not found
     */
    public static Image generateBackGold(Symbol kingdom){
        String s = "";
        if(kingdom == Symbol.FUNGI){
            s = "/images/cards/back/041.png";
        } else if (kingdom == Symbol.PLANT) {
            s = "/images/cards/back/051.png";
        } else if(kingdom == Symbol.ANIMAL){
            s = "/images/cards/back/061.png";
        } else if(kingdom == Symbol.INSECT){
            s = "/images/cards/back/071.png";
        }
        InputStream image = ImagePath.class.getResourceAsStream(s);
        return image == null ? null : new Image(image);
    }
}
