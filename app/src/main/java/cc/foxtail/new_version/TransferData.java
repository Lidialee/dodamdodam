package cc.foxtail.new_version;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Created by gahyeon on 2017-03-31.
 *
 * 통신코드 1번
 * * 서버에서 그날의 메뉴 받아오기
 * * 인자 : year, month, day
 * * 결과값 : { menu : menu배열(6), allergy : allergy배열(6) }
 *
 * 통신코드 2번
 * * 서버에서 그달의 일정 받아오기
 * * 인자 : year, month
 * * 결과값 : { num : 일정수, schedule : {day : 일, name : 일정명}배열(num) }
 *
 * 통신코드 3번
 * * 서버에 아이 키번호 보내기
 * * 인자 : child ID
 * * 결과값 :
 * * { name : 아이이름, year : 태어난해, month : 태어난달, height : 현재키, weight : 현재몸무게, allergy : 알러지목록,
 * *   ainfo1 : 7번알러지정보, ainfo2 : 8번알러지정보, ainfo3 : 9번알러지정보, illness : 질환목록, add : 추가기입정보 }
 *
 * 통신코드 4번
 * * 서버에 아이 정보 업데이트하기
 * * 인자 : id, 알러지목록, 알러지 7번 추가정보, 알러지 8번 추가정보, 알러지 9번 추가정보, 질환목록, 기타주의사항
 *
 * 통신코드 5번
 * * 서버에서 아이의 키/몸무게 누적 받아오기
 * * 인자 : child ID
 * * 결과값 : { num : 누적기록수, month : 월배열(num), height : 키배열(num), weight : 몸무게배열(num) }
 *
 * 통신코드 6번
 * * 서버에 아이의 현재 키/몸무게 업데이트하기
 * * 인자 : child ID, month, height, weight
 *
 * 통신코드 7번
 * * 서버에서 아이의 예방접종 기록 받아오기
 * * 인자 : child ID
 * * 결과값 : { DTaP : 디프테리아/파상풍/백일해 접종횟수, OPV : 폴리오 접종횟수, MMR : 홍역/이하선염 접종횟수,
 * *            JEV : 일본뇌염 접종횟수, HAV : A형간염 접종횟수 }
 *
 * 통신코드 8번
 * * 서버에 아이의 예방접종 기록 업데이트하기
 * * 인자 : child ID, 디프테리아/파상풍/백일해 횟수, 폴리오 횟수, 홍역/이하선염 횟수, 일본뇌염 횟수, A형간염 횟수
 *
 * 통신코드 9번
 * * 서버에서 투약의뢰 목록 받아오기
 * * 인자 : child ID
 * * 결과값 : { num : 기록수, list : { name : 약이름, date : 날짜, period : 기간, time : 약먹는시간(B,L,D), isEnd : 현재 진행중인 의뢰인지 여부,
 * *              check : 5일짜리 체크결과 }의 배열(num) }
 *
 * 통신코드 10번
 * * 서버에 투약의뢰 등록하기
 * * 인자 : child ID, 약이름, 등록날짜, 기간, 투약시간(B,L,D), 주의사항
 *
 * 통신코드 11번
 * * 서버에 건강기록 등록하기
 * * 인자 : { child ID, 등록년, 등록월, 부위, 큰버튼, 작은버튼 및 열기록, 치료 }
 *
 * 통신코드 12번
 * * 서버에서 건강기록 리스트 받아오기
 * * 인자 : child ID
 * * 결과값 : { num : 갯수, list : {
 * *               id: 기록id, manage : 조치(어디서), part : 부위, mainClass : 큰버튼, subClass : 작은버튼, detail : 상세설명 }배열(num) }
 *
 * 통신코드 13번
 * * 서버에서 가입된 회원인지 확인여부를 받아오기 (소셜로그인)
 * * 인자 : 로그인ID
 * * 결과값 : { serverResult : yes 혹은 no, childID : 아이ID (yes일때만) }
 *
 * 통신코드 14번
 * * 서버에서 가입된 회원인지 확인여부를 받아오기 (자체로그인)
 * * 인자 : 로그인ID, 패스워드
 * * 결과값 : { serverResult : yes 혹은 no, childID : 아이ID (yes일때만), parentID : 학부모ID(yes일때만) }
 *
 * 통신코드 15번
 * * 서버에서 이미 가입된 회원과 아이디가 겹치는지 확인여부를 받아오기 (자체회원가입)
 * * 인자 : 회원ID
 * * 결과값 : { possible 혹은 impossible }
 *
 * 통신코드 16번
 * * 서버에 회원 가입 올리고 ID 받아오기
 * * 인자 : { 분류, 아이이름, 아이탄생년, 아이탄생월, 아이성별, (아이키), (아이몸무게), 현재년도, 현재월, FCM용 token, 학부모ID, (PW) }
 * * 결과값 : childID
 *
 * 통신코드 17번
 * * 서버에서 가입되어있는 유치원 리스트 가져오기
 * * 결과값 : { suwon : 수원지역 유치원 배열, seoul : 서울지역 유치원 배열, anyang : 안양지원 유치원 배열,
 *              gawcheon : 과천지역 유치원 배열, yongin : 용인지역 유치원 배열 }
 *           * 유치원 배열 형태 : { id : 유치원ID, center : 유치원이름, class : 반이름 }의 배열
 *
 * 통신코드 18번
 * * 서버에 아이의 유치원 반을 등록하기
 * * 인자 : 아이ID, 유치원ID
 *
 * 통신코드 19번
 * * 서버에 FCM용 TOKEN 등록하기
 * * 인자 : 학부모ID, FCM용 TOKEN
 *
 * 통신코드 20번
 * * 서버에서 멘트에 필요한 정보 가져오기
 * * 인자 : 아이ID, 현재연, 현재월, 현재일, 현재시간(시)
 * * 결과값 : { eyeOrNose : 눈/코 질환여부, skinIll : 피부병 여부, weight : 현재몸무게, height : 현재키, stdHeight : 표준키,
 *              stdWeight : 표준몸무게, skinWho : 피부질환지수, fsnLife : 식중독지수, ultrvLife : 자외선지수,
 *              ctprvn : 미세먼지지수, dayDiff : 일교차 }
 */

