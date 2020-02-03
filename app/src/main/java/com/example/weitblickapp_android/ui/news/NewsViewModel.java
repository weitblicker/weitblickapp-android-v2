package com.example.weitblickapp_android.ui.news;

import android.location.Location;

import com.example.weitblickapp_android.ui.project.ProjectViewModel;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//@Entity(tableName = "news")
public class NewsViewModel extends ViewModel {
 //   @Ignore
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
   // @Ignore
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");

    //@PrimaryKey
    private int id;

    private String title;
    private String text;
    private String teaser;
    private String author;
    private String author_image;
    private int image_id;
    private String published;
    private Location location;
    private ArrayList<String> imageUrls;
    private ArrayList<String> hosts;
    String name;
    String image;
    ArrayList<ProjectViewModel> project;

    public NewsViewModel() {
    }

    public NewsViewModel(int id, String title, String text, String teaser,String date, ArrayList <String> imageUrls, String name,String image, ArrayList<String> hosts, ArrayList<ProjectViewModel> project) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.teaser = teaser;
        this.imageUrls = imageUrls;
        this.name = name;
        this.image = image;
        this.hosts = hosts;
        this.project = project;

        try {
            this.published = formatterRead.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public NewsViewModel(int id, String title, String text, String teaser,String date, ArrayList <String> imageUrls, String name,String image, ArrayList<String> hosts) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.teaser = teaser;
        this.imageUrls = imageUrls;
        this.name = name;
        this.image = image;
        this.hosts = hosts;

        try {
            Date temp = formatterRead.parse(date);
            this.published = formatterWrite.format(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public NewsViewModel(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public ArrayList<ProjectViewModel> getProject() {
        return project;
    }

    public void setProject(ArrayList<ProjectViewModel> project) {
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {return text; }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getDate() { return published; }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public ArrayList<String> getImageUrls() { return imageUrls; }

    public void setImageUrls(ArrayList<String> imageUrl) { this.imageUrls = imageUrl; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getHosts() {
        return hosts;
    }

    public void setHosts(ArrayList<String> hosts) {
        this.hosts = hosts;
    }

    @Override
    public String toString() {
        return "NewsViewModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", image_id=" + image_id +
                ", date=" + published +
                '}';
    }
}