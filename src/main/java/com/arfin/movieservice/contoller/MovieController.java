package com.arfin.movieservice.contoller;

import com.arfin.movieservice.model.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MovieController {
    List<Movie> movieDatabase = new ArrayList<>();

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @GetMapping(path = {"/", "/home"})
    ModelAndView getHome(){
        //Calculating today
        SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("dd-MMM-yyyy, E \nhh:mm:ss aa ");
        dateTimeInGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
        String today = dateTimeInGMT.format(new Date());

        //Creating model
        ModelAndView mv = new ModelAndView();
        mv.addObject("today", today);
        mv.setViewName("index.html");

        return mv;
    }

    @GetMapping(path = {"/movies", "/movie-list"})
    ModelAndView getAllMovie(){
        var mv = new ModelAndView();
        mv.addObject("movieList", movieDatabase);
        mv.setViewName("movie-list.html");
        return mv;
    }

    @GetMapping("/movie-detail")
    ModelAndView getMoiveDetailWithId(@RequestParam(name = "id") Integer id){
        Movie movie = movieDatabase.get(id-1);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("movie-detail.html");
        mv.addObject("movie", movie);
        mv.addObject("id", id); //Manually sending id.
        System.out.println(movie.getName());
        return mv;
    }

//    @ModelAttribute("movie")
//    public Movie movieModel(){
//        return new Movie();
//    }

    @GetMapping("/add-movie")
    public String getAddMovieForm(Movie movie){
        return "add-movie.html";
    }

    @PostMapping("/add-movie")
    public ModelAndView addMovieToDb(Movie movie){
        movieDatabase.add(movie);
        System.out.println("New added movie is " + movie.getName());
        System.out.println("Movie Database Size is " + movieDatabase.size());

//        ModelAndView mv = new ModelAndView("redirect:/add-movie");
//        mv.addObject("message", "movie added");
//        ModelMap modelMap = new ModelMap();
//        modelMap.addAttribute("message", "Movie Added");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("add-movie.html");
        mv.addObject("message", "Movie Added");
        return mv;
    }

    @GetMapping("/delete-movie/{id}")
    ModelAndView getDeleteMovie(@PathVariable(name = "id") String id){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("delete-movie.html");
        mv.addObject("id", id);
        return mv;
    }

    @PostMapping("/confirm-delete")
    public String confirmDeleteMovie(@RequestParam(name = "id") long id) {
        System.out.println("Deleted Movie: " + id);
        movieDatabase.remove((int)id-1); // ei jaygay ekta faltu bug. long id dile o object dhore ney, object dile o index diye remove kore na. so cast kore explicityly oke int bole nite hobe.
        System.out.println("Moive Db Size: " + movieDatabase.size());
        return "delete-confirm.html";
    }

    @GetMapping("/update-movie/{id}")
    public ModelAndView getUpdateMoviePage(@PathVariable(name = "id") int id){
        System.out.println("Update for " + id);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("update-mov.html");
        mv.addObject("id", id);
        mv.addObject("movie", movieDatabase.get(id-1));
        return mv;
    }

    @PostMapping("/confirm-update")
    public String confirmUpdate(Movie movie, @RequestParam(name = "id") int id){
        System.out.println(movie.getName());
        System.out.println(id);

        movieDatabase.set(id-1, movie);
        return "redirect:/movie-detail?id="+id;
    }

    @GetMapping("/demo")
    public ModelAndView demo(){
        var mv = new ModelAndView("z-playground.html");
        mv.addObject("data", 10);
        return mv;
    }

}
