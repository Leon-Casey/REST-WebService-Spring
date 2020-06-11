/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DAO.BreweriesService;
import DAO.CategoriesService;
import DAO.StylesService;
import java.util.ArrayList;
import java.util.List;
import model.Categories;
import model.Styles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Leon
 */
@RestController
@RequestMapping("/styles")
public class StylesRestController {

    @Autowired
    StylesService stylesService;

    @Autowired
    BreweriesService breweriesService;

    @Autowired
    CategoriesService catsService;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Styles> getStyles() {
        List<Styles> styles = stylesService.getAllStyles();

        Resources<Styles> result = new Resources<Styles>(styles);
        return result;
    }

    @GetMapping(value = "/{categoryId}", produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Styles> getStylesByCategory(@PathVariable("categoryId") int categoryId) {
        List<Styles> styles = stylesService.getStylesByCategory(categoryId);

        Resources<Styles> result = new Resources<Styles>(styles);
        return result;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") int id) {
        Styles style = stylesService.getStyleById(id);
        return stylesService.deleteStyle(style);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody Styles s) {
        s.setId(0);
        return stylesService.addStyle(s);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean update(@PathVariable("id") int id, @RequestBody Styles s) {

        List<Categories> cats = catsService.getAllCategories();
        List<Integer> catIds = new ArrayList<>();

        for (Categories c : cats) {
            catIds.add(c.getResourceId());
        }

        if (catIds.contains(s.getCatId())) {
            s.setId(id);
            return stylesService.editStyle(s);
        } else {
            return false;
        }
    }
}
