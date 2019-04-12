package com.example.cst2355_final_19w;

public class Dict_Model {
    /**
     * declare string word, pronunciation, definition
     */
    private String word, pronunciation, definition;
    /**
     * declare ID
     */
    private long id;

    /**
     * constructor
     */
    public Dict_Model() {


    }

    public Dict_Model(String word, String pronunciation, String definition, long id) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.definition = definition;
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
