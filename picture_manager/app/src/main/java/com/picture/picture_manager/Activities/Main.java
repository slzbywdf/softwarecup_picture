package com.picture.picture_manager.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.picture.picture_manager.Fragments.ALLPicLoadFragment;
import com.picture.picture_manager.Fragments.AllPictureFragment;
import com.picture.picture_manager.Fragments.AllUserInfoFragment;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.ActivityCollector;
import com.picture.picture_manager.Utils.GlobalFlags;

public class Main extends AppCompatActivity {
    //public int all_id = 1, sys_id = 2, his_id = 3;
    //public int now_id = 0;
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private AllUserInfoFragment fragment;
    private ALLPicLoadFragment fragment1;
    private AllPictureFragment fragment2;
    private Fragment nowFragment;
    private int nowId;
    //private ImageView userIcon;
    //public ArrayList<OneFragment> allFragments = new ArrayList<>();
    //private TextView score;
    //private RatingBar ratingBar;
    //private LinearLayout rating;
    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.all_picture:
                    if (now_id != all_id) {
                        AllPictureFragment fragment1 = (AllPictureFragment) contain(all_id);
                        if (fragment1 == null) {
                            fragment1 = new AllPictureFragment();
                            allFragments.add(new OneFragment(fragment1, all_id));
                        }
                        changeFragment(fragment1, all_id);
                        now_id = all_id;
                    }
                    return true;
                case R.id.system_push:
                    if (now_id != sys_id) {
                        SystemPushFragment fragment2 = (SystemPushFragment) contain(sys_id);
                        if (fragment2 == null) {
                            fragment2 = new SystemPushFragment();
                            allFragments.add(new OneFragment(fragment2, sys_id));
                        }
                        changeFragment(fragment2, sys_id);
                        now_id = sys_id;
                    }
                    return true;
                case R.id.history_picture:
                    if (now_id != his_id) {
                        LabelHistoryFragment fragment3 = (LabelHistoryFragment) contain(his_id);
                        if (fragment3 == null) {
                            fragment3 = new LabelHistoryFragment();
                            allFragments.add(new OneFragment(fragment3, his_id));
                        }
                        changeFragment(fragment3, his_id);
                        now_id = his_id;
                    }
                    return true;
            }
            return false;
        }
    };*/

    /*
     * 切换fragment
     *
     * @param fragment 要切换到的fragment
     * @param toId     要切换到的fragment的标识id
     */
    /*public void changeFragment(Fragment fragment, int toId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (now_id < toId)
            transaction.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit);
        else
            transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit);
        Fragment nowFragment = contain(now_id);
        if (!fragment.isAdded()) {
            //要显示的fragment没有在activity中，则隐藏当前fragment，并添加要显示的fragment到activity中并显示
            transaction.hide(nowFragment).add(R.id.content, fragment);
        } else {
            //要显示的fragment已在activity中，则隐藏当前fragment，并显示要显示的fragment
            transaction.hide(nowFragment).show(fragment);
        }
        //transaction.replace(R.id.content, fragment);
        //transaction.addToBackStack(null);
        //Log.d("Main", "changeFragment: " + transaction.commit());
        transaction.commit();
    }*/

    /*
     * 根据id判断当前的fragment是否在所有fragment链表中，在则返回该id对应的fragment引用
     *
     * @param id 要判断的fragment的标识id
     * @return 存在则返回fragment的引用，否则返回null
     */
    /*public Fragment contain(int id) {
        for (OneFragment item : allFragments) {
            if (item.getFragmentId() == id) {
                return item.getFragment();
            }
        }
        return null;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.activities.add(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //初始的fragment为“所有图片”fragment
        //AllPictureFragment fragment = new AllPictureFragment();
        //allFragments.add(new OneFragment(fragment, all_id));
        fragment = new AllUserInfoFragment();
        nowFragment = fragment;
        nowId = R.id.nav_home;
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        //now_id = all_id;


        //侧面滑动菜单栏
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(GravityCompat.START);
        navView = (NavigationView) findViewById(R.id.nav_view);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        //Fragment fragment1 = new AllUserInfoFragment();
                        //getFragmentManager().beginTransaction().replace(R.id.content, fragment1).commit();
                        if (nowFragment != fragment) {
                            if (fragment != null) {
                                transaction.hide(nowFragment).show(fragment);
                            } else {
                                fragment = new AllUserInfoFragment();
                                transaction.hide(nowFragment).add(R.id.content, fragment);
                            }
                            transaction.commit();
                            nowFragment = fragment;
                        }
                        nowId = R.id.nav_home;
                        break;
                    case R.id.nav_load:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        //Fragment fragment = new ALLPicLoadFragment();
                        //getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                        if (nowFragment != fragment1) {
                            if (fragment1 != null) {
                                transaction.hide(nowFragment).show(fragment1);
                            } else {
                                fragment1 = new ALLPicLoadFragment();
                                transaction.hide(nowFragment).add(R.id.content, fragment1);
                            }
                            transaction.commit();
                            nowFragment = fragment1;
                        }
                        nowId = R.id.nav_load;
                        break;
                    case R.id.nav_allpic:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        //Fragment fragment2 = new AllPictureFragment();
                        //getFragmentManager().beginTransaction().replace(R.id.content, fragment2).commit();
                        if (nowFragment != fragment2) {
                            if (fragment2 != null) {
                                transaction.hide(nowFragment).show(fragment2);
                            } else {
                                fragment2 = new AllPictureFragment();
                                transaction.hide(nowFragment).add(R.id.content, fragment2);
                            }
                            transaction.commit();
                            nowFragment = fragment2;
                        }
                        nowId = R.id.nav_allpic;
                        break;
                    case R.id.nav_exit:
                        Intent intent1 = new Intent(Main.this, Login.class);
                        GlobalFlags.setIsLoggedIn(false);
                        startActivity(intent1);
                        break;
                    case R.id.nav_picpush:
                        nowId = R.id.nav_picpush;
                        //mDrawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(Main.this, picupActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onRestart() {
        //在主界面时，菜单栏选中的是“主页”
        navView.setCheckedItem(nowId);
        fragment.getAllUserInfos();
        //刷新HeadView
        //setHeadView();
        //判断是否要刷新fragment
        /*if (GlobalFlags.isNeedtoRefresh()) {
            *//*Fragment tfrag = contain(now_id);
            if (tfrag != null) {
                if (now_id == all_id) {
                    if (!((AllPictureFragment) tfrag).isSearched())//刷新所有图片界面
                        ((AllPictureFragment) tfrag).getAllPicPaths();
                    else
                        ((AllPictureFragment) tfrag).searchPics();//是搜索后再打标签返回的，刷新搜索界面
                } else if (now_id == sys_id)
                    ((SystemPushFragment) tfrag).getAllPicPaths();
            }*//*
            Fragment tfrag = contain(all_id);
            if (tfrag != null) {
                if (!((AllPictureFragment) tfrag).isSearched())//刷新所有图片界面
                    ((AllPictureFragment) tfrag).getAllPicPaths();
                else
                    ((AllPictureFragment) tfrag).searchPics();//是搜索后再打标签返回的，刷新搜索界面
            }
            tfrag = contain(sys_id);
            if (tfrag != null) {
                ((SystemPushFragment) tfrag).getAllPicPaths();
            }
            tfrag = contain(his_id);
            if (tfrag != null) {
                ((LabelHistoryFragment) tfrag).getAllPicPaths();
            }
            GlobalFlags.setIsNeedtoRefresh(false);
        }*/
        //判断是否要重新加载用户头像
        /*if (GlobalFlags.isIconChanged()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).load(GlobalFlags.UserIcons[GlobalFlags.getIconIndex()]).into(userIcon);
                }
            });
            Glide.with(getApplicationContext()).load(GlobalFlags.UserIcons[GlobalFlags.getIconIndex()]).into(userIcon);
            GlobalFlags.setIsIconChanged(false);
        }*/
        super.onRestart();
    }
}
