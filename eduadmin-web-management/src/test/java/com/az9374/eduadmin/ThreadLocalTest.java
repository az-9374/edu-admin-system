package com.az9374.eduadmin;


public class ThreadLocalTest {

    private static ThreadLocal<String> local = new ThreadLocal<>();
    public static void main( String args[]){
       System.out.println(Thread.currentThread().getName()+" : "+local.get());
       local.set("local message");

       new Thread(new Runnable() {
           @Override
           public void run() {
//               ThreadLocal<String> local2 = new ThreadLocal<>();
               System.out.println(Thread.currentThread().getName()+" : "+local.get());
               local.set(" message");
               System.out.println(Thread.currentThread().getName()+" : "+local.get());
               local.remove();
           }
       }).start();

       System.out.println(Thread.currentThread().getName()+" : "+local.get());
       local.remove();
    }

}
