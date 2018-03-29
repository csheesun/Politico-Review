package kr.co.company.politicoreview;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SM on 2018-01-19. - 강의 목록 보여주기 담당
 */

public class PoliticoListAdapter extends BaseAdapter {

    private Context context;
    private List<Politico> politicoList;  //politico 담길 수 있도록
    private Fragment parent;  //12강에서 추가

    //13강 => 불필요
    private String userID = MainActivity.userID; //13강 세줄 추가 - 사용자 들어감 중복 확인 용
   // private Schedule schedule = new Schedule();  //스케줄 안필요함!
    private List<Integer> politicoIDList;  //과목 중복검사위해 courseID 들어감

    public static int totalPolitico = 0; //19강 - 학점제한관련 변수 추가 구 : totalCredit => 불필요

    public PoliticoListAdapter(Context context, List<Politico> politicoList, Fragment parent) {  // Fragment parent자신을 불러낸 부모 fragment를 담을 수 있도록
        this.context = context;
        this.politicoList = politicoList;
        this.parent = parent;  //12강 parent 추가 => 다음은 아래 getView 보면서 고칠 부분 찾아봄 특정한 강의 추가하는 강의 이벤트 위해서

        //13강 => 아래 세줄 추가, 중복검사용임 불필요
        //schedule = new Schedule();
        politicoIDList = new ArrayList<Integer>();
        //new BackgroundTask().execute();  //, Fragment parent - 이거 계속 주석처리 해놨었음- 어차피 안됨 => 백그라운드 스케줄을 추가 한다는거 같음

        totalPolitico = 0; //19강 추가 => 학점제한관련 변수, 불필요
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    @Override
    public int getCount() {
        return politicoList.size();
    }

    @Override
    public Object getItem(int i) {
        return politicoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {  //의원 부분 추가 , 12강 => final 추가
        View v = View.inflate(context, R.layout.politico, null); //politico로 바꿀 수가 없다!
        TextView politicoState = (TextView)v.findViewById(R.id.politicoState);
        TextView politicoCity = (TextView)v.findViewById(R.id.politicoCity);
        TextView localProportional = (TextView)v.findViewById(R.id.localProportional); //추가
        TextView politicoName = (TextView)v.findViewById(R.id.politicoName); //여기도 바꿔야 - 실제 보여줄 것만, course 안에 있는 것들
        TextView politicoParty = (TextView)v.findViewById(R.id.politicoParty);
        TextView politicoElectionNumber = (TextView)v.findViewById(R.id.electionNumber); //여기도 바꿔야 - 실제 보여줄 것만, course 안에 있는 것들
        //TextView politicoHomepage = (TextView)v.findViewById(R.id.politicoHomepage);

        politicoState.setText(politicoList.get(i).getPoliticoState());
        politicoCity.setText(politicoList.get(i).getPoliticoCity());
        localProportional.setText(politicoList.get(i).getLocalProportional()); //추가  - 받은거 전시
        politicoName.setText(politicoList.get(i).getPoliticoName());
        politicoParty.setText(politicoList.get(i).getPoliticoParty());
        politicoElectionNumber.setText(politicoList.get(i).getPoliticoElectionNumber()); //추가
        //politicoHomepage.setText(politicoList.get(i).getPoliticoHomepage()); //필요없는데 놔둠

        v.setTag(politicoList.get(i).getPoliticoID());  //ID를 태그로

       /* //3/24 - 홈페이지 버튼 누르면 웹페이지로 이동
        Button politicoHomepageButton = (Button) v.findViewById(R.id.politicoHomepage); //=> 차라리 상임위원회는 어떨까?
        politicoHomepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //politicoList.clear(); //사실 할 필요 없다.
                //new BackgroundTaskForWebsite().execute();
            }
        });*/

        //버튼 이벤트 처리 (추가 버튼) - 12 강 - 관심정치인 추가로 변형하겠음 => 버튼 이벤트 처리 여기서 하는거 같음
        Button addButton = (Button) v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //13강 - 관심 정치인 등록할 수 있는지 현재 강의 추가 가능한지 타당성 검증 => 불필요
               //boolean validate = false; // 13강 - 관심 정치인 등록할 수 있는지 현재 강의 추가 가능한지 타당성 검증 - 한번 추가한 정치인 또 하지 추가하지 않도록
                //validate = schedule.validate(politicoList.get(i).getPoliticoID()+"");  //추가 하려는 강의의 강의시간 넣어 타당성 검증 - 바꿨음! - 강의시간 자체가 타당한건지 보는거
                if(politicoList.get(i).getPoliticoName().equals("현재 의원이 없습니다.")) { //현재 - 3월 23일 추가 - 안먹힘
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());  //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                    AlertDialog dialog = builder.setMessage("현재 의원이 없습니다.\n투표할 수 없습니다.")
                            .setPositiveButton("다시 시도", null)
                            .create();
                    dialog.show();
                }
                //13강
                if(!alreadyIn(politicoIDList, politicoList.get(i).getPoliticoID())){ //현재 신청하는 강의가(두번째파라메터), 리스트에 있으면 politicoIDList이미 추가한 정치인임
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());  //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                    AlertDialog dialog = builder.setMessage("이미 투표한 의원 입니다.\n투표내역에서 확인하세요")
                            .setPositiveButton("다시 시도", null)
                            .create();
                    dialog.show();
                }
                else {  //둘다 아니면
                    //13 강 지금 까지는 무조건 추가 => 지금 부터는 중복되거나 없는 강의는 추가 못하게 막아야  => validate 이용 하기
                    //String userID = MainActivity.userID;  //public 으로 선언 했던거 => 지움
                    Response.Listener<String> responseListener = new Response.Listener<String>() {  //정상입력 했을 때 등록완료=> 위에서 복사해옴

                        @Override
                        public void onResponse(String response) {  //해당 웹사이트 접속 후 특정 json 응답을 받을 수 있도록(RegisterActivity 에서 복사해온거)
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");  //success 는 해당 응답이 제대로 됐는지 response 값 의미
                                if(success){ //사용할 수 있는 아이디라면
                                        AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());  //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                                        AlertDialog dialog = builder.setMessage("해당 의원에 투표하였습니다.\n투표내역 에서 확인하세요")
                                                .setPositiveButton("확인", null)
                                                .create();
                                        dialog.show();
                                        //강의 추가 될 때마다 강의 직접 추가 해야 함 ( 왜 ?) - 원본
                                        // politicoIDList.add(politicoList.get(i).getPoliticoID());  //강의번호, 스케줄에 시간 추가
                                        // schedule.addSchedule(politicoList.get(i).getCourseTime()); //필요 없음 => 시간을 얻
                                        // totalCredit += politicoList.get(i).getCourseCredit(); //19강 추가 - 신청하면 신청한 만큼 추가 => 불필요
                                        //time credit 이런게 없으니까 임의로 함
                                        politicoIDList.add(politicoList.get(i).getPoliticoID());  //강의번호, 스케줄에 시간 추가 ???
                                        //schedule.addSchedule(politicoList.get(i).getPoliticoState());
                                        totalPolitico += 1; //그냥 한명 더하면 됨 19강 추가 - 신청하면 신청한 만큼 추가 : 구 totalCredit
                                }
                                else { //회원 등록에 실패, 사용할 수 없는 아이디라면
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity()); //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                                    AlertDialog dialog = builder.setMessage("투표에 실패했습니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };  //수정했음
                    //if(!politicoList.get(i).politicoName.equals("현재 의원이 없습니다.")) {
                        AddRequest addRequest = new AddRequest(userID, politicoList.get(i).getPoliticoID() + "", responseListener);  //어떤 회원이 어떤 정치인 선택 했는지 ""문자열 형태로 바꿔줌  //여길 더 추가 해야 하는지 고민
                        RequestQueue queue = Volley.newRequestQueue(parent.getActivity());// 요청 보낼수 있도록 큐 만들기
                        queue.add(addRequest); //특정 정치인 추가 완료
                   // }
                }
                }

        });

        //여기다 버튼 추가 하고
        //button.set~ 부르기 (복사) - 여기도 평가버튼 추가 안됨

        return v; //뷰 반환
    }

