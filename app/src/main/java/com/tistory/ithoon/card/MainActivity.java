package com.tistory.ithoon.card;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tistory.ithoon.card.resource.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String LOG_TAG = "card";

    private int cardMarginDp = 15;

    // card 선언
    private FrameLayout mainCard;
    private FrameLayout subCard1;
    private FrameLayout subCard2;
    private FrameLayout subCard3;
    private FrameLayout subCard4;

    // 애니메이션이 적용될 카드뷰 리스트
    private ArrayList<View> animationViews;

    // 메인 컨테이너 레이아웃
    private RelativeLayout containerLayout;

    // 메인 백그라운드 변경될 이미지뷰
    private ImageView mainBackgroundView;

    // 중복 클릭을 방지 하기 위해 사용합니다.
    boolean isEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        containerLayout = findViewById(R.id.container);
        // 메인 카드 레이아웃
//        final RelativeLayout relativeLayout = findViewById(R.id.card_layout);
        mainCard = findViewById(R.id.main_card_frame);

        // 메인 백 그라운드
        mainBackgroundView = findViewById(R.id.top_card_background);
        // top card background 의 이미지 3/1 만큼 사이즈를 기준으로 작업
        mainBackgroundView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int topViewHeight = mainBackgroundView.getMeasuredHeight() - mainBackgroundView.getMeasuredHeight() / 3;

        // top card의 layout params
        RelativeLayout.LayoutParams marginLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, topViewHeight);

        marginLayoutParams.topMargin = topViewHeight;
        marginLayoutParams.leftMargin = (int) ResUtil.convertDpToPixel(cardMarginDp, this);
        marginLayoutParams.rightMargin = (int) ResUtil.convertDpToPixel(cardMarginDp, this);

        mainCard.setLayoutParams(marginLayoutParams);
        mainCard.setBackgroundResource(R.drawable.bg_card);

        subCard1 = findViewById(R.id.card1_frame);
        subCard2 = findViewById(R.id.card2_frame);
        subCard3 = findViewById(R.id.card3_frame);
        subCard4 = findViewById(R.id.card4_frame);

        mainCard.setOnClickListener(this);
        subCard1.setOnClickListener(this);
        subCard2.setOnClickListener(this);
        subCard3.setOnClickListener(this);
        subCard4.setOnClickListener(this);

        showLeftInAnimation();

        findViewById(R.id.animation_reset_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetViews();
            }
        });
    }

    @Override
    public void onClick(View view) {

        if(isEnable) return;

        if (view instanceof FrameLayout) {
            showCardAnimation((FrameLayout) view);
        }
    }

    /**
     * reset all view
     * Alpha or Animation reset
     * */
    private void resetViews(){

        if(!isEnable) return;

        isEnable = false;

        mainCard.setAlpha(1);
        mainCard.clearAnimation();
        for (int i = 0; i < animationViews.size(); i++) {
            animationViews.get(i).setAlpha(1);
            animationViews.get(i).clearAnimation();
        }

        showLeftInAnimation();
    }


    /**
     * 카드가 우측에서 좌측으로 나오는 애니메이션
     */
    private void showLeftInAnimation() {
        mainCard.setAlpha(1f);
        mainCard.bringToFront();

        animationViews = new ArrayList<>();

        animationViews.add(subCard1);
        animationViews.add(subCard2);
        animationViews.add(subCard3);
        animationViews.add(subCard4);

        Animation first = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_in);
        first.setDuration(500);
        first.setStartOffset(200);
        mainCard.setAnimation(first);

        Animation second = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_in);
        second.setDuration(500);
        second.setStartOffset(400);
        subCard1.setAnimation(second);
        subCard2.setAnimation(second);

        Animation third = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_in);
        third.setDuration(500);
        third.setStartOffset(600);
        subCard3.setAnimation(third);
        subCard4.setAnimation(third);

        first.start();
        second.start();
        third.start();
    }

    private void showRightOutAnimation() {

        Animation first = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_out);
        first.setDuration(500);
        first.setStartOffset(0);
        mainCard.startAnimation(first);

        Animation second = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_out);
        second.setDuration(500);
        second.setStartOffset(200);
        subCard1.startAnimation(second);
        subCard2.startAnimation(second);

        Animation third = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_out);
        third.setDuration(500);
        third.setStartOffset(400);
        subCard3.startAnimation(third);
        subCard4.startAnimation(third);
    }

    private void showCardAnimation(FrameLayout v) {

        isEnable = true;

        List<Animator> animatorList = new ArrayList<>();

        final ImageView origin = new ImageView(this);
        final ImageView changed = new ImageView(this);

        origin.setX(v.getX());
        origin.setY(v.getY());

        containerLayout.addView(origin);
        containerLayout.addView(changed);

        v.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        v.destroyDrawingCache();

        origin.setImageBitmap(bitmap);
        origin.requestLayout();

        // get the center for the clipping circle
        final int cy = v.getTop();
        int cx = v.getLeft();

        switch (v.getId()) {
            case R.id.main_card_frame:
                changed.setBackgroundResource(R.drawable.bg_top_01);
                break;
            case R.id.card1_frame:
                changed.setBackgroundResource(R.drawable.bg_top_02);
                break;
            case R.id.card2_frame:
                changed.setBackgroundResource(R.drawable.bg_top_03);
                break;
            case R.id.card3_frame:
                changed.setBackgroundResource(R.drawable.bg_top_04);
                break;
            case R.id.card4_frame:
                changed.setBackgroundResource(R.drawable.bg_top_05);
                break;
            default:
                break;
        }

        int duration = 750;

        int minWidth = v.getMeasuredWidth(); //getResources().getDimensionPixelSize(R.dimen.width_474);
        int maxWidth = mainBackgroundView.getMeasuredWidth();

        int minHeight = v.getMeasuredHeight();//getResources().getDimensionPixelSize(R.dimen.height_332);
        int maxHeight = mainBackgroundView.getMeasuredHeight();

        AnimatorSet touchAnimatorSet = new AnimatorSet();
        // 백그라운드 뷰
        ObjectAnimator translateX = ObjectAnimator.ofFloat(origin, "translationX", cx, 0);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(origin, "translationY", cy, 0);
        ObjectAnimator alphaOrigin = ObjectAnimator.ofFloat(origin, "alpha", 1f, 0f);
        ObjectAnimator alphaChanged = ObjectAnimator.ofFloat(changed, "alpha", 0f, 1f);
        ObjectAnimator alphaView = ObjectAnimator.ofFloat(v, "alpha", 0f, 0f);
        ValueAnimator width = ObjectAnimator.ofInt(minWidth, maxWidth);
        ValueAnimator height = ObjectAnimator.ofInt(minHeight, maxHeight);

        touchAnimatorSet.setDuration(duration);

        width.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) origin.getLayoutParams();
                layoutParams.width = value;
                origin.requestLayout();

            }
        });

        height.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) origin.getLayoutParams();
                layoutParams.height = value;
                origin.requestLayout();
            }
        });

        touchAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

