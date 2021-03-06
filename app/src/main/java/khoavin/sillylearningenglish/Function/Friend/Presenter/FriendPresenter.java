package khoavin.sillylearningenglish.Function.Friend.Presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import khoavin.sillylearningenglish.Depdency.SillyApp;
import khoavin.sillylearningenglish.EventListener.SingleEvent.FriendActionListener;
import khoavin.sillylearningenglish.EventListener.SingleEvent.FriendEventListener;
import khoavin.sillylearningenglish.FirebaseObject.FirebaseAccount;
import khoavin.sillylearningenglish.Function.Arena.Views.Implementation.BattlePrepareActivity;
import khoavin.sillylearningenglish.Function.FindNewFriends.FindFriendDialog;
import khoavin.sillylearningenglish.Function.FindNewFriends.View.FindFriendViewHolder;
import khoavin.sillylearningenglish.Function.Friend.ChatObject.ManyChatRoom;
import khoavin.sillylearningenglish.Function.Friend.View.ChatDialog;
import khoavin.sillylearningenglish.Function.Friend.View.FriendView;
import khoavin.sillylearningenglish.Function.Social.SocialFragment.PostNotifyFragment;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IArenaService;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IAuthenticationService;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IChatService;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IFriendService;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IPlayerService;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IVolleyResponse;
import khoavin.sillylearningenglish.NetworkService.Interfaces.IVolleyService;
import khoavin.sillylearningenglish.NetworkService.NetworkModels.Enemy;
import khoavin.sillylearningenglish.NetworkService.NetworkModels.ErrorCode;
import khoavin.sillylearningenglish.Pattern.ConnectDialog;
import khoavin.sillylearningenglish.Pattern.NetworkAsyncTask;
import khoavin.sillylearningenglish.Pattern.ProgressAsyncTask;
import khoavin.sillylearningenglish.Pattern.YesNoDialog;
import khoavin.sillylearningenglish.SYSTEM.MessageEvent.MessageEvent;
import khoavin.sillylearningenglish.SYSTEM.Service.Constants;
import khoavin.sillylearningenglish.SYSTEM.Service.MessageListenerService;
import khoavin.sillylearningenglish.SingleViewObject.Common;
import khoavin.sillylearningenglish.SingleViewObject.Friend;

/**
 * Created by KhoaVin on 2/17/2017.
 */

public class FriendPresenter implements IFriendPresenter {

    private static final String TAG = "FriendPresenter";

    private Activity ControlActivity;

    private FriendView friendView;

    private FriendActionListener friendActionListener;

    private ChatDialog chatDialog;

    private BroadcastReceiver mReceiver;
    @Inject
    IFriendService friendService;

    @Inject
    IChatService chatService;

    @Inject
    IAuthenticationService authenticationService;

    @Inject
    IArenaService arenaService;

    @Inject
    IVolleyService volleyService;

    @Inject
    IPlayerService playerService;

    @Inject
    ManyChatRoom manyChatRoom;

