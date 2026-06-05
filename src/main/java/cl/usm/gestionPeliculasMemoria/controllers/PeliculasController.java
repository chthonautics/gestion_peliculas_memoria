package cl.usm.gestionPeliculasMemoria.controllers;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.services.PeliculasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PeliculasController {

    private PeliculasService peliculasService;

    @Autowired
    public PeliculasController(PeliculasService peliculasService) {this.peliculasService = peliculasService;} // allow tests

    @GetMapping("/peliculas")
    public ResponseEntity<List<Pelicula>> getAll(@RequestParam(required = false) String q) {
        try {
            if (q != null && !q.isEmpty()) {
                List<Pelicula> filtradas = this.peliculasService.filter(q);
                return ResponseEntity.ok(filtradas);
            }
            List<Pelicula> peliculas = this.peliculasService.getAll();
            return ResponseEntity.ok(peliculas);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/peliculas")
    public ResponseEntity<?> createPelicula(@RequestBody @Valid Pelicula pelicula) {
        Pelicula res = this.peliculasService.createPelicula(pelicula);
        if (res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> findById(@PathVariable String id) {
        try {
            Pelicula pelicula = this.peliculasService.findById(id);

            if (pelicula == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pelicula);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/peliculas/{id}/comentarios")
    public ResponseEntity<?> getComentarios(@PathVariable String id) {
        try {
            Pelicula pelicula = this.peliculasService.findById(id);

            if (pelicula == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pelicula.getComentarios());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
