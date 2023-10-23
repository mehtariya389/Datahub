import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVImportUtility {

    public static List<Post> readCSV(String pathToFile) throws IOException {
        List<Post> posts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line = reader.readLine(); // Skipping the header
            while ((line = reader.readLine()) != null) {
                List<String> data = parseCSVLine(line);
                if (data.size() != 6) {
                    // Invalid row in CSV, skipping this row
                    continue;
                }

                int id = Integer.parseInt(data.get(0).trim());   // Parse id as int
                String content = data.get(1).trim();
                String author = data.get(2).trim();
                int likes = Integer.parseInt(data.get(3).trim());
                int shares = Integer.parseInt(data.get(4).trim());
                String dateStr = data.get(5).trim();

                posts.add(new Post(id, content, author, likes, shares, dateStr));  // Pass dateStr to the Post constructor
            }
        }

        return posts;
    }
    
    // This method allows the content field to include commas.
    private static List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        
        String[] splitted = line.split(",");
        
        String id = splitted[0].trim();
        fields.add(id);
        
        String content = String.join(",", Arrays.copyOfRange(splitted, 1, splitted.length - 4));
        fields.add(content);
        
        fields.add(splitted[splitted.length - 4].trim()); // author
        fields.add(splitted[splitted.length - 3].trim()); // likes
        fields.add(splitted[splitted.length - 2].trim()); // shares
        fields.add(splitted[splitted.length - 1].trim()); // date-time

        return fields;
    }
}
