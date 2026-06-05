package cl.usm.gestionPeliculasMemoria.repositories;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeliculasRepositoryImplTest {

    private PeliculasRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new PeliculasRepositoryImpl();
    }

    private Pelicula pelicula(String id) {
        return new Pelicula(id, "Titulo " + id, "Director " + id, null, null);
    }

    @Test
    void insert_storesAndReturnsPelicula() {
        Pelicula p = pelicula("1");

        assertSame(p, repository.insert(p));
        assertSame(p, repository.findById("1"));
    }

    @Test
    void insert_throwsWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> repository.insert(pelicula(null)));
    }

    @Test
    void insert_throwsOnDuplicateIdCaseInsensitive() {
        repository.insert(pelicula("ABC"));

        assertThrows(IllegalArgumentException.class, () -> repository.insert(pelicula("abc")));
    }

    @Test
    void findAll_returnsAllInserted() {
        repository.insert(pelicula("1"));
        repository.insert(pelicula("2"));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void findAll_returnsDefensiveCopy() {
        repository.insert(pelicula("1"));

        List<Pelicula> result = repository.findAll();
        result.clear();

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findById_isCaseInsensitive() {
        Pelicula p = pelicula("ABC");
        repository.insert(p);

        assertSame(p, repository.findById("abc"));
    }

    @Test
    void findById_returnsNullWhenNullId() {
        assertNull(repository.findById(null));
    }

    @Test
    void findById_returnsNullWhenNotFound() {
        assertNull(repository.findById("missing"));
    }
}
