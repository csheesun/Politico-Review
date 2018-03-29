package kr.co.company.politicoreview;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SM on 2018-01-19. - 로그인 후 메인화면
 */

public class MainActivity extends AppCompatActivity {

        private ListView noticeListView;
        private NoticeListAdapter adapter;
        private List<Notice> noticeList;
        public static String userID;  //12강에서 추가 => 모든 클래스에서 접근 가능하도록

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
                                //setContentView(R.layout.politico); //추가 해봄
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //12강 - 스마트폰을 특정 방향으로 고정 - 세로로 고정

            userID = getIntent().getStringExtra("userID");  //12 강에서 추가 - 인텐트로 받은거

            noticeListView = (ListView)findViewById(R.id.noticeListView);
            noticeList = new ArrayList<Notice>();  //7강에서 예시 데이터 삭제

            //noticeList.add(new Notice("공지사항입니다.", "생활정치", "2017-01-01")); //- 이건 보임

            adapter = new NoticeListAdapter(getApplicationContext(), noticeList);
            noticeListView.setAdapter(adapter);  //원래 여기 있었는데 onpostexeute 쪽에 집어넣기 => 안됨


            final Button politicoButton = (Button)findViewById(R.id.politicoButton);  //수정 => 아래도 다 수정함
            final Button myVoteButton = (Button)findViewById(R.id.myVoteButton); //수정
            final Button statisticsButton = (Button)findViewById(R.id.statisticsButton);
            final LinearLayout notice = (LinearLayout)findViewById(R.id.notice);  //해당 프래그 먼트 눌렀을때 화면 바뀜

            politicoButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view){
                    notice.setVisibility(View.GONE);
                    politicoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    myVoteButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, new PoliticoFragment()); //대체!
                    fragmentTransaction.commit();
                }
            });

            myVoteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view){
                    notice.setVisibility(View.GONE);
                    politicoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    myVoteButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, new MyVoteFragment()); //대체!
                    fragmentTransaction.commit();
                }
            });

            statisticsButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    notice.setVisibility(View.GONE);
                    politicoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    myVoteButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, new StatisticsFragment()); //대체!
                    fragmentTransaction.commit();
                }
            });
      new BackgroundTask().execute();
        }

        class BackgroundTask extends AsyncTask<Void, Void, String> { //php 만든 후 db 접속하도록
            String target; // 접속한 홈페이지 주소 들어감

            @Override
            protected void onPreExecute(){
               target = "http://politicoreview.dothome.co.kr/NoticeList.php";
            }

            @Override
            protected String doInBackground(Void... voids){
                try{  //실질적으로 데이터 가져오는 부분
                    URL url = new URL(target);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream(); //받아온거 저장
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //인풋 스트림에 있는 내용을 버퍼에 저장해서 읽을 수 있도록
                    String temp;
                    StringBuilder stringBuilder = new StringBuilder(); //temp 에 하나씩 읽어와서 문자열 형태로 저장하도록
                    while((temp = bufferedReader.readLine()) != null) {  //버퍼에서 받아온 값을 한줄씩 읽으면서 temp에 넣는다.
                        stringBuilder.append(temp +"\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim(); //다들어간 문자열이 반환 된다.
                }catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onProgressUpdate(Void... values) {
                super.onProgressUpdate();
            }

            @Override
            public void onPostExecute(String result){  //결과 처리
                try {  //응답 부분 처리
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열 받아오기 - response 에 각각의 공지사항 담기게 됨
                    int count = 0;
                    String noticeContent, noticeName, noticeDate;
                    while(count < jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);  //배열이 원소값 저장 가능
                        noticeContent = object.getString("noticeContent");
                        noticeName = object.getString("noticeName");
                        noticeDate = object.getString("noticeDate");
                        Notice notice = new Notice(noticeContent, noticeName, noticeDate);  //공지 하나를 객체로
                        noticeList.add(notice);
                        //noticeListView.setAdapter(adapter); //=> 안됨
                        adapter.notifyDataSetChanged();  //이거 해야 보인다.
                        count++;
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //두번 뒤로 누르면 앱 종료 - 9강
        private long lastTimeBackPressd;

        @Override
        public void onBackPressed(){
            if(System.currentTimeMillis() - lastTimeBackPressd < 1500)
            {
                finish();
                return;
            }
            Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();  //show 추가
            lastTimeBackPressd = System.currentTimeMillis();
        }

}
