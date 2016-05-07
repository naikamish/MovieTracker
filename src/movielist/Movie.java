/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movielist;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Amish Naik
 */
public class Movie {
    private final SimpleStringProperty viewDate;
    private final SimpleStringProperty movieTitle;
    private final SimpleStringProperty releaseDate;
    private final SimpleStringProperty director;
    private final SimpleStringProperty runtime;
    private final SimpleStringProperty genre;
    private final SimpleStringProperty imdbID;
    
    public Movie(String viewDate, String movieTitle, String releaseDate, String director, String runtime, String genre, String imdbID){
        this.viewDate = new SimpleStringProperty(viewDate);
        this.movieTitle = new SimpleStringProperty(movieTitle);
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.director = new SimpleStringProperty(director);
        this.runtime = new SimpleStringProperty(runtime);
        this.genre = new SimpleStringProperty(genre);
        this.imdbID = new SimpleStringProperty(imdbID);
    }
    
    public Movie(String movieTitle, String releaseDate, String director, String runtime, String genre, String imdbID){
        this.viewDate=new SimpleStringProperty("");
        this.movieTitle = new SimpleStringProperty(movieTitle);
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.director = new SimpleStringProperty(director);
        this.runtime = new SimpleStringProperty(runtime);
        this.genre = new SimpleStringProperty(genre);
        this.imdbID = new SimpleStringProperty(imdbID);
    }
    
    public String getViewDate(){
        return viewDate.get();
    }
    
    public void setViewDate(String viewDate){
        this.viewDate.set(viewDate);
    }
    
    public String getMovieTitle(){
        return movieTitle.get();
    }
    
    public void setMovieTitle(String movieTitle){
        this.movieTitle.set(movieTitle);
    }
    
    public String getReleaseDate(){
        return releaseDate.get();
    }
    
    public void setReleaseDate(String releaseDate){
        this.releaseDate.set(releaseDate);
    }
    
    public String getDirector(){
        return director.get();
    }
    
    public void setDirector(String director){
        this.director.set(director);
    }
    
    public String getRuntime(){
        return runtime.get();
    }
    
    public void setRuntime(String runtime){
        this.runtime.set(runtime);
    }
    
    public String getGenre(){
        return genre.get();
    }
    
    public void setGenre(String genre){
        this.genre.set(genre);
    }
    
    public String getimdbID(){
        return imdbID.get();
    }
    
    public void setimdbID(String imdbID){
        this.imdbID.set(imdbID);
    }
}
