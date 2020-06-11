/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DAO.BreweriesService;
import DAO.CategoriesService;
import java.util.List;
import model.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
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
@RequestMapping("/categories")
public class CategoriesRestController {

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    BreweriesService breweriesService;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Categories> getCategories() {
        List<Categories> cats = categoriesService.getAllCategories();

        for (Categories c : cats) {
            c.add(linkTo(StylesRestController.class).slash(c.getResourceId()).withRel("styles"));
        }

        Resources<Categories> result = new Resources<Categories>(cats);
        return result;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") int id) {
        Categories c = categoriesService.getCategoryById(id);
        return categoriesService.deleteCategory(c);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody Categories c) {
        c.setResourceId(0);
        return categoriesService.addCategory(c);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean update(@PathVariable("id") int id, @RequestBody Categories c) {
        c.setResourceId(id);
        return categoriesService.editCategory(c);
    }
}
