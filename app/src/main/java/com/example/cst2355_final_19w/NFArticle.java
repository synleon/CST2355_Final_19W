package com.example.cst2355_final_19w;

public class NFArticle
{
    private String title;
    private String text;
    private String urlAddress;
    private long id;

    public NFArticle(String title, long id)
    {
        setTitle(title);
        setId(id);
    }

    public NFArticle(String title, String text, long id)
    {
        setTitle(title);
        setText(text);
        setId(id);
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
}
