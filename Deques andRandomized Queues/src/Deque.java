import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item>  {

    private Node first;
    private Node last;
    private int size;

    private class Node{
        Item item;
        Node previous;
        Node next;
    }

    // construct an empty deque
    public Deque(){
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the deque
    public int size(){
        return size;
    }

    // add the item to the front
    public void addFirst(Item item){
        if(item == null){
            throw new java.lang.IllegalArgumentException();
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.previous = null;
        first.next = oldFirst;
        if(isEmpty()){
            last = first;
        }else{
            oldFirst.previous = first;
        }
        size ++;
    }

    // add the item to the back
    public void addLast(Item item){
        if(item == null){
            throw new java.lang.IllegalArgumentException();
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        last.next = null;
        if (isEmpty()){
            first = last;
        }else{
            oldLast.next = last;
        }
        size ++;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if(isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        size --;
        Item temp = first.item;
        first = first.next;
        if(first != null){
            first.previous = null;
        }else {
            last = null;
        }
        return temp;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if(isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        size --;
        Item temp = last.item;
        last = last.previous;
        if(last != null){
            last.next = null;
        }else{
            first = null;
        }
        return temp;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>{

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if(current == null){
                throw new java.util.NoSuchElementException();
            }
            Item temp = current.item;
            current = current.next;
            return temp;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args){

        Deque<Integer> d = new Deque<Integer>();
        d.addLast(1);
        d.removeLast();



        System.out.println(d.isEmpty());
        d.addFirst(1);
        d.addFirst(2);
        System.out.println(d.removeLast());
        System.out.println(d.removeLast());
        System.out.println(d.size());
        d.addLast(1);
        d.addLast(2);
        d.addLast(2);
        d.addLast(2);
        d.addLast(2);
        d.addLast(2);
        d.addLast(2);
        System.out.println(d.removeFirst());
        for(int i : d){
            StdOut.print(i + "  ");
        }


    }

}
