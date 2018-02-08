package assignment9;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Sean Reddington
 * November 14, 2017
 * CIS1068: Assignment 9 - Netflix challenge
 */

public class Recommender {
    
    /*Gives movie recommendations for the user*/
    public static void giveRecommendations(String[] rec, int[] sortedRatings){
        //how many movies the user has not seen
        int notSeen = 0;
        for(int i=0;i<sortedRatings.length;i++){
            if(sortedRatings[i]==-1)
                notSeen++;
        }

        //Extra credit: Changes how many movies to recommend
        int numRecommendations;
        if(notSeen<5)
            numRecommendations = notSeen;
        else
            numRecommendations = 5;
        
        //final output
        System.out.println("Here are your top movie recommendations:\n");
        
        int j = 0;
        for(int i=0;i<sortedRatings.length;i++){
            if(j<numRecommendations){
                if(sortedRatings[i]==-1){
                    System.out.println(j+1 + ": " + rec[i]);
                    j++;
                }
            }
        }
    }
    
    /*Sorts recommended movies by weighted score.*/
    public static void sortMovies
        (String[] recommended, String[] movieList
                , int[] sRatings, int[] userRatings
                , double[] sAvrg, double[] wScores){
            
            for(int i=0;i<recommended.length;i++){
                for(int j=0;j<sAvrg.length;j++){
                    if(wScores[j]==sAvrg[i]){
                        recommended[i] = movieList[j];
                        sRatings[i] = userRatings[j];
                    }
                }
            }
    }
    
    /*Sorts the weighted scores array by most recommended.*/
    public static double[] sortWeightedAverage(double[] wScores){
        double[] sortedAvrg = new double[wScores.length];
        
        //copys array
        for(int i=0;i<sortedAvrg.length;i++){
            sortedAvrg[i] = wScores[i];
        }
        //sort array
        double largest = 0;
        for(int i=0;i<sortedAvrg.length;i++){
            for(int j=i+1;j<sortedAvrg.length;j++){
                if(sortedAvrg[i]<sortedAvrg[j]){
                    largest = sortedAvrg[j];
                    sortedAvrg[j] = sortedAvrg[i];
                    sortedAvrg[i] = largest;
                }
            }
        }

        return sortedAvrg;
    }
    
    /*Calculates the weighted average of each movie.*/
    public static void getWeightedAverage
    (int[][] ratingsData, double[] simScores, double[] weightedAverageScores){

        for(int i=0;i<ratingsData[i].length;i++){
            double movieAverage = 0;
            double simScoreAverage = 0;
            
            for(int j=0;j<ratingsData.length;j++){
                if(ratingsData[j][i]!=-1)
                    movieAverage += (simScores[j] * ratingsData[j][i]);
                
                simScoreAverage += simScores[j];
            }
            weightedAverageScores[i] = (movieAverage/simScoreAverage);
        }
    }
    
    /*Calculates the similarity scores of other users'(from ratings.txt)
     to the current user.*/
    public static void getSimilarityScores
        (int[] userRatings, int[][] ratingsData, double[] simScores){

        double usersRootedScore = rootedScore(userRatings); //p1

        for(int i=0;i<ratingsData.length;i++){
           double both = 0;
           double othersRootedScore = rootedScore(ratingsData[i]);//p2
           for(int j=0;j<userRatings.length;j++){
               if(userRatings[j] != -1 && ratingsData[i][j] != -1)
                    both += (userRatings[j] * ratingsData[i][j]);
           }
           simScores[i] = both /(usersRootedScore*othersRootedScore);
        }
    }
    
    /*Calculates the rooted score of the passed person's move ratings.*/
    public static double rootedScore(int[] personsRatings){
        double p =0;
        
        for(int i=0;i<personsRatings.length;i++){
            if(personsRatings[i]!=-1)//scoring for unseen movies
                p += personsRatings[i] * personsRatings[i];
        }
        p = Math.sqrt(p);
        return p;
    }
        
