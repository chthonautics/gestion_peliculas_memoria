package cl.usm.gestionPeliculasMemoria.services;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.repositories.PeliculasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeliculasServiceImplTest {

    PeliculasServiceImpl service;

    @Mock
    PeliculasRepository peliculasRepository;

    @BeforeEach
    void setUp() {
        this.service = new PeliculasServiceImpl(peliculasRepository);
    }

    private Pelicula pelicula(String id, String titulo) {
        return new Pelicula(id, titulo, "Director " + id, null, null);
    }

    @Test
    void createPelicula() {
        Pelicula p = pelicula("1", "Inception");
        when(peliculasRepository.insert(p)).thenReturn(p);

        Pelicula result = service.createPelicula(p);

        assertSame(p, result);
    }

    @Test
    void createPelicula_assignsTokenBeforeInserting() {
        Pelicula p = pelicula("1", "Inception");
        ArgumentCaptor<Pelicula> captor = ArgumentCaptor.forClass(Pelicula.class);
        when(peliculasRepository.insert(captor.capture())).thenReturn(p);

        service.createPelicula(p);

        Pelicula inserted = captor.getValue();
        assertNotNull(inserted.getTokenDescarga());
        assertEquals(10, inserted.getTokenDescarga().length());
        assertTrue(inserted.getTokenDescarga().matches("[A-Za-z0-9]{10}"));
    }

    @Test
    void createPelicula_returnsNullWhenRepositoryThrows() {
        Pelicula p = pelicula("1", "Inception");
        when(peliculasRepository.insert(any(Pelicula.class)))
                .thenThrow(new IllegalArgumentException("duplicado"));

        assertNull(service.createPelicula(p));
    }

    @Test
    void getAll() {
        List<Pelicula> peliculas = List.of(pelicula("1", "A"), pelicula("2", "B"));
        when(peliculasRepository.findAll()).thenReturn(peliculas);

        List<Pelicula> result = service.getAll();

        assertSame(peliculas, result);
        verify(peliculasRepository).findAll();
    }

    @Test
    void findById() {
        Pelicula p = pelicula("42", "The Matrix");
        when(peliculasRepository.findById("42")).thenReturn(p);

        assertSame(p, service.findById("42"));
    }

    @Test
    void findById_returnsNullWhenNotFound() {
        when(peliculasRepository.findById("missing")).thenReturn(null);

        assertNull(service.findById("missing"));
    }

    @Test
    void filter() {
        Pelicula inception = pelicula("1", "Inception");
        Pelicula matrix = pelicula("2", "The Matrix");
        Pelicula interstellar = pelicula("3", "Interstellar");
        when(peliculasRepository.findAll())
                .thenReturn(List.of(inception, matrix, interstellar));

        List<Pelicula> result = service.filter("inter");

        assertEquals(1, result.size());
        assertSame(interstellar, result.get(0));
    }

    @Test
    void filter_matchesByTitleCaseInsensitive() {
        Pelicula inception = pelicula("1", "Inception");
        Pelicula matrix = pelicula("2", "The Matrix");
        when(peliculasRepository.findAll()).thenReturn(List.of(inception, matrix));

        List<Pelicula> result = service.filter("MATRIX");

        assertEquals(1, result.size());
        assertSame(matrix, result.get(0));
    }

    @Test
    void filter_matchesById() {
        Pelicula inception = pelicula("abc123", "Inception");
        Pelicula matrix = pelicula("xyz789", "The Matrix");
        when(peliculasRepository.findAll()).thenReturn(List.of(inception, matrix));

        List<Pelicula> result = service.filter("XYZ");

        assertEquals(1, result.size());
        assertSame(matrix, result.get(0));
    }

    @Test
    void filter_returnsEmptyWhenNoMatch() {
        when(peliculasRepository.findAll())
                .thenReturn(List.of(pelicula("1", "Inception")));

        assertTrue(service.filter("nope").isEmpty());
    }
}
