import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.hadoop.io.LongWritable;
import org.apache.mahout.clustering.UncommonDistributions;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.clustering.kmeans.KMeansDriver;

public class Main {


        public static void main(String[] args) throws IOException {
        //System.out.println("Hello world!");
        //List<Vector> sampleData = MathUtil.
         List<Vector> sampleData =   readFileToVector("/Users/msughter/Downloads/csc513/certificate_data.csv");
            for (int i = 0; i < sampleData.size(); i++)
            {
                // code block to be executed
                System.out.println(sampleData.get(i).size());
            }
            int k = 4;
            double threshold = 0.01;

            List<Vector> randomPoints = chooseRandomPoints(sampleData,k);
            for (Vector vector:randomPoints){
                System.out.println("Intial Point center: " + vector);
            }

            List<Kluster> clusters = new ArrayList<Kluster>();
            for (int i = 0;i<k;i++){
                clusters.add(new Kluster(randomPoints.get(i),i,new EuclideanDistanceMeasure()));
            }
            File inputData = new File("/Users/msughter/Downloads/csc513/inputdata");
            if (!inputData.exists()){
                inputData.mkdir();
            }

            inputData = new File("/Users/msughter/Downloads/csc513/inputdata/points");
            if(!inputData.exists()){
                inputData.mkdir();
            }

            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            // write the points to the file
            writePointsToFile(sampleData,"/Users/msughter/Downloads/csc513/inputdata/points/file1",fs,conf);
            Path path = new Path("/Users/msughter/Downloads/csc513/inputdata/clusters/part-00000");
            @SuppressWarnings("deprecation")
            SequenceFile.Writer writer = new SequenceFile.Writer(fs,conf,path,Text.class, Kluster.class);

            for (int i = 0;i<k;i++){
               Vector vec = sampleData.get(i);
               Kluster cluster = new Kluster(vec,i,new EuclideanDistanceMeasure());
               writer.append(new Text(cluster.getIdentifier()),cluster);
            }
            writer.close();
            //KMeansDriver.run();
            //KMeansDriver.run();

            //DistanceMeasure measure = new EuclideanDistanceMeasure();
           // KMeansDriver.run(conf, new Path("inputdata/points"), new Path("inputdata/clusters"), new Path("output"), 0.001, 10, true, 0.5, false);
           // KMeansDriver.run(conf, new Path("inputdata/points"), new Path("inputdata/clusters"), new Path("output"), measure, 0.001, 10, true, false);
//            //KMeansDriver.buildClusters(conf,new Path("inputdata/points"),new Path("inputdata/clusters"),new Path("output"),10,"0.001",false);
//            @SuppressWarnings("deprecation")
//            SequenceFile.Reader reader = new SequenceFile.Reader(fs,new Path("output/" + Kluster.CLUSTERED_POINTS_DIR+"/part-m-00000"),conf);
//            IntWritable key = new IntWritable();
//            WeightedVectorWritable value = new WeightedVectorWritable();
//            while (reader.next(key,value)){
//                System.out.println(value.toString()+" belongs to cluster " + key.toString());
//            }
//
//            reader.close();
    }

    public static void writePointsToFile(List<Vector> points,
                                         String fileName,FileSystem fs,Configuration conf) throws IOException {
        Path path = new Path(fileName);
        @SuppressWarnings("deprecation")
        SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf,path, LongWritable.class, VectorWritable.class);

        long recNum = 0;
        VectorWritable vec = new VectorWritable();
        for (Vector point : points) {
            vec.set(point);
            writer.append(new LongWritable(recNum++), vec);
        }    writer.close();
    }

    public static List<Vector> readFileToVector(String file) throws IOException {
        List<Vector> vectors = new ArrayList<Vector>();
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = buffer.readLine()) != null) {
            String[] arr = line.split(",");
            vectors.add(new DenseVector(new double[] { Double.parseDouble(arr[0]), Double.parseDouble(arr[1]),Double.parseDouble(arr[2]),Double.parseDouble(arr[3]) }));
        }
        buffer.close();
        return vectors;
    }

    public static void generateSamples(List<Vector> vectors, int num, double mx, double my, double sd) {
        for (int i = 0; i < num; i++) {
            vectors.add(new DenseVector(new double[] { UncommonDistributions.rNorm(mx, sd), UncommonDistributions.rNorm(my, sd) }));
        }
    }

    public static List<Vector> chooseRandomPoints(Iterable<Vector> vectors, int k) {
        List<Vector> chosenPoints = new ArrayList<Vector>(k);
        Random random = RandomUtils.getRandom();
        for (Vector value : vectors) {
            int currentSize = chosenPoints.size();
            if (currentSize < k) {
                chosenPoints.add(value);
            } else if (random.nextInt(currentSize + 1) == 0) {
                int indexToRemove = random.nextInt(currentSize);
                chosenPoints.remove(indexToRemove);
                chosenPoints.add(value);
            }
        }
        return chosenPoints;
        }
}