//13강 => 강의가 이미 들어가 있는 상태면 체크해 주기 => 불필요
    public boolean alreadyIn(List<Integer> politicoIDList, int item){
        for(int i = 0; i < politicoIDList.size(); i++){  //모든 강의 리스트 아이디 돌면서
            if(politicoIDList.get(i) == item) {  //현재추가하려는 id 값과 일치하는 원소가 하나라도 존재하면 추가 못하게
                return false;
            }
        }
        return true;
    }

    /*class BackgroundTaskForWebsite extends AsyncTask<Void, Void, String> { //php 만든 후 db 접속하도록 - 홈페이지 말고 상임위원회?
        String target; // 접속한 홈페이지 주소 들어감
        @Override
        protected void onPreExecute(){
            try { //검색 여러개로 하려면 이렇게 해야함 - 내가 어플로 입력해 주는 것만 => party는 아직 아님 if 문걸어서 타켓을 여러개로 하면
                //target = "http://politicoreview.dothome.co.kr/PoliticoListForLocals.php?politicoID=" + URLEncoder.encode(politicoID, "UTF-8");
                //주소가지러 간다.
            }
            catch(Exception e){
                e.printStackTrace();
            }
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
        public void onPostExecute(String result){  //특정학과 넣었을 때 강의 나와야 - 주고 가져온다. 그리고 그 주소로 창열기
            try {  //응답 부분 처리
                politicoList.clear();  //기존 목록 삭제
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int politicoID;  //Politico 안에 있는 변수임
                String localProportional;
                String politicoState;
                String politicoCity;
                String politicoName;  //받고 싶은 정보들 다
                String politicoParty;
                String politicoElectionNumber;
                String politicoHomepage;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);  //현재 배열의 원소값 저장 가능
                    politicoID = object.getInt("politicoID");
                    politicoState = object.getString("politicoState");
                    politicoCity = object.getString("politicoCity");
                    localProportional = object.getString("localProportional");
                    politicoName = object.getString("politicoName");
                    politicoParty = object.getString("politicoParty");
                    politicoElectionNumber = object.getString("politicoElectionNumber");
                    politicoHomepage = object.getString("politicoHomepage");

//첫번째 생성자 씀 - 여기 딱 하나임 - 수정 마지막 만든 생성자를 쓰게 됨
                    Politico politico = new Politico(politicoID, localProportional, politicoState, politicoCity, politicoName, politicoParty, politicoElectionNumber, politicoHomepage);  //정치인 (사실은 공약이다!) 하나를 객체로
                    politicoList.add(politico);
                    //noticeListView.setAdapter(adapter); //=> 안됨
                    //adapter.notifyDataSetChanged();  //이거 해야 보인다.- 기존에는 여기 있었음
                    count++;
                }
                if(count == 0){  //조회된 강의가 없다.
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(PoliticoFragment.this.getActivity());
                    dialog = builder.setMessage("조회된 의원이 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();

            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }*/


}
