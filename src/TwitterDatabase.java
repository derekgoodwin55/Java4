import java.util.*;
import java.util.List;
import java.lang.*;

public class TwitterDatabase {

    //PART 1 Start
    //Instance Variables (HashMaps)
    Map<String,TwitterUser> name2User = new HashMap<String,TwitterUser>();
    Map<Tweet,TwitterUser> tweet2User = new HashMap<Tweet, TwitterUser>();
    Map<String,Set<Tweet>> word2Tweet = new HashMap<String, Set<Tweet>>();
    Map<TwitterUser,Set<Tweet>> user2Tweet = new HashMap<TwitterUser, Set<Tweet>>();

    //Constructor
    public TwitterDatabase(String datfile){

        TweetReader tr = new TweetReader(datfile);

        while(tr.advance()){

            TwitterUser tempUser = new TwitterUser(tr.getTweeterID());
            Tweet tempTweet = new Tweet(tr.getTweet());

            //Initializes name2User HashMap
            name2User.put(tempUser.getName(),addOrGetUser(tempUser.getName()));

            //Initializes user2Tweet and tweet2User HashMap
            addTweet(tempTweet.getContent(),addOrGetUser(tempUser.getName()));

            //Creates the word2Tweet HashMap
            Set<String> tweetWords = tempTweet.getWords();
            for(String word : tweetWords){
                addWord(word ,tempTweet);
            }
        }
    }

    //Part 3 consctuctor
    public TwitterDatabase ( String datfile, Map<String, TwitterUser> name2User,
                             Map<Tweet, TwitterUser>tweet2User, Map<String, Set<Tweet>> word2Tweet,
                             Map<TwitterUser, Set<Tweet>> user2Tweet){
        TweetReader tr1 = new TweetReader(datfile);

        while (tr1.advance()) {
            TwitterUser tempUser = new TwitterUser(tr1.getTweeterID());
            Tweet tempTweet = new Tweet(tr1.getTweet());

            //Initializes name2User HashMap
            name2User.put(tempUser.getName(),addOrGetUser(tempUser.getName()));

            //Initializes user2Tweet and tweet2User HashMap
            addTweet(tempTweet.getContent(),addOrGetUser(tempUser.getName()));

            //Creates the word2Tweet HashMap
            Set<String> tweetWords = tempTweet.getWords();
            for(String word : tweetWords){
                addWord(word ,tempTweet);
            }
        }
    }

    //Returns the User ID of Existing Member or Creates new Member and Returns New ID
    public TwitterUser addOrGetUser(String name){

        if(name2User.containsKey(name)){
            return name2User.get(name);
        }
        else{
            TwitterUser temp = new TwitterUser(name);
            name2User.put(name,temp);

            Set<Tweet> tempTweets = new HashSet<Tweet>();
            user2Tweet.put(temp,tempTweets);
            return temp;
        }
    }
    //Method used to initialize word2Tweet HashMap
    public int addWord(String word, Tweet tweet){

        if(word2Tweet.containsKey(word)){
            word2Tweet.get(word).add(tweet);
            return word2Tweet.get(word).size();

        }
        else{
            Set<Tweet> emptyTweetSet = new HashSet<Tweet>();
            emptyTweetSet.add(tweet);
            word2Tweet.put(word,emptyTweetSet);
            return 1;
        }
    }
    //Method used to initialize tweet2User and user2Tweet
    public Tweet addTweet(String msg, TwitterUser user){
        Tweet tempTweet = new Tweet(msg);
        tweet2User.put(tempTweet,user);
        Set<Tweet> tempSetofTweets = new HashSet<Tweet>();

        //Check to prevent NullPointerException
        if(user2Tweet.containsKey(user)) {
            user2Tweet.get(user).add(tempTweet);
        }else{
            user2Tweet.put(user, tempSetofTweets);
            user2Tweet.get(user).add(tempTweet);
        }

        return tempTweet;
    }


    public Map<String,TwitterUser> getNameTable(){
        return name2User;
    }

    public Map<Tweet,TwitterUser> getTweetTable(){
        return tweet2User;
    }

    public Map<String,Set<Tweet>> getWordTable(){
        return word2Tweet;
    }

    public Map<TwitterUser,Set<Tweet>> getUserTable(){
        return user2Tweet;
    }
    //PART 1 END

