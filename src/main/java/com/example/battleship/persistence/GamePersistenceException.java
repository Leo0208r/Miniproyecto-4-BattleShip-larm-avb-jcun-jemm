package com.example.battleship.persistence;
/**
 * Custom exception class for handling errors that occur during game state persistence operations.
 * <p>
 * Wraps underlying I/O or serialization errors that arise when saving or loading game matches.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class GamePersistenceException extends Exception {
    /**
     * Constructs a new {@code GamePersistenceException} with a detailed message and root cause.
     *
     * @param message Descriptive error message detailing the persistence failure.
     * @param cause   The underlying cause/exception that triggered this failure.
     */
    public GamePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

