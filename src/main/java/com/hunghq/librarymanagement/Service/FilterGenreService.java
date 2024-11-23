package com.hunghq.librarymanagement.Service;

import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterGenreService {

    private List<Document> documentList = new ArrayList<>();
    private Set<String> genres = new HashSet<>();
    private DocumentDAO documentDAO = new DocumentDAO();

    public Set<String> getGenres() {
        genres.clear();
        documentList = documentDAO.getAll();
        for (Document document : documentList) {
            String genreStr = document.getGenre();
            genreStr = genreStr.replace("[", "").replace("]", "").replace("'", "").trim();
            String[] genreArray = genreStr.split(",\\s*");
            for (String genre : genreArray) {
                genres.add(genre.trim());
            }
        }
        return genres;
    }

    public String formatGenres(String genres) {
        genres = genres.replace("[", "").replace("]", "").replace("'", "").trim();
        return genres.replace(",", ", ");
    }

    public static void main(String[] args) {
        FilterGenreService filterGenreService = new FilterGenreService();
        System.out.println(filterGenreService.getGenres());
    }
}
