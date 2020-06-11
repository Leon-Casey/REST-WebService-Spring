/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DAO.BeersService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import model.Beers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Leon
 */
@RestController
@RequestMapping("/beers")
public class BeersRestController {

    @Autowired
    BeersService service;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Beers> getBeers(@RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "offset", required = false) Integer offset) {
        List<Beers> allBeers = service.getAllBeers();
        if (size == null && offset == null) {
            size = 50;
            offset = 0;
        }

        List<Beers> paginatedList = allBeers.subList(offset, offset + size);

        for (Beers b : paginatedList) {
            b.add(linkTo(BreweriesRestController.class).slash(b.getBreweryId()).withRel("brewery"));
        }

        Resources<Beers> result = new Resources<Beers>(paginatedList);
        return result;
    }

    @GetMapping(value = "/BeersByBrewery/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Beers> getBeersByBrewery(@PathVariable("id") int id) {
        return new Resources<Beers>(service.getBeersByBreweryId(id));
    }

    @GetMapping(value = "/RankBySellPrice", produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Beers> rankBySellPrice() {
        List<Beers> allBeers = service.getAllBeers();

        Collections.sort(allBeers, new Comparator<Beers>() {
            @Override
            public int compare(Beers b1, Beers b2) {
                return Double.compare(b1.getSellPrice(), b2.getSellPrice());
            }
        });

        Collections.reverse(allBeers);

        for (Beers b : allBeers) {
            b.add(linkTo(BreweriesRestController.class).slash(b.getBreweryId()).withRel("brewery"));
        }

        return new Resources<Beers>(allBeers.subList(0, 10));
    }

    @GetMapping(value = "/MostProfitable", produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Beers> mostProfitable() {
        List<Beers> allBeers = service.getAllBeers();

        Collections.sort(allBeers, new Comparator<Beers>() {
            @Override
            public int compare(Beers b1, Beers b2) {
                return Double.compare(b1.getSellPrice() - b1.getBuyPrice(), b2.getSellPrice() - b2.getBuyPrice());
            }
        });

        Collections.reverse(allBeers);

        for (Beers b : allBeers) {
            b.add(linkTo(BreweriesRestController.class).slash(b.getBreweryId()).withRel("brewery"));
        }

        return new Resources<Beers>(allBeers.subList(0, 5));
    }

    @GetMapping(value = "/UnderSellPrice", produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<Beers> underSellPrice(@RequestParam(name = "price") double price) {
        List<Beers> allBeers = service.getAllBeers();

        List<Beers> beersUnderPrice = new ArrayList<>();

        for (Beers b : allBeers) {
            if (b.getSellPrice() < price) {
                beersUnderPrice.add(b);
            }
            b.add(linkTo(BreweriesRestController.class).slash(b.getBreweryId()).withRel("brewery"));
        }
        return new Resources<Beers>(beersUnderPrice);
    }

    @PutMapping(value = "/UpdatePrice/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean updatePrice(@PathVariable("id") int id, @RequestParam(name = "price") double price) {
        Beers beer = service.getBeerById(id);
        beer.setResourceId(id);
        beer.setAddUser(0);
        beer.setSellPrice(price);
        beer.setLastMod(new Date());
        return service.editBeer(beer);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public Resource<Beers> getBeer(@PathVariable("id") int id) {
        Resource<Beers> resource = new Resource<Beers>(service.getBeerById(id));

        resource.add(linkTo(BreweriesRestController.class).slash(resource.getContent().getBreweryId()).withRel("brewery"));

        return resource;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") int id) {
        Beers beer = service.getBeerById(id);
        return service.deleteBeer(beer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody Beers b) {
        b.setResourceId(0);
        b.setAddUser(0);
        b.setLastMod(new Date());
        return service.addBeer(b);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean update(@PathVariable("id") int id, @RequestBody Beers b) {
        b.setResourceId(id);
        b.setAddUser(0);
        b.setLastMod(new Date());
        return service.editBeer(b);
    }
}
