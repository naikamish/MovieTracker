/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movielist;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 *
 * @author Amish Naik
 */
public class FXMLDocumentController implements Initializable {
    private TableView<Movie> table = new TableView<Movie>();
    private TableView<Show> showTable = new TableView<Show>();
    private final ObservableList<Movie> data = FXCollections.observableArrayList();
    private final ObservableList<Show> showData = FXCollections.observableArrayList();
    private LoadDriver sqlConn;
    
    @FXML private AnchorPane movieTableView, showTableView;
    
    @FXML private TextField SearchTextField, ShowSearchTextField, SeasonTextField, EpisodeTextField;
    
    @FXML private Button SearchButton, AddMovieButton, ShowSearchButton;
    
    @FXML private DatePicker ViewDatePicker, ViewShowDatePicker;
    
    @FXML private VBox SearchResultsBox, SingleMovieResult, ShowSearchResultsBox, SingleShowResult; 
    
    private Movie selectedMovie;
    private Show selectedShow;
    
    @FXML
    private void SearchButtonClick(ActionEvent event) {
        SearchResultsBox.getChildren().removeAll();
        System.setProperty("http.agent", "Chrome");
        String title = SearchTextField.getText();
        title = title.replaceAll(" ", "+");
        int count=0;
        try{
            URL siteURL = new URL("http://www.imdb.com/find?ref_=nv_sr_fn&q=" + title + "&s=tt");
            URLConnection yc = siteURL.openConnection();
        //set the User-Agent value (currently using the Firefox user-agent value)
            yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:x.x.x) Gecko/20041107 Firefox/x.x");
            InputStreamReader isr = new InputStreamReader(yc.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String fullString = "";
            for (String line; (line = reader.readLine()) != null;) {
                fullString+=line;
            }
            fullString = fullString.substring(fullString.indexOf("<a name=\"tt\">"));
            fullString = fullString.substring(0,fullString.indexOf("</table>"));
            String[] results = fullString.split("<tr");
            //results[0] = results[0].substring(results[0].indexOf("<tr"));
            for(int i=1; i<=results.length-1; i++){
                count++;
                String result = results[i];
                if(result.indexOf(") (TV")==-1){
                    String imdbID = result.substring(result.indexOf("/title/tt")+9,result.indexOf("/?"));
                    String img = result.substring(result.indexOf("img src=")+9,result.indexOf("<td class=\"result_text\">")-15);
                    String subResult = result.substring(result.lastIndexOf("a href"));
                    String movTitle = subResult.substring(subResult.indexOf(">")+1,subResult.indexOf("<"));
                    String movYear = subResult.substring(subResult.indexOf("</a>")+6,subResult.indexOf(")"));
                    
                    System.out.println(imdbID);
                    System.out.println(movYear);
                    System.out.println(movTitle);
                    System.out.println(img);
                    System.out.println();
                    
                    addSearchResult(imdbID, movTitle, movYear, img);
                }
            }
        }
        catch(Exception e){
            //System.out.println(img);
            e.printStackTrace();
            //System.out.println(count);
        }
    }
    
    @FXML
    private void AddMovieButtonClick(ActionEvent event) {
        selectedMovie.setViewDate(ViewDatePicker.getValue().toString());
        data.add(selectedMovie);
        String insertQuery = "insert into movie(viewDate, imdbID, title, releaseDate, director, runtime, genre) values(?,?,?,?,?,?,?);";
        try{
            LoadDriver sqlConn = new LoadDriver("jdbc:mysql://localhost:3306/movies","root","password");
            sqlConn.prepareInsertMovieQuery(insertQuery, selectedMovie.getViewDate(), selectedMovie.getimdbID(), 
                    selectedMovie.getMovieTitle(), selectedMovie.getReleaseDate(), selectedMovie.getDirector(), 
                    selectedMovie.getRuntime(), selectedMovie.getGenre());
            //Server.showMessage(query);
        }
        catch(Exception e){
           // sqlConn.showMessage("conn line 106"+e.toString());
        }
    }
    
