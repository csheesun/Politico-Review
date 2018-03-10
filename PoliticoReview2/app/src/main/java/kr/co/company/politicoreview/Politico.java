package kr.co.company.politicoreview;

/**
 * Created by SM on 2018-01-24. - 생성자 차차 추가 시켜나가기
 */

public class Politico {

    int politicoID;
    String localProportional;
    String politicoName;
    String politicoParty;
    String politicoState;
    String politicoCity;

    int numberOfVotesSinglePoliticoReceived; //구 :  courseRival; //18강 추가 특정 정치인에 투표한 사용자 수 - 강의 경쟁자 수

    String policyTitle;  //공약위해 두 개 추가
    String policyContent;

    public String getPolicyTitle() {
        return policyTitle;
    }

    public void setPolicyTitle(String policyTitle) {
        this.policyTitle = policyTitle;
    }

    public String getPolicyContent() {
        return policyContent;
    }

    public void setPolicyContent(String policyContent) {
        this.policyContent = policyContent;
    }

    public int getPoliticoID() {
        return politicoID;
    }

    public void setPoliticoID(int politicoID) {
        this.politicoID = politicoID;
    }

    public String getPoliticoState() {
        return politicoState;
    }

    public void setPoliticoState(String politicoState) {
        this.politicoState = politicoState;
    }

    public String getPoliticoCity() {
        return politicoCity;
    }

    public void setPoliticoCity(String politicoCity) {
        this.politicoCity = politicoCity;
    }

    public String getLocalProportional() {
        return localProportional;
    }

    public void setLocalProportional(String localProportional) {
        this.localProportional = localProportional;
    }

    public String getPoliticoName() {
        return politicoName;
    }

    public void setPoliticoName(String politicoName) {
        this.politicoName = politicoName;
    }

    public String getPoliticoParty() {
        return politicoParty;
    }

    public void setPoliticoParty(String politicoParty) {
        this.politicoParty = politicoParty;
    }

    public int getNumberOfVotesSinglePoliticoReceived() {
        return numberOfVotesSinglePoliticoReceived;
    }

    public void setNumberOfVotesSinglePoliticoReceived(int numberOfVotesSinglePoliticoReceived) {
        this.numberOfVotesSinglePoliticoReceived = numberOfVotesSinglePoliticoReceived;
    }

    public Politico(int politicoID, String localProportional, String politicoState, String politicoCity, String politicoName, String politicoParty) {  //원래 있던거
        this.politicoID = politicoID;  //제일 처음에 있던거 for PoliticoFragment, 정치인추가때 씀
        this.politicoState = politicoState;
        this.politicoCity = politicoCity;
        this.localProportional = localProportional;
        this.politicoName = politicoName;
        this.politicoParty = politicoParty;
    }

    public Politico(int politicoID, String politicoState, String politicoCity, String localProportional, String politicoName, String politicoParty, int numberOfVotesSinglePoliticoReceived) {
        this.politicoID = politicoID;  //18강에서 추가 - statistics.xml, 삭제 화면을 전시할 때 필요 => statisticsFragment에서
        this.politicoState = politicoState;
        this.politicoCity = politicoCity;
        this.localProportional = localProportional;
        this.politicoName = politicoName;
        this.politicoParty = politicoParty;
        this.numberOfVotesSinglePoliticoReceived = numberOfVotesSinglePoliticoReceived;
    }

    //19강에서 18강에 에서 추가한 생성자에 courseCredit 추가해서 새로운 생성자 만듬 , 학점제한 관련해서 => 필요 없어서 안만듦


    // for RankListAdapter - 21강에서 추가 - 첫번째 생성자랑 완전 같아서 따로 안만듦

    //politicoFragment 에서 비례대표 위해 또 만듦
    public Politico(int politicoID, String localProportional, String politicoName, String politicoParty) {  //원래 있던거
        this.politicoID = politicoID;  //제일 처음에 있던거 for PoliticoFragment, 정치인추가때 씀
        this.localProportional = localProportional;
        this.politicoName = politicoName;
        this.politicoParty = politicoParty;
    }
}
