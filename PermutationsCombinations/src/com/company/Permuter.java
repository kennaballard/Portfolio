package com.company;

import java.util.*;


public class Permuter<T> implements Sequences<T, List<T>> {

    // The collection of elements to be "permuted"
    private Collection<T> permuteVals;
    // Num of elements to be in each permutation
    private int k;

    private Set<List<T>> fullSet;
    private Set<List<T>> limitSet;

    public Permuter(Collection<T> values, int size){
        permuteVals = values;
        k = size;
        fullSet = new HashSet<>();
        limitSet = new HashSet<>();
    }

    /***************** GETS ALL PERMUTATIONS **************/

    // Returns all possible permutations --> CALLS RECURSIVE FUNCTION
    public Set<List<T>> generateAll(){
        fullSet.clear();
        // A temp of size k
        T[] perm = (T[]) new Object[k];
        // call recursive algorithm
        getPermutations((ArrayList<T>) permuteVals, perm, k, 0, permuteVals.size());
        return fullSet;

    }

    // SETS PERMUTATION RESULTS
    private void getPermutations(ArrayList<T> elements, T[] perm, int spotsLeft, int start, int len){
        if(spotsLeft == 0){
            // add the permutation to the set
            T[] addition = (T[]) new Object[k];
            for(int i=0; i<k; i++)
                addition[i]=perm[i];
            fullSet.add(Arrays.asList(addition));
            return;
        }

        for(int i=start; i<len; i++){
            // set current position in the temp
            perm[k-spotsLeft] = elements.get(i);
            // call recursively, for next position
            getPermutations(elements, perm, spotsLeft-1, start+1, len);
        }
    }

    // TODO

    /************ GETS LIMITED NUMBER OF PERMUTATIONS ***************/
    // Returns "limit" number of permutations
    public Set<List<T>> generateSome(int limit){
        limitSet.clear();
        // A temp of size k
        T[] perm = (T[]) new Object[k];
        // call recursive algorithm
        getPermutationsLimited((ArrayList<T>) permuteVals, perm, k, 0, permuteVals.size(), (Integer) limit);
        return  limitSet;
    }

    private void getPermutationsLimited(ArrayList<T> elements, T[] perm, int spotsLeft, int start, int len, int limit){
        // Check if limit has been reached
        if(limitSet.size() == limit)
            return;
        if(spotsLeft == 0){
            // add the permutation to the set
            T[] addition = (T[]) new Object[k];
            for(int i=0; i<k; i++)
                addition[i]=perm[i];
            limitSet.add(Arrays.asList(addition));
            // increment permutation count
            return;
        }

        for(int i=start; i<len; i++){
            // set current position in the temp
            perm[k-spotsLeft] = elements.get(i);
            // call recursively, for next position
            getPermutationsLimited(elements, perm, spotsLeft-1, start+1, len, limit);
        }
    }
}
