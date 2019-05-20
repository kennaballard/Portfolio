using System;
namespace equationParse
{
    public class MatrixException : Exception {
        public MatrixException() {
        }
        public MatrixException(string message)
            : base(message) {
        }

        public MatrixException(string message, Exception inner)
            : base(message, inner) {
        }
    }
   
}
