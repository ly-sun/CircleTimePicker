package com.kyesun.ly.CircleTimePicker.callback;

/**
 * @version V1.0
 * @Author ly
 * @Description
 * @Date 2017/12/8
 */
public interface OnTimeChangeListener {
    /**
     * 开始时间发生变化
     * @param startDegree
     */
    void startTimeChanged(float startDegree, float endDegree);

    /**
     * 结束时间发生变化
     */
    void endTimeChanged(float startDegree, float endDegree);

    /**
     * 初始化开始时间和结束时间
     * @param startDegree
     * @param endDegree
     */
    void onTimeInitail(float startDegree, float endDegree);


    /**
     * 开始,结束时间都发生变化
     * @param startDegree
     * @param endDegree
     */
    void onAllTimeChanaged(float startDegree, float endDegree);
}
