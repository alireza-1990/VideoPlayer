package com.alirezaahmadi.videoplayer.util;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    public static int[] convertIntegerListToArray(List<Integer> integerList){
        int[] result = new int[integerList.size()];

        for(int i = 0; i < integerList.size(); i++)
            result[i] = integerList.get(i);

        return result;
    }

    public static List<Integer> convertIntArrayToList(int[] intArray){
        List<Integer> integerList = new ArrayList<>();

        for(int value: intArray)
            integerList.add(value);

        return integerList;
    }
}
