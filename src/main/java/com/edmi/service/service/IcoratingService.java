package com.edmi.service.service;

import com.edmi.entity.icorating.ICO_icorating_list;

/**
 * IcoratingService
 */
public interface IcoratingService {
    /**
     * list
     */
    public void getIcotatingList();

    /**
     * 根据列表页的item link 删除item
     *
     * @param link
     */
    public int deleteICO_icorating_listByLink(String link);

    /**
     * detail
     */
    public void getIcoratingDetail(ICO_icorating_list item);
}
