package kr.co.company.politicoreview;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SM on 2018-01-18. 가입 가능 한지 아이디 체크 요청 보내기
*/

public class ValidateRequest extends StringRequest {

    final static private String URL = "http://politicoreview.dothome.co.kr/UserValidate.php";
    private Map<String, String> parameters;


    public ValidateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);  //URL의 파라메터들을 POST 방식으로, 해당 요청을 숨겨서 보내주어라
        parameters = new HashMap<>(); // 파라메터 값을 넣을 수 있도록 해쉬 맴을 만듦. 키 추가 하려면 여기를 추가 해야 함
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
