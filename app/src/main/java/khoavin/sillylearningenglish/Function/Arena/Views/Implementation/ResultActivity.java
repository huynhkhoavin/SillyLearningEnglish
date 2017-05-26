package khoavin.sillylearningenglish.Function.Arena.Views.Implementation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import khoavin.sillylearningenglish.Function.Arena.Presenters.Implementation.ResultPresenter;
import khoavin.sillylearningenglish.Function.Arena.Views.IResultView;
import khoavin.sillylearningenglish.Function.MailBox.MailBoxList.View.MailActivity;
import khoavin.sillylearningenglish.Function.Ranking.Views.RankingActivity;
import khoavin.sillylearningenglish.Function.UIView;
import khoavin.sillylearningenglish.R;
import khoavin.sillylearningenglish.SingleViewObject.Common;

/**
 * Created by OatOal on 2/18/2017.
 */

public class ResultActivity extends AppCompatActivity implements IResultView {

    private final String STATE_BACK_TO_ARENA = "GoHome";
    private final String STATE_BACK_INBOX = "Inbox";
    private final String STATE_FIND_OTHER_BATTLE = "FindMore";
    private final String STATE_BACK_TO_RANKING = "Ranking";

    @BindView(R.id.question_title)
    TextView questionTitle;

    @BindView(R.id.question)
    TextView questionContent;

    @BindView(R.id.answer_a)
    TextView answerA;

    @BindView(R.id.answer_b)
    TextView answerB;

    @BindView(R.id.icon_playing)
    ImageView hearImage;

    @BindView(R.id.total_time)
    TextView totalTime;

    @BindView(R.id.find_battle_button)
    Button findBattleButton;

    @BindView(R.id.go_arena_button)
    Button goArenaButton;

    @BindView(R.id.back_to_inbox)
    Button backToInboxButton;

    @BindView(R.id.back_to_ranking)
    Button backToRankingButton;

    ImageView[] answerButtons;

    /**
     * The button state manager.
     */
    UIView buttonState;
    Common.BattleCalledFrom battleCalledFrom = Common.BattleCalledFrom.NOT_FOUND;

    private int defaultColor;
    private int trueAnswerColor;
    Drawable trueButtonId;
    Drawable falseButtonId;


    //The presenter
    ResultPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_result);
        ButterKnife.bind(this);
        setTitle(R.string.result_view_title);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        buttonState = new UIView();
        buttonState.RegistryState(STATE_BACK_INBOX, backToInboxButton);
        buttonState.RegistryState(STATE_FIND_OTHER_BATTLE, findBattleButton);
        buttonState.RegistryState(STATE_BACK_TO_ARENA, goArenaButton);
        buttonState.RegistryState(STATE_BACK_TO_RANKING, backToRankingButton);

        Initialize();
    }

    @Override
    public void onBackPressed() {
        switch (battleCalledFrom)
        {
            case FROM_ARENA:
                //Do something to go home
                Intent goHomeIntent = new Intent(ResultActivity.this, ArenaActivity.class);
                goHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goHomeIntent);
                break;
            case FROM_INBOX:
                Intent toMailIntent = new Intent(ResultActivity.this, MailActivity.class);
                toMailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toMailIntent);
                break;
            case FROM_RANKING:
                Intent intent = new Intent(ResultActivity.this, RankingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                Intent toPrepareIntent = new Intent(ResultActivity.this, BattlePrepareActivity.class);
                toPrepareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toPrepareIntent);
        }
    }

    private void Initialize() {
        //Get drawable and color
        this.trueAnswerColor = getResources().getColor(R.color.Green);
        this.defaultColor = getResources().getColor(R.color.BlackText);
        this.trueButtonId = getResources().getDrawable(R.drawable.true_icon);
        this.falseButtonId = getResources().getDrawable(R.drawable.false_icon);

        answerButtons = new ImageView[5];
        this.answerButtons[0] = (ImageView) findViewById(R.id.your_answer_1);
        this.answerButtons[1] = (ImageView) findViewById(R.id.your_answer_2);
        this.answerButtons[2] = (ImageView) findViewById(R.id.your_answer_3);
        this.answerButtons[3] = (ImageView) findViewById(R.id.your_answer_4);
        this.answerButtons[4] = (ImageView) findViewById(R.id.your_answer_5);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 5; i++) {
                    if (v == answerButtons[i]) {
                        presenter.ShowQuestionWithIndex(i);
                    }
                }

            }
        };

        for (int i = 0; i < 5; i++) {
            this.answerButtons[i].setOnClickListener(onClickListener);
        }

        findBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Find other battle
                Intent intent = new Intent(ResultActivity.this, BattlePrepareActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        goArenaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something to go home
                Intent intent = new Intent(ResultActivity.this, ArenaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        backToInboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        backToRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, RankingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        presenter = new ResultPresenter(this);
    }

    // region Implementation
    @Override
    public void HeightLineTrueAnswer(Common.AnswerKey answerKey) {
        switch (answerKey) {
            case A:
                this.answerA.setTextColor(trueAnswerColor);
                this.answerB.setTextColor(defaultColor);
                break;
            case B:
                this.answerA.setTextColor(defaultColor);
                this.answerB.setTextColor(trueAnswerColor);
                break;
        }
    }

    @Override
    public void HideOrShowHearIcon(boolean show) {
        if (show)
            this.hearImage.setVisibility(View.VISIBLE);
        else
            this.hearImage.setVisibility(View.GONE);
    }

    @Override
    public void SetAnswerA(String answerA) {
        this.answerA.setText(answerA);
    }

    @Override
    public void SetAnswerB(String answerB) {
        this.answerB.setText(answerB);
    }

    @Override
    public void SetQuestionContent(String questionContent) {
        this.questionContent.setText(questionContent);
    }

    @Override
    public void SetQuestionTitle(String questionTitle) {
        this.questionTitle.setText(questionTitle);
    }

    @Override
    public void SetAnswerArrayButton(boolean[] answerState) {
        if (answerState.length != 5) return;
        for (int i = 0; i < answerState.length; i++) {
            if (answerState[i]) {
                answerButtons[i].setBackground(trueButtonId);
            } else {
                answerButtons[i].setBackground(falseButtonId);
            }
        }
    }

    @Override
    public void SetTotalTimes(long millisecond) {
        this.totalTime.setText(Common.GetSillyDateFormat().MillisecondToString(millisecond));
    }

    @Override
    public void SetButtonState(Common.BattleCalledFrom calledFrom) {
        battleCalledFrom = calledFrom;
        buttonState.DeactiveAllcontrol();
        switch (calledFrom) {
            case FROM_ARENA:
                buttonState.ActiveControl(STATE_BACK_TO_ARENA);
                buttonState.ActiveControl(STATE_FIND_OTHER_BATTLE);
                break;
            case FROM_INBOX:
                buttonState.ActiveControl(STATE_BACK_INBOX);
                break;
            case FROM_RANKING:
                buttonState.ActiveControl(STATE_BACK_TO_RANKING);
                break;
            default:
                buttonState.ActiveControl(STATE_FIND_OTHER_BATTLE);
                break;
        }
    }

    // endregion
}