//                mainBackgroundView.setVisibility(View.VISIBLE);
//                mainBackgroundView.bringToFront();
//                mainBackgroundView.invalidate();

                mainCard.bringToFront();
                mainCard.invalidate();

                changed.bringToFront();
                changed.invalidate();

                origin.bringToFront();
                origin.invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

//                Intent intent = MyPageActivity.getMyPageIntent(SubMainMyPageActivity.this, myPageStartOption);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivityForResult(intent, REQUEST_CODE_MY_PAGE);

                changed.setVisibility(View.GONE);
                mainBackgroundView.setImageDrawable(changed.getBackground());

                containerLayout.removeView(origin);
                containerLayout.removeView(changed);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if (v.getId() != R.id.main_card_frame) {
            Animation animationCard = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_out);
            animationCard.setDuration(duration);
            mainCard.startAnimation(animationCard);
        }

        for (int i = 0; i < animationViews.size(); i++) {

            // 터치한 뷰 가 아니면 애니메이션을 추가합니다.
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_push_left_out);

            switch (animationViews.get(i).getId()) {
                case R.id.card1_frame:
                case R.id.card2_frame:
                    animation.setDuration(duration);
                    animation.setStartOffset(200);
                    break;
                case R.id.card3_frame:
                case R.id.card4_frame:
                    animation.setDuration(duration);
                    animation.setStartOffset(400);
                    break;
            }

            animationViews.get(i).startAnimation(animation);
        }

        animatorList.add(translateX);
        animatorList.add(translateY);
        animatorList.add(width);
        animatorList.add(height);
        animatorList.add(alphaOrigin);
        animatorList.add(alphaChanged);
        animatorList.add(alphaView);

        touchAnimatorSet.playTogether(animatorList);
        touchAnimatorSet.start();

    }
}
