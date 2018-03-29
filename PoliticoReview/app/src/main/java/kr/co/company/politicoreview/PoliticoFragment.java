package kr.co.company.politicoreview;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PoliticoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PoliticoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PoliticoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PoliticoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PoliticoFragment newInstance(String param1, String param2) {
        PoliticoFragment fragment = new PoliticoFragment();
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
        //여기다 평가 버튼 추가 하면 생성자 못찾는다고 나옴
    }
    private ArrayAdapter partyAdapter; //새로 추가 시작
    private Spinner partySpinner;
    private ArrayAdapter stateAdapter;
    private Spinner stateSpinner;
    private ArrayAdapter cityAdapter;
    private Spinner citySpinner;
    /*
    private ArrayAdapter electionNumberAdapter;
    private Spinner electionNumberSpinner;
    private ArrayAdapter committeeAdapter;
    private Spinner committeeSpinner;   //이름검색 ?

    private EditText nameSpinner;
    */
    private String localProportional="";

    private ListView politicoListView; //11강 추가
    private PoliticoListAdapter adapter; //
    private List<Politico> politicoList;

    @Override  //액티비티 만들었을 때 처리
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        final RadioGroup localProportionalGroup = (RadioGroup) getView().findViewById(R.id.localProportionalGroup);  //비례대표, 지역구의원 선택
        partySpinner = (Spinner) getView().findViewById(R.id.partySpinner);
        stateSpinner = (Spinner) getView().findViewById(R.id.stateSpinner);
        citySpinner = (Spinner) getView().findViewById(R.id.citySpinner);
        /*electionNumberSpinner = (Spinner) getView().findViewById(R.id.electionNumberSpinner);
        committeeSpinner = (Spinner) getView().findViewById(R.id.committeeSpinner);

        nameSpinner = (EditText) getView().findViewById(R.id.nameSpinner);

        //선수(비례/지역과 무관)
        electionNumberAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.electionNumber, android.R.layout.simple_spinner_dropdown_item);  //초기화, year 부분과 매칭
        electionNumberSpinner.setAdapter(electionNumberAdapter);
        electionNumberSpinner.setSelection(0);
        //상임위원회(비례/지역과 무관)
        committeeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.committee, android.R.layout.simple_spinner_dropdown_item);  //초기화, year 부분과 매칭
        committeeSpinner.setAdapter(committeeAdapter);
        committeeSpinner.setSelection(0);*/

        localProportionalGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton politicoButton = (RadioButton) getView().findViewById(i);
                localProportional = politicoButton.getText().toString();  //선택한 거에서 텍스트가져옴

                if (localProportional.equals("비례대표 국회의원")) {
                    partyAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.parties_proportional, android.R.layout.simple_spinner_dropdown_item);  //초기화, year 부분과 매칭
                    partySpinner.setAdapter(partyAdapter);
                    partySpinner.setSelection(0);
                }
                if (localProportional.equals("지역구 국회의원")) {
                    stateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_dropdown_item);  //초기화, year 부분과 매칭
                    stateSpinner.setAdapter(stateAdapter);
                    stateSpinner.setSelection(0);
                }
            }
        });  //지금까지 스피너 설정함 - 사라지게 하는건 모르겠다.


        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){  //도 선택 했을 때 그에 맞는 시 보여줘야 함
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){

                if(stateSpinner.getSelectedItem().equals("서울특별시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_seoul, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("부산광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_busan, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("대구광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_daegu, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("인천광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_incheon, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("광주광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_gwangju, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("대전광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_daejeon, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("울산광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_ulsan, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("세종특별자치시")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_sejong, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("경기도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("강원도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_gangwon, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("충청북도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_chungbuk, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("충청남도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_chungnam, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("전라북도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_jeonbuk, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("전라남도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_jeonnam, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("경상북도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_gyungbuk, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("경상남도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_gyungnam, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
                else if(stateSpinner.getSelectedItem().equals("제주특별자치도")){
                    cityAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.cities_jeju, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                    citySpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        politicoListView = (ListView)getView().findViewById(R.id.politicoListView);  //11강 - 초기화
        politicoList = new ArrayList<Politico>();
        adapter = new PoliticoListAdapter(getContext().getApplicationContext(), politicoList, this); //12강 => this 추가
        politicoListView.setAdapter(adapter);

//10강에서 추가
        Button searchButton = (Button)getView().findViewById(R.id.searchButton); //검색 버튼임
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(localProportional.equals("비례대표 국회의원")){  //22강에서 수정
                    politicoList.clear();
                    new BackgroundTaskForProportionals().execute(); //전체에서 인기 강의 순위 가져옴
                }else if(localProportional.equals("지역구 국회의원")) {
                    politicoList.clear();
                    new BackgroundTaskForLocals().execute();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_politico, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    class BackgroundTaskForLocals extends AsyncTask<Void, Void, String> { //php 만든 후 db 접속하도록
        String target; // 접속한 홈페이지 주소 들어감
        @Override
        protected void onPreExecute(){
            try { //검색 여러개로 하려면 이렇게 해야함 - 내가 어플로 입력해 주는 것만 => party는 아직 아님 if 문걸어서 타켓을 여러개로 하면
                target = "http://politicoreview.dothome.co.kr/PoliticoListForLocals.php?localProportional=" + URLEncoder.encode(localProportional, "UTF-8")
                        + "&politicoState=" + URLEncoder.encode(stateSpinner.getSelectedItem().toString(), "UTF-8")
                        +"&politicoCity=" + URLEncoder.encode(citySpinner.getSelectedItem().toString(), "UTF-8");
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
        public void onPostExecute(String result){  //특정학과 넣었을 때 강의 나와야
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
                //String politicoHomepage;

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
                    //politicoHomepage = object.getString("politicoHomepage");

//첫번째 생성자 씀 - 여기 딱 하나임 - 수정 마지막 만든 생성자를 쓰게 됨
                    Politico politico = new Politico(politicoID, localProportional, politicoState, politicoCity, politicoName, politicoParty, politicoElectionNumber);  //정치인 (사실은 공약이다!) 하나를 객체로
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
    }

    class BackgroundTaskForProportionals extends AsyncTask<Void, Void, String> { //php 만든 후 db 접속하도록
        String target; // 접속한 홈페이지 주소 들어감
        @Override
        protected void onPreExecute(){
            try { //검색 여러개로 하려면 이렇게 해야함 - 내가 어플로 입력해 주는 것만 => party는 아직 아님 if 문걸어서 타켓을 여러개로 하면
                target = "http://politicoreview.dothome.co.kr/PoliticoListForProportionals.php?localProportional=" + URLEncoder.encode(localProportional, "UTF-8")
                    +"&politicoParty="+ URLEncoder.encode(partySpinner.getSelectedItem().toString(), "UTF-8");  //당선방법, 소속정당주기
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
        public void onPostExecute(String result){  //특정학과 넣었을 때 강의 나와야
            try {  //응답 부분 처리
                politicoList.clear();  //기존 목록 삭제
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int politicoID;  //Politico 안에 있는 변수임
                String localProportional;
                //String politicoState;
                //String politicoCity;
                String politicoName;  //받고 싶은 정보들 다
                String politicoParty;
                String politicoElectionNumber;
                //String politicoHomepage;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);  //현재 배열의 원소값 저장 가능
                    politicoID = object.getInt("politicoID");
                    //politicoState = object.getString("politicoState");
                    //politicoCity = object.getString("politicoCity");
                    localProportional = object.getString("localProportional");
                    politicoName = object.getString("politicoName");
                    politicoParty = object.getString("politicoParty");  //아예 받아온게 없는 것 같다.
                    politicoElectionNumber = object.getString("politicoElectionNumber");
                    //politicoHomepage = object.getString("politicoHomepage");


                    //첫번째 생성자 씀 - 여기 딱 하나임 - 수정 마지막 만든 생성자를 쓰게 됨
                    Politico politico = new Politico(politicoID, localProportional, politicoName, politicoParty, politicoElectionNumber);  //for 비례대표
                    politicoList.add(politico);
                    //noticeListView.setAdapter(adapter); //=> 안됨
                    //adapter.notifyDataSetChanged();  //이거 해야 보인다.- 기존에는 여기 있었음
                    count++;
                }
                if(count == 0){  //조회된 강의가 없다.
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(PoliticoFragment.this.getActivity());
                    dialog = builder.setMessage("조회된 의원이 없습니다.") //count 0 임
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();

            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
