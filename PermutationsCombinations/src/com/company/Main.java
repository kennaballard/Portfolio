package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> vals = new ArrayList<>();
        vals.add(1);
        vals.add(2);
        vals.add(3);
        vals.add(4);

        int limit = 5;

        Permuter<Integer> perm = new Permuter<>(vals, 2);

        /*------------------- TEST PERMUTATIONS ---------------------*/
        /* GENERATE ALL */
        Set<List<Integer>> perms = perm.generateAll();
        System.out.println("All permutations of " + vals.toString() + " : " + perms.toString());

        /* GENERATE SOME */
        Set<List<Integer>> limitPerms = perm.generateSome(limit);
        System.out.println(limit + " permutations of " + vals.toString() + " : " + limitPerms.toString());

        /*------------------- TEST COMBINATIONS ---------------------*/
        Combiner<Integer> comb= new Combiner<>(vals, 2);

        /* GENERATE ALL */
        Set<Set<Integer>> combos = comb.generateAll();
        System.out.println("All Combinations " + vals.toString() + " : " + combos.toString());

        /* GENERATE SOME */
        limit = 3;
        Set<Set<Integer>> limitCombos = comb.generateSome(limit);
        System.out.println(limit + " Combinations of " + vals.toString() + " : " + limitPerms.toString());
    }
}
