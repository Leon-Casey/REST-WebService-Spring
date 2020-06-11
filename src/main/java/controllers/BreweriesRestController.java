/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DAO.BreweriesService;
import java.util.Date;
import java.util.List;
import model.Breweries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Leon
 */
@RestController
@RequestMapping("/breweries")
public class BreweriesRestController {

    @Autowired
    BreweriesService service;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Breweries> getBreweries(@RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "offset", required = false) Integer offset) {
        
        List<Breweries> allBreweries = service.getAllBreweries();

        if (size == null && offset == null) {
            size = 50;
            offset = 0;
        }

        List<Breweries> paginatedList = allBreweries.subList(offset, offset + size);

        for (Breweries b : paginatedList) {
            int breweryId = b.getResourceId();

            b.add(linkTo(methodOn(this.getClass()).getBrewery(breweryId)).withSelfRel());

            b.add(linkTo(GeocodeRestController.class).slash(breweryId).withRel("Latitude & Longitude on Map"));

            b.add(linkTo(QRCodeRestController.class).slash(breweryId).withRel("QR Code"));
            
        }

        Resources<Breweries> result = new Resources<Breweries>(paginatedList);
        return result;
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public Resource<Breweries> getBrewery(@PathVariable("id") int id) {
        Resource<Breweries> resource = new Resource<Breweries>(service.getBreweryById(id));

        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getBreweries(99999, 0));

        resource.add(linkTo.withRel("all-breweries"));

        resource.add(linkTo(GeocodeRestController.class).slash(id).withRel("Latitude & Longitude on Map"));

        resource.add(linkTo(QRCodeRestController.class).slash(id).withRel("QR code"));

        return resource;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") int id) {
        Breweries brewery = service.getBreweryById(id);
        return service.deleteBrewery(brewery);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody Breweries b) {
        b.setResourceId(0);
        b.setAddUser(0);
        b.setLastMod(new Date());
        return service.addBrewery(b);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean update(@PathVariable("id") int id, @RequestBody Breweries b) {
        b.setResourceId(id);
        b.setAddUser(0);
        b.setLastMod(new Date());
        return service.editBrewery(b);
    }
}
