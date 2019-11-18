package com.khan.polio;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    int []tabIcons = {
            R.drawable.home,
            R.drawable.add_worker,
            R.drawable.search,
            R.drawable.complain,
            R.drawable.upload
    };
    TabsAccessorAdapter tabsAccessorAdapter;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        relativeLayout = findViewById(R.id.parentFragment);
        tabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        tabsAccessorAdapter.addFragment(new ViewActivity(),"");
        tabsAccessorAdapter.addFragment(new AddWorker(),"");
        tabsAccessorAdapter.addFragment(new SearchChild(),"");
        tabsAccessorAdapter.addFragment(new ViewComplaint(),"");
        tabsAccessorAdapter.addFragment(new UploadFileFragment(),"");
        viewPager.setAdapter(tabsAccessorAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        mAuth = FirebaseAuth.getInstance();

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
