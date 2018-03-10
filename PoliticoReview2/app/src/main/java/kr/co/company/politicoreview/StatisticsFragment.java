package kr.co.company.politicoreview;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //18 강 추가
    /*
    private ListView politicoListView;
    private StatisticsPoliticoListAdapter adapter;
    private List<Politico> politicoList;

    public static int totalPoliticoNumberVotedByAUserTemp = 0;  //구 : totalCredit = 0 유저 한명이 투표한 총 정치인 수 19강 학점은 맨위에서 공통으로 쓰는 거니까 기존에 있던 변수는 지움
    public static TextView totalPoliticoNumberVotedByAUser;  //구 : credit
     */

    //20강 추가
    private ArrayAdapter rankAdapter;
    private Spinner rankSpinner;

    //21강 추가 - 순위를 보여줄 수 있도록
    private ListView rankListView;
    private RankListAdapter rankListAdapter;
    private List<Politico> rankList;

    @Override  //18강
    public void onActivityCreated(Bundle b){ //액티비티 처음 불러 왔을 때 실행
        super.onActivityCreated(b);/*
        politicoListView = (ListView) getView().findViewById(R.id.politicoListView);  //초기화
        politicoList = new ArrayList<Politico>();
        adapter = new StatisticsPoliticoListAdapter(getContext().getApplicationContext(), politicoList, this);
        politicoListView.setAdapter(adapter);  //어댑터 연결
        new BackgroundTask().execute();  // => db 와 소통하는 부분 아직 안만듬, 바로 아래 만들 거임

        //19강 추가
        totalPoliticoNumberVotedByAUserTemp = 0;
        totalPoliticoNumberVotedByAUser = (TextView)getView().findViewById(R.id.totalPoliticoNumberVotedByAUser);*/

        //20강 추가
        rankSpinner = (Spinner)getView().findViewById(R.id.rankSpinner);
        rankAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.politico_rank, R.layout.spinner_item); //dropdown 대신 자체로 만든걸로 - 21강
        rankSpinner.setAdapter(rankAdapter);
        rankSpinner.setPopupBackgroundResource(R.color.colorPrimary);  //스피너에 나오는 배경색을 연두색으로=> gradle 16으로 수정했더니 빨간줄 없어짐

        rankListView = (ListView) getView().findViewById(R.id.rankListView); //21강 추가
        rankList = new ArrayList<Politico>();
        rankListAdapter = new RankListAdapter(getContext().getApplicationContext(), rankList, this); //생성자 초기화
        rankListView.setAdapter(rankListAdapter);
        // 전체에서 강의 순위를 불러오는 부분이 여기에 또 있어서 2번 출력됨 - new ByEntire().execute(); 이줄 지워야 함 => 아래로 내려 줄거임 바로 만들어 줄거임
        //시작 되지 마자 전체에서가 기본 원소여서

        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                if(rankSpinner.getSelectedItem().equals("전체에서")){  //22강에서 수정
                    rankList.clear();
                    new PopularityByEntire().execute(); //전체에서 인기 강의 순위 가져옴
                }
                else if(rankSpinner.getSelectedItem().equals("남자 선호도")){
                    rankList.clear();
                    new PopularityByMale().execute();
                }
                else if(rankSpinner.getSelectedItem().equals("여자 선호도")){
                    rankList.clear();
                    new PopularityByFemale().execute(); //전체에서 인기 강의 순위 가져옴
                }
                else if(rankSpinner.getSelectedItem().equals("10대까지의 선호도")){  //여기부터 해야 함
                    rankList.clear();
                    new PopularityByTeens().execute();
                }
                else if(rankSpinner.getSelectedItem().equals("전공 인기도")){
                    rankList.clear();
                    //new PopularityByMajor().execute();
                }
                else if(rankSpinner.getSelectedItem().equals("교양 인기도")){
                    rankList.clear();
                    //new PopularityByRefinement().execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });
    }

    class PopularityByEntire extends AsyncTask<Void, Void, String> { //21강에서 복사해서 추가  - 신규 추가
        String target; // 접속한 홈페이지 주소 들어감

        @Override
        protected void onPreExecute(){
            try {                                                                            //여기 수정
                target = "http://politicoreview.dothome.co.kr/PopularityByEntire.php";  //아까전에 저장한 아이디를 유티에프로 인코딩 - php 파일이랑 매칭
            }catch(Exception e){
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
        public void onPostExecute(String result){  //결과 처리 - course 에서 생성자 만들고 돌아와서 수정
            try {  //응답 부분 처리 - php 로 질의해서 얻은 거 출력하기
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열 받아오기 - response 에 각각의 공지사항 담기게 됨
                int count = 0;

                int politicoID;  //Politico 안에 있는 변수임
                String localProportional;
                String politicoState;
                String politicoCity;
                String politicoName;  //받고 싶은 정보들 다
                String politicoParty;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);  //배열이 원소값 저장 가능 - //받아온거 변수에 저장 쿼리해서 받아온거 저장
                    politicoID = object.getInt("politicoID");
                    politicoState = object.getString("politicoState");
                    politicoCity = object.getString("politicoCity");
                    localProportional = object.getString("localProportional");
                    politicoName = object.getString("politicoName");
                    politicoParty = object.getString("politicoParty");

                    rankList.add(new Politico(politicoID, localProportional, politicoState, politicoCity, politicoName, politicoParty));  //생성자에 추가 - 하나의 강의 추가 - 해당 인스턴스에 전달!
                    count++;
                }
                rankListAdapter.notifyDataSetChanged();//adapter 갱신 해 줄수 있도록  //없어도 됨 - credit = (TextView) getView().findViewById(R.id.totalCredit);  //초기화
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PopularityByFemale extends AsyncTask<Void, Void, String> { //21강에서 복사해서 추가  - 신규 추가
        String target; // 접속한 홈페이지 주소 들어감

        @Override
        protected void onPreExecute(){
            try {                                                                            //여기 수정
                target = "http://politicoreview.dothome.co.kr/PopularityByFemale.php";  //아까전에 저장한 아이디를 유티에프로 인코딩 - php 파일이랑 매칭
            }catch(Exception e){
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
        public void onPostExecute(String result){  //결과 처리 - course 에서 생성자 만들고 돌아와서 수정
            try {  //응답 부분 처리 - php 로 질의해서 얻은 거 출력하기
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열 받아오기 - response 에 각각의 공지사항 담기게 됨
                int count = 0;

                int politicoID;  //Politico 안에 있는 변수임
                String localProportional;
                String politicoState;
                String politicoCity;
                String politicoName;  //받고 싶은 정보들 다
                String politicoParty;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);  //배열이 원소값 저장 가능 - //받아온거 변수에 저장 쿼리해서 받아온거 저장
                    politicoID = object.getInt("politicoID");
                    politicoState = object.getString("politicoState");
                    politicoCity = object.getString("politicoCity");
                    localProportional = object.getString("localProportional");
                    politicoName = object.getString("politicoName");
                    politicoParty = object.getString("politicoParty");

                    rankList.add(new Politico(politicoID, localProportional, politicoState, politicoCity, politicoName, politicoParty));  //생성자에 추가 - 하나의 강의 추가 - 해당 인스턴스에 전달!
                    count++;
                }
                rankListAdapter.notifyDataSetChanged();//adapter 갱신 해 줄수 있도록  //없어도 됨 - credit = (TextView) getView().findViewById(R.id.totalCredit);  //초기화
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PopularityByMale extends AsyncTask<Void, Void, String> { //21강에서 복사해서 추가  - 신규 추가
        String target; // 접속한 홈페이지 주소 들어감

        @Override
        protected void onPreExecute(){
            try {                                                                            //여기 수정
                target = "http://politicoreview.dothome.co.kr/PopularityByMale.php";  //아까전에 저장한 아이디를 유티에프로 인코딩 - php 파일이랑 매칭
            }catch(Exception e){
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
        public void onPostExecute(String result){  //결과 처리 - course 에서 생성자 만들고 돌아와서 수정
            try {  //응답 부분 처리 - php 로 질의해서 얻은 거 출력하기
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열 받아오기 - response 에 각각의 공지사항 담기게 됨
                int count = 0;

                int politicoID;  //Politico 안에 있는 변수임
                String localProportional;
                String politicoState;
                String politicoCity;
                String politicoName;  //받고 싶은 정보들 다
                String politicoParty;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);  //배열이 원소값 저장 가능 - //받아온거 변수에 저장 쿼리해서 받아온거 저장
                    politicoID = object.getInt("politicoID");
                    politicoState = object.getString("politicoState");
                    politicoCity = object.getString("politicoCity");
                    localProportional = object.getString("localProportional");
                    politicoName = object.getString("politicoName");
                    politicoParty = object.getString("politicoParty");

                    rankList.add(new Politico(politicoID, localProportional, politicoState, politicoCity, politicoName, politicoParty));  //생성자에 추가 - 하나의 강의 추가 - 해당 인스턴스에 전달!
                    count++;
                }
                rankListAdapter.notifyDataSetChanged();//adapter 갱신 해 줄수 있도록  //없어도 됨 - credit = (TextView) getView().findViewById(R.id.totalCredit);  //초기화
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PopularityByTeens extends AsyncTask<Void, Void, String> { //21강에서 복사해서 추가  - 신규 추가
        String target; // 접속한 홈페이지 주소 들어감

        @Override
        protected void onPreExecute(){
            try {                                                                            //여기 수정
                target = "http://politicoreview.dothome.co.kr/PopularityByTeens.php";  //아까전에 저장한 아이디를 유티에프로 인코딩 - php 파일이랑 매칭
            }catch(Exception e){
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
        public void onPostExecute(String result){  //결과 처리 - course 에서 생성자 만들고 돌아와서 수정
            try {  //응답 부분 처리 - php 로 질의해서 얻은 거 출력하기
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열 받아오기 - response 에 각각의 공지사항 담기게 됨
                int count = 0;

                int politicoID;  //Politico 안에 있는 변수임
                String localProportional;
                String politicoState;
                String politicoCity;
                String politicoName;  //받고 싶은 정보들 다
                String politicoParty;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);  //배열이 원소값 저장 가능 - //받아온거 변수에 저장 쿼리해서 받아온거 저장
                    politicoID = object.getInt("politicoID");
                    politicoState = object.getString("politicoState");
                    politicoCity = object.getString("politicoCity");
                    localProportional = object.getString("localProportional");
                    politicoName = object.getString("politicoName");
                    politicoParty = object.getString("politicoParty");

                    rankList.add(new Politico(politicoID, localProportional, politicoState, politicoCity, politicoName, politicoParty));  //생성자에 추가 - 하나의 강의 추가 - 해당 인스턴스에 전달!
                    count++;
                }
                rankListAdapter.notifyDataSetChanged();//adapter 갱신 해 줄수 있도록  //없어도 됨 - credit = (TextView) getView().findViewById(R.id.totalCredit);  //초기화
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


/*
    //만들어 본적 있었음 , ScheduleFragment 에서 복사해옴 18강임 => 스케줄 때문에 하는거, 스케줄 안하므로, 불필요함
    class BackgroundTask extends AsyncTask<Void, Void, String> { //php 만든 후 db 접속하도록 - 13강 추가, mainActivity 에서 불러 옴
        String target; // 접속한 홈페이지 주소 들어감

        @Override
        protected void onPreExecute(){
            try {                                                                            //여기 수정
                target = "http://politicoreview.dothome.co.kr/StatisticsPoliticoList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8");  //아까전에 저장한 아이디를 유티에프로 인코딩
            }catch(Exception e){
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
        public void onPostExecute(String result){  //결과 처리
            try {  //응답 부분 처리 - php 로 질의해서 얻은 거 출력하기
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열 받아오기 - response 에 각각의 공지사항 담기게 됨
                int count = 0;

                int politicoID;  //Politico 안에 있는 변수임
                String politicoState;
                String politicoCity;
                String localProportional;
                String politicoName;  //받고 싶은 정보들 다
                String politicoParty;  //퍼스넬은 필요 없음
                int numberOfVotesSinglePoliticoReceived;  //퍼스넬은 필요 없음 - 특정 정치인에 투표한 유저 수     구: courseRival

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);  //배열이 원소값 저장 가능
                    politicoID = object.getInt("politicoID");
                    politicoState = object.getString("politicoState"); //받아온거 변수에 저장
                    politicoCity = object.getString("politicoCity");
                    localProportional = object.getString("localProportional"); //추가
                    politicoName = object.getString("politicoName");
                    politicoParty = object.getString("politicoParty");
                    numberOfVotesSinglePoliticoReceived = object.getInt("COUNT(VOTE.politicoID)");  //특정 정치인에 투표한 유저 수 //구 courseRival

                    //19강에서도 이거 추가하게 될 거임
                    totalPoliticoNumberVotedByAUserTemp += 1; //한 사용자가 투표한 정치인 수

                    politicoList.add(new Politico(politicoID, politicoState, politicoCity, localProportional, politicoName, politicoParty, numberOfVotesSinglePoliticoReceived));  //생성자에 추가 - 하나의 강의 추가
                    count++;
                }
                adapter.notifyDataSetChanged(); //adapter 갱신 해 줄수 있도록  //없어도 됨 - credit = (TextView) getView().findViewById(R.id.totalCredit);  //초기화
                totalPoliticoNumberVotedByAUser.setText(totalPoliticoNumberVotedByAUserTemp+"명"); //한 사용자가 투표한 정치인 수
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics_rank, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //onAttach() 삭제

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
