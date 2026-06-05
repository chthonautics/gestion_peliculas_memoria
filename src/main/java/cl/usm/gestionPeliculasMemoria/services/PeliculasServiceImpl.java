package cl.usm.gestionPeliculasMemoria.services;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.repositories.PeliculasRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeliculasServiceImpl implements PeliculasService {

    private final PeliculasRepository peliculasRepository;

    @Autowired
    public PeliculasServiceImpl(PeliculasRepository peliculasRepository) {
        this.peliculasRepository = peliculasRepository;
    }

    @Override
    public Pelicula createPelicula(Pelicula pelicula) {
        try {
            pelicula.setTokenDescarga(RandomStringUtils.secure().nextAlphanumeric(10));
            return this.peliculasRepository.insert(pelicula);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Pelicula> getAll() {
        return this.peliculasRepository.findAll();
    }

    @Override
    public Pelicula findById(String id) {
        return this.peliculasRepository.findById(id);
    }

    @Override
    public List<Pelicula> filter(String query) {
        List<Pelicula> peliculas = this.getAll();
        
        return peliculas.stream()
                .filter(p -> p.getId().toLowerCase().contains(query.toLowerCase()) 
                          || p.getTitulo().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
