package com.example.battleship.model;

import com.example.battleship.model.exceptions.InvalidCoordinateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void shouldCreateValidCoordinate() {
        Coordinate coordinate = new Coordinate(0, 0);
        assertEquals(0, coordinate.getRow());
        assertEquals(0, coordinate.getCol());
    }

    @Test
    void shouldCreateCoordinateAtUpperBound() {
        Coordinate coordinate = new Coordinate(9, 9);
        assertEquals(9, coordinate.getRow());
        assertEquals(9, coordinate.getCol());
    }

    @Test
    void shouldThrowWhenRowIsNegative() {
        assertThrows(InvalidCoordinateException.class, () -> new Coordinate(-1, 0));
    }

    @Test
    void shouldThrowWhenColIsNegative() {
        assertThrows(InvalidCoordinateException.class, () -> new Coordinate(0, -1));
    }

    @Test
    void shouldThrowWhenRowExceedsBoard() {
        assertThrows(InvalidCoordinateException.class, () -> new Coordinate(10, 0));
    }

    @Test
    void shouldThrowWhenColExceedsBoard() {
        assertThrows(InvalidCoordinateException.class, () -> new Coordinate(0, 10));
    }

    @Test
    void shouldFormatToStringAsLetterAndNumber() {
        assertEquals("A1", new Coordinate(0, 0).toString());
        assertEquals("B5", new Coordinate(4, 1).toString());
        assertEquals("J10", new Coordinate(9, 9).toString());
    }

    @Test
    void shouldBeEqualWhenSameRowAndCol() {
        Coordinate a = new Coordinate(3, 4);
        Coordinate b = new Coordinate(3, 4);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentRowOrCol() {
        Coordinate a = new Coordinate(3, 4);
        Coordinate b = new Coordinate(4, 3);
        assertNotEquals(a, b);
    }

    @Test
    void shouldNotBeEqualToNullOrOtherType() {
        Coordinate a = new Coordinate(0, 0);
        assertNotEquals(a, null);
        assertNotEquals(a, "A1");
    }

    @Test
    void shouldBeEqualToItself() {
        Coordinate a = new Coordinate(5, 5);
        assertEquals(a, a);
    }
}
