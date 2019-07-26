import java.util.Iterator;
import java.util.Objects;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] list;
    private int size;
    private int itemNum;
    private int headIndex;
    private int tailIndex;

    // construct an empty randomized queue
    public RandomizedQueue(){
        list = (Item[]) new Object[1];
        size = 1;
        itemNum = 0;
        headIndex = -1;
        tailIndex = -1;
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return itemNum == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return itemNum;
    }

    // add the item
    public void enqueue(Item item){
        if(item == null){
            throw new java.lang.IllegalArgumentException();
        }

        if(tailIndex + 1 >= size){
            resize(size * 2);
        }
        tailIndex ++;
        list[tailIndex] = item;

        if(isEmpty()){
            headIndex ++;
        }
        itemNum ++;
    }

    // remove and return a random item
    public Item dequeue(){
        if(isEmpty()){
            throw new java.util.NoSuchElementException();
        }

        int randomIndex = headIndex + StdRandom.uniform(0, itemNum);
        Item temp = list[headIndex];
        list[headIndex] = list[randomIndex];
        list[randomIndex] = temp;

        Item result =  list[headIndex];
        list[headIndex] = null;
        headIndex ++;
        itemNum --;
        if(itemNum < size / 4){
            resize(size / 2);
        }
        if(isEmpty()){
            headIndex --;
        }
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if(isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        int randomIndex = headIndex + StdRandom.uniform(itemNum);
        return list[randomIndex];
    }

    private void resize(int capacity){
        Item[] temp = (Item[]) new Object[capacity];
        for(int i = headIndex; i <= tailIndex; i ++){
            temp[i - headIndex] = list[i];
        }
        if(capacity != 0){
            tailIndex = tailIndex - headIndex;
        }else{
            tailIndex = -1;
        }
        headIndex = 0;
        size = capacity;
        list = temp;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item>{

        private int current = headIndex;
        private int index = 0;
        private int[] sequence;

        public RandomizedQueueIterator(){
            int s = tailIndex - headIndex + 1;
            sequence = new int[s];
            for(int i = headIndex; i < tailIndex + 1; i ++){
                sequence[i - headIndex] = i;
            }
            for(int i = 0; i < s; i ++){
                int r = StdRandom.uniform(i, s);
                int temp = sequence[r];
                sequence[r] = sequence[i];
                sequence[i] = temp;
            }
        }

        @Override
        public boolean hasNext() {
            return current <= tailIndex && itemNum != 0;
        }

        @Override
        public Item next() {
            if(current > tailIndex){
                throw new java.util.NoSuchElementException();
            }
            current ++;
            index ++;
            return list[sequence[index - 1]];
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
        System.out.println("Empty: " + r.isEmpty());
        System.out.println("Size: " + r.size());

        //--------------------------------------------------
        int size = 10;
        for(int i = 0; i < size; i ++){
            r.enqueue(i);
        }
        for(int i : r){
            System.out.print(i + "  ");
        }
        System.out.println();
        //--------------------------------------------------
        System.out.print("Sample: ");
        for(int i = 0; i < size; i ++){
            System.out.print(r.sample() + " ");
        }
        System.out.println();
        //--------------------------------------------------
        System.out.println("Empty: " + r.isEmpty());
        System.out.println("Size: " + r.size());
        for(int i : r){
            System.out.print(i + "  ");
        }
        System.out.println();
        //--------------------------------------------------
        System.out.print("Deque: ");
        for(int i = 0; i < size; i ++){
            System.out.print(r.dequeue() + "  ");
        }
        System.out.println();
        System.out.println("Empty: " + r.isEmpty());
        System.out.println("Size: " + r.size());
        for(int i : r){
            System.out.print(i + "  ");
        }

    }
}