public class TransferData implements Callable<String> {
    //EC2 json 통신용
    private JSONObject sendData = new JSONObject();
    private String fileName;

    //json에 들어갈 내용이 없을 때
    TransferData(int c) {
        setFileName(c);
    }

    //json에 들어갈 내용이 한개일때
    TransferData(int c, String params) throws Exception {
        String[] info = { params };
        setSendData(c, info);
        setFileName(c);
    }

    //json에 들어갈 내용이 여러개일때
    TransferData(int c, String[] params) throws Exception {
        setSendData(c, params);
        setFileName(c);
    }

    @Override
    public String call() throws Exception {
        String info = "";
        /*
         * 1. json 작성
         * JSONObject에 필요한 정보 올리기
         * accumulate(키, 값) : json에 정보쌍 추가
         * */

         /*
          * 2. 서버 접속하기
          * 링크를 가지고 있는 URL 오브젝트 생성하기
          * 이 URL로 통신을 열고(포트 연결), 이 통신부를 HttpURLConnection 오브젝트에 저장하기
          * setRequestMethod("POST") : POST 웹통신 요청방식 설정
          * setDoOutput(true) : 보낼 정보가 있다고 표시
          * setDoInput(true) : 받을 정보가 있다고 표시
          * setRequestProperty(property명, 값) : 웹통신의 다양한 설정 (이거는 캐시없게하는 설정)
          * */
        URL url = new URL("http://52.78.214.77/" + fileName + ".php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Cache-Control", "no-cache");

        /*
         * 3. 정보 보내기
         * OuputStreamWriter : OutputStream에 정보를 써주는 클래스
         * write(정보) : 정보를 stream에 작성
         * flush() : stream 운행 (물길에 배를 띄워 저쪽으로 보내준다고 상상하면 됨)
         * close() : writer를 닫음. OuputStream을 닫는 것과 동일.
         * */
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
        osw.write(sendData.toString());
        osw.flush();
        osw.close();

        /*
         * 4. 정보 받아오기
         * InputStreamReader : InputStream에 있는 정보를 읽어주는 클래스
         * BufferedReader : 비트 정보를 버퍼에 올려 문자 등으로 바꿔 읽어주는 클래스
         * readLine() : 버퍼를 한줄씩 읽어주는 함수
         * close() : reader를 닫음. InputStream을 닫는 것과 동일.
         * */
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String output;
        while ((output = bufferedReader.readLine()) != null) {
            info += output;
        }
        bufferedReader.close();

        /*
         * 5. 연결 끊기
         * 안정성과 다음 사용을 위해 웹통신 연결 끊기
         * */
        conn.disconnect();

        return info;
    }

