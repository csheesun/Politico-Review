package kr.co.company.politicoreview;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SM on 2018-01-18.  12강 시간표에 강의데이터 추가 - 이건 공통, URL 만 바꾸면 됨 - 삭제하기
 */

public class DeleteRequest extends StringRequest{

    final static private String URL = "http://politicoreview.dothome.co.kr/PoliticoDelete.php";  //여기 수정
    private Map<String, String> parameters;

    public DeleteRequest(String userID, String politicoID, Response.Listener<String> listener) {  //두번쨰 변수랑
        super(Method.POST, URL, listener, null);  //URL의 파라메터들을 POST 방식으로, 해당 요청을 숨겨서 보내주어라
        parameters = new HashMap<>(); // 파라메터 값을 넣을 수 있도록 해쉬 맴을 만듦
        parameters.put("userID", userID);
        parameters.put("politicoID", politicoID);  //여기 수정 - 받은 걸 매개 변수로 보내 줄 수 있도록
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

