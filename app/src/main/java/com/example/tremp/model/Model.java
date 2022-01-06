package com.example.tremp.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tremp.MyApplication;

import java.util.List;

public class Model {

    public final static Model instance = new Model();//singleton

    ModelFirebase modelFirebase = new ModelFirebase();

    private Model(){
        reloadUsersList();
    }

    public interface GetAllUsersListener{
        void onComplete(List<User> data);
    }

    MutableLiveData<List<User>> usersListLd = new MutableLiveData<List<User>>();
    private void reloadUsersList() {
        //1. get local last update
        Long localLastUpdate = User.getLocalLastUpdated();
        Log.d("TAG","localLastUpdate: " + localLastUpdate);
        //2. get all students record since local last update from firebase
        modelFirebase.getAllUsers(localLastUpdate,(list)->{
            MyApplication.executorService.execute(()->{
                //3. update local last update date
                //4. add new records to the local db
                Long lLastUpdate = new Long(0);
                Log.d("TAG", "FB returned " + list.size());
                for(User u : list){
                    AppLocalDB.db.userDao().insertAll(u);
                    if (u.getLastUpdated() > lLastUpdate){
                        lLastUpdate = u.getLastUpdated();
                    }
                }
                User.setLocalLastUpdated(lLastUpdate);

                //5. return all records to the caller
                List<User> usersList = AppLocalDB.db.userDao().getAll();
                usersListLd.postValue(usersList);
            });
        });
    }

    public LiveData<List<User>> getAll(){
        return usersListLd;
    }

    public interface AddUserListener{
        void onComplete();
    }
    public void addUserToFirebaseDB(User user, AddUserListener listener) {
        modelFirebase.addUserToFirebaseDB(user, () -> {
            reloadUsersList();
            listener.onComplete();
        });
    }

    public interface GetUserByEmailListener{
        void onComplete(User user);
    }
    public void getUserByEmail(String userEmail,GetUserByEmailListener listener) {
        modelFirebase.getUserByEmail(userEmail, listener);
    }
}
