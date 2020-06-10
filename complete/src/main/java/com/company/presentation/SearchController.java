package com.company.presentation;

import com.company.business.SearchBusiness;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @GetMapping("/searchByTitle")
    public String searchByTitle(
            @RequestParam(value = "searchTitle", required = true) String searchTitle,
            @RequestParam(value = "searchYear", required = true) String searchYear) {

        return SearchBusiness.searchByTitle(searchTitle, searchYear);
    }

    @GetMapping("/searchById")
    public String searchById(
            @RequestParam(value = "searchId", required = true) String searchId) {

        return SearchBusiness.searchById(searchId);
    }
}
