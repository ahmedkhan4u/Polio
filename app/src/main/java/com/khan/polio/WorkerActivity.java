package com.khan.polio;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class WorkerActivity extends AppCompatActivity {


    FrameLayout relativeLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    int []tabIcons = {
            R.drawable.home,
            R.drawable.search,
            R.drawable.worker_contact,
            R.drawable.complain,
            R.drawable.download
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        TabsAccessorAdapter tabsAccessorAdapter;
            //tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.viewPager);
            relativeLayout = findViewById(R.id.parentFragmentWorker);
            tabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
            tabsAccessorAdapter.addFragment(new WorkerRegisterChild(),"");
            tabsAccessorAdapter.addFragment(new WorkerStartActivity(),"");
            tabsAccessorAdapter.addFragment(new WorkerContact(),"");
            tabsAccessorAdapter.addFragment(new WorkerComplaint(),"");
            tabsAccessorAdapter.addFragment(new DownloadFileFragment(),"");
            viewPager.setAdapter(tabsAccessorAdapter);
            tabLayout = findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
            setUpTabIcons();
        }
        private void setUpTabIcons(){
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);
            tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        }
}
