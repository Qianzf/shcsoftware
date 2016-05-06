package info.qianzf.design;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Handler;



public class MainActivity extends AppCompatActivity {

    private EditText titleEditText = null;
    private EditText infoEditText = null;
    //设置一个调试用的显示控件
    private TextView tipsTextView = null;

    private Button submitButton = null;

    private ProgressDialog pd = null;

    private AlertDialog.Builder builder = null;

    private WifiManager wifiManager = null;
    private ConnectivityManager cm  = null;

    public static int status = 0;
    public static Handler handler = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleEditText = (EditText) findViewById(R.id.title_info);
        infoEditText = (EditText) findViewById(R.id.title2_info);
        submitButton = (Button) findViewById(R.id.submit);
        tipsTextView = (TextView) findViewById(R.id.Tips);

        builder = new AlertDialog.Builder(MainActivity.this);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        WifiInfo wf = wifiManager.getConnectionInfo();

        NetworkInfo net = cm.getActiveNetworkInfo();

        if (net == null||net.getType() != ConnectivityManager.TYPE_WIFI) {
            builder.setTitle("警告");
            builder.setMessage("当前未连接到无线网！请先连接到无线网再打开程序！");
            builder.setPositiveButton("退出程序", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    System.exit(0);
                }
            });
            builder.show();
        } else {

            String ssid = wf.getSSID();

            if (!ssid.equals("\"Qianzf\"")) {
                builder.setTitle("警告");
                builder.setMessage("当前的连接的无线网名称为"+ssid+"不是系统无线名称Qianzf,是否继续使用程序？");
                builder.setNeutralButton("继续使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("退出程序", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                });
                builder.show();
            }

            submitButton.setOnClickListener(new submitListener());


            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    pd.dismiss();
                    if (status < 0) {
                        tipNow("提示", "抱歉，提交信息失败，错误代码：" + status + "");
                    } else {
                        tipNow("提示", "操作已成功完成！");
                    }

                }
            };
        }
    }


    //写一个内部类实现OnClickListener接口
    class submitListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            String title = titleEditText.getText().toString();
            String info = infoEditText.getText().toString();

            if(title.equals("")||info.equals("")){

                tipNow("提示","智能提示牌标题或者内容不能为空！");

            }else{
                String total = "title:" + title + "^info:" + info + "^-";
                tipsTextView.setText(total);
                ServerService ss = new ServerService("192.168.4.1", 4848, total);

                new Thread(ss).start();
                pd = ProgressDialog.show(MainActivity.this, "提示", "请稍候……");

            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
/*        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://info.qianzf.design/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        */
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://info.qianzf.design/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, FullscreenActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //写一个提示信息显示的函数
    public void tipNow(String title,String info){
        builder.setTitle(title);
        builder.setMessage(info);
        builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("", null);
        builder.show();
    }
}
