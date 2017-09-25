package com.example.yunwen.text_country_language;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.yunwen.text_country_language.utils.SharedPreferenceUtils;

import java.util.Locale;

public class MainActivity extends BaseActivity {

    public static final String TAG="——选择语言——";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**每次进来都直接从sp取出后设置语言*/
        changeAppLanguage_at_first(get_language_from_sp());
        /**按钮点击选择语言*/
        selector_lanuage_from_button();
        /**按钮点击通过dialog选择语言*/
        selector_lanuage_from_dialog();
    }


    /**第一次进来时候选择语言，不需要finish本界面*/
    private void changeAppLanguage_at_first(String language_from_sp) {
        if(language_from_sp.isEmpty()){
            Log.e(TAG, "sp里面没有存储语言类别，将使用默认的。");
            return;
        }
        Log.e(TAG, "sp里面有存储的语言类型");
        Locale myLocale = new Locale(language_from_sp);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    /**按钮点击选择语言*/
    private void selector_lanuage_from_button() {
        /**切换英语*/
        findViewById(R.id.bt_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_language_to_sp("en");
                changeAppLanguage("en");
            }
        });
        /**切换中文*/
        findViewById(R.id.bt_cn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_language_to_sp("zh");
                changeAppLanguage("zh");
            }
        });
    }


    /**
     * 按钮点击通过dialog选择语言
     */
    public static final String launages[] = new String[]{"英语", "中文"};
    private int Which_One_Selected=-1;
    private void selector_lanuage_from_dialog() {
        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(getResources().getDrawable(R.drawable.timg));
                builder.setTitle("选择语言");
                builder.setSingleChoiceItems(launages, -1, new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Which_One_Selected = which;
                           //不添加确认和取消的话，只能通过这个监听去关闭dialog
                           //Toast.makeText(MainActivity.this, "选择的是：" + launages[which],Toast.LENGTH_LONG).show();
                           //dialog.dismiss();
                  }
                });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Which_One_Selected == -1){
                            return;
                        }
                        Toast.makeText(MainActivity.this, "选择的是：" + launages[Which_One_Selected],Toast.LENGTH_LONG).show();
                        selecte_number(Which_One_Selected);
                        Which_One_Selected = -1;
                    }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Which_One_Selected = -1;
                    }
            });
            builder.show();
            }
        });
    }


    /**选择语言——通过finish了activity来实现*/
    public void changeAppLanguage(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**通过数组的位置去判断使用对应的语言*/
    public void selecte_number(int position){
        switch (position){
            case 0:
                changeAppLanguage("en");
                save_language_to_sp("en");
                break;
            case 1:
                changeAppLanguage("zh");
                save_language_to_sp("zh");
                break;
        }
    }

    /**将选择的语言存到sp*/
    public void save_language_to_sp(String language){
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putString("language",language).commit();
    }

    /**从sp拿到存储的语言类型*/
    private String language_type;
    public String get_language_from_sp(){
        language_type=PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("language","");
        Log.e("get_language_from_sp: ",language_type);
        return language_type;
    }
}
