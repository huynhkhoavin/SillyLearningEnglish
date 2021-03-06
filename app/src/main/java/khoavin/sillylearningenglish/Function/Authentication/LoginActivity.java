package khoavin.sillylearningenglish.Function.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import khoavin.sillylearningenglish.Depdency.SillyApp;
import khoavin.sillylearningenglish.Function.Authentication.Login.ILoginPresenter;
import khoavin.sillylearningenglish.Function.Authentication.Login.ILoginView;
import khoavin.sillylearningenglish.Function.Authentication.Login.LoginPresenter;
import khoavin.sillylearningenglish.Function.HomeMenu.HomeActivity;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IAuthenticationService;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IPlayerService;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IVolleyResponse;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IVolleyService;
import khoavin.sillylearningenglish.NetworkService.NetworkModels.ErrorCode;
import khoavin.sillylearningenglish.NetworkService.NetworkModels.User;
import khoavin.sillylearningenglish.NetworkService.Retrofit.IServerResponse;
import khoavin.sillylearningenglish.NetworkService.Retrofit.SillyError;
import khoavin.sillylearningenglish.Pattern.NetworkAsyncTask;
import khoavin.sillylearningenglish.Pattern.Transition.BaseDetailActivity;
import khoavin.sillylearningenglish.R;

import static khoavin.sillylearningenglish.SYSTEM.Constant.WebAddress.GET_POPULAR_LESSON;

public class LoginActivity extends BaseDetailActivity implements ILoginView {

    private ILoginPresenter loginPresenter;
    @BindView(R.id.btnHostChange)
    ImageView mHostChange;
    private Intent it;

    @Inject
    IPlayerService playerService;

    @Inject
    IAuthenticationService authenticationService;

    @Inject
    IVolleyService volleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //do not move to another line
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((SillyApp) (this).getApplication())
                .getDependencyComponent()
                .inject(this);


        loginPresenter = new LoginPresenter(this);
        loginPresenter.onCreate(this);

        mHostChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToHomeScreen();

            }
        });

        //inject arena service

        //endregion

    }

    /**
     * Move to home screen
     */
    private void MoveToHomeScreen()
    {
        FirebaseUser fUser =  authenticationService.getCurrentUser();
        if(fUser != null && playerService != null)
        {
            playerService.GetuserInformation(fUser.getUid(), fUser.getDisplayName(), fUser.getPhotoUrl().toString(), this, new IVolleyResponse<User>() {
                @Override
                public void onSuccess(User user) {
                    it = new Intent(LoginActivity.this,HomeActivity.class);
                    transitionTo(it);
                }

                @Override
                public void onError(ErrorCode error) {
                    int l = 0;
                }
            });
        }
    }
@Override
    public void onPause(){
    super.onPause();
        loginPresenter.DetachListener();
}
    @Override
    public void onResume(){
        super.onResume();
        loginPresenter.AttachListener();
    }


    @Override
    public void LoginSuccess() {
        it = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(it);
    }

    @Override
    public void LoginFail() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginPresenter.onActivityResult(requestCode,resultCode,data);
        MoveToHomeScreen();
    }
}