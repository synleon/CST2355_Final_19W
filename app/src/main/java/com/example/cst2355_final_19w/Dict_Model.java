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

    /**
     * constructor of dict_model
     * @param word
     * @param pronunciation
     * @param definition
     * @param id
     */
    public Dict_Model(String word, String pronunciation, String definition, long id) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.definition = definition;
        this.id = id;
    }

    /**
     * get word
     * @return word
     */
    public String getWord() {
        return word;
    }

    /**
     * set word
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * get pronunciation
     * @return pronunciation
     */
    public String getPronunciation() {
        return pronunciation;
    }

    /**
     * set pronunciation
     * @param pronunciation
     */
    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    /**
     * get definition
     * @return definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * set definition
     * @param definition
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * get id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * set id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }
}
