package com.company.presentation;

import com.company.business.SearchBusiness;
import com.company.data.model.Search;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @GetMapping("/newSearch")
    public Search newSearch(
            @RequestParam(value = "searchTitle", required = true) String searchTitle,
            @RequestParam(value = "searchYear", required = true) String searchYear) {

        return SearchBusiness.newSearch(searchTitle, searchYear);
    }

    @GetMapping("/nextSearch")
    public Search newSearch(
            @RequestParam(value = "searchTitle", required = true) String searchTitle,
            @RequestParam(value = "searchYear", required = true) String searchYear,
            @RequestParam(value = "returnValue", required = true) int returnValue) {

        return SearchBusiness.newSearch(searchTitle, searchYear, returnValue);
    }

    @GetMapping("/retrieveSearch")
    public Search retrieveSearch(
            @RequestParam(value = "searchTitle", required = true) String searchTitle,
            @RequestParam(value = "searchYear", required = true) String searchYear) {

        return SearchBusiness.retrieveSearch(searchTitle, searchYear);
    }
}
