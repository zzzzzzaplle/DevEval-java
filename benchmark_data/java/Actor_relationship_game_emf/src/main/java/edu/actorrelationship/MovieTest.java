package edu.actorrelationship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void testMovieIdAndTitle() {
        Movie movie = ActorrelationshipFactory.eINSTANCE.createMovie();
        movie.setId("301");
        movie.setTitle("Example Movie");
        assertEquals("301", movie.getId());
        assertEquals("Example Movie", movie.getTitle());
    }

    @Test
    void testActorIds() {
        Movie movie = ActorrelationshipFactory.eINSTANCE.createMovie();
        movie.setId("302");
        movie.setTitle("Another Movie");

        movie.getActorIds().add("101");
        movie.getActorIds().add("102");

        assertTrue(movie.getActorIds().contains("101"));
        assertTrue(movie.getActorIds().contains("102"));
    }
}
