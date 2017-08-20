package ankoushinist.SukoSukoDroid;

import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String URL_LOGIN = "https://accounts.google.com/ServiceLogin";
    static final String URL_MOVIE = "https://www.youtube.com/watch?v={0}";
    static final String URL_CHANNEL = "https://www.youtube.com/channel/{0}/videos";
    static final String USER_AGENT = "https://www.youtube.com/channel/{0}/videos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content,new BeginFragment())
            .commit();
    }

    public static class BeginFragment extends FuckingFragment{
        TextView email,password,channel;
        Button start;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_main,container,false);
        }

        @Override
        public void onResume() {
            super.onResume();
            email=findViewById(R.id.email);
            password=findViewById(R.id.password);
            channel=findViewById(R.id.channelId);
            start=findViewById(R.id.start);

            start.setOnClickListener(a -> {
                SukoSukoWorkerFragment worker = new SukoSukoWorkerFragment();
                Bundle command = new Bundle();
                command.putString("email", email.getText().toString());
                command.putString("password", password.getText().toString());
                command.putString("channel", channel.getText().toString());
                worker.setArguments(command);
                getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, worker)
                    .commit();
            });
        }
    }
    public static class SukoSukoWorkerFragment extends FuckingFragment{
        WebView wv;
        LinearLayout logContainer;
        final Handler handler=new Handler();
        List<Runnable> tasks=new ArrayList<>();
        int running=0;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_sukosuko,container,false);
        }

        @Override
        public void onResume() {
            super.onResume();
            wv=findViewById(R.id.sukosukoDisplay);
            logContainer=findViewById(R.id.sukosukoTextViewContainer);
            handler.post(this::start);
            tasks.add(()->{
                createLogText("ログイン中",true);
                wv.loadUrl(URL_LOGIN);
            });
        }

        void start(){
            running=0;
            tasks.get(0).run();
            wv.getSettings().setUserAgentString("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0");
            wv.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    running++;
                    if(tasks.size()>running)
                        tasks.get(running).run();
                }
            });
        }

        TextView createLogText(CharSequence str,boolean append){
            TextView tv= (TextView) getActivity().getLayoutInflater().inflate(R.layout.line_sukosuko,logContainer,false);
            tv.setText(str);
            if(append)
                logContainer.addView(tv);
            return tv;
        }
    }





    public static abstract class FuckingFragment extends Fragment{
        public <T extends View> T findViewById(@IdRes int id){
            return (T)getView().findViewById(id);
        }
    }
}
