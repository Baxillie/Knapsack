import com.sun.org.apache.xpath.internal.operations.Div;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by baxie on 15-12-15.
 */
public class Knapsack {

    public static final int MAXIMUM_PRIZE = 20;
    public static final int MINIMUM_PRIZE = 5;
    public static final int MINIMUM_FREQUENCY = 20;
    public static final int MAXIMUM_FREQUENCY = 40;
    public static final int TRUCK_LENGTH = 165;
    public static final int TRUCK_WIDTH = 25;
    public static final int TRUCK_HEIGHT = 40;

    public static void main(String[] argv) throws HollowSpace.NoRoomException{
        //create rng
        Random rng = new Random(System.currentTimeMillis());

        //create a set of products
        //product A, B and C as described in the project booklet
        Product A = new Product(10, 10, 20, 3/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "A");
        Product B = new Product(10, 15, 20, 4/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "B");
        Product C = new Product(15, 15, 15, 5/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "C");
        Product[] originals = {A, B, C};

        //creation of set
        Product[] set = createDefaultProductArray();
        System.out.println("Sorting finished");

        //create a truck as described in the project booklet and fill it with the made set

        Truck truck = new Truck(165, 25, 40);
        greedyFill(truck, set);

       /* Truck truck1 = new Truck(10, 25, 20);
     // DivideFill(truck1, set);
        truck1.add(A);
        truck1.add(B);
        System.out.println(truck1);
        truck1.printTruckCoronally();
/*
        Truck truck2 = new Truck(80, 25, 20);
        greedyFill(truck2, set);

        Truck truck3 = new Truck(55, 25, 40);
        greedyFill(truck3, set);

        Truck truck4 = new Truck(165, 25, 20);
        greedyFill(truck4, set);
*/
        //backTrackFill(truck, set);
        //testShit();*/
    }

    public static void testShit() throws HollowSpace.NoRoomException
    {
        Product A = new Product(5, 5, 5, 5/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "A");
        Truck truck = new Truck(165, 25, 40);

        for(int i = 0; i < 40; i++)
        {
            truck.add(A);
        }
        System.out.println(truck);
        truck.printTruckCoronally(0,1,1);
    }
    public static void DivideFill(Truck truck, Product[] set)
    {

        System.out.println("####################BACKTRACK_FILL################################");
        long beginTime = System.nanoTime();

        //int value = backTrack(truck, getArrayCopy(set), 0);
        //System.out.println("value of truck: " + value);
        int count = 0;
        int score;
        int max=0;
        int i=0;
        Product[] order= new Product[20];
        Product[] BestOrder;
        while(truck.canFit(set[count])|| count != 20)
        {
            try{
                truck.add(set[count]);
                count++;
                order[i]= set[count];
            }
            catch(HollowSpace.NoRoomException e)
            {

                score = truck.getValue();
                if(score > max) {
                    max = score;
                    e.printStackTrace();
                    BestOrder = order;
                    truck.remove(count);
                    order[i]= null;
                    i--;

                    count++;


                }


            }
        }


    long endTime = System.nanoTime();
    System.out.println(truck);
    truck.printTruckCoronally();
    System.out.printf("Execution time: %d ms\n", ((double)endTime - beginTime) / 10e9);
    System.out.println("##################################################################");

}


