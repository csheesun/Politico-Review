package kr.co.company.politicoreview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/*하나의 강의 정보 순위를 얻어 옴 */
public class RankListAdapter extends BaseAdapter {

    private Context context;
    private List<Politico> politicoList;  //politico 담길 수 있도록
    private Fragment parent;  //12강에서 추가 - 유저 아이디는 필요 없어서 삭제

    public RankListAdapter(Context context, List<Politico> politicoList, Fragment parent) {  // Fragment parent자신을 불러낸 부모 fragment를 담을 수 있도록
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
    public View getView(final int i, View view, ViewGroup viewGroup) {  //의원 부분 추가 , 12강 => final 추가
        View v = View.inflate(context, R.layout.rank, null);
        TextView rankTextView = (TextView)v.findViewById(R.id.rankTextView); // 21 강 추가
        TextView politicoState = (TextView)v.findViewById(R.id.politicoState);
        TextView politicoCity = (TextView)v.findViewById(R.id.politicoCity);
        TextView localProportional = (TextView)v.findViewById(R.id.localProportional);
        TextView politicoName = (TextView)v.findViewById(R.id.politicoName); //여기도 바꿔야 - 실제 보여줄 것만, course 안에 있는 것들
        TextView politicoParty = (TextView)v.findViewById(R.id.politicoParty);

        rankTextView.setText((i + 1) + "위"); //21강
        if(i == 0) //21강 - 1위 면
        {
            rankTextView.setBackgroundColor(parent.getResources().getColor(R.color.colorWinner)); //1위가 아니면 다 이 배경색임
        }
        else
        {
            rankTextView.setBackgroundColor(parent.getResources().getColor(R.color.colorPrimary)); //1위~3위가 아니면 다 이 배경색임
        }

        politicoState.setText(politicoList.get(i).getPoliticoState()+"");
        politicoCity.setText(politicoList.get(i).getPoliticoCity());

        localProportional.setText(politicoList.get(i).getLocalProportional()+"");
        politicoName.setText(politicoList.get(i).getPoliticoName()+"");
        politicoParty.setText(politicoList.get(i).getPoliticoParty()+"");

        v.setTag(politicoList.get(i).getPoliticoID());
        return v;
    }
}