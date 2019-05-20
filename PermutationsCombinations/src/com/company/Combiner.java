package com.company;

import java.util.*;

public class Combiner<T> implements Sequences<T, Set<T>> {

    private Collection<T> comboValues;
    private int k;

    private Set<Set<T>> fullSet;
    private Set<Set<T>> limitedSet;



    public Combiner(Collection<T> values, int size){
        comboValues = values;
        k = size;

        fullSet = new HashSet<>();
        limitedSet = new HashSet<>();
    }

    @Override
    public Set<Set<T>> generateAll(){
        T[] combo = (T[])new Object[k];
        fullSet.clear();
        getCombos((T[])comboValues.toArray(), combo, k, 0, 0, comboValues.size());
        return fullSet;
    }

    public void getCombos(T[]elements, T[] combo, int n, int pos, int start, int end){
        if(pos == n){
            // Transfer combination values to set
            Set<T> tmp = new HashSet<>();
            for(T elem : combo)
                tmp.add(elem);

            // Add combination set to main set
            fullSet.add(tmp);
            return;
        }

        for(int i=start; i<end; i++){
            // Set current position
            combo[pos]=elements[i];
            // Recursive call for next position
            // --> find combos with all elements after position
            getCombos(elements, combo, n, pos+1, i+1, end);
        }
    }

    @Override
    public Set<Set<T>> generateSome(int limit){
        T[] combo = (T[])new Object[k];
        limitedSet.clear();
        getLimitedCombos((T[])comboValues.toArray(), combo, k, 0, 0, comboValues.size(), limit);
        return limitedSet;
    }


    public void getLimitedCombos(T[]elements, T[] combo, int n, int pos, int start, int end, int limit){
        // Check if limit has been reached
        if(limitedSet.size() == limit)
            return;
        if(pos == n){
            // Transfer combination values to set
            Set<T> tmp = new HashSet<>();
            for(T elem : combo)
                tmp.add(elem);

            // Add combination set to main set
            limitedSet.add(tmp);
            return;
        }

        for(int i=start; i<end; i++){
            // Set current position
            combo[pos]=elements[i];
            // Recursive call for next position
            // --> find combos with all elements after position
            getLimitedCombos(elements, combo, n, pos+1, i+1, end, limit);
        }
    }

}