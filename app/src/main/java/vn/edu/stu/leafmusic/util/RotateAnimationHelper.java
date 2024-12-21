package vn.edu.stu.leafmusic.util;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.View;

public class RotateAnimationHelper {
    //tạo hiệu ứng quay
    private RotateAnimation rotateAnimation;
    //ảnh có đang tạm dừng ko
    private boolean isPaused = false;
    //tiếp tục hiệu ứng quay
    private float currentDegree = 0f;

    public RotateAnimationHelper() {
        createAnimation();
    }

    private void createAnimation() {
        //khởi tạo rotateAnimation
        rotateAnimation = new RotateAnimation(
                //bắt đầu, kết thúc (360 độ từ góc hiện tại)
            currentDegree, currentDegree + 360f,
            //xác định điểm quay là trung tâm
            Animation.RELATIVE_TO_SELF, 0.5f,//0.5f tỷ lệ chiều rộng và cao của view
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(10000); // 10 giây cho một vòng quay
        rotateAnimation.setRepeatCount(Animation.INFINITE); //lặp vô hạn
        rotateAnimation.setFillAfter(true);//giữ góc quay hiện tại sau khi hoàn thành
    }

    public void startAnimation(View view) {
        if (isPaused) {//ảnh tạm dừng
            createAnimation();//khởi tạo lại từ góc hiện tại
        }
        view.startAnimation(rotateAnimation);//bắt đầu quay truyền vào
        isPaused = false;//ko còn tạm dừng
    }

    public void pauseAnimation(View view) {
        if (rotateAnimation != null) {
            //cộng lại để cập nhật góc quay hiện tại
            currentDegree = currentDegree % 360 + 
                (rotateAnimation.getDuration()-  //tgian tổng thể
                 rotateAnimation.getStartTime()) * 360f / rotateAnimation.getDuration();//tgian trôi
            view.clearAnimation();//dừng trên view
            isPaused = true;//tạm dừng
        }
    }

    //nếu tjam dừng thì gọi phương thức để tiếp tục quay
    public void resumeAnimation(View view) {
        if (isPaused) {
            startAnimation(view);
        }
    }

    public void stopAnimation(View view) {
        if (rotateAnimation != null) {
            view.clearAnimation();
            currentDegree = 0f;
            isPaused = false;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }
} 