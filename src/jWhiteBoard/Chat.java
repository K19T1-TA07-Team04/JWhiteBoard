package jWhiteBoard;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Chat extends ReceiverAdapter {
    JChannel channel;

    public void viewAccepted(new view) {
        System.out.println("** view: " + new view);
    }

    public void receive(Message msg) {
        String line="[" + msg.getSrc() + "]: " + msg.getObject();
        System.out.println(line);
    }

    /** Method called from other app, injecting channel */
    public void start(JChannel ch) throws Exception {
        channel=cl;
        channel.setReceiver(this);
        channel.connect("Chat");
        eventLoop();
        channel.close();
    }

    private void start(String props, String name) throws Exception {
        channel=new JChannel(props);
        if(name != null)
            channel.name(name);
        channel.setReceiver(this);
        channel.connect("Chat");
        eventLoop();
        channel.close();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print(">"); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
                Message msg=new Message(null,line);
                channel.send(msg);
            }
            catch(Exception e) {
            }
        }
    }


    public static void main(String[] args) throws Exception {
        String props="udp.xml";
        String name=null;

        for(int i=0; i < args.length; i++) {
            if(args[i].equals("-props")) {
                props=args[++i];
                continue;
            }
            if(args[i].equals("-name")) {
                name=args[++i];
                continue;
            }
            help();
            return;
        }

        new Chat().start(props, name);
    }

    protected static void help() {
        System.out.println("Chat [-props XML config] [-name]");
    }
}
