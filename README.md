## CircleTimePicker
这是一款仿IOS10(就寝功能)圆盘时间选择器的自定义控件


## 项目演示
<p align="center">
	<img src = "/gifs/CircleTimePicker_01.gif" height="300">
</p>


## 简单用例

1.在 buld.gradle 中添加依赖
```groovy
dependencies {
    compile 'com.kyesun.ly:CircleTimePicker:1.0.0'
}
```
2.在XML布局文件中添加 CirclePicker

```xml
    <com.kyesun.ly.CircleTimePicker.widget.CirclePicker
        android:id="@+id/timer"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

3.在 Activity 或者 Fragment 中添加代码

```java
    // 0到720度对应0到24小时
     mTimer.setOnTimerChangeListener(new OnCirclePickerTimeChangedListener() {
            @Override
            public void startTimeChanged(float startDegree, float endDegree) {
                // 设置开始时间的回调
            }

            @Override
            public void endTimeChanged(float startDegree, float endDegree) {
                 // 设置结束时间的回调
            }

            @Override
            public void onTimeInitail(float startDegree, float endDegree) {
                   // 初始化开始,结束时间的回调

            }

            @Override
            public void onAllTimeChanaged(float startDegree, float endDegree) {
                    // 滑动选中时间段的回调
            }
        });
```

## 自定义属性

XML Attribute | description
------------ | -------------
Degree_Cycle | 一个循环的周期
Start_Degree | 开始时间对应的角度
End_Degree | 结束时间对应的角度
Start_Btn_Bg | 开始时间设置按钮的背景图
End_Btn_Bg | 结束时间设置按钮的背景图
Clock_Bg | 中间时钟的背景图
Ring_Default_Color | 外层圆环的默认背景
Start_Btn_Color | 开始时间设置按钮的背景颜色
End_Btn_Color |  结束时间设置按钮的背景颜色
Btn_Width | 开始,结束设置时间按钮的直径
Btn_Offset_Size | 开始,结束时间按钮背景图与背景颜色的间隔

