using System;
using System.Collections.Generic;

namespace equationParse
{

    public class Parser {

        // Make uninstantiable
        private Parser() {
        }

        /// <summary>
        /// States for parsing a double.
        /// </summary>
        private enum DoubleParseStates {
            START,             // start of parsing.
            NEGATIVE_INT,      // number begins with a '-' sign.
            POSITIVE_INT,      // number begins with a '+' sign.
            ZERO,              // number begins with a '0'.
            INT,               // parsing the integer component.
            DECIMAL,           // parsing the decimal component.
            BEFORE_EXP,        // read an 'e' and will parse exponent.
            NEGATIVE_EXP,      // exponent begins with a '-' sign.
            POSITIVE_EXP,      // exponent begins with a '+' sign.
            EXP                // parsing the exponent.
        }

        /// <summary>
        /// Parses the double.
        /// </summary>
        /// <returns>The double parsed from the input string.</returns>
        /// <param name="input">The input string to parse.</param>
        public static double ParseDouble(String input) {

            // used to construct the integer portion of the double.
            int intResult = 0;
            bool isNegative = false;

            // used to construct the decimal portion of the double
            double decimalResult = 0.0;
            double currentPrecision = 0.1;

            // use to construct the exponent portion of the double.
            int exponentResult = 0;
            bool negativeExponent = false;

            DoubleParseStates state = DoubleParseStates.START;

            // position is used for user friendly error reporting
            int pos = 0;

            foreach (char c in input) {

                pos++;

                switch (state) {
                    
                    case DoubleParseStates.START:

                        /*
                        * In the START state, we can get the following transitions:
                        * 
                        *   .    --> DECIMAL
                        *   -    --> NEGATIVE_INT and result is now a negative number.
                        *   +    --> POSITIVE_INT
                        *   1-9  --> INT and result is set to this value.
                        * */

                        if (c == '.')
                            state = DoubleParseStates.DECIMAL;
                        else if (c == '0')
                            state = DoubleParseStates.ZERO;
                        else if (c == '-') {
                            isNegative = true;
                            state = DoubleParseStates.NEGATIVE_INT;
                        } else if (c == '+')
                            state = DoubleParseStates.POSITIVE_INT;
                        else if (Char.IsDigit(c)) {
                            intResult = c - '0';
                            state = DoubleParseStates.INT;
                        } else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");

                        break;

                    case DoubleParseStates.NEGATIVE_INT:
                    case DoubleParseStates.POSITIVE_INT:

                        /*
                         *  In the NEGATIVE_INT and POSITIVE_INT states, we can get the following transitions:
                         * 
                         *   .    --> DECIMAL
                         *   1-9  --> INT and result is set to this value.
                         * */

                        if (Char.IsDigit(c) && c != 0) {
                            intResult = c - '0';
                            state = DoubleParseStates.INT;
                        } else if (c == '.')
                            state = DoubleParseStates.DECIMAL;
                        else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");

                        break;

                    case DoubleParseStates.INT:

                        /*
                         *  In the INT state, we can get the following transitions:
                         * 
                         *   .    --> DECIMAL
                         *   e    --> BEFORE_EXP
                         *   0-9  --> stay in INT and result updated to end in this value
                         * */

                        if (Char.IsDigit(c))
                            intResult = intResult * 10 + (c - '0');
                        else if (c == '.')
                            state = DoubleParseStates.DECIMAL;
                        else if (c == 'e' || c == 'E')
                            state = DoubleParseStates.BEFORE_EXP;
                        else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");

                        break;

                    case DoubleParseStates.ZERO:

                        /*
                         *  In the INT state, we can get the following transitions:
                         * 
                         *   .    --> DECIMAL
                         * */

                        if (c == '.')
                            state = DoubleParseStates.DECIMAL;
                        else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");
                        break;

                    case DoubleParseStates.DECIMAL:

                        /*
                         *  In the DECIMAL state, we can get the following transitions:
                         * 
                         *   e    --> BEFORE_EXP
                         *   0-9  --> DECIMAL and update the decimal result to end in this value
                         * */

                        if (Char.IsDigit(c)) {
                            decimalResult += (c - '0') * currentPrecision;
                            currentPrecision *= 0.1;
                        } else if (c == 'e' || c == 'E')
                            state = DoubleParseStates.BEFORE_EXP;
                        else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");
                        break;

                    case DoubleParseStates.BEFORE_EXP:

                        /*
                         *  In the BEFORE_EXP state, we can get the following transitions:
                         * 
                         *   -    --> NEGATIVE_EXP
                         *   +    --> POSITIVE_EXP
                         *   1-9  --> EXP and exponent is set to this value
                         * */

                        if (c == '-') {
                            negativeExponent = true;
                            state = DoubleParseStates.NEGATIVE_EXP;
                        } else if (c == '+')
                            state = DoubleParseStates.POSITIVE_EXP;
                        else if (Char.IsDigit(c) && c != '0') {
                            exponentResult = c - '0';
                            state = DoubleParseStates.EXP;
                        } else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");
                        break;

                    case DoubleParseStates.NEGATIVE_EXP:
                    case DoubleParseStates.POSITIVE_EXP:

                        /*
                         *  In the NEGATIVE_EXP and POSITIVE_EXP states, we can get the following transitions:
                         * 
                         *   1-9  --> EXP and exponent is set to this value.
                         * */

                        if (Char.IsDigit(c) && c != '0') {
                            exponentResult = c - '0';
                            state = DoubleParseStates.EXP;
                        } else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");
                        break;

                    case DoubleParseStates.EXP:

                        /*
                         *  In the EXP state, we can get the following transitions:
                         * 
                         *   0-9  --> stay in EXP and exponent is updated to end in this value
                         * */

                        if (Char.IsDigit(c))
                            exponentResult = exponentResult * 10 + (c - '0');
                        else
                            throw new ParseException("Unexpected " + c + " at position " + pos + ".");
                        break;
                }
            }

            // check that the state is either INT, ZERO, DECIMAL or EXP at the end of the parse
            if (!(state == DoubleParseStates.INT || state == DoubleParseStates.ZERO || state == DoubleParseStates.DECIMAL || state == DoubleParseStates.EXP))
                throw new ParseException("Invalid double.");

            // calculate the double to return
            double result = (double)intResult + decimalResult;

            // set a negative number
            if (isNegative)
                result = -result;

            // adjust based on exponent
            if (negativeExponent) {
                // negative exponent means divide by 10 exp times.
                for (int i = 0; i < exponentResult; i++)
                    result /= 10;
            } else {
                // negative exponent means multiply by 10 exp times.
                for (int i = 0; i < exponentResult; i++)
                    result *= 10;
            }
             
            return result;
        }