    // PART 2 START
    // Creates a List of ItemCount Objects of each user and their total number of tweets
    public List<ItemCount> getTweetCounts(){
        long start = System.currentTimeMillis();
        List<ItemCount> tempList = new ArrayList<ItemCount>();

        for (Map.Entry<TwitterUser, Set<Tweet>> entry : user2Tweet.entrySet()) {
            Object key = entry.getKey();
            int value = entry.getValue().size();
            ItemCount pair = new ItemCount(key,value);
            tempList.add(pair);
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("getTweetCounts() took " + duration + "ms to execute");
        return  tempList;
    }

    public List<ItemCount> getWordCounts(){
        long start = System.currentTimeMillis();
        List<ItemCount> tempList = new ArrayList<ItemCount>();

        for (Map.Entry<String, Set<Tweet>> entry : word2Tweet.entrySet()) {
            Object key = entry.getKey();
            int value = entry.getValue().size();
            ItemCount pair = new ItemCount(key,value);
            tempList.add(pair);
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("getWordCounts() took " + duration + "ms to execute");
        return  tempList;
    }

    public List<ItemCount> getWordUsage(String word){
        long start = System.currentTimeMillis();
        List<ItemCount> tempList = new ArrayList<ItemCount>();
        //if word not in set of hash keys
        if(!word2Tweet.containsKey(word)) {
            return tempList;
        }
        //Iterate through user2tweet map
        for (Map.Entry<TwitterUser, Set<Tweet>> entry : user2Tweet.entrySet()) {
            Object key = entry.getKey();
            int value = 0;
            //value +1 if entry contains word
            for(Tweet tweet: entry.getValue()){
                if(tweet.getWords().contains((word))){
                    value++;
                }
            }
            ItemCount pair = new ItemCount(key, value);
            tempList.add(pair);
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("getWordUsage() took " + duration + "ms to execute");
        return tempList;
    }

    public static void main(String[] args) {

        //Create TwitterDatabase object
        TwitterDatabase twitDat = new TwitterDatabase("/home/goodw171/IdeaProjects/HW4/uofmtweets.dat");
        //Create TreeMap and LinearScanMap objects
        TwitterDatabase map = new TwitterDatabase("/home/goodw171/IdeaProjects/HW4/uofmtweets.dat",
                new TreeMap<String,TwitterUser>(),new TreeMap<Tweet, TwitterUser>(),new TreeMap<String, Set<Tweet>>(),new TreeMap<TwitterUser,Set<Tweet>>());
        TwitterDatabase linear = new TwitterDatabase("/home/goodw171/IdeaProjects/HW4/uofmtweets.dat",
                new LinearScanMap<String,TwitterUser>(),new LinearScanMap<Tweet, TwitterUser>(),new LinearScanMap<String, Set<Tweet>>(),new LinearScanMap<TwitterUser,Set<Tweet>>());

        System.out.println("-- Part 1 --");

        System.out.println("name table size = " + twitDat.getNameTable().size());
        System.out.println("tweet table size = " + twitDat.getTweetTable().size());
        System.out.println("word table size = " + twitDat.getWordTable().size());
        System.out.println("user table size = " + twitDat.getUserTable().size());
        System.out.println();

        System.out.println("-- Part 2 --");

        //Call getTweetCounts and generate a List to store values using ItemCount's toString()
        List<ItemCount> tempTweetCountsList = twitDat.getTweetCounts();
        Collections.sort(tempTweetCountsList);
        System.out.println("Top 10 results of getTweetCounts");
        for ( int i = 0; i < 10; i++){
            System.out.println(tempTweetCountsList.get(i));
        }
        System.out.println();

        List<ItemCount> tempWordCountList = twitDat.getWordCounts();
        Collections.sort(tempWordCountList);
        System.out.println("Top 10 results of getWordCounts");
        for ( int i = 0; i < 10; i++){
            System.out.println(tempTweetCountsList.get(i));
        }
        System.out.println();

        List<ItemCount> tempWordUsageCountList1 = twitDat.getWordUsage("university");
        Collections.sort(tempWordUsageCountList1);
        System.out.println("Top 10 results of getWordUsage (university)");
        for ( int i = 0; i < 10; i++){
            System.out.println(tempWordUsageCountList1.get(i));
        }
        System.out.println();

        List<ItemCount> tempWordUsageCountList2 = twitDat.getWordUsage("minnesota");
        Collections.sort(tempWordUsageCountList2);
        System.out.println("Top 10 results of getWordUsage (minnesota)");
        for ( int i = 0; i < 10; i++){
            System.out.println(tempWordUsageCountList2.get(i));
        }
        System.out.println();

        System.out.println("-- Part 3 --");

        System.out.println("HashMap");
        twitDat.getTweetCounts();
        twitDat.getWordCounts();
        twitDat.getWordUsage("minnesota");
        twitDat.getWordUsage("univestiy");
        System.out.println();

        System.out.println("TreeMap");
        map.getTweetCounts();
        map.getWordCounts();
        map.getWordUsage("minnesota");
        map.getWordUsage("university");
        System.out.println();

        System.out.println("LinearScanMap");
        linear.getTweetCounts();
        linear.getWordCounts();
        linear.getWordUsage("minnesota");
        linear.getWordUsage("university");
    }

}