    public void addSearchResult(String imdbID, String title, String year, String image){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                HBox hbox = new HBox();
                //hbox.getStyleClass().add("chatListHBox");
                hbox.setId(imdbID);

                ImageView icon = new ImageView();
                
                try{
                    URL url = new URL(image);
                    BufferedImage bf = ImageIO.read(url);

                    WritableImage wr = null;
                    if (bf != null) {
                        wr = new WritableImage(bf.getWidth(), bf.getHeight());
                        PixelWriter pw = wr.getPixelWriter();
                        for (int x = 0; x < bf.getWidth(); x++) {
                            for (int y = 0; y < bf.getHeight(); y++) {
                                pw.setArgb(x, y, bf.getRGB(x, y));
                            }
                        }
                    }

                    icon = new ImageView(wr);
                }
                catch(Exception e){

                }
                
                icon.getStyleClass().add("iconPadding");

                TextFlow textFlow = new TextFlow();
                textFlow.getStyleClass().add("searchResultText");
                textFlow.setPrefHeight(63.0);
                textFlow.setPrefWidth(312.0);
                textFlow.setTextAlignment(TextAlignment.CENTER);

                Text text = new Text(title + " (" + year + ")");
                text.getStyleClass().add("searchResultTextObject");
                textFlow.getChildren().add(text);
                hbox.getChildren().addAll(icon,textFlow);
                hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        selectSearchResult(imdbID);
                    }
                });
                SearchResultsBox.getChildren().add(hbox);
            }
        });
    }
    
    public void selectSearchResult(String imdbID){
        try{
            URL siteURL = new URL("http://www.imdb.com/title/tt"+imdbID);
            URLConnection yc = siteURL.openConnection();
        //set the User-Agent value (currently using the Firefox user-agent value)
            yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:x.x.x) Gecko/20041107 Firefox/x.x");
            InputStreamReader isr = new InputStreamReader(yc.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String fullString = "";
            for (String line; (line = reader.readLine()) != null;) {
                fullString+=line;
            }
            String movTitle = fullString.substring(fullString.indexOf("<title>"), fullString.indexOf("</title>"));
            String title = movTitle.substring(movTitle.indexOf("<h1 itemprop=\"name\"")+8, movTitle.lastIndexOf("(")-1);
            String relDate = movTitle.substring(movTitle.lastIndexOf(")")-4, movTitle.lastIndexOf(")"));
            fullString = fullString.substring(fullString.indexOf("<time itemprop=\"duration\" datetime=\""));
            String rnTm = fullString.substring(fullString.indexOf("itemprop=\"duration\" datetime=\"")+32, fullString.indexOf("M"));
            fullString = fullString.substring(fullString.indexOf("itemprop='url'><span class=\"itemprop\" itemprop=\"name\">"));
            String dir = fullString.substring(fullString.indexOf("<span class=\"itemprop\" itemprop=\"name\">")+39, fullString.indexOf("</span></a>"));

            String genresString = fullString.substring(fullString.indexOf("<h4 class=\"inline\">Genres:</h4>")+26);
            genresString = genresString.substring(0, genresString.indexOf("</div>"));
            genresString = genresString.replaceAll("\\<[^>]*>","");
            genresString = genresString.replaceAll("&nbsp;","");
            genresString = genresString.replaceAll(" ","");
            genresString = genresString.replaceAll("\\|",",");
            
            TextFlow textFlow = new TextFlow();
            Text text = new Text(title+"\n"+relDate+"\n"+rnTm+"\n"+dir+"\n"+genresString);
            text.getStyleClass().add("searchResultTextObject");
            textFlow.getChildren().add(text);
            SingleMovieResult.getChildren().add(textFlow);
            selectedMovie = new Movie(title, relDate,dir, rnTm, genresString,imdbID);
        }
        catch(Exception e){
            //System.out.println(img);
            e.printStackTrace();
            //System.out.println(count);
        }
    }
    
    @FXML
    private void ShowSearchButtonClick(ActionEvent event) {
        ShowSearchResultsBox.getChildren().removeAll();
        System.setProperty("http.agent", "Chrome");
        String title = ShowSearchTextField.getText();
        title = title.replaceAll(" ", "+");
        int count=0;
        try{
            URL siteURL = new URL("http://www.imdb.com/find?ref_=nv_sr_fn&q=" + title + "&s=tt");
            URLConnection yc = siteURL.openConnection();
        //set the User-Agent value (currently using the Firefox user-agent value)
            yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:x.x.x) Gecko/20041107 Firefox/x.x");
            InputStreamReader isr = new InputStreamReader(yc.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String fullString = "";
            for (String line; (line = reader.readLine()) != null;) {
                fullString+=line;
            }
            fullString = fullString.substring(fullString.indexOf("<a name=\"tt\">"));
            fullString = fullString.substring(0,fullString.indexOf("</table>"));
            String[] results = fullString.split("<tr");
            //results[0] = results[0].substring(results[0].indexOf("<tr"));
            for(int i=1; i<=results.length-1; i++){
                count++;
                String result = results[i];
                if(result.indexOf(") (TV Episode")==-1&&result.indexOf(") (TV Series")!=-1){
                    String imdbID = result.substring(result.indexOf("/title/tt")+9,result.indexOf("/?"));
                    String img = result.substring(result.indexOf("img src=")+9,result.indexOf("<td class=\"result_text\">")-15);
                    String subResult = result.substring(result.lastIndexOf("a href"));
                    String movTitle = subResult.substring(subResult.indexOf(">")+1,subResult.indexOf("<"));
                    String movYear = subResult.substring(subResult.indexOf("</a>")+6,subResult.indexOf(")"));
                    
                    System.out.println(imdbID);
                    System.out.println(movYear);
                    System.out.println(movTitle);
                    System.out.println(img);
                    System.out.println();
                    
                    addShowSearchResult(imdbID, movTitle, movYear, img);
                }
            }
        }
        catch(Exception e){
            //System.out.println(img);
            e.printStackTrace();
            //System.out.println(count);
        }
    }

    public void addShowSearchResult(String imdbID, String title, String year, String image){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                HBox hbox = new HBox();
                //hbox.getStyleClass().add("chatListHBox");
                hbox.setId(imdbID);

                ImageView icon = new ImageView();
                
                try{
                    URL url = new URL(image);
                    BufferedImage bf = ImageIO.read(url);

                    WritableImage wr = null;
                    if (bf != null) {
                        wr = new WritableImage(bf.getWidth(), bf.getHeight());
                        PixelWriter pw = wr.getPixelWriter();
                        for (int x = 0; x < bf.getWidth(); x++) {
                            for (int y = 0; y < bf.getHeight(); y++) {
                                pw.setArgb(x, y, bf.getRGB(x, y));
                            }
                        }
                    }

                    icon = new ImageView(wr);
                }
                catch(Exception e){

                }
                
                icon.getStyleClass().add("iconPadding");

                TextFlow textFlow = new TextFlow();
                textFlow.getStyleClass().add("searchResultText");
                textFlow.setPrefHeight(63.0);
                textFlow.setPrefWidth(312.0);
                textFlow.setTextAlignment(TextAlignment.CENTER);

                Text text = new Text(title + " (" + year + ")");
                text.getStyleClass().add("searchResultTextObject");
                textFlow.getChildren().add(text);
                hbox.getChildren().addAll(icon,textFlow);
                hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        selectShowSearchResult(imdbID);
                    }
                });
                ShowSearchResultsBox.getChildren().add(hbox);
            }
        });
    }
    
    public void selectShowSearchResult(String imdbID){
        try{
            URL siteURL = new URL("http://www.imdb.com/title/tt"+imdbID);
            URLConnection yc = siteURL.openConnection();
        //set the User-Agent value (currently using the Firefox user-agent value)
            yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:x.x.x) Gecko/20041107 Firefox/x.x");
            InputStreamReader isr = new InputStreamReader(yc.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String fullString = "";
            for (String line; (line = reader.readLine()) != null;) {
                fullString+=line;
            }
            String movTitle = fullString.substring(fullString.indexOf("<title>"), fullString.indexOf("</title>"));
            String title = movTitle.substring(movTitle.indexOf("<h1 itemprop=\"name\"")+8, movTitle.lastIndexOf("(")-1);
            String relDate = movTitle.substring(movTitle.lastIndexOf(")")-4, movTitle.lastIndexOf(")"));
            fullString = fullString.substring(fullString.indexOf("<time itemprop=\"duration\" datetime=\""));
            String rnTm = fullString.substring(fullString.indexOf("itemprop=\"duration\" datetime=\"")+32, fullString.indexOf("M"));
            fullString = fullString.substring(fullString.indexOf("itemprop='url'><span class=\"itemprop\" itemprop=\"name\">"));
            String dir = fullString.substring(fullString.indexOf("<span class=\"itemprop\" itemprop=\"name\">")+39, fullString.indexOf("</span></a>"));

            String genresString = fullString.substring(fullString.indexOf("<h4 class=\"inline\">Genres:</h4>")+26);
            genresString = genresString.substring(0, genresString.indexOf("</div>"));
            genresString = genresString.replaceAll("\\<[^>]*>","");
            genresString = genresString.replaceAll("&nbsp;","");
            genresString = genresString.replaceAll(" ","");
            genresString = genresString.replaceAll("\\|",",");
            
            TextFlow textFlow = new TextFlow();
            Text text = new Text(title+"\n"+relDate+"\n"+rnTm+"\n"+dir+"\n"+genresString);
            text.getStyleClass().add("searchResultTextObject");
            textFlow.getChildren().add(text);
            SingleShowResult.getChildren().add(textFlow);
            selectedShow = new Show(title, imdbID);
            String selectQuery = "select showImdbID from tvShow where showImdbID='" + imdbID + "';";
            ResultSet resultSet = sqlConn.selectQuery(selectQuery);
            if(!resultSet.next()){
                String insertQuery = "insert into tvShow(title, showImdbID, genre) values(?,?,?);";
                sqlConn.prepareInsertShowQuery(insertQuery, title, imdbID, genresString);
            }
        }
        catch(Exception e){
            //System.out.println(img);
            e.printStackTrace();
            //System.out.println(count);
        }
    }
    
    @FXML
    private void AddShowButtonClick(ActionEvent event) {
        selectedShow.setViewDate(ViewShowDatePicker.getValue().toString());
        selectedShow.setSeasonNumber(SeasonTextField.getText());
        selectedShow.setEpisodeNumber(EpisodeTextField.getText());
        String episodeTitle = getEpisodeTitle(selectedShow.getShowImdbID(), selectedShow.getSeasonNumber(), selectedShow.getEpisodeNumber());
        selectedShow.setEpisodeTitle(episodeTitle);
        showData.add(selectedShow);
        String insertQuery = "insert into episodeWatched(viewDate, showImdbID, episodeSeason, episodeNumber, episodeTitle, episodeImdbID) values(?,?,?,?,?,?);";
        try{
            sqlConn.prepareInsertEpisodeQuery(insertQuery, selectedShow.getViewDate(), selectedShow.getShowImdbID(), 
                    selectedShow.getSeasonNumber(), selectedShow.getEpisodeNumber(), selectedShow.getEpisodeTitle(), 
                    selectedShow.getEpisodeImdbID());
            //Server.showMessage(query);
        }
        catch(Exception e){
           // sqlConn.showMessage("conn line 106"+e.toString());
        }
    }
    
    private String getEpisodeTitle(String imdbID, String season, String episode){
        String episodeTitle = "";
        try{
            URL siteURL = new URL("http://www.imdb.com/title/tt"+imdbID+"/episodes?season="+season);
            URLConnection yc = siteURL.openConnection();
        //set the User-Agent value (currently using the Firefox user-agent value)
            yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:x.x.x) Gecko/20041107 Firefox/x.x");
            InputStreamReader isr = new InputStreamReader(yc.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String fullString = "";
            for (String line; (line = reader.readLine()) != null;) {
                fullString+=line;
            }
            String subStr = "itemprop=\"episodeNumber\" content=\""+episode+"\"";
            fullString = fullString.substring(fullString.indexOf(subStr));
            episodeTitle = fullString.substring(fullString.indexOf("title=")+7,fullString.indexOf("itemprop=\"name\"")-2);
            String episodeImdbID = fullString.substring(fullString.indexOf("/title/tt")+9,fullString.indexOf("/?"));
            selectedShow.setEpisodeImdbID(episodeImdbID);
            System.out.println(episodeTitle+episodeImdbID);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return episodeTitle;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sqlConn = new LoadDriver("jdbc:mysql://localhost:3306/movies","root","password");
        initializeMoviePane();
        initializeShowPane();
    }   
    
    public void initializeShowPane(){
        showTable.setEditable(true);
        showTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        AnchorPane.setTopAnchor(showTable, 0.0);
        AnchorPane.setLeftAnchor(showTable, 0.0);
        AnchorPane.setRightAnchor(showTable, 0.0);
        AnchorPane.setBottomAnchor(showTable, 0.0);
        
        TableColumn viewDate = new TableColumn("View Date");
        viewDate.setMinWidth(100);
        viewDate.setCellValueFactory(new PropertyValueFactory<Show, String>("viewDate"));
        
        TableColumn showTitle = new TableColumn("Show Title");
        showTitle.setMinWidth(300);
        showTitle.setCellValueFactory(new PropertyValueFactory<Show, String>("showTitle"));
        
        TableColumn seasonNumber = new TableColumn("Season");
        seasonNumber.setMinWidth(100);
        seasonNumber.setCellValueFactory(new PropertyValueFactory<Show, String>("seasonNumber"));
        
        TableColumn episodeNumber = new TableColumn("Episode");
        episodeNumber.setMinWidth(300);
        episodeNumber.setCellValueFactory(new PropertyValueFactory<Show, String>("episodeNumber"));
        
        TableColumn epTitle = new TableColumn("Episode Title");
        epTitle.setMinWidth(300);
        epTitle.setCellValueFactory(new PropertyValueFactory<Show, String>("episodeTitle"));
        
        showTable.setItems(showData);
        showTable.getColumns().addAll(viewDate, showTitle, seasonNumber, episodeNumber, epTitle);
        showTableView.getChildren().addAll(showTable);
        try{
            String query = "select e.viewDate, s.title, e.episodeSeason, e.episodeNumber, e.episodeTitle from episodeWatched e inner join tvShow s on e.showImdbID=s.showImdbID order by viewDate;";
            ResultSet rs = sqlConn.selectQuery(query);
            while(rs.next()){
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                String viewDt= rs.getDate("viewDate").toString();
                String shwTitle = rs.getString("title");
                int epSeason = rs.getInt("episodeSeason");
                int epNumber = rs.getInt("episodeNumber");
                String episodeTitle = rs.getString("episodeTitle");
                showData.add(new Show(viewDt, shwTitle, Integer.toString(epSeason), Integer.toString(epNumber), episodeTitle));
            }
        }
        catch(Exception e){System.out.println("line 284"+e.toString());}
    }
    
    public void initializeMoviePane(){
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        AnchorPane.setTopAnchor(table, 0.0);
        AnchorPane.setLeftAnchor(table, 0.0);
        AnchorPane.setRightAnchor(table, 0.0);
        AnchorPane.setBottomAnchor(table, 0.0);
        
        TableColumn viewDate = new TableColumn("View Date");
        viewDate.setMinWidth(100);
        viewDate.setCellValueFactory(new PropertyValueFactory<Movie, String>("viewDate"));
        
        TableColumn movieTitle = new TableColumn("Title");
        movieTitle.setMinWidth(300);
        movieTitle.setCellValueFactory(new PropertyValueFactory<Movie, String>("movieTitle"));
        
        TableColumn releaseDate = new TableColumn("Release Date");
        releaseDate.setMinWidth(100);
        releaseDate.setCellValueFactory(new PropertyValueFactory<Movie, String>("releaseDate"));
        
        TableColumn director = new TableColumn("Director");
        director.setMinWidth(300);
        director.setCellValueFactory(new PropertyValueFactory<Movie, String>("director"));
        
        TableColumn runtime = new TableColumn("Runtime");
        runtime.setMinWidth(100);
        runtime.setCellValueFactory(new PropertyValueFactory<Movie, String>("runtime"));
        
        TableColumn genre = new TableColumn("Genre");
        genre.setMinWidth(300);
        genre.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));
        
        table.setItems(data);
        table.getColumns().addAll(viewDate, movieTitle, releaseDate, director, runtime, genre);
        movieTableView.getChildren().addAll(table);
        try{
            String query = "select viewDate, title, releaseDate, director, runtime, genre, imdbID from movie order by viewDate;";
            ResultSet rs = sqlConn.selectQuery(query);
            while(rs.next()){
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                String viewDt= rs.getDate("viewDate").toString();
                String movTitle = rs.getString("title");
                String relDt = rs.getString("releaseDate");
                String dir = rs.getString("director");
                String rnTm = rs.getString("runtime");
                String gen = rs.getString("genre");
                String imdbID = rs.getString("imdbID");
                data.add(new Movie(viewDt, movTitle, relDt, dir, rnTm, gen, imdbID));
            }
        }
        catch(Exception e){System.out.println("line 289"+e.toString());}
    }
    
}
