using System;
namespace equationParse
{
    // ====================================================================
    // Matrix class
    //
    // provides standard operations on Matrices.
    // NOTE: Matrices are zero based (rather than 1 based as in usual math context)
    // ====================================================================
    public class Matrix
    {
        private int _rows;
        private int _cols;
        private double[,] _data;

        public int Rows { get { return _rows; } }
        public int Cols { get { return _cols; } }

        // ====================================================================
        // Matrix constructor 
        // Inputs: rows, cols (size of the Matrix)
        // Returns: a Matrix object
        // Errors: Throws exception if row or cols less than 1
        // ====================================================================
        public Matrix(int rows, int cols)
        {
            // set rows & columns
            if (rows < 1 || cols < 1)
            {
                throw new MatrixException("Invalid row/col definition");
            }
            _rows = rows;
            _cols = cols;

            // create private 2d array and initialize
            _data = new double[rows, cols];
            for (int r = 0; r < rows; r++)
            {
                for (int c = 0; c < cols; c++)
                {
                    _data[r, c] = 0;
                }
            }

        }

        // ====================================================================
        // Matrix constructor
        // Inputs: 2d integer array (used to set the new matrix)
        // Returns: a Matrix object
        // ====================================================================
        public Matrix(double[,] data_array)
        {
            _rows = data_array.GetLength(0);
            _cols = data_array.GetLength(1);

            // create private 2d array and initialize
            _data = new double[this.Rows,this.Cols];

            for (int r = 0; r < this.Rows; r++)
            {
                for (int c = 0; c < this.Cols; c++)
                {
                    _data[r, c] = data_array[r,c];
                }
            }

        }

        // ====================================================================
        // Matrix constructor 
        // Inputs: 1d integer array (creates a 1xn matrix, or vector)
        // Returns: a Matrix object;
        // ====================================================================
        public Matrix(double[] data_array)
        {
            _rows = 1;
            _cols = data_array.Length;

            // create private 2d array and initialize
            _data = new double[this.Rows,this.Cols];

            for (int r = 0; r < this.Rows; r++)
            {
                for (int c = 0; c < this.Cols; c++)
                {
                    _data[r, c] = data_array[c];
                }
            }

        }

        // ====================================================================
        // validate row and column requests
        // Inputs: row/col
        // Returns: true if row/col valid for this Matrix object
        // ====================================================================
        private Boolean valid_row_col(int row, int col)
        {

            if (row < 0 || row >= this.Rows || col < 0 || col >= this.Cols)
            {
                return false;
            }
            return true;

        }


        // ====================================================================
        // Get indivividual cell value
        // Inputs:  row/col
        // Returns: The value of the matrix at that row/col (zero indexed)
        // Errors: Throws exception if row/col not valid for this Matrix object
        // ====================================================================
        public double Get(int row, int col)
        {

            // input validation
            if (!this.valid_row_col(row, col))
            {
                throw new MatrixException("Invalid row or column definition in Get");
            }

            // return info
            return _data[row, col];

        }

        // ====================================================================
        // Set indivividual cell value
        // - sets the matrix element at row/col (zero indexed) to value
        // Inputs: row/col, value
        // Returns: void
        // ====================================================================
        public void Set(int row, int col, double value)
        {
            // input validation
            if (!this.valid_row_col(row, col))
            {
                throw new MatrixException("Invalid row or column definition in Set");
            }

            // set info
            _data[row, col] = value;
            return;
        }

        // ====================================================================
        // return this matrix multipled by a scalar
        // Inputs: value
        // Returns: a NEW matrix which is equal to the scalar multiplication
        //          of this matrix times the input value
        // ====================================================================
        public Matrix Mult(double rhs)
        {

            Matrix lhs = new Matrix(this.Rows, this.Cols);
            for (int r = 0; r < this.Rows; r++)
            {
                for (int c = 0; c < this.Cols; c++)
                {
                    lhs.Set(r, c, this.Get(r, c) * rhs);
                }
            }
            return lhs;
        }


        // ====================================================================
        // Add rhs and this matrix, return result
        // Inputs: Matrix object
        // Returns: a NEW matrix which is the result of adding this matrix
        //          to the input matrix
        // Errors: If the two matrices are not the same size, an
        //         exception is thrown
        // ====================================================================
        public Matrix Add(Matrix rhs)
        {
            // validate size of both arrays
            if (this.Rows != rhs.Rows || this.Cols != rhs.Cols)
            {
                throw new MatrixException("Cannot add two matrices of different sizes");
            }

            // add two matrices
            Matrix lhs = new Matrix(this.Rows, this.Cols);
            for (int r = 0; r < this.Rows; r++)
            {
                for (int c = 0; c < this.Cols; c++)
                {
                    lhs.Set(r, c, this.Get(r, c) + rhs.Get(r, c));
                }
            }
            return lhs;
        }

