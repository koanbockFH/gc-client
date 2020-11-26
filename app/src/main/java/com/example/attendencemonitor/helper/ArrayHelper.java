package com.example.attendencemonitor.helper;

public final class ArrayHelper
{
    public static boolean contains(int[] array, int v) {
        if(array == null)
        {
            return false;
        }
        for(int i : array){
            if(i == v){
                return true;
            }
        }

        return false;
    }
}
