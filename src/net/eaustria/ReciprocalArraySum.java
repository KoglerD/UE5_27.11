/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria;

/**
 *
 * @author bmayr
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Class wrapping methods for implementing reciprocal array sum in parallel.
 */
public final class ReciprocalArraySum {

    /**
     * Default constructor.
     */
    private ReciprocalArraySum() {
    }

    /**
     * Sequentially compute the sum of the reciprocal values for a given array.
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double seqArraySum(final double[] input) {
        double sum = 0;
        for (double l: input) {
            sum += l;
        }
        return sum;
    }
  

    /**
     * This class stub can be filled in to implement the body of each task
     * created to perform reciprocal array sum in parallel.
     */
    //MEIN THREAD
    private static class ReciprocalArraySumTask extends RecursiveAction {

        private final int startIndexInclusive;
        private final int endIndexExclusive;
        private final double[] input;

        private double value;
        private static int SEQUENTIAL_THRESHOLD = 2;


        /**
         * Constructor.
         * @param setStartIndexInclusive Set the starting index to begin
         *        parallel traversal at.
         * @param setEndIndexExclusive Set ending index for parallel traversal.
         * @param setInput Input values
         */
        ReciprocalArraySumTask(final int setStartIndexInclusive,
                final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
        }



        /**
         * Getter for the value produced by this task.
         * @return Value produced by this task
         */
        public double getValue() {
            return value;
        }


        @Override
        protected void compute() {
            // TODO: Implement Thread forking on Threshold value. (If size of
            // array smaller than threshold: compute sequentially else, fork 
            // 2 new threads
            if (input.length <= SEQUENTIAL_THRESHOLD){
                value = seqArraySum(input);
            }else{
                int mid = input.length / 2;
                ReciprocalArraySumTask firstSubTask = new ReciprocalArraySumTask(startIndexInclusive, mid, Arrays.copyOfRange(input, startIndexInclusive, mid));
                ReciprocalArraySumTask secondSubTask = new ReciprocalArraySumTask(mid, endIndexExclusive, Arrays.copyOfRange(input, mid, endIndexExclusive));

                invokeAll(firstSubTask, secondSubTask);
                firstSubTask.join();
                secondSubTask.join();
                value = firstSubTask.getValue() + secondSubTask.getValue();
                //return firstSubTask.join() + secondSubTask.join();
            }
        }

    }
  

    /**
     * TODO: Extend the work you did to implement parArraySum to use a set
     * number of tasks to compute the reciprocal array sum. 
     *
     * @param input Input array
     * @param numTasks The number of tasks to create
     * @return The sum of the reciprocals of the array input
     */
    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
        double sum = 0;
        // ToDo: Start Calculation with help of ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool(numTasks);
        ReciprocalArraySumTask r = new ReciprocalArraySumTask(0,input.length,input);
        pool.invoke(r);
        sum = r.getValue();
       return sum;
    }

    public static void main(String[] args) {
        System.out.println(ReciprocalArraySum.parManyTaskArraySum(new double[]{1,2,3,4,5,6,7.8}, 8));
    }
}

