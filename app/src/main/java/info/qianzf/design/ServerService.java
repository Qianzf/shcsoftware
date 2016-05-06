package info.qianzf.design;

/**
 * Created by Dobby on 2016/4/8.
 */
public class ServerService implements Runnable {
    private String sendInfo = "";
    //设置一个默认的ip地址和端口号
    private String ip = "192.168.4.1";
    private int port = 4848;
    @Override
    public void run() {
        SocketService ss = new SocketService(ip,port,sendInfo);

        MainActivity.status = ss.submit();
        MainActivity.handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
    }

    public ServerService(String sendInfo){
        this.sendInfo = sendInfo;
    }

    public ServerService(String ip,int port,String sendInfo){

        this.ip = ip;
        this.port = port;
        this.sendInfo = sendInfo;
    }
}
