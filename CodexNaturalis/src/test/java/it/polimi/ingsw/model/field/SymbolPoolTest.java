package it.polimi.ingsw.model.field;

import it.polimi.ingsw.model.card.Symbol;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SymbolPoolTest {

    private SymbolPool symbolPool;

    @Before
    public void setUp() throws Exception {
        symbolPool = new SymbolPool();

    }

    @After
    public void tearDown() throws Exception {
        symbolPool = null;
    }

    @Test
    public void addSymbol() {
        symbolPool.addSymbol(Symbol.ANIMAL);
        symbolPool.addSymbol(Symbol.ANIMAL);
        symbolPool.addSymbol(Symbol.INSECT);
        symbolPool.addSymbol(Symbol.INSECT);
        symbolPool.addSymbol(Symbol.INSECT);
        assertEquals(2, symbolPool.getNumOfSymbol(Symbol.ANIMAL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.FUNGI));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.PLANT));
        assertEquals(3, symbolPool.getNumOfSymbol(Symbol.INSECT));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.INKWELL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.QUILL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.MANUSCRIPT));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.EMPTY));
    }

    @Test
    public void removeSymbol() {
        symbolPool.addSymbol(Symbol.ANIMAL);
        symbolPool.addSymbol(Symbol.ANIMAL);
        symbolPool.addSymbol(Symbol.INSECT);
        symbolPool.addSymbol(Symbol.INSECT);
        symbolPool.addSymbol(Symbol.INSECT);
        symbolPool.removeSymbol(Symbol.ANIMAL);
        symbolPool.removeSymbol(Symbol.INSECT);
        symbolPool.removeSymbol(Symbol.INSECT);
        assertEquals(1, symbolPool.getNumOfSymbol(Symbol.ANIMAL));
        assertEquals(1, symbolPool.getNumOfSymbol(Symbol.INSECT));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.PLANT));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.FUNGI));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.INKWELL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.QUILL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.MANUSCRIPT));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.EMPTY));
    }

    @Test
    public void getNumOfSymbol() {
        symbolPool.addSymbol(Symbol.ANIMAL);
        symbolPool.addSymbol(Symbol.ANIMAL);
        symbolPool.addSymbol(Symbol.ANIMAL);
        symbolPool.removeSymbol(Symbol.ANIMAL);
        symbolPool.addSymbol(Symbol.INSECT);
        symbolPool.addSymbol(Symbol.PLANT);
        symbolPool.addSymbol(Symbol.FUNGI);
        symbolPool.removeSymbol(Symbol.FUNGI);
        assertEquals(2, symbolPool.getNumOfSymbol(Symbol.ANIMAL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.FUNGI));
        assertEquals(1, symbolPool.getNumOfSymbol(Symbol.PLANT));
        assertEquals(1, symbolPool.getNumOfSymbol(Symbol.INSECT));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.INKWELL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.QUILL));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.MANUSCRIPT));
        assertEquals(0, symbolPool.getNumOfSymbol(Symbol.EMPTY));
    }


}