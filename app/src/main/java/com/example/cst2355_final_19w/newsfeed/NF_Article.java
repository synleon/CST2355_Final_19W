package com.example.cst2355_final_19w.newsfeed;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/** this class defines an article searched on website */
public class NF_Article
{
    private String title;
    private String text;
    private String urlAddress;
    private String imageLink;
    private long id;
    private Bitmap bitmap;

    /** used for adding favor list*/
    public NF_Article(String title, String text, String url, String imageLink, long id)
    {
        setTitle(title);
        setText(text);
        setUrlAddress(url);
        setImageLink(imageLink);
        setId(id);
    }

    /** used for adding search list*/
    public NF_Article(String title, String text, String url, String imageLink, Bitmap bitmap)
    {
        setTitle(title);
        setText(text);
        setUrlAddress(url);
        setImageLink(imageLink);
        setBitmap(bitmap);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public void setUrlAddress(String urlAddress)
    {
        this.urlAddress = urlAddress;
    }

    public String getUrlAddress()
    {
        return urlAddress;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }

    public void setImageLink(String imageLink)
    {
        this.imageLink = imageLink;
    }

    public String getImageLink()
    {
        return imageLink;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }
}
