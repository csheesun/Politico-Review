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

        politicoState.setText(politicoList.get(i).getPoliticoState());
        politicoCity.setText(politicoList.get(i).getPoliticoCity());
        localProportional.setText(politicoList.get(i).getLocalProportional()); //추가  - 받은거 전시
        politicoName.setText(politicoList.get(i).getPoliticoName());
        politicoParty.setText(politicoList.get(i).getPoliticoParty());


        v.setTag(politicoList.get(i).getPoliticoID());  //ID를 태그로


        //버튼 이벤트 처리 (추가 버튼) - 12 강 - 관심정치인 추가로 변형하겠음 => 버튼 이벤트 처리 여기서 하는거 같음
        Button addButton = (Button) v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // 13강 - 관심 정치인 등록할 수 있는지 현재 강의 추가 가능한지 타당성 검증 => 불필요
               //boolean validate = false; // 13강 - 관심 정치인 등록할 수 있는지 현재 강의 추가 가능한지 타당성 검증 - 한번 추가한 정치인 또 하지 추가하지 않도록
                //validate = schedule.validate(politicoList.get(i).getPoliticoID()+"");  //추가 하려는 강의의 강의시간 넣어 타당성 검증 - 바꿨음! - 강의시간 자체가 타당한건지 보는거
                ///////13강
                if(!alreadyIn(politicoIDList, politicoList.get(i).getPoliticoID())){ //현재 신청하는 강의가(두번째파라메터), 리스트에 있으면 politicoIDList이미 추가한 정치인임
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());  //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                    AlertDialog dialog = builder.setMessage("이미 투표한 의원 입니다.\n투표내역에서 확인하세요")
                            .setPositiveButton("다시 시도", null)
                            .create();
                    dialog.show();
                }
                else{  //둘다 아니면
                    //13 강 지금 까지는 무조건 추가 => 지금 부터는 중복되거나 없는 강의는 추가 못하게 막아야  => validate 이용 하기
                    //String userID = MainActivity.userID;  //public 으로 선언 했던거 => 지움
                    Response.Listener<String> responseListener = new Response.Listener<String>() {  //정상입력 했을 때 등록완료=> 위에서 복사해옴

                        @Override
                        public void onResponse(String response) {  //해당 웹사이트 접속 후 특정 json 응답을 받을 수 있도록(RegisterActivity 에서 복사해온거)
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                /*if(politicoList.get(i).politicoName.equals("현재 의원이 없습니다.")){ //의원이 없으면 투표 못하게 해야 함 => 어디다 놔야 하는지 모르겠다. 현재 신청하는 강의가(두번째파라메터), 리스트에 있으면 politicoIDList이미 추가한 정치인임
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());  //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                                    AlertDialog dialog = builder.setMessage("현재 의원이 없으므로 투표 할 수 없습니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }*/
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
                                        //schedule.addSchedule(politicoList.get(i).getPoliticoState()); //필요 없음 => 시간을 얻
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
}
