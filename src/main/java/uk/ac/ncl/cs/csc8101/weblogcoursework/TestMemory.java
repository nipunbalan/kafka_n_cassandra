package uk.ac.ncl.cs.csc8101.weblogcoursework;

import java.util.ArrayList;

/**
 * Created by nipun on 2/12/2015.
 */
public class TestMemory {

    public static void main(String[] args) {

        int mb = 1024 * 1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        System.out.println("##### Heap utilization statistics [MB] #####");

        //Print used memory
        System.out.println("Used Memory:"
                + (runtime.totalMemory() - runtime.freeMemory()) / mb + "MB");

        //Print free memory
        System.out.println("Free Memory:"
                + runtime.freeMemory() / mb + "MB");

        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb + "MB");

        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb + "MB");


        ArrayList<String> arr = new ArrayList<String>();
        for (int i = 0; i < 20000000; i++) {
            arr.add("4464 [01/May/1998:02:58:34 +0000] 4464 [01/May/1998:02:58:34 +0000] 4464 [01/May/1998:02:58:34 +0000] 4464 [01/May/1998:02:58:34 +0000] ");
        }


        System.out.println("##### Heap utilization statistics [MB] #####");

        //Print used memory
        System.out.println("Used Memory:"
                + (runtime.totalMemory() - runtime.freeMemory()) / mb + "MB");

        //Print free memory
        System.out.println("Free Memory:"
                + runtime.freeMemory() / mb + "MB");

        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb + "MB");

        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb + "MB");




    }
}
