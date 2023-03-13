package com.jml.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jml.reggie.dto.SetmealDto;
import com.jml.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveSetmeal(SetmealDto dto);

    public void updateSetmeal(SetmealDto dto);

    public void DeleteSetmeal(List<Long> ids);
}
