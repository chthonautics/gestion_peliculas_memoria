package cl.usm.gestionPeliculasMemoria.controllers;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.services.PeliculasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeliculasControllerTest {

    PeliculasController controller;

    @Mock
    PeliculasService peliculasService;

    @BeforeEach
    void setUp() {
        this.controller = new PeliculasController(peliculasService);
    }

    private Pelicula pelicula(String id, String titulo) {
        return new Pelicula(id, titulo, "Director " + id, null, null);
    }

    @Test
    void getAll() {
        List<Pelicula> peliculas = List.of(pelicula("1", "A"), pelicula("2", "B"));
        when(peliculasService.getAll()).thenReturn(peliculas);

        ResponseEntity<List<Pelicula>> response = controller.getAll(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(peliculas, response.getBody());
        verify(peliculasService).getAll();
        verify(peliculasService, never()).filter(anyString());
    }

    @Test
    void getAll_ignoresEmptyQuery() {
        List<Pelicula> peliculas = List.of(pelicula("1", "A"));
        when(peliculasService.getAll()).thenReturn(peliculas);

        ResponseEntity<List<Pelicula>> response = controller.getAll("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(peliculas, response.getBody());
        verify(peliculasService).getAll();
        verify(peliculasService, never()).filter(anyString());
    }

    @Test
    void getAll_usesFilterWhenQueryPresent() {
        List<Pelicula> filtradas = List.of(pelicula("2", "Matrix"));
        when(peliculasService.filter("matrix")).thenReturn(filtradas);

        ResponseEntity<List<Pelicula>> response = controller.getAll("matrix");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(filtradas, response.getBody());
        verify(peliculasService).filter("matrix");
        verify(peliculasService, never()).getAll();
    }

    @Test
    void getAll_returnsInternalServerErrorWhenServiceThrows() {
        when(peliculasService.getAll()).thenThrow(new RuntimeException("boom"));

        ResponseEntity<List<Pelicula>> response = controller.getAll(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createPelicula() {
        Pelicula p = pelicula("1", "Inception");
        when(peliculasService.createPelicula(p)).thenReturn(p);

        ResponseEntity<?> response = controller.createPelicula(p);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(p, response.getBody());
    }

    @Test
    void createPelicula_returnsInternalServerErrorWhenServiceReturnsNull() {
        Pelicula p = pelicula("1", "Inception");
        when(peliculasService.createPelicula(p)).thenReturn(null);

        ResponseEntity<?> response = controller.createPelicula(p);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void findById() {
        Pelicula p = pelicula("42", "The Matrix");
        when(peliculasService.findById("42")).thenReturn(p);

        ResponseEntity<Pelicula> response = controller.findById("42");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(p, response.getBody());
    }

    @Test
    void findById_returnsNotFoundWhenMissing() {
        when(peliculasService.findById("missing")).thenReturn(null);

        ResponseEntity<Pelicula> response = controller.findById("missing");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void findById_returnsInternalServerErrorWhenServiceThrows() {
        when(peliculasService.findById("42")).thenThrow(new RuntimeException("boom"));

        ResponseEntity<Pelicula> response = controller.findById("42");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getComentarios() {
        Pelicula p = pelicula("42", "The Matrix");
        Comentario[] comentarios = {
                new Comentario("usuario1", "Excelente"),
                new Comentario("usuario2", "Muy buena")
        };
        p.setComentarios(comentarios);
        when(peliculasService.findById("42")).thenReturn(p);

        ResponseEntity<?> response = controller.getComentarios("42");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(comentarios, response.getBody());
    }

    @Test
    void getComentarios_returnsNotFoundWhenMissing() {
        when(peliculasService.findById("missing")).thenReturn(null);

        ResponseEntity<?> response = controller.getComentarios("missing");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getComentarios_returnsInternalServerErrorWhenServiceThrows() {
        when(peliculasService.findById("42")).thenThrow(new RuntimeException("boom"));

        ResponseEntity<?> response = controller.getComentarios("42");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