    public void setSendData(int code, String[] info) throws Exception {
        switch (code) {
            case 1:
                sendData.accumulate("year", info[0]);
                sendData.accumulate("month", info[1]);
                sendData.accumulate("day", info[2]);
                return;
            case 2:
                sendData.accumulate("year", info[0]);
                sendData.accumulate("month", info[1]);
                return;
            case 4:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("allergy", info[1]);
                sendData.accumulate("info7", info[2]);
                sendData.accumulate("info8", info[3]);
                sendData.accumulate("info9", info[4]);
                sendData.accumulate("chronic", info[5]);
                sendData.accumulate("additional", info[6]);
                return;
            case 6:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("month", info[1]);
                sendData.accumulate("height", info[2]);
                sendData.accumulate("weight", info[3]);
                return;
            case 8:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("DTaP", info[1]);
                sendData.accumulate("OPV", info[2]);
                sendData.accumulate("MMR", info[3]);
                sendData.accumulate("JEV", info[4]);
                sendData.accumulate("HAV", info[5]);
                return;
            case 10:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("name", info[1]);
                sendData.accumulate("date", info[2]);
                sendData.accumulate("period", info[3]);
                sendData.accumulate("time", info[4]);
                sendData.accumulate("info", info[5]);
                return;
            case 11:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("year", info[1]);
                sendData.accumulate("month", info[2]);
                sendData.accumulate("manage", "parent");
                sendData.accumulate("part", info[3]);
                sendData.accumulate("mainClass", info[4]);
                sendData.accumulate("subClass", info[5]);
                sendData.accumulate("detail", info[6]);
                return;
            case 14:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("pw", info[1]);
                return;
            case 16:
                sendData.accumulate("class", info[0]);
                sendData.accumulate("name", info[1]);
                sendData.accumulate("birthYear", info[2]);
                sendData.accumulate("birthMonth", info[3]);
                sendData.accumulate("sex", info[4]);
                sendData.accumulate("height", info[5]);
                sendData.accumulate("weight", info[6]);
                sendData.accumulate("year", info[7]);
                sendData.accumulate("month", info[8]);
                sendData.accumulate("token", info[9]);
                sendData.accumulate("id", info[10]);
                if (info[0].equals("Self"))
                    sendData.accumulate("pw", info[11]);
                return;
            case 18:
                sendData.accumulate("child", info[0]);
                sendData.accumulate("class", info[1]);
                return;
            case 19:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("token", info[1]);
                return;
            case 20:
                sendData.accumulate("id", info[0]);
                sendData.accumulate("year", info[1]);
                sendData.accumulate("month", info[2]);
                sendData.accumulate("day", info[3]);
                sendData.accumulate("hour", info[4]);
                return;
            case 3:
            case 5:
            case 7:
            case 9:
            case 12:
            case 13:
            case 15:
                sendData.accumulate("id", info[0]);
                return;
        }
    }

    public void setFileName(int code) {
        switch (code) {
            case 1:
                fileName = "dbGetMenu";
                return;
            case 2:
                fileName = "dbGetSchedule";
                return;
            case 3:
                fileName = "dbGetKidInfo";
                return;
            case 4:
                fileName = "dbUpdateKidInfo";
                return;
            case 5:
                fileName = "dbGetConditions";
                return;
            case 6:
                fileName = "dbUpdateCondition";
                return;
            case 7:
                fileName = "dbGetVaccines";
                return;
            case 8:
                fileName = "dbUpdateVaccines";
                return;
            case 9:
                fileName = "dbGetMedicine";
                return;
            case 10:
                fileName = "dbSetMedicine";
                return;
            case 11:
                fileName = "dbSetHealthSetting";
                return;
            case 12:
                fileName = "dbGetHealthList";
                return;
            case 13:
                fileName = "dbCheckMemberForSocial";
                return;
            case 14:
                fileName = "dbCheckMemberForIDPW";
                return;
            case 15:
                fileName = "dbIsAnExistingId";
                return;
            case 16:
                fileName = "dbSetMemberAndChild";
                return;
            case 17:
                fileName = "dbGetKindergardenList";
                return;
            case 18:
                fileName = "dbUpdateKidClass";
                return;
            case 19:
                fileName = "dbSetFcmToken";
                return;
            case 20:
                fileName = "dbGetDataForMent";
                return;
        }
    }
}