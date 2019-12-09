package com.example.weitblickapp_android.ui.blog_entry;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BlogEntryViewModel extends ViewModel {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");

    private int id;
    private String title;
    private String text;
    private  int image_id;
    private String teaser;
    private ArrayList<String> imageUrls;
    Date published;
    private int location_id;


    public BlogEntryViewModel(int id, String title, String text, String teaser, ArrayList<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.text = text;
        /*try {
            this.published = formatterRead.parse(published);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.imageUrls = imageUrls;*/
        this.teaser = teaser;

    }

    public BlogEntryViewModel(int id, String title, String text, String published) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image_id = image_id;
        try {
            this.published = formatterRead.parse(published);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.location_id = location_id;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public BlogEntryViewModel() {
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

    public String getText(){return this.text;}

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getPublished() {
        return formatterWrite.format(published);
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public ArrayList<String> getImageUrls() { return imageUrls; }

    public void setImageUrl(ArrayList<String> imageUrl) { this.imageUrls = imageUrls; }


    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    @Override
    public String toString() {
        return "BlogEntryViewModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", image_id=" + image_id +
                ", created_at=" + published +
                ", location_id=" + location_id +
                '}';
    }
}