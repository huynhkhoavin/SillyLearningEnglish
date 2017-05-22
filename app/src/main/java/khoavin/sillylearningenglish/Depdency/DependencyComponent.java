package khoavin.sillylearningenglish.Depdency;

import javax.inject.Singleton;

import dagger.Component;
import khoavin.sillylearningenglish.Function.Arena.Presenters.Implementation.AnswerPresenter;
import khoavin.sillylearningenglish.Function.Arena.Presenters.Implementation.ArenaPresenter;
import khoavin.sillylearningenglish.Function.Arena.Presenters.Implementation.BattlePreparePresenter;
import khoavin.sillylearningenglish.Function.Arena.Presenters.Implementation.ResultPresenter;
import khoavin.sillylearningenglish.Function.Authentication.Login.LoginPresenter;
import khoavin.sillylearningenglish.Function.Authentication.LoginActivity;
import khoavin.sillylearningenglish.Function.Friend.Presenter.FriendPresenter;
import khoavin.sillylearningenglish.Function.Friend.View.ChatDialog;
import khoavin.sillylearningenglish.Function.MailBox.MailBoxDetail.Presenter.MailBoxDetailPresenter;
import khoavin.sillylearningenglish.Function.MailBox.MailBoxList.Presenter.MailBoxPresenter;
import khoavin.sillylearningenglish.Function.TrainingRoom.BookLibrary.Home.Presenter.TrainingPresenter;
import khoavin.sillylearningenglish.Function.TrainingRoom.LessonDetail.LessonInfo.LessonInfoFragment;
import khoavin.sillylearningenglish.Function.TrainingRoom.LessonDetail.View.LessonProgressFragment;
import khoavin.sillylearningenglish.NetworkService.Implementation.UserService;
import khoavin.sillylearningenglish.SYSTEM.Service.BackgroundMusicService;

@Singleton
@Component(modules = {DependencyModule.class})
public interface DependencyComponent {
    void inject(AnswerPresenter presenter);
    void inject(ArenaPresenter presenter);
    void inject(ResultPresenter presenter);
    void inject(BattlePreparePresenter presenter);
    void inject(MailBoxPresenter presenter);
    void inject(LoginActivity activity);
    void inject(MailBoxDetailPresenter presenter);


    void inject(TrainingPresenter presenter);
    void inject(FriendPresenter friendPresenter);
    void inject(UserService userService);
    void inject(ChatDialog chatDialog);
    void inject(LoginPresenter loginPresenter);
    void inject(LessonProgressFragment lessonProgressFragment);
    void inject(LessonInfoFragment lessonInfoActivity);
    void inject(BackgroundMusicService backgroundMusicService);
}
