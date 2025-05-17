import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
import java.util.*;
public class ServerGUI extends JFrame {
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    ServerGUI() {

        try {
            server = new ServerSocket(7777);
            System.out.println("The Client is waiting to Connect");
            System.out.println("Waiting");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());


            createGUI();
            handleEvents();
            startReading();
//            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    Border border = BorderFactory.createLineBorder(Color.white);
                    messageArea.setBorder(border);
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI() {
        this.setTitle("Server Messenger");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageArea.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add(heading, BorderLayout.NORTH);
//        this.add(messageArea,BorderLayout.CENTER);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reading started");
            while (true) {
                try {
//                    String msg=br.readLine();
//                    if(msg.equals("exit")){
//                        System.out.println("Terminated");
//                        break;
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated");
                        JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                        messageArea.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    messageArea.append( "Client : "+ msg+ "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            ;
        };
        new Thread(r1).start();
    }


    public void startWriting () {
        Runnable r2 = () -> {
            while (true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }
    public static void main (String[]args){
        new ServerGUI();
    }
}

