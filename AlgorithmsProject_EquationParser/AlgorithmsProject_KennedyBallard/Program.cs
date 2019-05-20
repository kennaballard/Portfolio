using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace equationParse
{
    class Program
    {
        static void Main(string[] args)
        {
            // ***************** TESTING ***************** \\

            // ----- VALID INPUTS ----- \\

            Console.WriteLine(" *************** VALID INPUTS ***************");

            // NORMAL INPUTS
            Test("3x+2y=6", "x+9y=7");

            // IN DIFFERENT ORDER
            Test("3x+2y=5", "3y+2x=5");
            Test("5a-2b=2", "2a+4b=20");

            // SUBTRACTION
            Test("3x-5y=14", "2x+5y=26");

            // ONE TERM
            Test("3x=5", "3y+2x=5");
            Test("3y+2x=5", "-3y=4");

            // SCIENTIFIC NOTATION
            Test("3.0E-1x+2.0e-2y=5.33E+1", "2x+3y=3");


            // ----- INVALID INPUTS ----- \\

            Console.WriteLine("\n\n *************** INVALID INPUTS ***************");

            // TOO MANY TERMS
            Test("3x-2y+2a=9", "3x+y=2");

            // WRONG VARIABLES
            Test("3a+2b=5", "3c+2a=5");

            // SPACES
            Test("x - y=15", "3x+3y=12");

            Console.ReadLine();
        }

        // PRINTS OUT THE TEST RESULTS
        public static void Test(string eq1, string eq2)
        {
            Console.WriteLine("\n\n- Test: ");
            Console.WriteLine(eq1 + "\n" + eq2 + "\n");

            try
            {
                // SHOW SOLUTION
                string res = SolveEquationSystem(eq1, eq2);

                Console.WriteLine(res);
            }
            catch (Exception e)
            {
                // IF FAILS --> SHOW ERROR MESSAGE
                Console.WriteLine(e.Message);
            }
        }

        // CALCULATES THE UNKNOWNS FOR A SIMPLE SYSTEM OF EQUATIONS
        // --> 2 UNKNOWNS, 2 EQUATIONS
        public static string SolveEquationSystem(string equation1, string equation2)       
        {
            const int TERMS = 2;
           
            // VALUES DEFAULT TO 0
            Matrix A = new Matrix(TERMS, TERMS);
            Matrix B = new Matrix(TERMS, 1);

            // PARSE THROUGH THE FIRST AND SECOND EQUATION, SAVE THE VARIABLES USED FOR UNKNOWNS
            char[] firstVars = SetValsGetVars(TERMS, ref A, ref B, equation1, 0);

            char[] secondVars = SetValsGetVars(TERMS, ref A, ref B, equation2, 1);

            // CHECK THAT THERE ARE ONLY TWO VARIABLES
            CheckNull(firstVars, secondVars);

            if (firstVars[0] == secondVars[0])
            {
                if (firstVars[1] != secondVars[1])
                    throw new ParseException("Too many variables");
            }
            else if (firstVars[1] == secondVars[0] && firstVars[0] == secondVars[1])
            {
                // SWAP POSITIONS TO MATCH VARIABLE ORDER
                double temp = A.Get(1, 1);
                A.Set(1, 1, A.Get(1, 0));
                A.Set(1, 0, temp);
            }
            // CHECK FOR EQUATIONS THAT HAVE A SINGLE TERM
            else
                throw new ParseException("Too many variables");
            


            // CALCULATE INVERSE MATRIX
            try
            {
                Matrix C = (A.Inverse()).Mult(B);
                string result = firstVars[0] + " = " + C.Get(0, 0) + ", " + firstVars[1] + " = " + C.Get(1, 0);

                return result;
            }
            catch
            {
                throw new Exception("These equations are unsolvable");
            }
        }

        // TAKES IN THE CURRENT EQUATION, PARSES THROUGH IT
        // --> SEPERATES THE DOUBLES FROM OPERATORS, RETURNS THE CHARS USED FOR UNKNOWNS
        // --> PUTS THE DOUBLES IN THE MATRICES
        public static char[] SetValsGetVars(int TERMS, ref Matrix A, ref Matrix B, string equation, int row)
        {
            char op = '=';
            bool neg = false;
            int index = 0;

            // WILL HOLD VARIABLE CHARS
            char[] variables = new char[TERMS];

            // PARSE EQUATION 1
            for (int i = 0; i < TERMS; i++)
            {
                double curr = Parser.ParseEquation(equation, ref index);
                if (neg)
                    curr = -curr;
             
                // SET THE MATRIX CELL
                A.Set(row, i, curr);

                // SAVE CHAR
                variables[i] = equation[index];
                // GET THE OPERATOR
                op = equation[++index];

                // THERE ARE NO MORE TERMS --> RESULT REMAINS
                if (op == '=')
                    break;

                // CHECK OPERATOR
                if (op == '-')
                {
                    // APPLY FLAG NEGATIVE
                    neg = true;
                }
                else if (op != '+')
                {
                    // THE CHARACTER IS INVALID
                    throw new ParseException("Equation has an invalid operator/character");
                }

                index++;
            }
            
            if(op != '=')
            {
                throw new ParseException("Too many terms");
            }
            // SAVE RESULT IN MATRIX
            string res = equation.Substring(++index);
            B.Set(row, 0, Parser.ParseDouble(res));

            return variables;
        }

        // CHECKS IF IS A SINGLE TERM IN THE EQUATION 
        // --> I.E. THE CONSTANT IS ZERO: 3X=5 .... 3X+0Y=5
        public static void CheckNull(char[] first, char[] second)
        {
            if(first[1] == '\0')
            {
                if (first[0] == second[0])
                    first[1] = second[1];
                else if (first[0] == second[1])
                    first[1] = second[0];
                else
                    throw new ParseException("Invalid variables");

            }
            else if(second[1] == '\0')
            {
                if (second[0] == first[0])
                    second[1] = first[1];
                else if (second[0] == first[1])
                    second[1] = first[0];
                else
                    throw new ParseException("Invalid variables");
            }
        }
    }
}
