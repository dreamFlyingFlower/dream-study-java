package com.wy.mapper;

import java.util.List;

import com.wy.model.AdLabel;

public interface AdLabelMapper {
    
    int deleteByPrimaryKey(Integer id);

    int insert(AdLabel record);
    
    int insertSelective(AdLabel record);

    AdLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdLabel record);
    
    int updateByPrimaryKey(AdLabel record);

    List<AdLabel> queryAdLabelByLabels(List<String> labelList);

    List<AdLabel> queryAdLabelByLabelIds(List<String> labelList);
}