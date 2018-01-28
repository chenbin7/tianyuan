package cn.tianyuan.common.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/8/31.
 */

public class AnimationUtils {

    public static void performHeightAnimate(final View target, final int fromHeight, final int toHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer)animator.getAnimatedValue();

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //这里我偷懒了，不过有现成的干吗不用呢
                //直接调用整型估值器通过比例计算出宽度，然后再设给Button
                target.getLayoutParams().height = mEvaluator.evaluate(fraction, fromHeight, toHeight);
                target.requestLayout();
            }
        });
        valueAnimator.setDuration(duration).start();
    }

    public static void performWidthAnimate(final View target, final int fromWidth, final int toWidth, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer)animator.getAnimatedValue();

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //这里我偷懒了，不过有现成的干吗不用呢
                //直接调用整型估值器通过比例计算出宽度，然后再设给Button
                target.getLayoutParams().width = mEvaluator.evaluate(fraction, fromWidth, toWidth);
                target.requestLayout();
            }
        });
        valueAnimator.setDuration(duration).start();
    }

    public static void doFadeAnimation(ImageView img, int repalceId){
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    Animator animator = ObjectAnimator.ofFloat(img, "alpha", 1.0f, 0f).setDuration(200);
                    animator.start();
                })
                .delay(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    img.setImageResource(repalceId);
                })
                .doOnNext(i -> {
                    Animator animator = ObjectAnimator.ofFloat(img, "alpha", 0f, 1.0f).setDuration(150);
                    animator.start();
                })
                .subscribe();
    }

    public static void doFadeAnimation(View out, View in){
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    out.setVisibility(View.VISIBLE);
                    in.setVisibility(View.GONE);
                })
                .doOnNext(i -> {
                    Animator animator = ObjectAnimator.ofFloat(out, "alpha", 1.0f, 0f).setDuration(200);
                    animator.start();
                })
                .delay(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    out.setVisibility(View.GONE);
                    in.setVisibility(View.VISIBLE);
                })
                .doOnNext(i -> {
                    Animator animator = ObjectAnimator.ofFloat(in, "alpha", 0f, 1.0f).setDuration(150);
                    animator.start();
                })
                .subscribe();
    }

    public static void doRotateAnimation(boolean rotationX, View out, View in){
        String ratation = rotationX ? "rotationX":"rotationY";
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    Animator animator = ObjectAnimator.ofFloat(out, ratation, 0, 90).setDuration(250);
                    animator.start();
                })
                .delay(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    out.setVisibility(View.GONE);
                    in.setVisibility(View.VISIBLE);
                })
                .doOnNext(i -> {
                    Animator animator = ObjectAnimator.ofFloat(in, ratation, 270, 360).setDuration(250);
                    animator.start();
                })
                .subscribe();
    }

    public static void doRotationViews(View... views){
        AnimatorSet set = new AnimatorSet();
        AnimatorSet.Builder builder = null;
        for (int i = 0; i < views.length; i++) {
            views[i].setRotationX(90);
            Animator anim = ObjectAnimator.ofFloat(views[i], "rotationX", 90, 0).setDuration(500);
            anim.setStartDelay(i*100);
            if(builder == null){
               builder = set.play(anim);
            } else {
                builder.with(anim);
            }
        }
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    public static void doFlyInAnimationX(View out, View in, int parentWidth, boolean fromRight){
        Observable.just(fromRight)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(right -> {
                    out.setVisibility(View.VISIBLE);
                    in.setVisibility(View.VISIBLE);
                    float outTo = -parentWidth;
                    float inFrom = parentWidth;
                    if(!right){
                        outTo = parentWidth;
                        inFrom = -parentWidth;
                    }
                    Animator anim1 = ObjectAnimator.ofFloat(out, "translationX", 0, outTo).setDuration(500);
                    Animator anim2 = ObjectAnimator.ofFloat(in, "translationX", inFrom, 0).setDuration(500);
                    AnimatorSet set = new AnimatorSet();
                    set.play(anim1).with(anim2);
                    set.start();
                })
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> {
                    out.setVisibility(View.GONE);
                    in.setVisibility(View.VISIBLE);
                })
                .subscribe();
    }

}
