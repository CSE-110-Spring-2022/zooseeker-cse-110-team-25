package com.example.zooseeker25;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface NodeInfoDao {
    @Query("SELECT * FROM `Zoo_node_item` WHERE `id` LIKE '%' || :id || '%'")
    List<NodeItem> findId(String id);

    @Query("SELECT * FROM `Zoo_node_item` WHERE `kind` LIKE '%' || :kind || '%'")
    List<NodeItem> findKind(String kind);

    @Query("SELECT * FROM `Zoo_node_item` WHERE `name` LIKE '%' || :name || '%'")
    List<NodeItem> findName(String name);

    @Query("SELECT * FROM `Zoo_node_item` WHERE `tags` LIKE '%' || :tag || '%'")
    List<NodeItem> findTag(String tag);

    @Insert
    List<Long> insertAll(List<NodeItem> nodeItems);
}
