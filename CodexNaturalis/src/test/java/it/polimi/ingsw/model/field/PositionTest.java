package it.polimi.ingsw.model.field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {

    private Position position;
    @Before
    public void setUp() throws Exception {
        int x = 2;
        int y = 3;
        position = new Position(x, y);
    }

    @After
    public void tearDown() throws Exception {
        position = null;
    }

    @Test
    public void equal(){
        assertEquals(new Position(1,1), new Position(1,1));
    }

    @Test
    public void sum() {
        assertEquals(5, position.sum());
    }

    @Test
    public void distance() {
        Position position2 = new Position(1, 1);
        Position distance = position.distance(position2);
        assertEquals(-1, distance.x());
        assertEquals(-2, distance.y());
    }
}