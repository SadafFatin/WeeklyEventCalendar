package co.jeeon.exam.eventcalender.models;

import android.net.Uri;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.google.firebase.auth.FirebaseUser;
import co.jeeon.exam.eventcalender.BR;

public class User extends BaseObservable {

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public boolean isIsloggedIn() {
        return isloggedIn;
    }

    public void setIsloggedIn(boolean isloggedIn) {
        this.isloggedIn = isloggedIn;
        notifyPropertyChanged(BR.isloggedIn);
    }

    private String name;
    private String email;
    private Uri image;
    private boolean isloggedIn;

    public User(FirebaseUser currentUser) {
        if (currentUser != null) {
            initCurrentUser(currentUser);
        } else {
            initNoUser();
        }
    }

    public User() {
    }

    private void initCurrentUser(FirebaseUser currentUser) {
        this.setIsloggedIn(true);
        this.setEmail((currentUser.getEmail()));
        this.setName((currentUser.getDisplayName()));
        this.setImage((currentUser.getPhotoUrl()));
    }

    private void initNoUser() {
        this.setIsloggedIn(false);
        this.setEmail(null);
        this.setName(null);
        this.setImage(null);
    }


    public void loggedOut() {
        initNoUser();
    }

    public void loggedIn(FirebaseUser currentUser) {
        if (currentUser != null) {
            initCurrentUser(currentUser);
        } else {
            initNoUser();
        }
    }


}
