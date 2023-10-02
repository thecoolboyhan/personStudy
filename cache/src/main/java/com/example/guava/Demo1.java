package com.example.guava;

import com.example.Constants;
import com.google.common.cache.*;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Demo1 {


    public static void initCache(LoadingCache<String,Object> cache) throws ExecutionException {
        for(int i=0;i<3;i++){
            //如果缓存没有就读取数据源
            cache.get(i+1+"");
        }

    }

    /**
     * 读取缓存中的数据，如果没有就查询然后回填缓存
     * @param key
     * @param cache
     */
    public static void get(String key,LoadingCache<String,Object> cache) throws ExecutionException {
        cache.get(key, new Callable<Object>() {
            @Override//回调方法用于数据源写入缓存
            public Object call() throws Exception {
                String value = Constants.hm.get(key);

                // todo: 这里即使不主动回写缓存，也会自动回写缓存
                //回写到cache中
//                cache.put(key,value);
                //只要return此value就会自动写
                //自动回写的是return 的值
                return value;
            }
        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //CacheLoader的创建方式
         LoadingCache<String,Object> cache = CacheBuilder.newBuilder()
                 /**
                  * 构建者模式
                  */
                 //设置缓存最大个数，如果不配置策略，为FIFO先进先出,采用LRU算法。
                 .maximumSize(3)
                 //统计缓存命中率
                 .recordStats()
                 //如果三秒内没有访问，则删除缓存,相当于给缓存中所有的数据设置过期时间
                 .expireAfterAccess(3, TimeUnit.SECONDS)
                 //ttl，缓存中对象的生命周期只有3秒，相当于redis中的expire，不管是否经常访问
                 .expireAfterWrite(3,TimeUnit.SECONDS)
                 //通过弱引用来删除
                 .weakValues()
                 //移除通知
                 .removalListener(new RemovalListener<Object, Object>() {
                     @Override
                     public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                         //移除的key，和移除的原因
                         System.out.println(removalNotification.getKey() + ":" + removalNotification.getCause());
                     }
                 })
                 .build(new CacheLoader<String, Object>() {
             //读取数据源
             @Override
             public Object load(String s) throws Exception {
                 //从初始化的HashMap里读取数据
                 return Constants.hm.get(s);
             }
         });

         initCache(cache);
        System.out.println(cache.size());
        //显示缓存中的数据
        disPlay(cache);
        //读取一次1，刷新1的过期情况
//        System.out.println(cache.getIfPresent("1"));

        get(4+"",cache);
        Thread.sleep(3100);
        System.out.println("======================");
        disPlay(cache);
        //cache统计
//        System.out.println(cache.stats());
    }

    public static void disPlay(LoadingCache<String,Object> cache){
        //利用迭代器代码缓存中的数据
        Iterator<Map.Entry<String, Object>> iterator = cache.asMap().entrySet().iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }


}
