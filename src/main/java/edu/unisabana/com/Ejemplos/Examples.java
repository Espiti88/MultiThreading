package edu.unisabana.com.Ejemplos;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Examples {

    private static int counter = 0;

    public static synchronized void increment() {
        counter++;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- ThreadExample --");
        ThreadExample();

        System.out.println("-- RunnableExample --");
        RunnableExample();

        System.out.println("-- SynchronizedExample --");
        SynchronizedExample();

        System.out.println("-- ExecutorServiceExample --");
        ExecutorServiceExample();

        System.out.println("-- CallableExample --");
        CallableExample();
    }

    public static void ThreadExample(){

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread: " + i);
                try {
                    sleep(1000); // Pausa el hilo por 1 segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try { thread.join(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private static void RunnableExample() {
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Runnable: " + i);
                try {
                    sleep(1000); // Pausa el hilo por 1 segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(task); // Crea un hilo con el Runnable
        thread.start(); // Inicia el hilo

        try { thread.join(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private static void SynchronizedExample() {
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                increment(); // Incrementa el contador de manera sincronizada
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        try {
            thread1.join(); // Espera a que thread1 termine
            thread2.join(); // Espera a que thread2 termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter: " + counter); // Imprime el valor final del contador
    }

    private static void ExecutorServiceExample() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(2); // Crea un pool de 2 hilos

        Runnable task1 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task 1: " + i);
                try {
                    Thread.sleep(1000); // Pausa el hilo por 1 segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task 2: " + i);
                try {
                    Thread.sleep(1000); // Pausa el hilo por 1 segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        executor.submit(task1); // Envía la tarea 1 al pool de hilos
        executor.submit(task2); // Envía la tarea 2 al pool de hilos
        executor.shutdown(); // Finaliza el pool de hilos una vez completadas las tareas
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    private static void CallableExample() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2); // Crea un pool de 2 hilos

        Callable<Integer> task = () -> {
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                sum += i;
                try {
                    Thread.sleep(1000); // Pausa el hilo por 1 segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return sum; // Retorna la suma de los números
        };

        Future<Integer> future = executor.submit(task); // Envía la tarea y recibe un Future

        try {
            Integer result = future.get(); // Obtiene el resultado de la tarea
            System.out.println("Sum: " + result); // Imprime el resultado
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown(); // Finaliza el pool de hilos
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

}
