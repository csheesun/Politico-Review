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

import java.util.List;

public class MyVoteListAdapter extends BaseAdapter {

    private Context context;
    private List<Politico> politicoList;  //politico 담길 수 있도록
    private Fragment parent;  //12강에서 추가
    private String userID = MainActivity.userID; //13강 세줄 추가 - 사용자 들어감 중복 확인용
    //아래 두줄 삭제 - 여기서는 스케줄 부분 불필요

    public MyVoteListAdapter(Context context, List<Politico> politicoList, Fragment parent) {  //Fragment parent자신을 불러낸 부모 fragment를 담을 수 있도록
        this.context = context;
        this.politicoList = politicoList;  //백그라운드 태스크도 삭제
        this.parent = parent;  //12강 parent 추가 => 다음은 아래 getView 보면서 고칠 부분 찾아봄 특정한 강의 추가하는 강의 이벤트 위해서//아래 두줄은 삭제
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
    public View getView(final int i, View view, ViewGroup viewGroup) {  //18강 추가
        View v = View.inflate(context, R.layout.statistics, null);  //삭제화면 띄우기
        TextView politicoState = (TextView)v.findViewById(R.id.politicoState);
        TextView politicoCity = (TextView)v.findViewById(R.id.politicoCity);
        TextView localProportional = (TextView)v.findViewById(R.id.localProportional);
        TextView politicoName = (TextView)v.findViewById(R.id.politicoName); //여기도 바꿔야 - 실제 보여줄 것만, course 안에 있는 것들
        TextView politicoParty = (TextView)v.findViewById(R.id.politicoParty);
        TextView numberOfVotesSinglePoliticoReceived = (TextView)v.findViewById(R.id.numberOfVotesSinglePoliticoReceived);

        politicoState.setText(politicoList.get(i).getPoliticoState());
        politicoCity.setText(politicoList.get(i).getPoliticoCity());
        localProportional.setText(politicoList.get(i).getLocalProportional()); //추가  - 받은거 전시
        politicoName.setText(politicoList.get(i).getPoliticoName());
        politicoParty.setText(politicoList.get(i).getPoliticoParty());
        numberOfVotesSinglePoliticoReceived.setText(politicoList.get(i).getNumberOfVotesSinglePoliticoReceived()+"표");  //추가 - 유저 한명이 투표한 의원 수

        v.setTag(politicoList.get(i).getPoliticoID()); //강의경쟁률 필요 없어서 이 외에 경쟁률 구하는 부분 필요 없음

        //19강 추가 - 삭제 버튼 이벤트
        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {  //courseListAdapter 에서 복사해옴
            @Override
            public void onClick(View view){
                     Response.Listener<String> responseListener = new Response.Listener<String>() {  //정상입력 했을 때 등록완료=> 위에서 복사해옴

                        @Override
                        public void onResponse(String response) {  //해당 웹사이트 접속 후 특정 json 응답을 받을 수 있도록(RegisterActivity 에서 복사해온거)
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");  //success 는 해당 응답이 제대로 됐는지 response 값 의미
                                if(success){ //사용할 수 있는 아이디라면
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());  //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                                    AlertDialog dialog = builder.setMessage("해당 의원이 삭제되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();

                                    //19강 추가
                                    MyVoteFragment.totalPoliticoNumberVotedByAUserTemp -= 1;
                                    MyVoteFragment.totalPoliticoNumberVotedByAUser.setText(MyVoteFragment.totalPoliticoNumberVotedByAUserTemp + "명"); //빼고 난후 공약개수 보여줌
                                    politicoList.remove(i); //진짜 지움
                                    notifyDataSetChanged(); //바뀌었다는거 알려줌
                                } else { //회원 등록에 실패, 사용할 수 없는 아이디라면
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity()); //자신을 불러낸 CourseFragment 에서 알림창 띄울 수 있도록
                                    AlertDialog dialog = builder.setMessage("해당 의원 삭제에 실패하였습니다.")
                                            .setNegativeButton("다시 시도", null)
                                            .create();
                                    dialog.show();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                     };
                    DeleteRequest deleteRequest = new DeleteRequest(userID, politicoList.get(i).getPoliticoID()+"", responseListener);  //어떤 회원이 어떤 정치인 선택 했는지 ""문자열 형태로 바꿔줌
                    RequestQueue queue = Volley.newRequestQueue(parent.getActivity());// 요청 보낼수 있도록 큐 만들기
                    queue.add(deleteRequest);  //특정 정치인 추가 완료 - DeleteRequest 액티비티 만들러 감( AddRequest 복사 )
            }
        });
        return v;
    }

}