package info.qianzf.design;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 这是一个网络客户端的类，主要实现向目标ip和端口发送指定的短String类型数据
 * Created by Dobby on 2016/4/8.
 */
public class SocketService {

    private String sendInfo = "";
    //设置一个默认的ip地址和端口号
    private String ip = "";
    private int port = 0;

    Socket so = null;
    PrintWriter out = null;
    public SocketService(String ip,int port,String sendInfo){
        this.ip = ip;
        this.port = port;
        this.sendInfo = sendInfo;

    }

    public int submit(){
        try{
            so = new Socket(ip, port);
            OutputStreamWriter dsw = new OutputStreamWriter(so.getOutputStream(),"GB2312");
            out = new PrintWriter(dsw);
            out.println(sendInfo);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally{

            try {
                if(out!=null)
                    out.close();
                if(so!=null)
                    so.close();
            }catch (Exception e){
                e.printStackTrace();
                return -2;
            }
        }
        return 1;
    }
}
