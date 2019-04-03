package com.example.cst2355_final_19w;

public class NF_Article
{
    private String title;
    private String text;
    private String urlAddress;
    private String imageLink;
    private long id;

    public NF_Article(String title)
    {
        setTitle(title);
    }

    public NF_Article(String title, long id)
    {
        setTitle(title);
        setId(id);
    }

    public NF_Article(String title, String text, long id)
    {
        setTitle(title);
        setText(text);
        setId(id);
    }

    public NF_Article(String title, String text, String url)
    {
        setTitle(title);
        setText(text);
        setUrlAddress(url);
    }

    public NF_Article(String title, String text, String url, String imageLink)
    {
        setTitle(title);
        setText(text);
        setUrlAddress(url);
        setImageLink(imageLink);
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
}
