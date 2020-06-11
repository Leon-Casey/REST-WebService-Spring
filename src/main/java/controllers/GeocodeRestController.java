/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DAO.BreweriesGeocodeService;
import DAO.BreweriesService;
import java.util.List;
import model.Breweries;
import model.BreweriesGeocode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

import static org.apache.lucene.util.SloppyMath.haversinMeters;

/**
 *
 * @author Leon
 */
@RestController
@RequestMapping("/map")
public class GeocodeRestController {

    @Autowired
    BreweriesGeocodeService geocodeService;

    @Autowired
    BreweriesService breweriesService;

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getMap(@PathVariable("id") int id) {
        BreweriesGeocode geocode = geocodeService.getGeocodeByBreweryId(id);

        Breweries brewery = breweriesService.getBreweryById(id);

        String output = "<html><body><h1>" + brewery.getName() + "</h1><h2><br>Latitude: " + geocode.getLatitude() + "<br>Longitude: " + geocode.getLongitude() + "</h2><div style=\"width: 100%\"><iframe width=\"100%\" height=\"600\" src=\"https://maps.google.com/maps?q=" + geocode.getLatitude() + "," + geocode.getLongitude() + "&hl=ie;z=3&amp;output=embed\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe></div></body></html>";

        return output;
    }

    @GetMapping(value = "/nearest/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String findNearest(@PathVariable("id") int id) {
        BreweriesGeocode breweryGeocode = geocodeService.getGeocodeByBreweryId(id);
        Breweries brewery = breweriesService.getBreweryById(id);

        BreweriesGeocode matchedGeocode = new BreweriesGeocode();
        Breweries matchedBrewery = new Breweries();

        List<BreweriesGeocode> list = geocodeService.getAllGeocodes();

        double dist = haversinMeters(breweryGeocode.getLatitude(), breweryGeocode.getLongitude(), list.get(0).getLatitude(), list.get(0).getLongitude());
        double temp;

        for (BreweriesGeocode bg : list) {
            temp = haversinMeters(breweryGeocode.getLatitude(), breweryGeocode.getLongitude(), bg.getLatitude(), bg.getLongitude());
            if (temp < dist && bg.getBreweryId() != breweryGeocode.getBreweryId()) {
                dist = temp;
                matchedGeocode = bg;
            }
        }

        matchedBrewery = breweriesService.getBreweryById(matchedGeocode.getBreweryId());

        String output = "<html><body>"
                + "<div><h1>You Provided: " + brewery.getName() + "</h1><div style=\"width: 100%\"><iframe width=\"100%\" height=\"600\" src=\"https://maps.google.com/maps?q=" + breweryGeocode.getLatitude() + "," + breweryGeocode.getLongitude() + "&hl=ie;z=3&amp;output=embed\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe></div>"
                + "<div><h1>Nearest Brewery: " + matchedBrewery.getName() + "</h1><div style=\"width: 100%\"><iframe width=\"100%\" height=\"600\" src=\"https://maps.google.com/maps?q=" + matchedGeocode.getLatitude() + "," + matchedGeocode.getLongitude() + "&hl=ie;z=3&amp;output=embed\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe></div>"
                + "</div></body></html>";
        return output;
    }

    @GetMapping(value = "/nearest", produces = MediaType.TEXT_HTML_VALUE)
    public String findNearestAndReturnMap(@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude) {
        BreweriesGeocode matchedGeocode = new BreweriesGeocode();
        Breweries matchedBrewery = new Breweries();

        List<BreweriesGeocode> list = geocodeService.getAllGeocodes();

        double dist = haversinMeters(latitude, longitude, list.get(0).getLatitude(), list.get(0).getLongitude());
        double temp;

        for (BreweriesGeocode bg : list) {
            temp = haversinMeters(latitude, longitude, bg.getLatitude(), bg.getLongitude());
            if (temp < dist) {
                dist = temp;
                matchedGeocode = bg;
            }
        }

        matchedBrewery = breweriesService.getBreweryById(matchedGeocode.getBreweryId());

        String output = "<html><body>"
                + "<div><h1>You Provided: <br>Latitude: " + latitude + "<br>Longitude: " + longitude + "</h1><div style=\"width: 100%\"><iframe width=\"100%\" height=\"600\" src=\"https://maps.google.com/maps?q=" + latitude + "," + longitude + "&hl=ie;z=3&amp;output=embed\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe></div>"
                + "<div><h1>Nearest Brewery: " + matchedBrewery.getName() + "</h1><div style=\"width: 100%\"><iframe width=\"100%\" height=\"600\" src=\"https://maps.google.com/maps?q=" + matchedGeocode.getLatitude() + "," + matchedGeocode.getLongitude() + "&hl=ie;z=3&amp;output=embed\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe></div>"
                + "</div></body></html>";
        return output;
    }

    @GetMapping(value = "/nearest/json", produces = MediaTypes.HAL_JSON_VALUE)
    public Resource<Breweries> findNearestAndReturnJSON(@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude) {
        BreweriesGeocode matchedGeocode = new BreweriesGeocode();
        Breweries matchedBrewery = new Breweries();

        List<BreweriesGeocode> list = geocodeService.getAllGeocodes();

        double dist = haversinMeters(latitude, longitude, list.get(0).getLatitude(), list.get(0).getLongitude());
        double temp;

        for (BreweriesGeocode bg : list) {
            temp = haversinMeters(latitude, longitude, bg.getLatitude(), bg.getLongitude());
            if (temp < dist) {
                dist = temp;
                matchedGeocode = bg;
            }
        }

        matchedBrewery = breweriesService.getBreweryById(matchedGeocode.getBreweryId());

        Resource<Breweries> resource = new Resource<Breweries>(matchedBrewery);
        return resource;
    }
}
