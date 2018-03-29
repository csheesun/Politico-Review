package kr.co.company.politicoreview;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SM on 2018-01-19.
 */

public class LoginRequest extends StringRequest {
    final static private String URL = "http://politicoreview.dothome.co.kr/UserLogin.php";
    private Map<String, String> parameters;

    public LoginRequest(Response.Listener<String> listener){
        super(Method.POST, URL,listener, null);
    }

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);  //URL의 파라메터들을 POST 방식으로, 해당 요청을 숨겨서 보내주어라
        parameters = new HashMap<>(); // 파라메터 값을 넣을 수 있도록 해쉬 맴을 만듦
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
