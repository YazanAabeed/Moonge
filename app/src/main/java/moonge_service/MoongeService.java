package moonge_service;

import android.app.Application;

import com.parse.Parse;

public class MoongeService extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }
}
