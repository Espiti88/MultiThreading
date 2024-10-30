package edu.unisabana.com.Propio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bank {
    public int balance = 5000;

    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println(" ++ Depositado: " + amount + "$, Saldo actual: " + balance + "$");
    }

    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(" -- Retirado: " + amount + "$, Saldo actual: " + balance + "$");
        } else {
            System.out.println(" -- Fondos insuficientes para retirar: " + amount + "$, Saldo actual: " + balance + "$");
        }
    }

    public static void main(String[] args) {
        Bank bank = new Bank();

        ExecutorService executor = Executors.newFixedThreadPool(2); // Crea un pool de 2 hilos

        //Runnable encargado de depositar dinero a la cuenta
        Runnable taskDeposit = () -> {
            for (int i = 1; i <= 5; i++) {

                try {
                    Thread.sleep(3000); // Pausa el hilo por 3 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int deposit = (int)(Math.random() * 10)*1000; // Numero aleatorio en intervalo de 1000 hasta 10000
                bank.deposit(deposit);
            }
        };

        Runnable taskWithdraw = () -> {
            for (int i = 1; i <= 5; i++) {

                try {
                    Thread.sleep(3000); // Pausa el hilo por 3 segundos
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                int deposit = (int)(Math.random() * 9 + 1)*1000; // Numero aleatorio en intervalo de 1000 hasta 10000
                bank.withdraw(deposit);

            }
        };

        System.out.println("Fondos iniciales: 5000$");

        executor.submit(taskDeposit); // Envía la tarea Deposit al pool de hilos
        executor.submit(taskWithdraw); // Envía la tarea Withdraw al pool de hilos
        executor.shutdown(); // Finaliza el pool de hilos una vez completadas las tareas

    }
}

