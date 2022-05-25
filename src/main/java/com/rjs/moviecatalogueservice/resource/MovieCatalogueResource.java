package com.rjs.moviecatalogueservice.resource;

import com.rjs.moviecatalogueservice.model.CatalogueItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("catalogue")
public class MovieCatalogueResource {

    @RequestMapping("/{userId}")
    public List<CatalogueItem> getCatalogueItemsForUserId(@PathVariable String userId){
        return Collections.singletonList(
                new CatalogueItem("Jaws", "A man eating shark terrorises seaside resort.",4 ));
    }
}