    public static void backTrackFill(Truck truck, Product[] set)
    {

        System.out.println("####################BACKTRACK_FILL################################");
        long beginTime = System.nanoTime();

        //int value = backTrack(truck, getArrayCopy(set), 0);
        //System.out.println("value of truck: " + value);
        int count = 0;
        int score;
        int max=0;

        while(truck.canFit(set[count]))
        {
            try{
                truck.add(set[count]);
                count++;
            }
            catch(HollowSpace.NoRoomException e)
            {
                score = truck.getValue();
                if(score > max) {
                    max = score;

                    e.printStackTrace();

                    //int[][]
                }
                //truck.remove(set[count])
                count++;


                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        System.out.println(truck);
        truck.printTruckCoronally();
        System.out.printf("Execution time: %d ms\n", (endTime - beginTime) );
        System.out.println("##################################################################");

    }

    public Product[] getArrayCopy(Product[] set)
    {
        Product[] copy = new Product[set.length];
        System.arraycopy(set,0,copy,0,set.length);
        return copy;
    }

    public static int fillTreeLeft(Truck truck, Product[] set, int n)
    {
        if(set.length == 0 || !truck.canFit(set[0]))
        {
            return truck.getValue();
        }
        else
        {
            try {
                truck.fill(set[0], n);
                Product[] newSet = new Product[set.length - 1];
                System.arraycopy(set, 1, newSet, 0, newSet.length);
                return fillTreeLeft(truck, newSet, n+1);
            } catch (HollowSpace.NoRoomException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void greedyFill(Truck truck, Product[] set)
    {
        System.out.println("####################GREEDY_FILL################################");
        long beginTime = System.currentTimeMillis();
        //sort the set from lowest to highest value/volume rating(value density)
        Arrays.sort(set);

        //begin filling the truck with the highest value density until the truck is full or there are no more products
        int index = set.length - 1;
        while(true)
        {
            System.out.printf("Putting Product with index %d in truck\n", index);
            try
            {
                truck.add(set[index].clone());
                index--;
            }
            catch( HollowSpace.NoRoomException e)
            {
                System.out.print("Truck is full\n");
                break;
            }
            catch ( ArrayIndexOutOfBoundsException e)
            {
                System.out.println("every product in the set has been added to the truck");
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("Execution time: %d ms\n", endTime - beginTime);
        System.out.println("###############################################################");
        System.out.println("Results: ");
        truck.printTruckCoronally();
        System.out.println(truck);
    }

    public static Product[] createRandomProductArray(Product[] originals, Random rng)
    {
        Product[] initalSet = new Product[originals.length * MAXIMUM_FREQUENCY];
        int index = 0;
        for (Product product : originals) {
            int frequency = MINIMUM_FREQUENCY + rng.nextInt(MAXIMUM_FREQUENCY - MINIMUM_FREQUENCY);
            System.out.printf("Filling the set with %d pieces of product %s\n", frequency, product.getName());
            for (int i = 0; i < frequency; i++) {
                initalSet[index] = product.clone();
                index++;
            }
        }
        //clean up null variables
        Product[] set = new Product[index];
        System.arraycopy(initalSet, 0, set, 0, index);

        //print for debugging
        for(int i = 0; i < set.length; i++)
        {
            //System.out.println(set[i]);
        }

        return set;
    }

    public static Product[] createDefaultProductArray()
    {
        Product A = new Product(10, 10, 20, 3/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "A");
        Product B = new Product(10, 15, 20, 4/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "B");
        Product C = new Product(15, 15, 15, 5/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "C");
        Product[] originals = {A, B, C};

        Product[] set = new Product[90];
        int index = 0;
        int frequency = 10;
        for (Product product : originals) {
            frequency += 10;
            System.out.printf("Filling the set with %d pieces of product %s\n", frequency, product.getName());
            for (int i = 0; i < frequency; i++) {
                set[index] = product.clone();
                index++;
            }
        }

        //print for debugging
        for(int i = 0; i < set.length; i++)
        {
            //System.out.println(set[i]);
        }

        return set;
    }

    public static ProductSet getDefaultProductSet()
    {
        Product A = new Product(10, 10, 20, 3/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "A");
        Product B = new Product(10, 15, 20, 4/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "B");
        Product C = new Product(15, 15, 15, 5/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "C");
        Product[] originals = {A, B, C};
        Set<Product> alleles = new HashSet<>();
        alleles.addAll(Arrays.asList(originals));

        ProductSet ps = new ProductSet(new Truck(TRUCK_LENGTH, TRUCK_WIDTH, TRUCK_HEIGHT),
                new Random(System.currentTimeMillis()), alleles);
        int frequency = 10;
        for (Product product : originals) {
            frequency += 10;
            ps.addProduct(product, frequency);
        }
        return ps;
    }

    public static ProductSet getDefaultUnboundedProductSet()
    {
        Product A = new Product(10, 10, 20, 3/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "A");
        Product B = new Product(10, 15, 20, 4/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "B");
        Product C = new Product(15, 15, 15, 5/*MINIMUM_PRIZE + rng.nextInt(MAXIMUM_PRIZE - MINIMUM_PRIZE)*/, "C");
        Product[] originals = {A, B, C};
        Set<Product> alleles = new HashSet<>();
        alleles.addAll(Arrays.asList(originals));

        ProductSet ps = new ProductSet(new Truck(TRUCK_LENGTH, TRUCK_WIDTH, TRUCK_HEIGHT),
                new Random(System.currentTimeMillis()), alleles);
        int frequency = 30;
        for (Product product : originals) {
            //frequency += 10;
            ps.addProduct(product, frequency);
        }
        return ps;
    }

}
