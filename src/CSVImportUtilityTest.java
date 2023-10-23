import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.File;
import java.util.List;

public class CSVImportUtilityTest {

    // Temporary CSV file for testing
    private File tempCSV;

    @Before
    public void setUp() throws IOException {
        tempCSV = File.createTempFile("temp", ".csv");
    }

    @After
    public void tearDown() {
        tempCSV.delete();
    }

    private void writeToFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(tempCSV)) {
            writer.write(content);
        }
    }

    @Test
    public void testReadCSV() throws IOException {
        String csvContent = "Id,Content,Author,Likes,Shares,Date\n" +
                            "1,Hello World,John,10,5,1/2/21\n" +
                            "2,Another Post,Jane,20,10,10/11/22\n";

        writeToFile(csvContent);
        
        List<Post> posts = CSVImportUtility.readCSV(tempCSV.getAbsolutePath());

        assertEquals(2, posts.size());
        assertEquals(1, posts.get(0).getId());
        assertEquals("Hello World", posts.get(0).getContent());
        assertEquals("John", posts.get(0).getAuthor());
        assertEquals(10, posts.get(0).getLikes());
        assertEquals(5, posts.get(0).getShares());
        assertEquals(LocalDateTime.parse("01/01/2023 12:00"), posts.get(0).getDateTime());

        assertEquals(2, posts.get(1).getId());
        assertEquals("Another Post", posts.get(1).getContent());
        assertEquals("Jane", posts.get(1).getAuthor());
        assertEquals(20, posts.get(1).getLikes());
        assertEquals(10, posts.get(1).getShares());
        assertEquals(LocalDateTime.parse("02/01/2023 15:00"), posts.get(1).getDateTime());
    }

    @Test(expected = IOException.class)
    public void testReadCSVWithInvalidPath() throws IOException {
        CSVImportUtility.readCSV("invalid/path/to/file.csv");
    }

    @Test
    public void testReadCSVWithIncompleteData() throws IOException {
        String csvContent = "Id,Content,Author,Likes,Shares,Date\n" +
                            "1,Hello World,John,10,5,1/2/21\n" +
                            "2,Another Post,Jane,20,10\n";

        writeToFile(csvContent);
        
        List<Post> posts = CSVImportUtility.readCSV(tempCSV.getAbsolutePath());

        assertEquals(1, posts.size());
        assertEquals(1, posts.get(0).getId());
    }
    
}


