package kr.co.company.politicoreview;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter stateAdapter;
    private Spinner stateSpinner;

    private ArrayAdapter cityAdapter;
    private Spinner citySpinner;

    private String userID;  //요청받은 정보 받을 수 있도록 변수 만든다.
    private String userPassword;
    private String userEmail;
    private String userGender;
    private String userAge;
    private String userState;
    private String userCity;

    private AlertDialog dialog;  //알림창 띄워줌 - 클래스 import 했는데 안되면 다른 걸로 해보기
    private boolean validate = false;  //사용할 수 있는 회원이메일인지 체크

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

        citySpinner = (Spinner) findViewById(R.id.citySpinner); //등록 시켜야

        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final EditText emailText = (EditText)findViewById(R.id.emailText);
        final EditText ageText = (EditText)findViewById(R.id.ageText);  //추가

        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();  //userGroup 에 선택한 성별 저장

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);  //현재 선택되어 있는 라디오 버튼 찾음
                userGender = genderButton.getText().toString();
            }
        });


        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){  //도 선택 했을 때 그에 맞는 시 보여줘야 함
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){

                if(stateSpinner.getSelectedItem().equals("서울특별시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_seoul, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("부산광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_busan, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("대구광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_daegu, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("인천광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_incheon, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("광주광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_gwangju, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("대전광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_daejeon, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("울산광역시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_ulsan, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("세종특별자치시")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_sejong, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("경기도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("강원도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_gangwon, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("충청북도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_chungbuk, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("충청남도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_chungnam, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("전라북도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_jeonbuk, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("전라남도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_jeonnam, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("경상북도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_gyungbuk, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("경상남도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_gyungnam, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
                else if(stateSpinner.getSelectedItem().equals("제주특별자치도")){
                    cityAdapter= ArrayAdapter.createFromResource(RegisterActivity.this, R.array.cities_jeju, android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final Button validateButton = (Button)findViewById(R.id.validateButton); // 회원 중복 체크 버튼
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                if (validate)  //중복체크 되어 있을 때
                {
                    return;
                }
                if (userID.equals(""))  //입력하지 않고 중복체크 버튼 눌렀을 때
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {  //정상입력 했을 때 => 중복체크 진행

                    @Override
                    public void onResponse(String response) {  //해당 웹사이트 접속 후 특정 json 응답을 받을 수 있도록
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");  //success 는 해당 응답이 제대로 됐는지 response 값 의미
                            if(success){ //사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false); //바꿀수 없도록 고정
                                validate = true; //이메일 체크 완료 표시
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));  //버튼색 바꿈
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }
                            else { //중복 체크에 실패, 사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);  //접근하도록 객체 생성
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);// 요청 보낼수 있도록 큐 만들기
                queue.add(validateRequest);
            }
        });  //회원중복 검사 끝- 일단 중복체크 안해도 될 수 있게

        Button registerButton = (Button)findViewById(R.id.registerButton);  //회원가입 버튼 - 문제없다.-> 이름을 2로 바꿈
        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String userID = idText.getText().toString();  //성별은 해놓았으므로 제외
                String userPassword = passwordText.getText().toString();
                String userEmail = emailText.getText().toString();
                String userAge = ageText.getText().toString();
                String userState = stateSpinner.getSelectedItem().toString();
                String userCity= citySpinner.getSelectedItem().toString();
                //여기 주석 시작
                if(!validate){  //현재 중복 체크가 되어 있지 않다면
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("먼저 중복 체크를 해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
//여기 주석 끝
                //하나라도 입력 안하면 계정 생성 불가
                if(userID.equals("") || userPassword.equals("")|| userEmail.equals("")|| userGender.equals("")|| userAge.equals("") || userState.equals("")|| userCity.equals("") ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸 없이 입력해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {  //정상입력 했을 때 등록완료=> 위에서 복사해옴

                    @Override
                    public void onResponse(String response) {  //해당 웹사이트 접속 후 특정 json 응답을 받을 수 있도록
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");  //success 는 해당 응답이 제대로 됐는지 response 값 의미
                            if(success){ //사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish(); //변경-등록창 닫도록
                            }
                            else { //회원 등록에 실패, 사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest RegisterRequest = new RegisterRequest(userID, userPassword, userEmail, userGender, userAge, userState, userCity, responseListener);  //접근하도록 객체 생성
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);// 요청 보낼수 있도록 큐 만들기
                queue.add(RegisterRequest);
            }
        });
    }

    @Override // 회원 등록 후 회원 등록 창이 꺼지게 되면 onStop 실행됨
    protected void onStop() {
        super.onStop();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}