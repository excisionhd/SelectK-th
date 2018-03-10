/***
 * file: Analysis.java
 * author: Amir Sotoodeh
 * class: CS 331 - Design and Analysis of Algorithms
 *
 * assignment: Project 2
 * date last modified: 3/6/18
 *
 * purpose: Compare the performance of four selection algorithms for
 * determining kth item.
 **/
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Analysis {

    private static final String FILENAME = "C:\\Users\\user\\Desktop\\CS331Project2\\src\\data.txt";

    public static void main(String[] args){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, true))) {

        Random r = new Random();
        final int SIZE = 10000000;
        final int K_VALUE = 1;

        int[] ARRAY = new int[SIZE];
        int[] ARRAY2 = new int[SIZE];
        int[] ARRAY3 = new int[SIZE];
        int[] ARRAY4 = new int[SIZE];


        for(int i = 0;i<SIZE;i++){
            ARRAY[i] = r.nextInt(200)+1;
        }

        ARRAY2 = Arrays.copyOf(ARRAY,SIZE);
        ARRAY3 = Arrays.copyOf(ARRAY,SIZE);
        ARRAY4 = Arrays.copyOf(ARRAY,SIZE);

        long totalTime = 0;
        //TEST AVERAGE TIME FOR 10 TRIALS
        long startTime = System.nanoTime();
        Alg1(ARRAY, 0, ARRAY.length);
        long endTime = System.nanoTime();
        totalTime = (endTime-startTime);
        bw.write(Long.toString(totalTime)+"\n");
        System.out.println("ALG 1 MERGESORT: ");
        System.out.println(totalTime);

        long startTime2 = System.nanoTime();
        int kth = Alg2Iterative(ARRAY2, 0, ARRAY2.length-1,K_VALUE);
        long endTime2 = System.nanoTime();
        totalTime = (endTime2-startTime2);
        bw.write(Long.toString(totalTime)+"\n");
        System.out.println("ALG 2 QUICKSORT PARTITION ITERATIVE: ");
        System.out.println(totalTime);

        long startTime3 = System.nanoTime();
        int kth2 = Alg2Recursive(ARRAY3,0,ARRAY3.length-1,K_VALUE);
        long endTime3 = System.nanoTime();
        totalTime = (endTime3-startTime3);
        System.out.println("ALG 2 QUICKSORT PARTITION RECURSIVE: ");
        bw.write(Long.toString(totalTime)+"\n");
        System.out.println(totalTime);

        Analysis a = new Analysis();
        long startTime4 = System.nanoTime();
        int kth3 = a.Alg3(ARRAY4,K_VALUE,0,ARRAY4.length-1);
        long endTime4 = System.nanoTime();
        totalTime = (endTime4-startTime4);
        System.out.println("ALG 3 MEDIAN OF MEDIANS: ");
        bw.write(Long.toString(totalTime)+"\n");
        System.out.println(totalTime);


        } catch (IOException e) {

            e.printStackTrace();

        }
    }


    //FOLLOWING STANDARD MERGE SORT ALGORITHM
    public static void Alg1(int[] a, int low, int high)
    {
        int size = high - low;
        if (size <= 1)
            return;
        int middle = low + size/2;

        // RECURSIVE CALL TO PARTITION
        Alg1(a, low, middle);
        Alg1(a, middle, high);

        // MERGE TWO PARTITIONS
        int[] hold = new int[size];
        int i = low, mid = middle;
        for (int count = 0; count < size; count++)
        {
            if (i == middle) {
                hold[count] = a[mid++];
            }
            else if (mid == high) {
                hold[count] = a[i++];
            }
            else if (a[mid]<a[i]) {
                hold[count] = a[mid++];
            }
            else {
                hold[count] = a[i++];
            }
        }
        for (int counter = 0; counter < size; counter++) {
            a[low + counter] = hold[counter];
        }
    }

    //QUICKSELECT ITERATIVE
    private static int Alg2Iterative(int[] array, int low, int high, int k) {
        if(low == high) {
            return array[low];
        }

        //LOOP UNTIL CONDITIONS MET
        while(true) {

            //GENERATE RANDOM PIVOT INDEX
            int pivotIndex = low + (int) Math.random() * (high - low + 1);

            //GET PIVOT VALUE FROM ARRAY
            int pivotValue = array[pivotIndex];

            //HOLD TEMPORARY VALUE
            int temp1 = array[pivotIndex];

            //SET ITEM EQUAL TO LAST ITEM
            array[pivotIndex] = array[high];

            //TAKE LAST ITEM TO HOLD
            array[high] = temp1;

            //GET STARTING INDEX
            int sI = low;

            //ITERATE AND SWAP
            for(int i = low; i < high; i++) {
                if(array[i] < pivotValue) {
                    int temp2 = array[sI];

                    array[sI] = array[i];
                    array[i] = temp2;
                    sI++;
                }
            }

            // LAST MOVEMENT FOR PIVOT, SWAP
            int t3 = array[high];

            array[high] = array[sI];
            array[sI] = t3;
            pivotIndex = sI;

            //RETURN K
            if(k == pivotIndex) {
                return array[k];
            } else if(k < pivotIndex) {
                high = pivotIndex - 1;
            } else {
                low = pivotIndex + 1;
            }
        }
    }

    //QUICKSELECT RECURSIVE
    private static int Alg2Recursive(int[] nums, int low, int high, int k) {
        //IF ARRAY SIZE = 0
        if (low == high) {
            return nums[low];
        }

        //DETERMINE PIVOT INDEX
        int pI = partition1(nums, low, high);

        //IF PIVOTINDEX = K, RETURN IT
        if (k == pI) {
            return nums[k];
        }
        //K IS LESS THAN PIVOT INDEX, SEARCH LEFT SIDE
        else if (k < pI) {
            return Alg2Recursive(nums, low, pI - 1, k);
        }
        //K IS GREATER THAN PIVOT INDEX. SEARCH RIGHT SIDE
        else {
            return Alg2Recursive(nums, pI + 1, high, k);
        }

    }

    //HELPER FUNCTION FOR ALG2RECURSIVE TO PARTITION ARRAYS
    private static int partition1(int[] array, int low, int high) {
        //PIVOTINDEX IS MIDDLE
        int pI = low + (high - low) / 2;
        int p = array[pI];

        //PIVOT SWAPPED TO THE END
        int temp = array[pI];
        array[pI] = array[high];
        array[high] = temp;

        int sI = low;
        //ITERATE AND SWAP
        for (int i = low; i < high; i++) {
            if (array[i] < p) {
                int temp2 = array[i];
                array[i] = array[sI];
                array[sI] = temp2;
                sI++;
            }
        }

        //SWAPPING PIVOT INTO POSITION
        int temp3 = array[high];
        array[high] = array[sI];
        array[sI] = temp3;
        return sI;
    }

    //ALG 3 QUICKSELECT MEDIAN OF MEDIANS, SIMILAR TO ALG2 RECURSIVE
    private int Alg3(int array[],int k,int low,int high)
    {
        if(low == high) return array[low];
        int n = alg3Partition(array,low,high);
        int length = n - low + 1;

        //IF K IS THE MEDIAN, RETURN IT (BEST CASE)
        if(length == k) {
            return array[n];
        }

        //IF K<MEDIAN SEARCH LEFT
        if(length > k) {
            return Alg3(array,k,low,n-1);
        }

        //IF K>MEDIAN SEARCH RIGHT
        else {
            return Alg3(array,k-length,n+1,high);
        }

    }


    //HELPER FUNCTION FOR ALG3 TO PARTITION
    private static int alg3Partition(int array[],int low, int high)
    {
        //OBTAIN THE VALUE OF THE PIVOT
        int pV = determinePivotValue(array, low, high);

        //DETERMINE WHERE THE PIVOTVALUE IS STORED WHEN SORTED
        while(low < high) {
            while(array[low] < pV) {
                low ++;
            }
            while(array[high] > pV) {
                high--;
            }
            if(array[low] == array[high]) {
                low ++;
            }
            if(low < high) {
                int t = array[low];
                array[low] = array[high];
                array[high] = t;
            }

        }
        return high;
    }

    //RETURNS THE VALUE OF THE PIVOT USING MEDIAN OF MEDIANS
    private static int determinePivotValue(int array[],int low,int high)
    {
        //GENERATING ARRAYS TO HOLD N/R SUBSETS OF SIZE 5
        int t[] = null;
        int medianArray[] = new int[(high-low+1)/5];
        int medianArrayIndex = 0;

        //UNTIL THROUGH ENTIRE ARRAY
        while(low <= high) {
            //GET VALUE OF NEXT
            t = new int[Math.min(5,high-low+1)];
            //MOVE INTO SUBARRAY
            for(int i=0;i<t.length && low <= high;i++) {
                t[i] = array[low];
                low++;
            }

            //UTILIZE BUILT IN LIBRARY TO SORT
            Arrays.sort(t);
            //CALCULATE THE MEAN TO STORE
            medianArray[medianArrayIndex] = t[t.length/2];
            //INCREMENT
            medianArrayIndex++;
        }

        // Call recursively to find median of medians
        return determinePivotValue(medianArray,0,medianArray.length-1);
    }


    //PRINT ARRAY
    public static void print(int array[])
    {
        for (int i=0; i<array.length; ++i)
            System.out.print(array[i]+" ");
        System.out.println();
    }



}
