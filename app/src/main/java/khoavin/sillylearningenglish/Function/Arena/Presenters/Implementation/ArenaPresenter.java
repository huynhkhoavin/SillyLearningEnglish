package khoavin.sillylearningenglish.Function.Arena.Presenters.Implementation;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import khoavin.sillylearningenglish.Function.Arena.Presenters.IArenaPresenter;
import khoavin.sillylearningenglish.Function.Arena.Views.IArenaView;
import khoavin.sillylearningenglish.Depdency.SillyApp;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IPlayerService;
import khoavin.sillylearningenglish.NetworkService.NetworkModels.User;
import khoavin.sillylearningenglish.NetworkService.Retrofit.IServerResponse;
import khoavin.sillylearningenglish.NetworkService.Retrofit.SillyError;
import khoavin.sillylearningenglish.SingleViewObject.Common;

/**
 * Created by OatOal on 2/18/2017.
 */

public class ArenaPresenter implements IArenaPresenter {

    //Tag name
    private static final String ARENA_TAG = "Anrena presenter: ";

    //The view
    private IArenaView arenaView;

    @Inject
    IPlayerService playerService;

    public ArenaPresenter(final  IArenaView arenaView)
    {
        this.arenaView = arenaView;

        //inject arena service
        ((SillyApp) ((AppCompatActivity) arenaView).getApplication())
                .getDependencyComponent()
                .inject(this);

        GetUserInformation();

    }

    private void GetUserInformation()
    {
        if(playerService != null)
        {
            //final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            playerService.GetuserInformation("b1d7dd8f11b32c9a0f66ea3c4416ca7f0aa02c80", new IServerResponse<User>() {
                @Override
                public void onSuccess(User user) {
                    //Find winrate
                    float winRate = 1.0f *user.getWinMatch() / user.getTotalMatch();
                    winRate *= 100;

                    arenaView.SetCoins(user.getCoin());
                    arenaView.SetBattleChain("00000");
                    arenaView.SetAvatar(user.getAvatarUrl());
                    arenaView.SetName(user.getName());
                    arenaView.SetLevel(Common.RankMedal.Sliver);
                    arenaView.SetTotalBattle(user.getTotalMatch());
                    arenaView.SetWinRate(String.format(java.util.Locale.US,"%.2f", winRate) + "%");
                }

                @Override
                public void onError(SillyError error) {
                    Log.e(ARENA_TAG, "Error Code: " + error.getErrorCode());
                    Log.e(ARENA_TAG, "Error Message: " + error.getMessage());
                }
            });
        }
    }

}
