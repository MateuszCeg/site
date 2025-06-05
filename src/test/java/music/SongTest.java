package music;

import database.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    @BeforeAll
    public static void connectingToDatabase(){
        DatabaseConnection.connect("src/main/resources/songs.db", "songs");
    }
    @Test
    void wrongSongFromDataBaseTest() {
        try {
            Optional<Song> song = Song.Persistence.read(50);
            assertFalse(song.isPresent());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Stream<Arguments> idsForTest(){
        return Stream.of(Arguments.of(5,new Song("Queen","Bohemian Rhapsody",355)),
                Arguments.of(10, new Song("The Who","My Generation",198)),
                Arguments.of(15, new Song("Aretha Franklin","Respect",147)));
    }
    @ParameterizedTest
    @MethodSource("idsForTest")
    void streamIDSongsTest(int id, Song songComp){
        try {
            Optional<Song> song = Song.Persistence.read(id);
            assertTrue(song.isPresent());
            assertEquals(songComp,song.get());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @ParameterizedTest
    @CsvFileSource(files = "src/main/resources/songs.csv", numLinesToSkip = 1)
    void fileSongsTest(int id, String author, String title, int length){
        try {
            Optional<Song> song = Song.Persistence.read(id);
            assertTrue(song.isPresent());
            assertEquals(new Song(author,title,length),song.get());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @AfterAll
    public static void disconnectingFromDatabase(){
        DatabaseConnection.disconnect("songs");
    }
    @Test
    void songFromDataBaseTest() {
        try {
            Optional<Song> song = Song.Persistence.read(3);
            assertTrue(song.isPresent());
            assertEquals(new Song("Led Zeppelin","Stairway to Heaven",482),song.get());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
