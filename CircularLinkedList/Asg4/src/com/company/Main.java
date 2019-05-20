package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class Main {

    private static class DoubleLink<T> {
        public T element;
        public DoubleLink<T> next;
        public DoubleLink<T> prev;

        public DoubleLink() {
        }

        public DoubleLink(T element) {
            this.element = element;
        }

        public static DoubleLink<Integer> buildCircle(int n){
            Integer elem = 1;

            DoubleLink<Integer> builder = new DoubleLink<>(elem);
            DoubleLink<Integer> head = builder;
            for(int i = elem + 1; i <= n; i++){
                // temp to save previous
                DoubleLink<Integer> tmp = builder;

                // set next
                builder.next = new DoubleLink<>(i);
                builder = builder.next;
                // set previous
                builder.prev = tmp;
            }
            // complete circle
            head.prev = builder;
            builder.next = head;

            return head;
        }
    }


    public static void main(String[] args) throws IOException {
        // number of elements in circle
        int n;
        // steps to take clockwise
        int m = 0;
        // steps to take counterclockwise
        int o = 0;

        // GET AND VALIDATE INTEGERS
        n = getInput("n");

        while(n <= 0){
            n = getInput("n");
        }

        while(m + o <= 0) {

            m = getInput("m");
            while(m < 0){
                m = getInput("m");
            }

            o = getInput("o");
            while(o < 0){
                o = getInput("o");
            }
        }

        // CREATE "CIRCLE"
        DoubleLink<Integer> circle = DoubleLink.buildCircle(n);

        boolean clockwise = true;

        // REMOVE FROM CIRCLE
        while(circle != null) {
            int count = 1;
            if (clockwise && m != 0) {
                // USE M
                while (count < m) {
                    // GO TO NEXT
                    circle = circle.next;
                    count++;
                }
                // SAVE ITEM TO BE REMOVED & REMOVE LINK
                circle = printAndRemove(circle);
            }
            else if(!clockwise && o != 0){
                // USE O
                while (count < o) {
                    count++;
                    // GO TO PREV
                    circle = circle.prev;
                }
                // SAVE ITEM TO BE REMOVED & REMOVE LINK
                circle = printAndRemove(circle);
            }
            clockwise = !clockwise;
        }
    }

    public static DoubleLink<Integer> printAndRemove(DoubleLink<Integer> original){
        DoubleLink<Integer> circle = original;

        Integer elem = circle.element;

        if (circle.prev == circle)
            // LAST ITEM HAS BEEN REMOVED
            circle = null;
        else {
            // REMOVE LINK
            DoubleLink<Integer> tmp = circle;
            circle = circle.next;
            circle.prev = tmp.prev;
            circle.prev.next = circle;
        }

        // OUTPUT REMOVED ELEMENT
        System.out.print(elem + " ");

        return circle;
    }

    public static int getInput(String elem) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter " + elem + ":");

        return Integer.parseInt(input.readLine());
    }
}
