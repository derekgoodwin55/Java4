public class ItemCount implements Comparable<ItemCount>{
    //instance variables
    int counter;
    Object key;

    //constructor
    public ItemCount(Object o, int i){
        this.key = o;
        this.counter = i;
    }

    public int getCount(){
        return this.counter;
    }

    public Object getObject(){
        return this.key;
    }

    @Override
    public int compareTo(ItemCount itemCount) {
        if(this.counter > itemCount.counter){
            return -1;
        }
        else if(this.counter < itemCount.counter){
            return 1;
        }
        else{
            return 0;
        }
    }

    public String toString(){
        return this.key + "\t" + Integer.toString(this.counter);
    }
}