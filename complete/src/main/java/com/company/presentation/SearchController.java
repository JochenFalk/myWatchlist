package com.company.presentation;

import com.company.business.SearchBusiness;
import com.company.data.model.Search;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class SearchController {

//    @GetMapping("/newSearch")
//    public Search newSearch(HttpSession session,
//                            @RequestParam(value = "searchTitle") String searchTitle,
//                            @RequestParam(value = "searchYear") String searchYear) {
//
//        return SearchBusiness.newSearch(searchTitle, searchYear, session);
//    }

    @GetMapping("/nextSearch")
    public Search newSearch(HttpSession session,
            @RequestParam(value = "searchTitle") String searchTitle,
            @RequestParam(value = "searchYear") String searchYear,
            @RequestParam(value = "returnValue") int returnValue) {

        return SearchBusiness.newSearch(searchTitle, searchYear, returnValue, session);
    }

    @GetMapping("/retrieveSearch")
    public Search retrieveSearch(
            @RequestParam(value = "searchTitle") String searchTitle,
            @RequestParam(value = "searchYear") String searchYear) {

        return SearchBusiness.retrieveSearch(searchTitle, searchYear);
    }

    @GetMapping("/saveSearch")
    public void saveSearch(HttpSession session,
                             @RequestParam(value = "searchObject") String searchObject) throws IOException {

        SearchBusiness.saveSearch(searchObject, session);
    }
}
