package com.kyesun.ly.circletimepicker_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kyesun.ly.circle_time_picker.callback.OnCirclePickerTimeChangedListener;
import com.kyesun.ly.circle_time_picker.widget.CirclePicker;

public class MainActivity extends AppCompatActivity {

    private TextView mTvEndTime;
    private TextView mTvStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CirclePicker mTimer = (CirclePicker) findViewById(R.id.timer);
        mTvStartTime = (TextView) findViewById(R.id.start_time);
        mTvEndTime = (TextView) findViewById(R.id.end_time);
        mTimer.setOnTimerChangeListener(new OnCirclePickerTimeChangedListener() {
            @Override
            public void startTimeChanged(float startDegree, float endDegree) {
                float startCount = (startDegree / 720) * (24 * 60);
                int startHour = (int) Math.floor(startCount / 60);
                int startMinute = (int) Math.floor(startCount % 60);
                mTvStartTime.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMinute < 10) ? ("0" + startMinute) : (startMinute + "")));
            }

            @Override
            public void endTimeChanged(float startDegree, float endDegree) {
                double endCount = (endDegree / 720) * (24 * 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMinute = (int) Math.floor(endCount % 60);
                mTvEndTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
            }

            @Override
            public void onTimeInitail(float startDegree, float endDegree) {
                double startCount = (startDegree / 720) * (24 * 60);
                int startHour = (int) Math.floor(startCount / 60);
                int startMinute = (int) Math.floor(startCount % 60);
                mTvStartTime.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMinute < 10) ? ("0" + startMinute) : (startMinute + "")));


                double endCount = (endDegree / 720) * (24 * 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMinute = (int) Math.floor(endCount % 60);
                mTvEndTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
            }

            @Override
            public void onAllTimeChanaged(float startDegree, float endDegree) {
                double startCount = (startDegree / 720) * (24 * 60);
                int startHour = (int) Math.floor(startCount / 60);
                int startMinute = (int) Math.floor(startCount % 60);
                mTvStartTime.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMinute < 10) ? ("0" + startMinute) : (startMinute + "")));


                double endCount = (endDegree / 720) * (24 * 60);
//                int endMinute = (int) (endCount % 60);
//                int endHour = (int) (endCount / 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMinute = (int) Math.floor(endCount % 60);
                mTvEndTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
            }
        });
    }
}
