package com.mdp.flatfish;

public interface PostCallback {
    void run(String response);

    void error(int errorCode);
}
