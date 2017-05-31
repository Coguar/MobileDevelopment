package android.rss;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class NewsRSS extends AppCompatActivity implements Serializable{

    public static RSSItem selectedRssItem = null;
    private RecyclerView mRssListView = null;
    private RecyclerView.Adapter mRssAdapter = null;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mRssFeedUrl = "https://lenta.ru/rss";
    private String mFeedName = "feed";
    private ArrayList<RSSItem> mRssItems = new ArrayList<RSSItem>();
    private NewsRSS self = this;

    private DataBaseManager manager;
    private Timer mTimer;
    private Context mContext = null;
    private Handler mHandler;
    private MyRunnable mRunnable;
    private boolean mIsUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DataBaseManager(new DBHelper(this));
        setContentView(R.layout.activity_rssfeed);
        manager.getNewsFromDB();
        Collections.reverse(mRssItems);


        mRssListView = (RecyclerView) findViewById(R.id.rssRecyclerView);
        mRssListView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRssListView.setLayoutManager(mLayoutManager);
        mRssAdapter = new MyViewAdapter(mRssItems);
        mRssListView.setAdapter(mRssAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRssListView.addItemDecoration(itemDecoration);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                mRssAdapter = new MyViewAdapter(mRssItems);
                mRssListView.setAdapter(mRssAdapter);
                mRssAdapter.notifyDataSetChanged();
            }
        };
        mRunnable = new MyRunnable(this, mFeedName);
        try {
            Intent service = new Intent(this, MyService.class);
            service.putExtra("size", mRssItems.size());
            startService(service);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }



        settingTimerToUpdate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyViewAdapter) mRssAdapter).setOnItemClickListener(new
             MyViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    selectedRssItem = mRssItems.get(position);
                    Intent intent = new Intent(self, RSSItemDisplayer.class);
                    intent.putExtra("description", selectedRssItem.getDescription());
                    intent.putExtra("title", selectedRssItem.getTitle());
                    intent.putExtra("link", selectedRssItem.getLink());
                    intent.putExtra("date", selectedRssItem.getPubDate());
                    startActivity(intent);
                }});
    }

    public int getNumberItems(){
        return mRssItems.size();
    }

    public void settingTimerToUpdate()
    {
        mTimer = new Timer("timer");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!self.mRunnable.isAlive()) {
                    self.updateNews();
                }
            }
        },1000,5000);
    }

    public void updateRssList(String name) {

        DBHelper db = new DBHelper(mContext);

        if (mFeedName == null) {
            mFeedName = "feed";
        }
        if (manager == null) {
            manager = new DataBaseManager(db);
        }
        if (mRssItems.isEmpty()) {
            mRssItems = manager.getNewsFromDB();
        }
        mIsUpdate = false;
        RSSItem.getRssItems(mRssFeedUrl, db);
        ArrayList<RSSItem> newItems = manager.getNewsFromDB();
        int size = mRssItems.size();
        if (size == 0 || !mRssItems.get(0).getTitle().equals(newItems.get(0).getTitle()))
        {
            mIsUpdate = true;
            if (name.equals(mFeedName)){
                mRssItems = manager.getNewsFromDB();
            }
        }
    }

    public boolean isUpdate()
    {
        return mIsUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {mIsUpdate = isUpdate;}

    public void setContext(Context context)
    {
        mContext = context;
    }

    public void  updateNews()
    {
        mRunnable.start();
        mHandler.sendEmptyMessage(0);
    }

}
