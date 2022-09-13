## 如何用LinkedHashMap实现LRU

继承LinkedHashMap

重写removeEldestEntry方法

removeEldestEntry方法标识在什么情况下删除最旧的一条数据。

```Java
class LRULinkedHashMap extends LinkedHashMap{
    private int LRUSize;
    public LRULinkedHashMap(int LRUSize){
      super(16,0.75f,true);
      this.LRUSize=LRUSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest){
    //如果元素个数超过上面定义的LRU容量，就删除最旧的数据
    return size()>LRUSize;
     }   
}
```



## 如何利用TreeMap实现一致性hash