    public FriendPresenter(Activity controlActivity) {
        this.ControlActivity = controlActivity;
        this.friendView = new FriendView(ControlActivity);
        ((SillyApp) (((AppCompatActivity) ControlActivity).getApplication())).getDependencyComponent().inject(this);
        manyChatRoom.SetContext(friendView.getActivity());
        chatDialog = new ChatDialog(controlActivity);
        ((SillyApp) (((AppCompatActivity) ControlActivity).getApplication())).getDependencyComponent().inject(chatDialog);
        EventBus.getDefault().register(this);

        friendView.setUpFriendView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFriendDialog findFriendDialog = new FindFriendDialog(ControlActivity, ControlActivity);
                findFriendDialog.show();
            }
        });
        friendView.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowListFriendFirst();
            }
        });
    }

    @Override
    public void DoFunction() {
        ShowListFriendFirst();
        ChatAction();
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void ShowListFriendFirst() {
        friendService.getListUserImmediately(new FriendEventListener() {
            @Override
            public void onListFriendsUid(ArrayList<String> listFriendsUid) {
                UpdateListFriend(listFriendsUid);
            }

            @Override
            public void onFindUser(FirebaseAccount userAccount) {

            }

            @Override
            public void onGetAllFriends(ArrayList<FirebaseAccount> listFriends) {
                friendView.ShowFriendFirst(listFriends);
                friendView.swipeRefreshLayout.setRefreshing(false);
                UpdateNotify();
            }
        }, ControlActivity);
    }

    public void UpdateListFriend(ArrayList<String> listFriendsUid) {
        friendService.getListUserRealtime(listFriendsUid, new FriendEventListener() {
            @Override
            public void onListFriendsUid(ArrayList<String> listFriendsUid) {

            }

            @Override
            public void onFindUser(FirebaseAccount userAccount) {

            }

            @Override
            public void onGetAllFriends(ArrayList<FirebaseAccount> listFriends) {
                friendView.UpdateListFriends(listFriends);
            }
        });
    }

    public void ChatAction() {
        friendActionListener = new FriendActionListener() {
            @Override
            public void ChatAction(int position, final Friend friend) {
                if (friendView.checkFriendHadSnooze(friend.getUid())) {
                    friendView.UpdateMessageNotify(friend.getUid(), false);
                }
                Log.e(TAG, "Chat: " + String.valueOf(position));
                chatDialog.setTitle(friend.getName());
                if (chatDialog.getCurrentChatter() == friend) {
                    chatDialog.show();
                    chatDialog.GetMessageFromUid(friend.getUid());
                } else {

                    chatDialog.Show(friend);
                    chatDialog.GetMessageFromUid(friend.getUid());
                }

            }

            @Override
            public void ChallengeAction(int position, Friend friend) {
                Log.e(TAG, "Challenge: " + String.valueOf(position));
                arenaService.FindEnemyFromIdentifier(playerService.GetCurrentUser().getUserId(), friend.getUid(), ControlActivity, volleyService, new IVolleyResponse<Enemy>() {
                    @Override
                    public void onSuccess(Enemy responseObj) {
                        arenaService.SetBattleCalledFrom(Common.BattleCalledFrom.FROM_FRIEND_LIST);
                        Intent it = new Intent(ControlActivity, BattlePrepareActivity.class);
                        ControlActivity.startActivity(it);
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        if (errorCode.getCode() == Common.ServiceCode.USER_NOT_FOUND) {
                            Common.ShowInformMessage(
                                    "User not found, error code: " + errorCode.getCode().toString(),
                                    "Alert",
                                    "OK",
                                    ControlActivity,
                                    null);
                        } else if (errorCode.getCode() == Common.ServiceCode.RESPONSE_NULL_OR_ZERO_SIZE) {
                            Common.ShowInformMessage(
                                    "Response null or size zero, error code: " + errorCode.getCode().toString(),
                                    "Alert",
                                    "OK",
                                    ControlActivity,
                                    null);
                        } else if (errorCode.getCode() == Common.ServiceCode.ENEMY_ID_EQUAL_TO_USER_ID) {
                            Common.ShowInformMessage(
                                    "Enemy's identifier equal to user's identifier, error code: " + errorCode.getCode().toString(),
                                    "Alert",
                                    "OK",
                                    ControlActivity,
                                    null);
                        } else if (errorCode.getCode() == Common.ServiceCode.ENEMY_ID_NOT_FOUND) {
                            Common.ShowInformMessage(
                                    "Enemy's identifier not found. error code: " + errorCode.getCode().toString(),
                                    "Alert",
                                    "OK",
                                    ControlActivity,
                                    null);
                        } else if (errorCode.getCode() == Common.ServiceCode.INTERNAL_SERVER_ERROR) {
                            Common.ShowInformMessage(
                                    "Internal server error, error code: " + errorCode.getCode().toString(),
                                    "Alert",
                                    "OK",
                                    ControlActivity.getBaseContext(),
                                    null);
                        } else if (errorCode.getCode() == Common.ServiceCode.JSON_PARSE_ERROR) {
                            Common.ShowInformMessage(
                                    "Json error, error code: " + errorCode.getCode().toString(),
                                    "Alert",
                                    "OK",
                                    ControlActivity,
                                    null);
                        } else {
                            Common.ShowInformMessage(
                                    "Unknow error, error code: " + errorCode.getCode().toString(),
                                    "Alert",
                                    "OK",
                                    ControlActivity,
                                    null);
                        }
                    }
                });
            }

            @Override
            public void GetInfoAction(int position, Friend friend) {
                Log.e(TAG, "Get Info: " + String.valueOf(position));
            }

            @Override
            public void UnFriend(int position, final Friend friend) {
                final YesNoDialog yesNoDialog = new YesNoDialog();
                yesNoDialog.show(((AppCompatActivity) ControlActivity).getSupportFragmentManager(), "yes no dialog");
                yesNoDialog.setMessage("Are you sure to unfriend this friend?");
                yesNoDialog.setOnPositiveListener(new ConnectDialog.Listener() {
                    @Override
                    public void onClick() {
                        friendService.unFriend(ControlActivity, authenticationService.getCurrentUser().getUid(), friend.getUid(), new IVolleyResponse<ErrorCode>() {
                            @Override
                            public void onSuccess(ErrorCode responseObj) {
                                Toast.makeText(ControlActivity, "Unfriend success!", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new MessageEvent(Constants.ACTION.UNFRIEND_SUCCESS));
                            }

                            @Override
                            public void onError(ErrorCode errorCode) {
                                Toast.makeText(ControlActivity, "Unfriend unsuccess. Something was wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        };
        friendView.setFriendListener(friendActionListener);
    }

    public void UpdateNotify() {
        Intent it = new Intent(friendView.getActivity(), MessageListenerService.class);
        friendView.getActivity().startService(it);
    }

    @Subscribe
    public void onEvent(HashMap<String, String> msg) {
        String talk_uid = msg.get("UID");
        if (msg.get("UID") != null) {
            //Update Notify trong giao dien

            if (chatDialog.isShowing()) {
                if (chatDialog.getCurrentChatter().getUid().equals(talk_uid)) {
                    return;
                }
            }
            friendView.UpdateMessageNotify(talk_uid, true);
        }
    }

    @Subscribe
    public void onEvent(MessageEvent messageEvent) {

    }
}