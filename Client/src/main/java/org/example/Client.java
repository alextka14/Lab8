package org.example;

import org.example.response.Response;
import org.example.response.STATUS;
import org.example.utility.PortAsker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Client {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final ArrayList<String> scriptHistory = new ArrayList<>();
    private final String host;
    private int port;
    private ResourceBundle resourceBundle;

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
        connectToServer();
    }

    private void connectToServer() {
        while (true) {
            try {
                Socket socket = new Socket(host, port);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                if (!socket.isClosed()) {
                    break;
                }
            } catch (IOException e) {
                System.err.println("Не удалось подключиться к серверу. Пожалуйста, попробуйте позже.");
                System.out.print("Введите exit, чтобы завершить работу или любой другой символ, чтобы продолжить: ");
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNextLine()) {
                    if (scanner.nextLine().equals("exit")) {
                        System.out.println("Завершение работы");
                        System.exit(0);
                    }
                }
                System.out.print("Введите Y, чтобы сменить порт: ");
                this.port = PortAsker.getPort();
            }
        }
    }

    public Response sendCommand(String command, Object object) {
        Response response1 = new Response(STATUS.COMMAND, command, object);
        try {
            oos.writeObject(response1);
            oos.flush();
            return (Response) ois.readObject();
        } catch (IOException e) {
            connectToServer();
            return sendCommand(command, object);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String userName, String passWord) throws IOException, ClassNotFoundException {
        Response checkUserRequest = new Response(STATUS.USERCHECK, "checkUser", userName);
        oos.writeObject(checkUserRequest);
        oos.flush();
        Response checkUserResponse = (Response) ois.readObject();
        if (passWord.isEmpty()) {
            return false;
        }
        if ((boolean) checkUserResponse.getObject()) {
            Response checkPasswordRequest = new Response(STATUS.USERCHECK, "checkPassword", userName + " " + passWord);
            oos.writeObject(checkPasswordRequest);
            oos.flush();
            Response checkPasswordResponse = (Response) ois.readObject();
            return (boolean) checkPasswordResponse.getObject();
        }
        return false;
    }

    public boolean register(String userName, String passWord1, String passWord2) throws IOException, ClassNotFoundException {
        if (passWord1.isEmpty() || passWord2.isEmpty()) {
            return false;
        }
        Response checkUserRequest = new Response(STATUS.USERCHECK, "checkUser", userName);
        oos.writeObject(checkUserRequest);
        oos.flush();
        Response checkUserResponse = (Response) ois.readObject();
        if (!((boolean) checkUserResponse.getObject())) {
            if (passWord1.equals(passWord2)) {
                Response registerUserRequest = new Response(STATUS.USERCHECK, "registerUser", userName + " " + passWord1);
                oos.writeObject(registerUserRequest);
                oos.flush();
                return true;
            }
            return false;
        }
        return false;
    }
}
