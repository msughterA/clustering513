import java.io.File;
import java.io.IOException;
import org.apache.commons.cli.*;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import java.util.Scanner;
import java.util.Vector;
import java.util.ArrayList;
public class VerifyCertificate {
    public static void main(String[] args){
        Scanner myObj = new Scanner(System.in);

        //userId
        System.out.println("Enter userId");
        // Numerical input
        double userId = myObj.nextDouble();

        //cgpa
        System.out.println("Enter cgpa");
        // Numerical input
        double cgpa = myObj.nextDouble();

        //DepartmentId
        System.out.println("Enter DepartmentId");
        double departmentId = myObj.nextDouble();

        //SchoolId
        System.out.println("Enter SchoolId");
        double schoolId = myObj.nextDouble();

        //System.out.println("all parameters cached");

        //center of the first cluster
        DenseVector c1 = new DenseVector(new double[] {25.5,2.128,7.0,4.75});

        //center of the second cluster
        DenseVector c2 = new DenseVector(new double[] {41.5,2.344,9.125,5.188});

        //center of the third cluster
        DenseVector c3 = new DenseVector(new double[] {9.875,2.045,16.375,4.75});


        //center of the fourth cluster
        DenseVector c4 = new DenseVector(new double[] {8.222,3.304,4.889,5.889});

        // get the input vector
        DenseVector inputVector = new DenseVector(new double[] {userId,cgpa,departmentId,schoolId});

        // set a threshold distance that would be used to determine if a certificate is valid or not
        double THRESHOLD = 12.0;

        EuclideanDistanceMeasure measure = new EuclideanDistanceMeasure();

        // calculate the distance from the first cluster
        double c1Distance = measure.distance(c1, inputVector);

        // calculate the distance from the second cluster
        double c2Distance = measure.distance(c2, inputVector);

        // calculate the distance from the third cluster
        double c3Distance = measure.distance(c3, inputVector);

        // calculate the distance from the fourth cluster
        double c4Distance = measure.distance(c4, inputVector);

        // Create an ArrayList of doubles
        double[] myClusters =  new double[4];

        // Add some strings to the list
        myClusters[0] = c1Distance;
        myClusters[1] = c2Distance;
        myClusters[2] = c3Distance;
        myClusters[3] = c4Distance;

       int clusterGroup = 1;
       double smallestDistance = c1Distance;
        for (int i = 2; i <= 3; i++) {
            if(smallestDistance > myClusters[i]){
                clusterGroup =i;
                smallestDistance = myClusters[i];
            }

        }
        if(smallestDistance <= THRESHOLD){
            System.out.println("THE CERTIFICATE IS VALID AND BELONGS TO CLUSTER "+ clusterGroup);
        }else{
            System.out.println("THE CERTIFICATE IS INVALID");
        }
    }
}
