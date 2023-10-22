import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVImportUtility {

    public static List<Post> readCSV(String pathToFile) throws IOException {
        List<Post> posts = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    // Skip the header
                    isHeader = false;
                    continue;
                }
                
                String[] values = line.split(",");
                
                String id = values[0];
                String content = values[1];
                String author = values[2];
                int likes = Integer.parseInt(values[3]);
                int shares = Integer.parseInt(values[4]);
                LocalDateTime dateTime = LocalDateTime.parse(values[5], DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                posts.add(new Post(id, content, author, likes, shares, dateTime));
            }
        }

        return posts;
    }
}
