package dev.jadss.jadapi;

@Deprecated
public class ReloadChecker {

    private static boolean instantExists = false;

    public static boolean reloadExists() {
        if(instantExists)
            return true;
        else
            instantExists = true;
        return false;
    }
}
