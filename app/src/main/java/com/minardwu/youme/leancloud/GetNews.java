package com.minardwu.youme.leancloud;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.minardwu.youme.adapter.ForNews;
import com.minardwu.youme.aty.NewsActivity;
import com.minardwu.youme.base.App;
import com.minardwu.youme.br.GetNewsReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MinardWu on 2016/3/15.
 */
public class GetNews {

    public static void get() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ForNews> list_lover = new ArrayList<ForNews>();
                final List<ForNews> list_follower = new ArrayList<ForNews>();
                final List<ForNews> list_all = new ArrayList<ForNews>();
                AVQuery<AVObject> query1 = AVQuery.getQuery("Love");
                query1.whereEqualTo("author1", AVUser.getCurrentUser());

                AVQuery<AVObject> query2 = AVQuery.getQuery("Love");
                query2.whereEqualTo("author2", AVUser.getCurrentUser());

//                AVQuery<AVObject> query3 = AVQuery.getQuery("Love");
//                query3.whereEqualTo("state",0);

                List<AVQuery<AVObject>> queries = new ArrayList<AVQuery<AVObject>>();
                queries.add(query1);
                queries.add(query2);

                AVQuery<AVObject> mainQuery = AVQuery.or(queries);
                mainQuery.findInBackground(new FindCallback<AVObject>() {
                    public void done(final List<AVObject> results, AVException e) {
                        if (e == null) {
                            if(results.size()==0){
                                //没有点赞，区分有无关注的情况
                                AVQuery<AVObject> query = new AVQuery<AVObject>("Follow");
                                query.whereEqualTo("followee", AVUser.getCurrentUser());
                                query.findInBackground(new FindCallback<AVObject>() {
                                    public void done(final List<AVObject> avObjects, AVException e) {
                                        if (e == null) {
                                            if (avObjects.size()==0){
                                                //无点赞，无关注
                                                Intent intent = new Intent(GetNewsReceiver.ACTION);
                                                intent.putExtra("GetNewsString", "noNews");
                                                App.getContext().sendBroadcast(intent);
                                            }else {
                                                //无点赞，有关注
                                                for (int i = 0; i < avObjects.size(); i++) {
                                                    //标记为已读
//                                                    AVObject post = AVObject.createWithoutData("Post",avObjects.get(i).getObjectId());
//                                                    post.put("state",1);
//                                                    post.saveInBackground();
                                                    avObjects.get(i).getAVObject("follower").fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                        @Override
                                                        public void done(AVObject avObject, AVException e) {
                                                            String userID = avObject.getObjectId();
                                                            String username = avObject.getString("username");
                                                            String portraitUrl = null;
                                                            if (avObject.getAVFile("UserPortrait") == null) {
                                                                portraitUrl = "http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg";
                                                            } else {
                                                                portraitUrl = avObject.getAVFile("UserPortrait").getUrl();
                                                            }
                                                            String type = "follower";
                                                            ForNews forNews = new ForNews(userID, username, portraitUrl, type, null);
                                                            list_follower.add(forNews);
                                                            if (list_follower.size() == avObjects.size()) {
                                                                list_all.addAll(list_lover);
                                                                list_all.addAll(list_follower);
                                                                Intent intent = new Intent(GetNewsReceiver.ACTION);
                                                                intent.putExtra("GetNewsString", "success");
                                                                intent.putExtra("GetNewsData", (Serializable) list_all);
                                                                App.getContext().sendBroadcast(intent);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }else {
                                //有点赞，区分有无关注的情况
                                for (int i = 0; i < results.size(); i++) {
                                    //标记为已读
//                                    AVObject post = AVObject.createWithoutData("Post",results.get(i).getObjectId());
//                                    post.put("state",1);
//                                    post.saveInBackground();
                                    final String photoUrl = results.get(i).getString("photoUrl");
                                    results.get(i).getAVObject("lover").fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                        @Override
                                        public void done(final AVObject avObject, AVException e) {
                                            String userID = avObject.getObjectId();
                                            String username = avObject.getString("username");
                                            String portraitUrl = null;
                                            if (avObject.getAVFile("UserPortrait") == null) {
                                                portraitUrl = "http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg";
                                            } else {
                                                portraitUrl = avObject.getAVFile("UserPortrait").getUrl();
                                            }
                                            String type = "lover";
                                            ForNews forNews = new ForNews(userID, username, portraitUrl, type,photoUrl);
                                            list_lover.add(forNews);
                                            if (list_lover.size() == results.size()) {
                                                AVQuery<AVObject> query = new AVQuery<AVObject>("Follow");
                                                query.whereEqualTo("followee", AVUser.getCurrentUser());
                                                query.findInBackground(new FindCallback<AVObject>() {
                                                    public void done(final List<AVObject> avObjects, AVException e) {
                                                        if (e == null) {
                                                            if (avObjects.size()==0){
                                                                //有点赞，无关注
                                                                list_all.addAll(list_lover);
                                                                Intent intent = new Intent(GetNewsReceiver.ACTION);
                                                                intent.putExtra("GetNewsString", "success");
                                                                intent.putExtra("GetNewsData", (Serializable) list_all);
                                                                App.getContext().sendBroadcast(intent);
                                                            }else {
                                                                //有点赞，有关注
                                                                for (int i = 0; i < avObjects.size(); i++) {
                                                                    avObjects.get(i).getAVObject("follower").fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                                                        @Override
                                                                        public void done(AVObject avObject, AVException e) {
                                                                            String userID = avObject.getObjectId();
                                                                            String username = avObject.getString("username");
                                                                            String portraitUrl = null;
                                                                            if (avObject.getAVFile("UserPortrait") == null) {
                                                                                portraitUrl = "http://ac-b2cy6yl4.clouddn.com/fdb31720f58cd084.jpg";
                                                                            } else {
                                                                                portraitUrl = avObject.getAVFile("UserPortrait").getUrl();
                                                                            }
                                                                            String type = "follower";
                                                                            ForNews forNews = new ForNews(userID, username, portraitUrl, type, null);
                                                                            list_follower.add(forNews);
                                                                            if (list_follower.size() == avObjects.size()) {
                                                                                list_all.addAll(list_lover);
                                                                                list_all.addAll(list_follower);
                                                                                Intent intent = new Intent(GetNewsReceiver.ACTION);
                                                                                intent.putExtra("GetNewsString", "success");
                                                                                intent.putExtra("GetNewsData", (Serializable) list_all);
                                                                                App.getContext().sendBroadcast(intent);
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }


                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }

                        } else {
                            Intent intent = new Intent(GetNewsReceiver.ACTION);
                            intent.putExtra("GetNewsString", "fail");
                            App.getContext().sendBroadcast(intent);
                        }
                    }
                });
            }
        }).start();
    }
}
