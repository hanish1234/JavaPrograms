package com.training.threads;

import java.util.Vector;

public class ProducerConsumerDemo {
	
	public static void main(String args[]){
		Vector sharedQueue = new Vector();
		int size = 4 ;
	
		Thread prodThread = new Thread(new Producer(sharedQueue , size),"Producer");
		Thread consThread = new Thread(new Consumer(sharedQueue , size),"Consumer");
					
		prodThread.start();
		consThread.start();
	}

}

class Producer implements Runnable{
	
	private final Vector sharedQueue;
	private final int SIZE;
	
	public Producer(Vector  sharedQueue , int size){
		this.sharedQueue = sharedQueue;
		this.SIZE = size;
	}
	
	public  void run(){
		for(int i = 0 ; i < 7 ; i++){
			System.out.println("Produced :"+ i );
			try {
				produce(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void produce(int i) throws InterruptedException{
		// wait if the queue is full
		while(sharedQueue.size() == SIZE){
			synchronized (sharedQueue) {
				System.out.println("Queue is full " + Thread.currentThread().getName()
						+ " is waiting , size : "+  sharedQueue.size() );
				sharedQueue.wait();	
			}
		}
		
	// producing element and notify members	
		synchronized(sharedQueue){
			sharedQueue.add(i);
			sharedQueue.notifyAll();
		}
	}
	
}

class Consumer implements Runnable{
	private final Vector sharedQueue;
	private final int SIZE;
	
	public Consumer(Vector  sharedQueue , int size){
		this.sharedQueue = sharedQueue;
		this.SIZE = size;
	}
	
	public void run(){
		while(true){
			try {
				System.out.println("Consumed :"+ consume());
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private int consume() throws InterruptedException {
		// wait if the queue is empty
		while(sharedQueue.isEmpty()){
			synchronized(sharedQueue){
				System.out.println("Queue is empty " + Thread.currentThread().getName()
						+ " is waiting , size : "+  sharedQueue.size() );
				sharedQueue.wait();	
				
			}
		}
		//Otherwise consumemthe element and notify waiting producer
		synchronized(sharedQueue){
			sharedQueue.notifyAll();
			return (Integer)sharedQueue.remove(0);
		}
	}
	
}


