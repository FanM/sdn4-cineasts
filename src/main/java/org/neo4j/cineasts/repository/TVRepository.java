package org.neo4j.cineasts.repository;

import org.neo4j.cineasts.domain.TV;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Created by fanmao on 12/8/16.
 */
public interface TVRepository extends GraphRepository<TV> {
    TV findById(String id);

    // Page<Movie> findByTitleLike(String title, Pageable page);

    @Query("MATCH (movie:Movie) WHERE movie.title=~{0} RETURN movie")
    Iterable<TV> findByTitleLike(String title);

}
