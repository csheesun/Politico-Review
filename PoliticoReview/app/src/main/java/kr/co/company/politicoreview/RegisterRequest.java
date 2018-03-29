package kr.co.company.politicoreview;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SM on 2018-01-18.  회원가입 요청, UserRegister.php 에 입력한 정보 보내서 회원가입 시켜라 라는 요청 보냄
 */

public class RegisterRequest extends StringRequest{

    final static private String URL = "http://politicoreview.dothome.co.kr/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userEmail, String userGender, String userAge, String userState, String userCity, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);  //URL의 파라메터들을 POST 방식으로, 해당 요청을 숨겨서 보내주어라
        parameters = new HashMap<>(); // 파라메터 값을 넣을 수 있도록 해쉬 맴을 만듦
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userEmail", userEmail);
        parameters.put("userGender", userGender);
        parameters.put("userAge", userAge);
        parameters.put("userState", userState);
        parameters.put("userCity", userCity);  //php 파일에 보내는 거임 => php 파일이 받아서 db 에 저장
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

