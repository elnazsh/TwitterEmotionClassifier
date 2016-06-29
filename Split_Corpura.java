import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by elnaz on 27.06.16.
 */
public class Split_Corpura {

    public static void main(String[] args) {

        try
        {
            String[] negationalTerms = {" no ",  " no!", " no?", " no.", " no_", " no-", " no,", "_no ", "-no ", ",no ", ".no ",
                    "#no ", "#no_", "#no-",
                    "not", "none", "no one", "nobody", "neither", "nowhere", "never",
                    "don't", "doesn't", "didn't", "aren't", "isn't", "wasn't", "weren't", "haven't", "hasn't",
                    "hadn't", "won't", "shouldn't", "wouldn't", "can't", "couldn't", "ain't",
                    "dont", "doesnt", "didnt", "arent", "isnt", "wasnt", "werent", "havent", "hasnt",
                    "hadnt", "wont", "shouldnt", "wouldnt", "cant", "couldnt", "aint"};

            String path = "/home/elnaz/Desktop/Courses/Team Lab/nohashtag/test";
            String all_path = "/home/elnaz/Desktop/Courses/Team Lab/nohashtag/test_all";
            String pos_path = "/home/elnaz/Desktop/Courses/Team Lab/nohashtag/test_pos";
            String neg_path = "/home/elnaz/Desktop/Courses/Team Lab/nohashtag/test_neg";
            File[] files = new File(path).listFiles();
            List<Tweet> pos = new LinkedList<Tweet>();
            List<Tweet> neg = new LinkedList<Tweet>();
            List<Tweet> all = new LinkedList<Tweet>();
            for (File file : files) {
                List<String> content = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
                for (int i = 0; i < content.size(); i++) {
                    String[] line = content.get(i).split("\t");
                    if (line.length > 8) {
                        boolean seen = false;
                        Tweet tweet = new Tweet(line[0], line[2],  new Long(line[3]), line[5], line[6], line[7], line[8]);
                        all.add(tweet);
                        for (String negterm : negationalTerms) {
                            if (tweet.getText().toLowerCase().contains(negterm)) {
                                neg.add(tweet);
                                seen = true;
                            }
                        }
                        if (!seen) pos.add(tweet);
                    }
                }
            }
            PrintWriter out_pos = new PrintWriter(pos_path);
            for (Tweet t:pos) {
                out_pos.write(t.toString());
            }
            out_pos.close();
            PrintWriter out_neg = new PrintWriter(neg_path);
            for (Tweet t:neg) {
                out_neg.write(t.toString());
            }
            out_neg.close();
            PrintWriter out_all = new PrintWriter(all_path);
            for (Tweet t:all) {
                out_all.write(t.toString());
            }
            out_all.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
