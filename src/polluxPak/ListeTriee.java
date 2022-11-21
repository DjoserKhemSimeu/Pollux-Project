package polluxPak;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;



public class ListeTriee extends LinkedList<Double> {
	 public ListeTriee() {
		 
	 }
	 public ListeTriee(Collection<? extends Double> c) {
		 addAll(c);
	 }
	 public boolean add(Double n) {
		 ListIterator<Double> it=listIterator();
		 if(size()==0) {
			 it.add(n);
			 return true;
		 }
		Double c= it.next();
		 while(n.compareTo(c)>0 && it.hasNext()) {
			 c=it.next();
		 }
		 if(n< getLast()) {
		 it.previous();
		 }
		 it.add(n);
		 return true;
	 }
	 public boolean addAll(Collection<? extends Double> c) {
		 for(Double n: c) {
			 add(n);
		 }
		 return true;
	 }
	 public static void main(String[]args) {
		 ListeTriee l=new ListeTriee();
		 l.add(6.);
		 l.add(8.);
		 l.add(5.);
		 l.add(7.);
		 l.add(2.);
		 ListIterator<Double>it=l.listIterator();
		 double c;
		 while(it.hasNext()) {
			 c=it.next();
			 System.out.println(c);
			 
		 }
		 
	 }
	 
	}