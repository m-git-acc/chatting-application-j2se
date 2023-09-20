package chatapplication.server;

//1. make a thread for audio, to play in background, which makes you application fast...
//2. send message like whatsapp, means:- sender message show right side, and receiver message show left side,
//3. set any background color in textarea, and change the message background color too.
//4. if client or server window is close, show message on another frame that 'another person is offline'
//5. (database)(hide ip address) get the ip, and username, and save in database, if client enter the username match with ip address.

import javazoom.jl.player.Player;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCode
{
    private JFrame server_frame;
    private  JTextArea text_area;
    private JScrollPane sp;
    private JTextField text_field;
    private ServerSocket server_socket;
    private InetAddress inet_address;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    ServerCode()
    {
        ServerCode();  //it will invoke the GUI part
        waitingForClient();     //it will wait for the client
        setIoStreams();     // it will set the streams through which we will transfer the data.
    }

    //------------------ Thread created---------------------------------
    Thread thread = new Thread()
    {
        @Override
        public void run()
        {
            while (true)
            {
                readMessage();
            }
        }
    };
    //-----------------------------------------------------------------

    private void ServerCode()
    {
        server_frame = new JFrame("Server");
        server_frame.setSize(500,500);
        server_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server_frame.setLocationRelativeTo(null);

        Font font = new Font("Arial",1,16);
        text_area = new JTextArea();
        text_area.setEditable(false);
        text_area.setLineWrap(true);
        text_area.setFont(font);
        sp = new JScrollPane(text_area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        server_frame.add(sp);

        text_field = new JTextField();
        server_frame.add(text_field, BorderLayout.SOUTH);
        text_field.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(text_field.getText());
                text_area.append("You :- "+text_field.getText()+"\n");
                text_field.setText(null);
            }
        });
        text_field.setEditable(false);

        server_frame.setVisible(true);
    }

    private void waitingForClient()
    {
        try
        {
            String ip_address = getIpAddress();
            server_socket = new ServerSocket(2005);
            text_area.setText("To connect with server, please provide IP Address : "+ip_address);
            socket = server_socket.accept();
            text_area.setText("client connected\n");
            text_area.append("--------------------------------------------------------------------------------------------------------------------\n");

            text_field.setEditable(true);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    private String getIpAddress()
    {
        String ip_address = "";
        try
        {
            inet_address = InetAddress.getLocalHost();
            ip_address = inet_address.getHostAddress();

        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

        return ip_address;
    }
    private void setIoStreams()
    {
        try
        {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        thread.start();
    }
    public void sendMessage(String message)
    {
        try
        {
            dos.writeUTF(message);
            dos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void readMessage()
    {
        try
        {
            String message = dis.readUTF();
            showMessage("Client :-  "+message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showMessage(String str)
    {
        text_area.append(str+"\n");
        chatReviveSound();
    }

    //------------------------------------this sound part is for receiving------------------------------------
    public void chatReviveSound()
    {
        try
        {
            String soundName = "src/project5/chatapplication/chat_sound.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //-----------------------------------------------------------------------------------------------

    //------------------------------------this sound part is for sending------------------------------------
    public void chatSendSound()
    {
        try
        {
            String file_name = "";
            FileInputStream fis = new FileInputStream("C:\\Users\\Mohit\\IdeaProjects\\Core-Java-Projects\\src\\project5\\chatapplication\\server\\send.mp3");
            Player p = new Player(fis);
            p.play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //-----------------------------------------------------------------------------------------------
}
