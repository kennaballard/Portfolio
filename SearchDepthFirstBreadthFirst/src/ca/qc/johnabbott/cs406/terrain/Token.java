/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.terrain;

import java.util.NoSuchElementException;

/**
 * <p>The characters used to input and output a terrain to console. Uses UNICODE characters.</p>

 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public enum Token {

      EMPTY(' ')
    , START('@')
    , GOAL('X')
    , WALL('#')
    , VERTICAL('\u2502')
    , HORIZONTAL('\u2500')
    , DOWN_AND_RIGHT('\u250c')
    , DOWN_AND_LEFT('\u2510')
    , UP_AND_RIGHT('\u2514')
    , UP_AND_LEFT('\u2518')
    , GREY_TOKEN('\u2591')
    , BLACK_TOKEN('\u2592')
    , BORDER_HORIZONTAL('\u2550')
    , BORDER_VERTICAL('\u2551')
    , BORDER_UP_AND_RIGHT('\u255a')
    , BORDER_UP_AND_LEFT('\u255d')
    , BORDER_DOWN_AND_RIGHT('\u2554')
    , BORDER_DOWN_AND_LEFT('\u2557')
    , PATH('*')
    , UNKNOWN('?')
    ;

    /**
     * A 2D array to help construct the solution path.
     */
    public static final Token[][] path =
            {    // NONE   UP            DOWN            LEFT           RIGHT
                    {   EMPTY, EMPTY,        EMPTY,          EMPTY,         EMPTY },           // NONE
                    {   EMPTY, EMPTY,        VERTICAL,       UP_AND_LEFT,   UP_AND_RIGHT   },  // UP
                    {   EMPTY, VERTICAL,     EMPTY,          DOWN_AND_LEFT, DOWN_AND_RIGHT },  // DOWN
                    {   EMPTY, UP_AND_LEFT,  DOWN_AND_LEFT,  EMPTY,         HORIZONTAL     },  // LEFT
                    {   EMPTY, UP_AND_RIGHT, DOWN_AND_RIGHT, HORIZONTAL,    EMPTY,          }, // RIGHT
            };

    /**
     * Get the Token constant from a specific character. For example, the Token for character '#' will be WALL.
     * @param c the character.
     * @return the Token corresponding to `c`.
     */
    public static Token fromChar(char c) {
        for(Token t : values()) {
            if(t.c == c)
                return t;
        }
        throw new NoSuchElementException();
    }

    // fields

    private char c;

    /**
     * Create a Token using a character.
     * @param c token character.
     */
    Token(char c) {
        this.c = c;
    }

    /**
     * Get string representation of the token.
     * @return the string representation of the token.
     */
    public String toString() {
        return Character.toString(c);
    }



}
