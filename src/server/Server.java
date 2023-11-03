package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    public static void main(String[] args) {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(9876); //Create UDP socket
        } catch (SocketException e1) {
            e1.printStackTrace();
            System.exit(1);
        }

        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String receivedSentence = new String(receivePacket.getData()); //Creating String of received data, sent by client through an array of bytes
            System.out.println("Received: " + receivedSentence); //Confirmation that data was received

            //Get IP Address and Port number of the received packet
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String[] values = receivedSentence.split(","); //Split received String into multiple strings and putting those strings into an array to pass data as variables for calculate function
            String operator = values[0];
            String firstNumber = values[1];
            String secondNumber = values[2];

            String sendingSentence = calculate(operator, firstNumber, secondNumber);//Passing variables to calculate function that returns a result in a String
            byte[] sendingData = sendingSentence.getBytes();//extracting data bytes from String result and putting it into an array of bytes
            DatagramPacket sendPacket = new DatagramPacket(sendingData, sendingData.length, IPAddress, port);//Preparing packet with String answer, to send it to the Client
            try {
                serverSocket.send(sendPacket);//Sending packet with data to Client through an array of bytes
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //Calculating function and returning string to put result in a packet
    public static String calculate(String operator, String firstNumber, String secondNumber) {
        double a = Double.parseDouble(firstNumber);
        double b = Double.parseDouble(secondNumber);
        double answer;
        String result = null;

        //checks which operation to perform and returns the String result to be displayed by the Client
        switch (operator) {
            case "+":
                answer =  a + b;
                result = String.format("Answer: %,.2f + %,.2f = %,.2f", a, b, answer);
                break;
            case "-":
                answer = a - b;
                result = String.format("Answer: %,.2f - %,.2f = %,.2f", a, b, answer);
                break;
            case "x":
                answer = a * b;
                result = String.format("Answer: %,.2f x %,.2f = %,.2f", a, b, answer);
                break;
            case "÷":
                if (b != 0) {
                    answer = a/b;
                    result = String.format("Answer: %,.2f ÷ %,.2f = %,.2f", a, b, answer);
                } else {
                    return "Division by zero is undefined!";
                }
                break;
            case "MAX":
                answer = Math.max(a, b);
                result = String.format("Answer: Maximum is: %,.2f", answer);
                break;
            case "MIN":
                answer = Math.min(a, b);
                result = String.format("Answer: Minimum is: %,.2f", answer);
                break;
        }
        return result;
    }
}

