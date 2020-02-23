package com.eden.trie.keyword;

import static com.eden.util.CollectionUtils.isEmpty;

public class Dictionary implements Comparable{

    public static final int DEFUALT_PRIORITY = 100;
    public static final int DICT_BASE_ID = 1;
    public static final Dictionary SYSTEM_DEFAULT_DICT = new Dictionary(DICT_BASE_ID, DictType.SYSTEM,"system_default", DEFUALT_PRIORITY);

    private int id;
    private String name;
    private DictType dictType;
    private Dictionary originSource;

    /** bigger value means higher priority */
    private int priority = DEFUALT_PRIORITY;

    private Dictionary(int id, DictType dictType, String name, int priority) {
        this(id, dictType, name, priority, null);
    }

    private Dictionary(int id, DictType dictType, String name, int priority, Dictionary source) {
        this.id = id;
        this.name = name;
        this.dictType = dictType;
        this.priority = priority;
        this.originSource = source;
    }

    public static Dictionary createDictionary(int id, DictType dictType, String name, Dictionary source) {
        return createDictionary(id, dictType, name, DEFUALT_PRIORITY, source);
    }

    public static Dictionary createDictionary(int id, DictType dictType, String name, int priority) {
        return createDictionary(id, dictType, name, priority, null);
    }

    public static Dictionary createDictionary(int id, DictType dictType, String name, int priority, Dictionary source) {
        if(id <= DICT_BASE_ID){
            throw new IllegalArgumentException("Dictionary Id should more than DICT_BASE_ID(" + DICT_BASE_ID + ").");
        }
        return new Dictionary(id, dictType, name, priority, source);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public DictType getDictType() {
        return dictType;
    }

    public Dictionary getOriginSource() {
        return originSource;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Dictionary)) {
            return false;
        }

        Dictionary other = (Dictionary) o;
        return this.id == other.id;
    }

    /**
     * Just simply compare the priority value.
     * */
    @Override
    public int compareTo(Object o)
    {
        if(o == null){
            return 1;
        }
        if (!(o instanceof Dictionary)){
            return 1;
        }

        Dictionary other = (Dictionary) o;
        return this.priority == other.priority ? 0 : (this.priority > other.priority ? 1 : -1 );
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dictType=" + dictType +
                ", priority=" + priority +
                ", originSource=" + originSource +
                '}';
    }

// -----------------------------
// not uesed data field
// -----------------------------
//    private List<Keyword> words = new LinkedList<>();
//
//    public Dictionary addAll(Set<String> wordlist) {
//        for(String word : wordlist){
//            this.words.add(new Keyword(word, this));
//        }
//        return this;
//    }
//
//    public List<Keyword> getWords() {
//        if(isEmpty(this.words)){
//            this.words = new LinkedList<>();
//        }
//        return this.words;
//    }
//
//    public void setWords(List<Keyword> words) {
//        if(isEmpty(this.words)){
//            this.words = new LinkedList<>();
//        }
//        this.words = words;
//    }
// -----------------------------

}