       // TAKES INPUT AND CURRENT INDEX TO RETURN THE DOUBLE IN EQUATION
       // SETS THE VARIABLE
       // CHANGES TO NEW INDEX
        public static double ParseEquation(string input, ref int index)
        {
            string theDouble = "";
            double defaultVal = 1;

            // GO THROUGH INPUT
            for(int i = index; i < input.Length; i++)
            {
                // VARIABLE IS REACHED WHEN CHAR IS A LETTER THAT ISN'T E
                if(char.IsLetter(input[i]) && input[i] != 'E' && input[i] != 'e')
                {
                    // SET NEW INDEX FOR NEXT ROUND
                    index = i;
                    if (theDouble == "")
                        // IF EMPTY RETURN 1
                        return defaultVal;
                    else
                    {
                        // PARSE THE DOUBLE
                        // IF NOT VALID, A PARSE ERROR WILL BE THROWN
                        return ParseDouble(theDouble);
                    }                     
                }
                else
                {
                    // IF NOT THE VARIABLE, IS PART OF THE DOUBLE
                    theDouble += input[i];
                }
            }

            throw new ParseException("Invalid equation");
        }
    }

    public class ParseException : Exception
    {
        public ParseException()
        {
        }
        public ParseException(string message)
            : base(message)
        {
        }

        public ParseException(string message, Exception inner)
            : base(message, inner)
        {
        }
    }
}
