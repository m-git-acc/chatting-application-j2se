package chatapplication.client;

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
import java.net.Socket;

public class ClientCode
{
    private JFrame client_frame;
    private  JTextArea text_area;
    private JScrollPane sp;
    private JTextField text_field;
    private Socket socket;
    private String get_ip;
    private DataInputStream dis;
    private DataOutputStream dos;

    ClientCode()
    {
        ipJoption();
        setIoStreams();
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

    private void ipJoption()
    {
        get_ip = JOptionPane.showInputDialog("Enter IP Address");
        if (get_ip!=null)
        {
            if (!get_ip.equals(""))
            {
                ClientCode();
                connectToServer();
            }
            else
            {
                JOptionPane.showMessageDialog(client_frame, "please enter IP address...");
                ipJoption();
            }
        }
        else
        {
            System.exit(0);
        }
    }
    private void ClientCode()
    {
        client_frame = new JFrame("Client Frame");
        client_frame.setSize(500,500);
        client_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client_frame.setLocationRelativeTo(null);

        Font font = new Font("Arial",1,16);
        text_area = new JTextArea();
        text_area.setEditable(false);
        text_area.setLineWrap(true);
        text_area.setFont(font);
        sp = new JScrollPane(text_area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        client_frame.add(sp);

        text_field = new JTextField();
        client_frame.add(text_field, BorderLayout.SOUTH);
        text_field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(text_field.getText());
                text_area.append("You :- "+text_field.getText()+"\n");
                text_field.setText("");
            }
        });

        client_frame.setVisible(true);
    }

    private void connectToServer()
    {
        try
        {
            socket = new Socket(get_ip,2005);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
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
            showMessage("Server :-  "+message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showMessage(String str)
    {
        text_area.append(str+"\n");
//        chatReviveSound();
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