        // ====================================================================
        // Multiply this and rhs matrices, return result
        // Inputs: Matrix object
        // Returns: a NEW matrix which is the result of multiplying this matrix
        //          by the input matrix
        // Errors: If this matrix's rows is not equal to the input matrix
        //         columns, matrix multipliciation cannot occur, and an
        //         exception is thrown
        // ====================================================================
        public Matrix Mult(Matrix rhs)
        {
            // can these two matrices be multiplied?
            if (this.Cols != rhs.Rows)
            {
                throw new MatrixException("Cannot multiply these two matrices because their sizes are not compatible");
            }

            // multiply two matrices
            Matrix lhs = new Matrix(this.Rows, rhs.Cols);

            // foreach row of 'this'
            for (int r = 0; r < this.Rows; r++)
            {
                // multiply by col of 'rhs'
                for (int c = 0; c < rhs.Cols; c++)
                {
                    double tmp = 0;
                    for (int i = 0; i < this.Cols; i++)
                    {
                        tmp = tmp + this.Get(r, i) * rhs.Get(i, c);
                    }
                    lhs.Set(r, c, tmp);
                }
            }
            return lhs;
        }

        // ====================================================================
        // Transpose
        // Inputs: none
        // Returns: A new matrix which is the transpose of the original
        // ====================================================================
        public Matrix Transpose()
        {

            // transpose the matrix
            Matrix rhs = new Matrix(this.Cols, this.Rows);

            for (int r = 0; r < this.Rows; r++)
            {
                for (int c = 0; c < this.Cols; c++)
                {
                    rhs.Set(c, r, this.Get(r, c));
                }
            }
            return rhs;
        }


        // ====================================================================
        // return inverse of this matrix.  Can only do a 2x2 matrix
        // Inputs: none
        // Returns: a NEW matrix object which is the inverse of the original
        // Errors:
        //      - throws exception if the original matrix is not 2x2
        //      - throws exception if original matrix is not invertible
        //        (zero determinant)
        // ====================================================================
        public Matrix Inverse()
        {
            // make sure we are working only with a 2x2 matrix
            if (this.Rows != 2 || this.Cols != 2)
            {
                throw new MatrixException("Cannot do inverse on non 2x2 matrix");
            }

            // calculate the determinant
            double d = this.Get(0, 0) * this.Get(1, 1) - this.Get(0, 1) * this.Get(1, 0);

            // Note: is difficult to compare floating points numbers because of round-off
            //       errors, so instead of comparing to zero, is determinant less than some
            //       really small number.
            if (Math.Abs(d) < 1e-99)
            {
                throw new MatrixException("This matrix is non-inversible");
            }

            // set the elements
            Matrix lhs = new Matrix(this.Rows, this.Cols);
            lhs.Set(0, 0, this.Get(1, 1) / d);
            lhs.Set(1, 1, this.Get(0, 0) / d);
            lhs.Set(0, 1, -this.Get(0, 1) / d);
            lhs.Set(1, 0, -this.Get(1, 0) / d);
            return lhs;
        }

        // ====================================================================
        // ToString
        // Inputs: None
        // Returns: A string that represents the matrix
        // ====================================================================
        public override string ToString()
        {
            String str = "";

            // calculate the max size of string for each value
            // "G5" general notation, with precision of 5
            int max = 0;
            for (int r = 0; r < this.Rows ;r++) {
                for (int c = 0; c < this.Cols; c++) {
                    max = max > this.Get(r, c).ToString("G5").Length ? 
                                    max : this.Get(r, c).ToString("G5").Length;
                }
            }

            // loop over rows
            for (int r = 0; r < this.Rows; r++)
            {

                // starting a row, insert brackect
                str = str + "[ ";

                // loop over every column in this row
                for (int c = 0; c < this.Cols; c++)
                {
                    // pad strings so numbers always line up
                    str = str + this.Get(r, c).ToString("G5").PadLeft(max+3,' ' );
                }

                // end row, insert end bracket
                str = str + "  ]\n";
            }

            // insert cr at end, so that matrix string will always end with
            // a blank line
            str = str + "\n";

            return str;

        }
    }
}
