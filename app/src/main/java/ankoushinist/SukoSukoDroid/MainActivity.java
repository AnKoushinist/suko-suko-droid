package ankoushinist.SukoSukoDroid;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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

        }
    }





    public static abstract class FuckingFragment extends Fragment{
        public <T extends View> T findViewById(@IdRes int id){
            return (T)getView().findViewById(id);
        }
    }
}
