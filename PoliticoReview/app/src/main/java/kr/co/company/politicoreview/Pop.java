package kr.co.company.politicoreview;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by SM on 2018-02-02. //스플래쉬 화면 위해
 */

public class Pop extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop); //이거 띄울 거임

        //띄우는 화면이 전체 화면 만큼 클 필요 없음 - 팝업창이니 크기 조절
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); //초기 세팅

        int width = dm.widthPixels;  //값 가져옴
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int)(height * 0.85)); // 90만큼의 너비 , 85만큼

    }
}
