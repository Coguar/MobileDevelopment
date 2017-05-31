package android.rss;


public class MyRunnable implements Runnable {
    private Thread thread;
    private NewsRSS rss;
    private boolean mIsAlive = false;
    private int mNumber = 0;
    private String mName = "";
    MyRunnable(NewsRSS newsRss, String name) {
        rss = newsRss;
        mName = name;
        thread = new Thread(this, name + String.valueOf(mNumber++));
    }

    public void run() {
        mIsAlive = true;
        rss.updateRssList(mName);
        mIsAlive = false;
    }

    public void join()
    {
        try {
            if (thread.isAlive()) {
                thread.join();
            }
        }catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isAlive()
    {
        return mIsAlive;
    }

    public void start()
    {
        thread = new Thread(this, mName + String.valueOf(mNumber++));
        mIsAlive = true;
        thread.start();
    }

}
