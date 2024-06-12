package com.lyh.ywx.application;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.lyh.ywx.application.databinding.ActivityMainBinding;

import net.liangyihui.android.ui.widget.bottomnavi.NavigationBar;
import net.liangyihui.android.ui.widget.bottomnavi.Tab;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //lj:eJyrVgrxCdYrSy1SslIy0jNQ0gHzM1NS80oy0zLBwjlZUNHilOzEgoLMFCUrQxMDAzNTc3NLS4hMakVBZlGqkpWFGVACIlSSmQsUMDQzMzCyMDcyMYYakZkONNExP9PD0y9GP9nPP7fcJNPXxNLdw6AiO9I02yTZKD-C1SfDMiIy2TlEu9jH11apFgAZlS9T
//        Log.e("test", GenerateTestUserSig.genTestUserSig(Constants.LJ));
//sd:eJyrVgrxCdYrSy1SslIy0jNQ0gHzM1NS80oy0zLBwsUpUNHilOzEgoLMFCUrQxMDAzNTc3NLS4hMakVBZlGqkpWFGVACIlSSmQsUMDQzMzCyMDcyMYYakZkONNHRJMQk0jxGv8zV1SczPyA9MSKoyKTIyTjD183AzK-CxNnAMzOgslQ7ytAp2VapFgANRi77
//        Log.e("test", GenerateTestUserSig.genTestUserSig(Constants.SD));
//        TIMManager.getInstance().testJava();
        setSupportActionBar(binding.toolbar);
        NavigationBar bar=findViewById(R.id.navi_bar);

        bar.setTabs(getMockTab());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    private List<Tab> getMockTab() {
        List<Tab> tabs =new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tab tab = new Tab();
            switch (i) {
                case 0:
                    tab.setSelectedIcon(R.mipmap.ic_tab_news_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_news_unchecked);
                    tab.setTitle("首页");
                    break;
                case 1:
                    tab.setSelectedIcon(R.mipmap.ic_tab_tool_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_tool_unchecked);
                    tab.setTitle("发现");
                    break;
                case 2:
                    tab.setSelectedIcon(R.mipmap.iv_tab_consult_doctor_checked);
                    tab.setUnSelectedIcon(R.mipmap.iv_tab_consult_doctor_unchecked);
                    tab.setTitle("工作");
                    break;
                case 3:
                    tab.setSelectedIcon(R.mipmap.ic_tab_suffer_manage_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_suffer_manage_unchecked);
                    tab.setTitle("消息");
                    break;
                case 4:
                    tab.setSelectedIcon(R.mipmap.ic_tab_me_checked);
                    tab.setUnSelectedIcon(R.mipmap.ic_tab_me_unchecked);
                    tab.setTitle("我的");
                    break;
                default:
                    break;
            }
            tabs.add(tab);
        }
        return tabs;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}