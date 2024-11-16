package org.example.final_btl_datve.service;

import org.example.final_btl_datve.dto.MovieDto;

import java.util.List;

public interface MovieService {
    //CRUD
    MovieDto create(MovieDto movieDto) throws Exception;
    List<MovieDto> reads();
    MovieDto read(Long movieID) throws Exception;
    MovieDto update(Long movieID, MovieDto movieDto) throws Exception;
    void delete(Long movieID) throws Exception;

    //Other
    List<MovieDto> search(String keyword);
}
