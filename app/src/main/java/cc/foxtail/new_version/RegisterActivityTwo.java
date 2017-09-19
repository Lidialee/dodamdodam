package cc.foxtail.new_version;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;


public class RegisterActivityTwo extends BaseActivity {
    private ImageView faceView; // 사진 이미지뷰
    private Button uploadButton, nextButton; // 사진올리기, 넘어가기
    EditText name_edittext2,year_edit,month_edit,weight,height;
    CheckBox agree_checkbox;
    RadioGroup sex_radio;

    //이미지가 존재하는 경로
    private String imageLink = "";

    //통신을 위한 변수
    private S3Connect imageUpload;
    private TransferData action;

    String nametxt,yeartxt,monthtxt,weightxt,heightxt,sextxt="",fromWhere;
    String id_txt,pw_txt;
    boolean checkbox_result;    // 동의상황

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_kidinfo);
        // 허가여부 먼저 확인
        checkPermission();

        faceView = (ImageView)findViewById(R.id.kidFace);
        uploadButton = (Button)findViewById(R.id.kidFaceUpload);
        nextButton = (Button)findViewById(R.id.membershipEndButton);

        name_edittext2 = (EditText) findViewById(R.id.regikidName); // 이름
        year_edit = (EditText) findViewById(R.id.regikidYear);      // 년
        month_edit = (EditText) findViewById(R.id.regikidMonth);    // 달
        weight = (EditText) findViewById(R.id.regiweight);          // 몸무게
        height = (EditText) findViewById(R.id.regiheight);          // 키

        // 라디오 버튼 클릭 반응 받아오는거 좀 하자
        sex_radio = (RadioGroup) findViewById(R.id.sexRadio);    // 성별
        agree_checkbox = (CheckBox) findViewById(R.id.regicheckBox);   // 동의 버튼

        // 이전 엑티비티에서 보낸 인텐트
        // 어디서부터 이 2단계로 접근했는지 기록
        Intent intent = getIntent();
        fromWhere = intent.getStringExtra("FROM");
        if(fromWhere.equals("Self")){
            // Self
            id_txt = intent.getExtras().getString("ID");
            pw_txt = intent.getExtras().getString("PASS");
        }else{
            // Social
            id_txt = String.valueOf(intent.getExtras().getLong("ID"));
        }

        //버튼과 갤러리 연결하기
        uploadButton.setOnClickListener(listener);
        nextButton.setOnClickListener(listener);

    }

    public void getDataFromView(){

        // 입력값 받아오기 ( imageLink 굳이 안해도됨 )
        nametxt = name_edittext2.getText().toString();
        yeartxt = year_edit.getText().toString();
        monthtxt = month_edit.getText().toString();
        heightxt = height.getText().toString();
        weightxt = weight.getText().toString();

        // 라디오 버튼 선택값 받아오기
        sex_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.kidSexM:
                        sextxt="남";
                        break;
                    case R.id.kidSexW:
                        sextxt="여";
                        break;
                }
            }
        });

        // 동의상황 체크바로
        // ***** 여기까지 버튼눌리기 전에 무조건 실행되는 값 받아오는 과정
        checkbox_result= agree_checkbox.isChecked();
    }

    private boolean checkState(){
        if(heightxt.equals("")){ heightxt="0";}
        if(weightxt.equals("")){ weightxt="0";}

        if(nametxt.equals("")){
            Toast.makeText(this,"이름을 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(yeartxt.equals("")){
            Toast.makeText(this,"태어난 해를 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(monthtxt.equals("")){
            Toast.makeText(this,"태어난 달을 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(sextxt.equals("")){
            Toast.makeText(this,"성별을 선택해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!checkbox_result){
            Toast.makeText(this,"동의 체크란에 체크해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(imageLink.equals("")){
            Toast.makeText(this,"사진을 등록해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        // 위 모든 if에 해당하지 않으면 3단계로 넘어가도 된다는
        // 사인을 true로 보낸다
        return true;
    }

    // 버튼리스너
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 각 뷰 받아오기
            getDataFromView();

            switch (v.getId())
            {
                // 사진 업로드
                case R.id.kidFaceUpload:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, 1);
                    break;

                // 회원가입 완료.
                case R.id.membershipEndButton:
                    if(checkState()) {
                        sendImage(sendData());
                        Intent in = new Intent(getApplicationContext(),RegisterActivityThree.class);
                        startActivity(in);
                        finish();
                    }
                    else{ Toast.makeText(getApplicationContext(),
                            "다시 한번 시도해주세요",Toast.LENGTH_SHORT).show();}
                    break;
            }
        }
    };

    //정보를 DB에 저장하기
    private int sendData() {

        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        String month = ( Calendar.getInstance().get(Calendar.MONTH) + 1 ) + "";
        String token;
        if (UserInfo.getFcmToken().equals("")) { token = FirebaseInstanceId.getInstance().getToken(); }
        else { token = UserInfo.getFcmToken(); }
        Log.d("보내지는 키몸무게",heightxt+" / "+weightxt+" / "+id_txt);
        String[] info = { fromWhere, nametxt, yeartxt, monthtxt, sextxt, heightxt, weightxt,
                year, month, token, id_txt, pw_txt };
        try {
            action = new TransferData(16, info);
            int child = Integer.parseInt(UserInfo.executeServer(action));
            UserInfo.setChildID(child);
            UserInfo.setChildName(nametxt);
            return child;
        } catch (Exception e) {
            e.printStackTrace();
            return 100;
        }
    }
    //이미지를 S3로 보내기
    private boolean sendImage(int id) {
        imageUpload = new S3Connect(0, imageLink, id);
        try {
            return (Boolean)UserInfo.server.submit(imageUpload).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //갤러리 연결 후 이미지 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(getBaseContext(), "갤러리에 이상이 있습니다", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == 1) {
            Uri imageUrl = data.getData();

            Cursor c = getContentResolver().query(Uri.parse(imageUrl.toString()), null,null,null,null);
            c.moveToNext();
            imageLink = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

            faceView.setImageURI(imageUrl);
        }
    }

    //permission 동의 여부 확인 후 안 되어있으면, 동의 받아내기
    private void checkPermission() {
        int readCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (readCheck != PackageManager.PERMISSION_GRANTED || writeCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    //permission 동의 여부를 확인한 후, 거부시 메시지 띄우는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "외부 저장소 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "외부 저장소 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

