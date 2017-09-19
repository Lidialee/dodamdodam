package cc.foxtail.new_version;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;


public class LoginActivity extends BaseActivity {
    SessionCallback callback;
    private TransferData action;
    private Button login;
    private Button register;
    EditText id_edit,pass_edit;
    private String id="";
    private String pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.usual_login);
        register = (Button) findViewById(R.id.register_button);
        id_edit = (EditText) findViewById(R.id.id_edit_text);
        pass_edit = (EditText) findViewById(R.id.password_edit_text);

        login.setOnClickListener(listener);
        register.setOnClickListener(listener);


        /**카카오톡 로그아웃 요청**/
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출합니다.
        //테스트 하시기 편하라고 매번 로그아웃 요청을 수행하도록 코드를 넣었습니다 ^^
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    } // onCreate() end

    // 리스너 등록
    Button.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.usual_login:
                    // 사용자가 입력한 아이디와 패스워드를 가져오기
                    id = id_edit.getText().toString();
                    pass = pass_edit.getText().toString();
                    boolean re = setResult(checkLoginForIDPW());
                    if(re){
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                case R.id.register_button:
                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    public void setToken(String id) {
        /*
        * 통신코드 19번
        * * 서버에 FCM용 TOKEN 등록하기
        * * 인자 : 학부모ID, FCM용 TOKEN
        * */
        if (!UserInfo.getFcmToken().equals("")) {
            try {
                String[] info = { id, UserInfo.getFcmToken() };
                action = new TransferData(19, info);
                UserInfo.executeServer(action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String checkLoginForIDPW() {
        /*
        * 통신코드 14번
        * 서버에서 가입된 회원인지 확인여부를 받아오기 (자체로그인)
        * 인자 : 로그인ID, 패스워드
        * 결과값 : { serverResult : yes 혹은 no, childID : 아이ID (yes일때만), parentID : 학부모ID(yes일때만) }
        * */
        String[] info = {id, pass};
        try {
            action = new TransferData(14, info);
            return UserInfo.server.submit(action).get();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }// checkLoginForIDPW end();

    public boolean setResult(String fromcheckLoginForIDPW){
        if(!(fromcheckLoginForIDPW.equals(""))){
            try {
                JSONObject object = new JSONObject(fromcheckLoginForIDPW);
                String result = object.getString("serverResult");

                if(result.equals("yes")){
                    UserInfo.setChildID(Integer.parseInt(object.getString("childID")));
                    UserInfo.setChildName(object.getString("name"));
                    setToken(object.getString("parentID"));
                    return true;
                }
                if(result.equals("no")){
                    Toast.makeText(getApplicationContext(),"아이디 or 비밀번호가 잘못됬음",Toast.LENGTH_LONG).show();
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"exception 문제",Toast.LENGTH_LONG).show();
                Log.e("SERVER",e.toString());
                return false;
            }
        }// if문 end();
        Toast.makeText(getApplicationContext(),"checkLoginForIDPW 결과가 빈경우",Toast.LENGTH_LONG).show();
        return false;
    }

    // 카카오톡에 소셜로그인이 성공하는 경우 사용하는 함수이다
    // 로그인 성공시에 보내주는 회원아이디를 서버에 보내주고
    // 디비에 존재하는 아이디라면 yes, 없는 아이디라면 no라고 한다
    public String sendId(long id){
        String[] info = {id+""};
        try {
            TransferData action = new TransferData(13, info);
            return UserInfo.server.submit(action).get();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        Log.d("onActivityResult","확인");
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {
        // 세션 연결 성공했을때
        @Override
        public void onSessionOpened() {
            Log.d("onSessionOpened","확인");
            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.d("onFailure","확인");
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }
                @Override
                public void onSessionClosed(ErrorResult errorResult){
                    Log.d("onSessionClosed","확인");
                }
                @Override
                public void onNotSignedUp() {
                    Log.d("onNotSignedUp","확인");
                }
                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    // 여기서 처음 사용 등록한 사용하는 아이정보를 입력하는 액티비티로 넘어가게 해야한다
                    Log.d("onSuccess","확인");
                    long id = userProfile.getId();      // 카카오톡 아이디 받아오기

                    try {
                        // login ={serverResult:"yes/no", childId:"1234"} 이렇게 옵니다.
                        String r = sendId(id);
                        JSONObject login = new JSONObject(r);
                        Intent intent;

                        if (login.getString("serverResult").equals("yes")) {
                            UserInfo.setChildID(Integer.parseInt(login.getString("childID")));
                            UserInfo.setChildName(login.getString("name"));
                            setToken(id+"");
                            intent = new Intent(LoginActivity.this, MainActivity.class);

                        } else {
                            intent = new Intent(getApplicationContext(),RegisterActivityTwo.class);
                            intent.putExtra("FROM","Social");
                            intent.putExtra("ID",id);
                        }

                        startActivity(intent);
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();}
                } // onSuccess() end
            });
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
        }
    }
}