    /*Loads movie ratings from ratings.txt into a 2D array*/
    public static void loadRatings(String fileName, int[][] ratingsData)
        throws FileNotFoundException {
        
        Scanner r = new Scanner(new File(fileName));
        for(int i=0;i<ratingsData.length;i++){
            for(int j=0;j<ratingsData[i].length;j++){
                ratingsData[i][j] = r.nextInt();
            }
        }
        r.close();
    }
    
    /*Prompts user to rate each movie,
     then stores the users ratings in an array.*/
    public static void getUserRatings(String[] movieList, int[] userRatings){
        System.out.println("Enter a rating between 1 and 5 for each movie.\n" 
            + "If you have not seen the movie, enter -1.\n");

        Scanner in = new Scanner(System.in);
        for(int i=0;i<movieList.length;i++){
            System.out.print(movieList[i] + ": ");
            int rating = in.nextInt();
            
            //checks for invalid token entered by user
            while((rating == 0 || rating < -1 || rating > 5)){
                System.out.println("Invalid rating: "
                        + "Please choose a number between 1 and 5\n"
                        + "or -1 if you have not seen the movie.\n");
                System.out.print(movieList[i] + ": ");
                rating = in.nextInt();
            }
            userRatings[i] = rating;
        }
        System.out.println();
    }
    
    /*Loads movie names for movies.txt into an array of strings */
    public static void loadMovies(String fileName, String[] movieList) 
        throws FileNotFoundException {
        
        Scanner m = new Scanner(new File(fileName));
        int i = 0;
        
        while(m.hasNextLine() && i<movieList.length){
                movieList[i] = m.nextLine();
                i++;
        }
        m.close();
    }
    
    /*For testing purposes, prints the passed array.*/
    public static void printIntArray(int A[]){
        System.out.print('{');
        for(int i=0;i<A.length;i++){
            System.out.print(A[i]);
            if(i<A.length-1)
                System.out.print(',');
        }
        System.out.print('}');
        System.out.println();
        
    }
    
    /*For testing purposes, prints the passed array.*/
    public static void printDoubleArray(double A[]){
        DecimalFormat format = new DecimalFormat("###.00");
        for(int i=0;i<A.length;i++){
            System.out.println(format.format(A[i]));
        }
        System.out.println();
        
    }    
    
    /*For testing purposes, prints the passed array.*/
    public static void printStringArray(String A[]){
        System.out.print('{');
        for(int i=0;i<A.length;i++){
            System.out.print(A[i]);
            if(i<A.length-1)
                System.out.print(", ");
        }
        System.out.print('}');
        System.out.println();
    }

    public static void main(String[] args) 
        throws FileNotFoundException {

            String moviesFile = "movies.txt";
            String ratingsFile = "ratings.txt";
            String[] movieList = new String[20];
            int[] userRatings = new int[movieList.length];
//            temp userRatings:
//            int[] userRatings = {1,1,1,-1,2,2,2,-1,3,3,3,-1,4,4,4,-1,5,5,5,-1};
            int[][] ratingsData = new int[30][20];
            double[] weightedAverageScores = new double[ratingsData[0].length];
            
            //loads movies and prompts user for ratings
            loadMovies(moviesFile, movieList);
            getUserRatings(movieList, userRatings);
            System.out.println();

            //stores data from ratings.txt
            loadRatings(ratingsFile, ratingsData);

            //gets similarity scores based off cosine similarity formula
            double[] similarityScores = new double[30];
            getSimilarityScores(userRatings, ratingsData, similarityScores);
            
            //gets weighted average of similarity scores
            getWeightedAverage
                (ratingsData, similarityScores, weightedAverageScores);
            
            //sorts similarity scores
            double[] sortedAverageScores = 
                    sortWeightedAverage(weightedAverageScores);
            System.out.println();
            
            String[] recommendedMovies = new String[movieList.length];
            int[] sortedRatings = new int[userRatings.length];
            
            //sorts recommenedMovies and SortedRatings
            sortMovies(recommendedMovies, movieList,
                    sortedRatings, userRatings,
                    sortedAverageScores, weightedAverageScores);
            
            //finally, gives movie recommendations to the user
            giveRecommendations(recommendedMovies, sortedRatings);
            System.out.println();
    }
    
}
