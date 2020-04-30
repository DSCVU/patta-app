package pk.patta.app.utils;

import android.os.NetworkOnMainThreadException;

import java.io.IOException;

public class InternetCheck {
    public static boolean netCheck()
    {
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue "+mExitValue);
            if(mExitValue==0){
                mIpAddrProcess.destroy();
                return true;
            }else{
                mIpAddrProcess.destroy();
                return false;
            }
        }
        catch (InterruptedException | IOException | NetworkOnMainThreadException | NullPointerException ex)
        {
            return false;
        }
    }
}
