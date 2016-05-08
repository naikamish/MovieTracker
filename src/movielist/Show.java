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
public class Show {
    private final SimpleStringProperty viewDate;
    private final SimpleStringProperty showTitle;
    private final SimpleStringProperty seasonNumber;
    private final SimpleStringProperty episodeNumber;
    private final SimpleStringProperty showImdbID;
    private final SimpleStringProperty episodeTitle;
    private final SimpleStringProperty episodeImdbID;
    private final SimpleStringProperty runtime;
    private final SimpleStringProperty releaseDate;
    
    public Show(String viewDate, String showTitle, String seasonNumber, String episodeNumber, String showImdbID, String episodeTitle, String episodeImdbID, String runtime, String releaseDate){
        this.viewDate = new SimpleStringProperty(viewDate);
        this.showTitle = new SimpleStringProperty(showTitle);
        this.seasonNumber = new SimpleStringProperty(seasonNumber);
        this.episodeNumber = new SimpleStringProperty(episodeNumber);
        this.showImdbID = new SimpleStringProperty(showImdbID);
        this.episodeTitle = new SimpleStringProperty(episodeTitle);
        this.episodeImdbID = new SimpleStringProperty(episodeImdbID);
        this.runtime = new SimpleStringProperty(runtime);
        this.releaseDate = new SimpleStringProperty(releaseDate);
    }
    
    public Show(String viewDate, String showTitle, String seasonNumber, String episodeNumber, String episodeTitle, String releaseDate, String runtime){
        this.viewDate = new SimpleStringProperty(viewDate);
        this.showTitle = new SimpleStringProperty(showTitle);
        this.seasonNumber = new SimpleStringProperty(seasonNumber);
        this.episodeNumber = new SimpleStringProperty(episodeNumber);
        this.showImdbID = new SimpleStringProperty("");
        this.episodeTitle = new SimpleStringProperty(episodeTitle);
        this.episodeImdbID = new SimpleStringProperty("");
        this.runtime = new SimpleStringProperty(runtime);
        this.releaseDate = new SimpleStringProperty(releaseDate);
    }
    
    public Show(String showTitle, String showImdbID){
        this.viewDate=new SimpleStringProperty("");
        this.showTitle = new SimpleStringProperty(showTitle);
        this.seasonNumber = new SimpleStringProperty("");
        this.episodeNumber = new SimpleStringProperty("");
        this.showImdbID = new SimpleStringProperty(showImdbID);
        this.episodeTitle = new SimpleStringProperty("");
        this.episodeImdbID = new SimpleStringProperty("");
        this.runtime = new SimpleStringProperty("");
        this.releaseDate = new SimpleStringProperty("");
    }
    
    public void setViewDate(String viewDate){
        this.viewDate.set(viewDate);
    }
    
    public String getViewDate(){
        return viewDate.get();
    }
    
    public void setShowTitle(String showTitle){
        this.showTitle.set(showTitle);
    }
    
    public String getShowTitle(){
        return showTitle.get();
    }
    
    public void setSeasonNumber(String seasonNumber){
        this.seasonNumber.set(seasonNumber);
    }
    
    public String getSeasonNumber(){
        return seasonNumber.get();
    }
    
    public void setEpisodeNumber(String episodeNumber){
        this.episodeNumber.set(episodeNumber);
    }
    
    public String getEpisodeNumber(){
        return episodeNumber.get();
    }
    
    public void setShowImdbID(String showImdbID){
        this.showImdbID.set(showImdbID);
    }
    
    public String getShowImdbID(){
        return showImdbID.get();
    }
    
    public void setEpisodeTitle(String episodeTitle){
        this.episodeTitle.set(episodeTitle);
    }
    
    public String getEpisodeTitle(){
        return episodeTitle.get();
    }
    
    public void setEpisodeImdbID(String episodeImdbID){
        this.episodeImdbID.set(episodeImdbID);
    }
    
    public String getEpisodeImdbID(){
        return episodeImdbID.get();
    }
    
    public void setRuntime(String runtime){
        this.runtime.set(runtime);
    }
    
    public String getRuntime(){
        return runtime.get();
    }
    
    public void setReleaseDate(String releaseDate){
        this.releaseDate.set(releaseDate);
    }
    
    public String getReleaseDate(){
        return releaseDate.get();
    }
}
